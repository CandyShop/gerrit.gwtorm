package com.google.gwtorm.schema.sql;

import com.google.gwtorm.client.OrmDuplicateKeyException;
import com.google.gwtorm.client.OrmException;
import com.google.gwtorm.schema.RelationModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/** Dialect for <a href="http://www.postgresql.org/>PostgreSQL</a> */
public class DialectPostgreSQL extends SqlDialect {
  public DialectPostgreSQL() {
    typeNames.put(Types.VARBINARY, "BYTEA");
    typeNames.put(Types.TIMESTAMP, "TIMESTAMP WITH TIME ZONE");
  }

  @Override
  public SqlDialect refine(final Connection c) throws SQLException {
    final int major = c.getMetaData().getDatabaseMajorVersion();
    final int minor = c.getMetaData().getDatabaseMinorVersion();
    if (major < 8 || (major == 8 && minor < 2)) {
      return new Pre82();
    }
    return this;
  }

  @Override
  public OrmException convertError(final String op, final String entity,
      final SQLException err) {
    switch (getSQLStateInt(err)) {
      case 23505: // UNIQUE CONSTRAINT VIOLATION
        return new OrmDuplicateKeyException(entity, err);

      case 23514: // CHECK CONSTRAINT VIOLATION
      case 23503: // FOREIGN KEY CONSTRAINT VIOLATION
      case 23502: // NOT NULL CONSTRAINT VIOLATION
      case 23001: // RESTRICT VIOLATION
      default:
        return super.convertError(op, entity, err);
    }
  }

  @Override
  public String getNextSequenceValueSql(final String seqname) {
    return "SELECT nextval('" + seqname + "')";
  }

  @Override
  public void appendCreateTableStorage(final StringBuilder sqlBuffer,
      final RelationModel relationModel) {
    sqlBuffer.append("WITH (OIDS = FALSE)");
  }

  private static class Pre82 extends DialectPostgreSQL {
    @Override
    public void appendCreateTableStorage(final StringBuilder sqlBuffer,
        final RelationModel relationModel) {
      sqlBuffer.append("WITHOUT OIDS");
    }
  }
}
