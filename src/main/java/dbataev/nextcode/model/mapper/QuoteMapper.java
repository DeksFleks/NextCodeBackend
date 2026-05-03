package dbataev.nextcode.model.mapper;

import dbataev.nextcode.model.base.Quote;
import dbataev.nextcode.model.dto.QuoteDto;

public class QuoteMapper {
    public static QuoteDto toDto(Quote quote) {
        QuoteDto quoteDto = new QuoteDto();

        quoteDto.setText(quote.getText());
        quoteDto.setAuthor(quote.getAuthor());

        return quoteDto;
    }
}
