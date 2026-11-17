package dbataev.nextcode;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.*;
class LegacyMigrationTests {
    @Test void migratesExistingContentAndKeepsIdsAndOrdering() throws Exception {
        try (var pg = EmbeddedPostgres.builder().setServerConfig("unix_socket_directories", "").start()) {
            var ds = pg.getPostgresDatabase();
            Flyway.configure().dataSource(ds).target("8").load().migrate();
            var sql = new JdbcTemplate(ds);
            sql.update("INSERT INTO lessons(id,title,theory_text,xp_reward) VALUES(50,'Введение','Теория',10)");
            sql.update("INSERT INTO tasks(id,type,question,correct_answer,options,explanation) VALUES(60,'test','Вопрос','A','[\"A\",\"B\"]','Почему'),(61,'finish','Пропуск','x',null,null),(62,'write','Код','Main',null,null)");
            sql.update("INSERT INTO lesson_tasks(lesson_id,task_id,order_index) VALUES(50,60,3),(50,61,2),(50,62,1)");
            Flyway.configure().dataSource(ds).load().migrate();
            assertEquals("Введение",sql.queryForObject("SELECT title FROM lesson_translations WHERE lesson_id=50 AND language_id=1",String.class));
            assertEquals("MULTIPLE_CHOICE",sql.queryForObject("SELECT type FROM tasks WHERE id=60",String.class));
            assertEquals(1,sql.queryForObject("SELECT count(*) FROM multiple_choice_options WHERE task_id=60 AND is_correct",Integer.class));
            assertEquals("x",sql.queryForObject("SELECT correct_answer FROM fill_blank_tasks WHERE task_id=61",String.class));
            assertEquals("Main",sql.queryForObject("SELECT solution_code FROM coding_tasks WHERE task_id=62",String.class));
            assertEquals(3,sql.queryForObject("SELECT order_index FROM lesson_tasks WHERE task_id=60",Integer.class));
            assertEquals(3,sql.queryForObject("SELECT count(*) FROM legacy_task_payloads",Integer.class));
            assertEquals(0,sql.queryForObject("SELECT count(*) FROM information_schema.columns WHERE table_name='tasks' AND column_name='correct_answer'",Integer.class));
        }
    }
    @Test void invalidLegacyContentRollsBackWithoutDroppingColumns() throws Exception {
        try (var pg = EmbeddedPostgres.builder().setServerConfig("unix_socket_directories", "").start()) {
            var ds = pg.getPostgresDatabase();
            Flyway.configure().dataSource(ds).target("8").load().migrate();
            var sql = new JdbcTemplate(ds);
            sql.update("INSERT INTO tasks(type,question,correct_answer,options) VALUES('test','Invalid','C','[\"A\",\"B\"]')");
            assertThrows(Exception.class,()->Flyway.configure().dataSource(ds).load().migrate());
            assertEquals("C",sql.queryForObject("SELECT correct_answer FROM tasks",String.class));
            assertEquals(0,sql.queryForObject("SELECT count(*) FROM information_schema.tables WHERE table_name='languages'",Integer.class));
        }
    }
}
