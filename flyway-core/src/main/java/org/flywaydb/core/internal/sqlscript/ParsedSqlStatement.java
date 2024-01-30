package org.flywaydb.core.internal.sqlscript;

import lombok.Getter;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.jdbc.Results;

/**
 * A sql statement from a script that can be executed at once against a database.
 */
public class ParsedSqlStatement implements SqlStatement {
    @Getter
    private final int pos;
    @Getter
    private final int line;
    @Getter
    private final int col;
    @Getter(onMethod = @__(@Override))
    private final String sql;

    /**
     * The delimiter of the statement.
     */
    private final Delimiter delimiter;

    private final boolean canExecuteInTransaction;

    /**
     * Whether this statement can be run as part of batch or whether it must be run individually.
     */
    @Getter(onMethod = @__(@Override))
    private final boolean batchable;

    public ParsedSqlStatement(int pos, int line, int col, String sql, Delimiter delimiter,
                              boolean canExecuteInTransaction, boolean batchable) {
        this.pos = pos;
        this.line = line;
        this.col = col;
        this.sql = sql;
        this.delimiter = delimiter;
        this.canExecuteInTransaction = canExecuteInTransaction;
        this.batchable = batchable;
    }

    @Override
    public final int getLineNumber() {
        return line;
    }

    @Override
    public String getDelimiter() {
        return delimiter.toString();
    }

    @Override
    public boolean canExecuteInTransaction() {
        return canExecuteInTransaction;
    }








    @Override
    public Results execute(JdbcTemplate jdbcTemplate




                          ) {
        return jdbcTemplate.executeStatement(sql);
    }
}