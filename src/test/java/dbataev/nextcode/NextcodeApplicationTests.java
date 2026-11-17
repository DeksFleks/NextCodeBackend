package dbataev.nextcode;

import dbataev.nextcode.model.dto.TaskAnswer;
import dbataev.nextcode.service.*;
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NextcodeApplicationTests {
    static final EmbeddedPostgres postgres;
    static { try { postgres = EmbeddedPostgres.builder().setServerConfig("unix_socket_directories", "").start(); } catch (IOException e) { throw new ExceptionInInitializerError(e); } }
    @DynamicPropertySource
    static void database(DynamicPropertyRegistry properties) {
        properties.add("spring.datasource.url", () -> postgres.getJdbcUrl("postgres", "postgres"));
        properties.add("spring.datasource.username", () -> "postgres");
        properties.add("spring.datasource.password", () -> "postgres");
        properties.add("spring.jpa.show-sql", () -> "false");
    }
    @Autowired jakarta.persistence.EntityManager entityManager;
    @Autowired JdbcTemplate jdbc;
    @Autowired dbataev.nextcode.repository.UserRepository users;
    @Autowired dbataev.nextcode.security.jwt.JwtService jwt;
    @Autowired LessonService lessons;
    @Autowired TaskService tasks;
    @Autowired ContentLanguageService languages;
    void seed() {
        jdbc.update("INSERT INTO users(id, username, password_hash) VALUES(101, 'tester', 'unused')");
        jdbc.update("INSERT INTO lessons(id,xp_reward) VALUES(201,10)");
        jdbc.update("INSERT INTO lesson_translations(lesson_id,language_id,title,theory_text) VALUES(201,1,'Урок','Теория')");
        jdbc.update("INSERT INTO tasks(id,type,is_deleted) VALUES(301,'MULTIPLE_CHOICE',false),(302,'FILL_BLANK',false),(303,'CODING',false),(304,'FILL_BLANK',true)");
        jdbc.update("INSERT INTO task_translations(task_id,language_id,question,explanation) SELECT id,1,'Вопрос','Пояснение' FROM tasks");
        jdbc.update("INSERT INTO lesson_tasks(lesson_id,task_id,order_index) VALUES(201,301,2),(201,302,1),(201,303,3),(201,304,4)");
        jdbc.update("INSERT INTO multiple_choice_tasks VALUES(301,true)");
        jdbc.update("INSERT INTO multiple_choice_options(option_id,task_id,is_correct,order_index) VALUES(401,301,true,1),(402,301,true,2),(403,301,false,3)");
        jdbc.update("INSERT INTO option_translations(option_id,language_id,option_text) SELECT option_id,1,'Текст ' || option_id FROM multiple_choice_options");
        jdbc.update("INSERT INTO fill_blank_tasks(task_id,correct_answer) VALUES(302,'print'),(304,'deleted')");
        jdbc.update("INSERT INTO coding_tasks(task_id,starter_code,solution_code) VALUES(303,'// start','System.out.println(1);')");
    }
    @Test void contentIsOrderedAndDoesNotExposeSolutions() throws Exception {
        seed();
        var result = tasks.findLessonTasks(201L,101L);
        assertEquals(List.of(302L,301L,303L),result.stream().map(t->t.getId()).toList());
        var json = new tools.jackson.databind.ObjectMapper().writeValueAsString(result);
        assertFalse(json.contains("correctAnswer")); assertFalse(json.contains("isCorrect"));
        assertFalse(json.contains("solutionCode")); assertFalse(json.contains("System.out.println"));
        assertEquals("// start",result.get(2).getStarterCode());
    }
    @Test void exactOptionSetAndCaseSensitiveTextAreCheckedOnServer() {
        seed();
        assertFalse(tasks.check(301L,101L,new TaskAnswer(201L,List.of(401L),null)).correct());
        assertFalse(tasks.check(301L,101L,new TaskAnswer(201L,List.of(401L,402L,403L),null)).correct());
        assertThrows(ResponseStatusException.class,()->tasks.check(301L,101L,new TaskAnswer(201L,List.of(999L),null)));
        assertThrows(ResponseStatusException.class,()->tasks.check(301L,101L,new TaskAnswer(201L,List.of(401L,401L),null)));
        assertTrue(tasks.check(301L,101L,new TaskAnswer(201L,List.of(402L,401L),null)).correct());
        assertFalse(tasks.check(302L,101L,new TaskAnswer(201L,null,"Print")).correct());
        assertTrue(tasks.check(302L,101L,new TaskAnswer(201L,null," print ")).correct());
        assertFalse(tasks.check(303L,101L,new TaskAnswer(201L,null,"system.out.println(1);")).correct());
        assertTrue(tasks.check(303L,101L,new TaskAnswer(201L,null,"System.out.println(1);\r\n")).correct());
        tasks.requirePassed(201L,101L);
        assertThrows(ResponseStatusException.class,()->tasks.check(304L,101L,new TaskAnswer(201L,null,"deleted")));
    }
    @Test void lessonCompletionIsIdempotent() {
        seed();
        tasks.check(301L,101L,new TaskAnswer(201L,List.of(401L,402L),null));
        tasks.check(302L,101L,new TaskAnswer(201L,null,"print"));
        tasks.check(303L,101L,new TaskAnswer(201L,null,"System.out.println(1);"));
        var token = jwt.generateToken(users.findById(101L).orElseThrow());
        lessons.lessonCompleted(201L,token);
        assertEquals(10L,users.findById(101L).orElseThrow().getTotalXp());
        assertTrue(lessons.lessonCompleted(201L,token).isEmpty());
        assertEquals(10L,users.findById(101L).orElseThrow().getTotalXp());
        assertEquals(1,users.findById(101L).orElseThrow().getCompletedLessons());
    }
    @Test void demoScriptIsExecutableAndIdempotent() throws Exception {
        String script = java.nio.file.Files.readString(java.nio.file.Path.of("docs/demo_content.sql")).replace("\nBEGIN;\n", "\n").replace("\nCOMMIT;", "\n");
        jdbc.execute(script);
        jdbc.execute(script);
        assertEquals(1,jdbc.queryForObject("SELECT count(*) FROM courses WHERE title='Java — MVP demo'",Integer.class));
        assertEquals(3,jdbc.queryForObject("SELECT count(*) FROM tasks",Integer.class));
    }
    @Test void unavailableLanguageIsRejectedAndMissingTranslationFallsBack() {
        seed();
        assertThrows(ResponseStatusException.class,()->languages.select(101L,2L));
        jdbc.update("UPDATE languages SET is_active=true WHERE language_id=2");
        entityManager.clear();
        languages.select(101L,2L);
        assertEquals("Вопрос",tasks.findLessonTasks(201L,101L).getFirst().getQuestion());
        assertThrows(ResponseStatusException.class,()->tasks.requirePassed(201L,101L));
    }
}
