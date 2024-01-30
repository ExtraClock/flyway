package org.flywaydb.database.spanner;

import lombok.CustomLog;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.parser.Parser;
import org.flywaydb.core.internal.parser.ParserContext;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.parser.PeekingReader;
import org.flywaydb.core.internal.parser.Token;
import org.flywaydb.core.internal.parser.TokenType;

import java.io.IOException;
import java.util.List;

@CustomLog
public class SpannerParser extends Parser {

    private boolean previousStatementStartedBatch = false;

    public SpannerParser(Configuration configuration, ParsingContext parsingContext) {
        super(configuration, parsingContext, 3);
    }

    @Override
    protected char getIdentifierQuote() {
        return '`';
    }

    protected char getAlternativeIdentifierQuote() {
        return '\"';
    }

    @Override
    protected Boolean detectCanExecuteInTransaction(String simplifiedStatement, List<Token> keywords) {
        LOG.debug("checking if [" + simplifiedStatement + "] can run in transaction");
        // Flyway tries to do hold transaction in which migration will happen
        return false;
    }

    @Override
    protected boolean shouldAdjustBlockDepth(ParserContext context, List<Token> tokens, Token token) {
        return previousStatementStartedBatch || super.shouldAdjustBlockDepth(context, tokens, token) || token.getType() == TokenType.DELIMITER;
    }

    @Override
    protected void adjustBlockDepth(ParserContext context, List<Token> tokens, Token keyword, PeekingReader reader) throws IOException {
        String keywordText = keyword.getText();
        if (previousStatementStartedBatch) {
            context.increaseBlockDepth("");
            previousStatementStartedBatch = false;
        }
        if (tokens.stream().anyMatch(t -> "START".equals(t.getText())) && keyword.getType() == TokenType.DELIMITER) {
            previousStatementStartedBatch = true;
        }
        if ("RUN".equals(keywordText) && context.getBlockDepth() > 0) {
            context.decreaseBlockDepth();
        }
        super.adjustBlockDepth(context, tokens, keyword, reader);
    }
}