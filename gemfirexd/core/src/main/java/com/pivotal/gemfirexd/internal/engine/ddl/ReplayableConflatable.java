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

package com.pivotal.gemfirexd.internal.engine.ddl;

import com.gemstone.gemfire.internal.cache.Conflatable;
import com.pivotal.gemfirexd.internal.iapi.error.StandardException;

/**
 * Interface for {@link Conflatable}s that can be replayed by GemFireXD DDL replay
 * in bootup.
 *
 * @author swale
 * @since gfxd 1.0
 */
public interface ReplayableConflatable extends Conflatable {

  /**
   * If this returns true then local execution for this object should be skipped
   * since it has already been considered at least once.
   */
  boolean skipInLocalExecution();

  /**
   * Mark this Conflatable as executing
   */
  void markExecuting();

  /**
   * Returns true if this object is currently being executed, or has finished
   * execution (and hence is not a valid candidate for conflation).
   */
  boolean isExecuting();

  /**
   * Get the equivalent SQL statement for this message.
   *
   * @return the equivalent SQL statement for this message
   */
  String getSQLStatement();
}
