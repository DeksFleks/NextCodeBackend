package dbataev.nextcode.service;

import dbataev.nextcode.model.dto.QuoteDto;
import dbataev.nextcode.model.mapper.QuoteMapper;
import dbataev.nextcode.repository.QuoteRepository;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public QuoteDto getRendomQuote(){
        return QuoteMapper.toDto(quoteRepository.getRandom());
    }
}


