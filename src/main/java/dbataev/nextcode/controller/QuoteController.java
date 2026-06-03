package dbataev.nextcode.controller;

import dbataev.nextcode.model.dto.QuoteDto;
import dbataev.nextcode.service.QuoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/quotes")
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping(path = "/random")
    public QuoteDto getRandomQuote() {
        return quoteService.getRendomQuote();
    }
}

