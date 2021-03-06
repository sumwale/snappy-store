/*
 * Copyright (c) 2010-2015 Pivotal Software, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License. See accompanying
 * LICENSE file.
 */
package sql.sqlutil;

import hydra.Log;
import hydra.TestConfig;
import sql.SQLPrms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLStmt {
  private static final Pattern SUBSTITUTION_PATTERN = Pattern.compile("\\?\\?"); 
  private static boolean logDML = TestConfig.tab().booleanAt(SQLPrms.logDML, false);
  private String orig_sql;
  private String sql;

  /**
   * For each unique '??' that we encounter in the SQL for this Statement,
   * we will substitute it with the number of '?' specified in this array. 
   */
  private final int substitutions[];
  
  /**
   * Constructor
   * @param sql
   * @param substitutions
   */
  public SQLStmt(String sql, int...substitutions) {
    this.substitutions = substitutions;
    this.setSQL(sql);
  }
  
  /**
   * Magic SQL setter!
   * Each occurrence of the pattern "??" will be replaced by a string
   * of repeated ?'s
   * @param sql
   * @param substitutions
   */
  protected final void setSQL(String sql) {
    this.orig_sql = sql;
    for (int ctr : this.substitutions) {
        assert(ctr > 0);
        String replace = "";
        for (int i = 0; i < ctr; i++) {
            replace += (i > 0 ? ", " : "") + "?";
        } // FOR
        Matcher m = SUBSTITUTION_PATTERN.matcher(sql);
        sql = m.replaceFirst(replace);
    } // FOR
    this.sql = sql;
    //if (logDML) Log.getLogWriter().info("Initialized SQL:\n" + this.sql);
  }
  
  public final String getSQL() {
    return (this.sql);
  }
  
  protected final String getOriginalSQL() {
    return (this.orig_sql);
  }

  @Override
  public String toString() {
    return "SQLStmt{" + this.sql + "}";
  }
  
}
