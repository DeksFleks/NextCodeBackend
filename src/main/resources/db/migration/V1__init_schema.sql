-- V1__init_schema.sql

-- USERS
CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    username           VARCHAR(100) UNIQUE NOT NULL,
    password_hash      TEXT                NOT NULL,
    nickname           VARCHAR(100),
    xp                 INT       DEFAULT 0,
    level              INT       DEFAULT 1,
    streak             INT       DEFAULT 0,
    last_activity_date DATE,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- COURSES
CREATE TABLE courses
(
    id         BIGSERIAL PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    is_active  BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN   DEFAULT FALSE
);

-- MODULES
CREATE TABLE modules
(
    id          BIGSERIAL PRIMARY KEY,
    course_id   BIGINT       NOT NULL,
    title       VARCHAR(255) NOT NULL,
    order_index INT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    is_deleted  BOOLEAN   DEFAULT FALSE,
    CONSTRAINT fk_modules_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);

-- LESSONS (без module_id, так как M:N)
CREATE TABLE lessons
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    xp_reward   INT       DEFAULT 0,
    theory_text TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP,
    is_deleted  BOOLEAN   DEFAULT FALSE
);

-- MODULE_LESSONS (M:N + порядок)
CREATE TABLE module_lessons
(
    id          BIGSERIAL PRIMARY KEY,
    module_id   BIGINT NOT NULL,
    lesson_id   BIGINT NOT NULL,
    order_index INT,
    CONSTRAINT fk_ml_module FOREIGN KEY (module_id) REFERENCES modules (id) ON DELETE CASCADE,
    CONSTRAINT fk_ml_lesson FOREIGN KEY (lesson_id) REFERENCES lessons (id) ON DELETE CASCADE,
    CONSTRAINT unique_module_lesson UNIQUE (module_id, lesson_id)
);

-- TASKS (без lesson_id, так как M:N)
CREATE TABLE tasks
(
    id             BIGSERIAL PRIMARY KEY,
    type           VARCHAR(20) NOT NULL,
    question       TEXT        NOT NULL,
    correct_answer TEXT,
    options        JSONB,
    explanation    TEXT,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP,
    is_deleted     BOOLEAN   DEFAULT FALSE
);

-- LESSON_TASKS (M:N + порядок)
CREATE TABLE lesson_tasks
(
    id          BIGSERIAL PRIMARY KEY,
    lesson_id   BIGINT NOT NULL,
    task_id     BIGINT NOT NULL,
    order_index INT,
    CONSTRAINT fk_lt_lesson FOREIGN KEY (lesson_id) REFERENCES lessons (id) ON DELETE CASCADE,
    CONSTRAINT fk_lt_task FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
    CONSTRAINT unique_lesson_task UNIQUE (lesson_id, task_id)
);

-- USER_PROGRESS
CREATE TABLE user_progress
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    lesson_id    BIGINT NOT NULL,
    is_completed BOOLEAN DEFAULT FALSE,
    completed_at TIMESTAMP,
    CONSTRAINT fk_up_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_up_lesson FOREIGN KEY (lesson_id) REFERENCES lessons (id) ON DELETE CASCADE,
    CONSTRAINT unique_user_lesson UNIQUE (user_id, lesson_id)
);

-- USER_DAILY
CREATE TABLE user_daily
(
    id           BIGSERIAL PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    task         TEXT   NOT NULL,
    date         DATE   NOT NULL,
    progress     INT       DEFAULT 0,
    goal         INT    NOT NULL,
    is_completed BOOLEAN   DEFAULT FALSE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP,
    is_deleted   BOOLEAN   DEFAULT FALSE,
    CONSTRAINT fk_ud_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- ACHIEVEMENTS
CREATE TABLE achievements
(
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    type            VARCHAR(20)  NOT NULL,
    condition_value INT          NOT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP,
    is_deleted      BOOLEAN   DEFAULT FALSE
);

-- USER_ACHIEVEMENTS
CREATE TABLE user_achievements
(
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    achievement_id BIGINT NOT NULL,
    achieved_at    TIMESTAMP,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP,
    is_deleted     BOOLEAN   DEFAULT FALSE,
    CONSTRAINT fk_ua_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_ua_achievement FOREIGN KEY (achievement_id) REFERENCES achievements (id) ON DELETE CASCADE,
    CONSTRAINT unique_user_achievement UNIQUE (user_id, achievement_id)
);

-- QUOTE
CREATE TABLE quote
(
    id     BIGSERIAL PRIMARY KEY,
    text   TEXT NOT NULL,
    author VARCHAR(255)
);

-- INDEXES (ускорение)
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_modules_course_id ON modules (course_id);
CREATE INDEX idx_module_lessons_module ON module_lessons (module_id);
CREATE INDEX idx_module_lessons_lesson ON module_lessons (lesson_id);
CREATE INDEX idx_lesson_tasks_lesson ON lesson_tasks (lesson_id);
CREATE INDEX idx_lesson_tasks_task ON lesson_tasks (task_id);
CREATE INDEX idx_user_progress_user ON user_progress (user_id);
CREATE INDEX idx_user_daily_user ON user_daily (user_id);
CREATE INDEX idx_user_achievements_user ON user_achievements (user_id);