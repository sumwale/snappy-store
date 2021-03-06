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
package com.gemstone.gemfire.internal.statistics;

import com.gemstone.gemfire.GemFireCheckedException;

/**
 * Indicates that a Statistics resource instance with a null StatisticsType
 * should be ignored by the statistics sampler.
 * <p/>
 * Extracted from {@link com.gemstone.gemfire.internal.StatArchiveWriter}.
 * 
 * @author Kirk Lund
 * @since 7.0
 */
public class IgnoreResourceException extends GemFireCheckedException {
  private static final long serialVersionUID = 3371071862581873081L;

  /**
   * Creates a new <code>IgnoreResourceException</code> with no detailed message.
   */
  public IgnoreResourceException() {
    super();
  }

  /**
   * Creates a new <code>IgnoreResourceException</code> with the given detail
   * message.
   */
  public IgnoreResourceException(String message) {
    super(message);
  }

  /**
   * Creates a new <code>IgnoreResourceException</code> with the given detail
   * message and cause.
   */
  public IgnoreResourceException(String message, Throwable cause) {
    super(message);
    this.initCause(cause);
  }
  
  /**
   * Creates a new <code>IgnoreResourceException</code> with the given cause and
   * no detail message
   */
  public IgnoreResourceException(Throwable cause) {
    super();
    this.initCause(cause);
  }
}
