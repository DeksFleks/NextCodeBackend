package dbataev.nextcode.service;

import dbataev.nextcode.model.base.Quote;
import dbataev.nextcode.model.dto.QuoteDto;
import dbataev.nextcode.model.mapper.QuoteMapper;
import dbataev.nextcode.repository.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class QuoteService {
    private final QuoteRepository quoteRepository;

    public QuoteService(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
    }

    public QuoteDto getRendomQuote(){
        List<Quote> quoteList = quoteRepository.findAll();
        Integer index = new Random().nextInt(quoteList.size());

        return QuoteMapper.toDto(quoteList.get(index));
    }
}
