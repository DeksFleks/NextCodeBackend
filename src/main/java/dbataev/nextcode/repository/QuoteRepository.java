package dbataev.nextcode.repository;

import dbataev.nextcode.model.base.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {

}
