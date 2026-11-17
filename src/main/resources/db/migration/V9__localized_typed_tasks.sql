-- Existing identifiers and lesson_tasks/module_lessons ordering are preserved.
CREATE TABLE languages (
    language_id BIGSERIAL PRIMARY KEY,
    code VARCHAR(10) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT FALSE
);
INSERT INTO languages(code, name, is_active) VALUES ('ru', 'Русский', TRUE), ('en', 'English', FALSE);
ALTER TABLE users ADD COLUMN language_id BIGINT NOT NULL DEFAULT 1 REFERENCES languages(language_id);
CREATE TABLE lesson_translations (
    lesson_translation_id BIGSERIAL PRIMARY KEY,
    lesson_id BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    language_id BIGINT NOT NULL REFERENCES languages(language_id),
    title TEXT NOT NULL,
    theory_text TEXT,
    UNIQUE(lesson_id, language_id)
);
CREATE TABLE task_translations (
    translation_id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    language_id BIGINT NOT NULL REFERENCES languages(language_id),
    question TEXT NOT NULL,
    explanation TEXT,
    UNIQUE(task_id, language_id)
);
CREATE TABLE multiple_choice_tasks (
    task_id BIGINT PRIMARY KEY REFERENCES tasks(id) ON DELETE CASCADE,
    is_multiple_select BOOLEAN NOT NULL DEFAULT FALSE
);
CREATE TABLE multiple_choice_options (
    option_id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL REFERENCES multiple_choice_tasks(task_id) ON DELETE CASCADE,
    is_correct BOOLEAN NOT NULL,
    order_index INT NOT NULL,
    UNIQUE(task_id, order_index)
);
CREATE TABLE option_translations (
    translation_id BIGSERIAL PRIMARY KEY,
    option_id BIGINT NOT NULL REFERENCES multiple_choice_options(option_id) ON DELETE CASCADE,
    language_id BIGINT NOT NULL REFERENCES languages(language_id),
    option_text TEXT NOT NULL,
    UNIQUE(option_id, language_id)
);
CREATE TABLE coding_tasks (
    task_id BIGINT PRIMARY KEY REFERENCES tasks(id) ON DELETE CASCADE,
    starter_code TEXT NOT NULL DEFAULT '',
    solution_code TEXT NOT NULL,
    programming_language VARCHAR(30) NOT NULL DEFAULT 'java'
);
CREATE TABLE fill_blank_tasks (
    task_id BIGINT PRIMARY KEY REFERENCES tasks(id) ON DELETE CASCADE,
    sentence_before TEXT NOT NULL DEFAULT '',
    sentence_after TEXT NOT NULL DEFAULT '',
    correct_answer TEXT NOT NULL,
    case_sensitive BOOLEAN NOT NULL DEFAULT TRUE
);
-- Retain the original payload for manual review, including deleted records.
CREATE TABLE legacy_task_payloads AS
    SELECT id AS task_id, type, question, correct_answer, options, explanation FROM tasks;
DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM tasks WHERE lower(type) NOT IN
        ('test','write','finish','multiple_choice','coding','fill_blank')) THEN
        RAISE EXCEPTION 'Unknown legacy task type; review tasks before migration';
    END IF;
    IF EXISTS (SELECT 1 FROM tasks WHERE correct_answer IS NULL OR btrim(correct_answer) = '') THEN
        RAISE EXCEPTION 'Missing legacy correct_answer; populate before migration';
    END IF;
    IF EXISTS (SELECT 1 FROM tasks WHERE lower(type) IN ('test','multiple_choice') AND
        (options IS NULL OR jsonb_typeof(options) <> 'array')) THEN
        RAISE EXCEPTION 'Choice options must be JSON arrays of strings';
    END IF;
    IF EXISTS (SELECT 1 FROM tasks t CROSS JOIN LATERAL jsonb_array_elements(
        CASE WHEN jsonb_typeof(t.options) = 'array' THEN t.options ELSE '[]'::jsonb END) o
        WHERE lower(t.type) IN ('test','multiple_choice') AND jsonb_typeof(o) <> 'string') THEN
        RAISE EXCEPTION 'Choice options must contain only strings';
    END IF;
    IF EXISTS (SELECT 1 FROM tasks t WHERE lower(type) IN ('test','multiple_choice') AND
        (jsonb_array_length(options) < 2 OR
         (SELECT count(*) FROM jsonb_array_elements_text(options) o WHERE o = t.correct_answer) <> 1)) THEN
        RAISE EXCEPTION 'Each legacy choice task needs at least two options and exactly one matching answer';
    END IF;
END $$;
INSERT INTO lesson_translations(lesson_id, language_id, title, theory_text)
    SELECT id, 1, title, theory_text FROM lessons;
INSERT INTO task_translations(task_id, language_id, question, explanation)
    SELECT id, 1, question, explanation FROM tasks;
UPDATE tasks SET type = CASE lower(type)
    WHEN 'test' THEN 'MULTIPLE_CHOICE' WHEN 'write' THEN 'CODING'
    WHEN 'finish' THEN 'FILL_BLANK' ELSE upper(type) END;
INSERT INTO multiple_choice_tasks(task_id) SELECT id FROM tasks WHERE type = 'MULTIPLE_CHOICE';
INSERT INTO multiple_choice_options(task_id, is_correct, order_index)
    SELECT t.id, o.value = t.correct_answer, o.ordinality::int
    FROM tasks t CROSS JOIN LATERAL jsonb_array_elements_text(
        CASE WHEN t.type = 'MULTIPLE_CHOICE' THEN t.options ELSE '[]'::jsonb END
    ) WITH ORDINALITY o(value, ordinality);
INSERT INTO option_translations(option_id, language_id, option_text)
    SELECT o.option_id, 1, t.options ->> (o.order_index - 1)
    FROM multiple_choice_options o JOIN tasks t ON t.id = o.task_id;
INSERT INTO coding_tasks(task_id, solution_code)
    SELECT id, correct_answer FROM tasks WHERE type = 'CODING';
-- Legacy FINISH has no structured context: keep its question and an empty surrounding sentence.
INSERT INTO fill_blank_tasks(task_id, correct_answer)
    SELECT id, correct_answer FROM tasks WHERE type = 'FILL_BLANK';
ALTER TABLE tasks ADD CONSTRAINT tasks_type_check CHECK(type IN ('MULTIPLE_CHOICE','CODING','FILL_BLANK'));
ALTER TABLE lessons DROP COLUMN title, DROP COLUMN theory_text;
ALTER TABLE tasks DROP COLUMN question, DROP COLUMN correct_answer, DROP COLUMN options, DROP COLUMN explanation;
CREATE TABLE task_attempts (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    lesson_id BIGINT NOT NULL REFERENCES lessons(id) ON DELETE CASCADE,
    task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    passed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(user_id, lesson_id, task_id)
);
