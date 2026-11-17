-- Optional demo data, only after V9. Run with psql -v ON_ERROR_STOP=1 -f docs/demo_content.sql.
-- Does not change or remove existing courses. Re-running does not duplicate this demo.
BEGIN;
DO $$
DECLARE course BIGINT; module BIGINT; lesson BIGINT; task BIGINT; opt BIGINT; n INT;
BEGIN
    IF EXISTS (SELECT 1 FROM courses WHERE title='Java — MVP demo') THEN RETURN; END IF;
    INSERT INTO courses(title,is_active) VALUES('Java — MVP demo',true) RETURNING id INTO course;
    INSERT INTO modules(course_id,title,order_index) VALUES(course,'Первый шаг',1) RETURNING id INTO module;
    INSERT INTO lessons(xp_reward,is_deleted) VALUES(25,false) RETURNING id INTO lesson;
    INSERT INTO module_lessons(module_id,lesson_id,order_index) VALUES(module,lesson,1);
    INSERT INTO lesson_translations(lesson_id,language_id,title,theory_text) VALUES
        (lesson,1,'Вывод текста','System.out.println("Hello"); выводит текст и переводит строку. Java различает регистр символов.'),
        (lesson,2,'Printing text','System.out.println("Hello"); prints text and a newline. Java is case-sensitive.');
    INSERT INTO tasks(type,is_deleted) VALUES('MULTIPLE_CHOICE',false) RETURNING id INTO task;
    INSERT INTO lesson_tasks(lesson_id,task_id,order_index) VALUES(lesson,task,1);
    INSERT INTO task_translations(task_id,language_id,question,explanation) VALUES
        (task,1,'Выберите обе корректные команды вывода текста Hello.','print выводит текст, println также добавляет перевод строки.'),
        (task,2,'Choose both valid commands that print Hello.','print writes text; println also adds a newline.');
    INSERT INTO multiple_choice_tasks VALUES(task,true);
    FOR n IN 1..3 LOOP
        INSERT INTO multiple_choice_options(task_id,is_correct,order_index) VALUES(task,n<3,n) RETURNING option_id INTO opt;
        INSERT INTO option_translations(option_id,language_id,option_text)
            SELECT opt,language_id,CASE n WHEN 1 THEN 'System.out.print("Hello");' WHEN 2 THEN 'System.out.println("Hello");' ELSE 'system.out.println("Hello");' END
            FROM languages WHERE code IN ('ru','en');
    END LOOP;
    INSERT INTO tasks(type,is_deleted) VALUES('FILL_BLANK',false) RETURNING id INTO task;
    INSERT INTO lesson_tasks(lesson_id,task_id,order_index) VALUES(lesson,task,2);
    INSERT INTO task_translations(task_id,language_id,question,explanation) VALUES
        (task,1,'Заполните пропуск, чтобы вывести Hello с переводом строки.','Нужен метод println. Регистр имеет значение.'),
        (task,2,'Fill the blank to print Hello followed by a newline.','Use println. Case matters.');
    INSERT INTO fill_blank_tasks(task_id,sentence_before,sentence_after,correct_answer) VALUES(task,'System.out.','("Hello");','println');
    INSERT INTO tasks(type,is_deleted) VALUES('CODING',false) RETURNING id INTO task;
    INSERT INTO lesson_tasks(lesson_id,task_id,order_index) VALUES(lesson,task,3);
    INSERT INTO task_translations(task_id,language_id,question,explanation) VALUES
        (task,1,'Напишите одну строку: System.out.println("Hello");','Скопируйте команду точно, включая регистр и точку с запятой. Это упражнение на синтаксис.'),
        (task,2,'Write this single line: System.out.println("Hello");','Copy the statement exactly, including case and semicolon. This is a syntax exercise.');
    INSERT INTO coding_tasks(task_id,starter_code,solution_code,programming_language) VALUES(task,'System.out.println("");','System.out.println("Hello");','java');
END $$;
COMMIT;
