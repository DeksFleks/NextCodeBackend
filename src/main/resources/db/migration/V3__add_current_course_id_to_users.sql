ALTER TABLE users
    ADD COLUMN current_course_id BIGINT,
ADD CONSTRAINT fk_users_current_course
FOREIGN KEY (current_course_id)
REFERENCES courses(id)
ON
DELETE
SET NULL;