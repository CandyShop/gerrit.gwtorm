// Copyright 2010 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gwtorm.nosql;

import com.google.gwtorm.client.OrmException;
import com.google.gwtorm.client.Schema;
import com.google.gwtorm.client.StatementExecutor;
import com.google.gwtorm.server.AbstractSchema;

/** Internal base class for implementations of {@link Schema}. */
public abstract class NoSqlSchema extends AbstractSchema {
  private boolean autoFlush = true;

  protected NoSqlSchema(final NoSqlDatabase<?, ?, ?> d) {
  }

  @Override
  public boolean isAutoFlush() {
    return autoFlush;
  }

  @Override
  public void setAutoFlush(boolean autoFlush) throws OrmException {
    if (!this.autoFlush && autoFlush) {
      flush();
    }

    this.autoFlush = autoFlush;
  }

  @Override
  public void pruneSchema(StatementExecutor e) throws OrmException {
    // Assume no action is required in a default NoSQL environment.
  }

  @Override
  public void updateSchema(StatementExecutor e) throws OrmException {
    // Assume no action is required in a default NoSQL environment.
  }
}
