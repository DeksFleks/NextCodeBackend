package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
    @Query(value = """
            SELECT *
        FROM quote
        ORDER BY RANDOM()
        LIMIT 1
        """, nativeQuery = true)
    Quote getRandom();
}

