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
package com.gemstone.gemfire.internal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;
import java.util.logging.Level;

import com.gemstone.gemfire.LogWriter;
import com.gemstone.gemfire.SystemFailure;
import com.gemstone.gemfire.distributed.internal.InternalDistributedSystem;
import com.gemstone.gemfire.i18n.LogWriterI18n;
import com.gemstone.gemfire.internal.i18n.LocalizedStrings;
import com.gemstone.org.jgroups.util.StringId;

/**
  * Abstract implementation of {@link LogWriterI18n}.
  * Each logger has a level and it will only print messages whose
  * level is greater than or equal to the logger's level. The supported
  * logger level constants, in ascending order, are:
  * <ol>
  * <li> {@link #ALL_LEVEL}
  * <li> {@link #FINEST_LEVEL}
  * <li> {@link #FINER_LEVEL}
  * <li> {@link #FINE_LEVEL}
  * <li> {@link #CONFIG_LEVEL}
  * <li> {@link #INFO_LEVEL}
  * <li> {@link #WARNING_LEVEL}
  * <li> {@link #ERROR_LEVEL}
  * <li> {@link #SEVERE_LEVEL}
  * <li> {@link #NONE_LEVEL}
  * </ol>
  * <p>
  * Subclasses must implement:
  * <ol>
  * <li> {@link #getLevel}
  * <li> {@link #put(int, String, Throwable)}
  * </ol>
  */
public abstract class LogWriterImpl
implements LogWriterI18n, LogWriter {
    // Constants
    /**
     * A bit mask to remove any potential flags added to the msgLevel.
     * Intended to be used in {@link #getRealLogLevel}.
     */
    private final static int LOGGING_FLAGS_MASK = 0x00FFFFFF;
    
    /**
     * A flag to indicate the {@link SecurityLogWriter#SECURITY_PREFIX} 
     * should be appended to the log level.
     */
    protected final static int SECURITY_LOGGING_FLAG = 0x40000000;

    /**
     * If the writer's level is <code>ALL_LEVEL</code> then all messages
     * will be logged.
     */
    public final static int ALL_LEVEL = Integer.MIN_VALUE;
    /**
     * If the writer's level is <code>FINEST_LEVEL</code> then
     * finest, finer, fine, config, info, warning, error, and severe messages will be logged.
     */
    public final static int FINEST_LEVEL = 300;
    /**
     * If the writer's level is <code>FINER_LEVEL</code> then
     * finer, fine, config, info, warning, error, and severe messages will be logged.
     */
    public final static int FINER_LEVEL = 400;
    /**
     * If the writer's level is <code>FINE_LEVEL</code> then
     * fine, config, info, warning, error, and severe messages will be logged.
     */
    public final static int FINE_LEVEL = 500;
    /**
     * If the writer's level is <code>CONFIG_LEVEL</code> then
     * config, info, warning, error, and severe messages will be logged.
     */
    public final static int CONFIG_LEVEL = 700;
    /**
     * If the writer's level is <code>INFO_LEVEL</code> then
     * info, warning, error, and severe messages will be logged.
     */
    public final static int INFO_LEVEL = 800;
    /**
     * If the writer's level is <code>WARNING_LEVEL</code> then
     * warning, error, and severe messages will be logged.
     */
    public final static int WARNING_LEVEL = 900;
    /**
     * If the writer's level is <code>SEVERE_LEVEL</code> then
     * only severe messages will be logged.
     */
    public final static int SEVERE_LEVEL = 1000;
    /**
     * If the writer's level is <code>ERROR_LEVEL</code> then
     * error and severe messages will be logged.
     */
    public final static int ERROR_LEVEL = (WARNING_LEVEL + SEVERE_LEVEL) / 2;
    /**
     * If the writer's level is <code>NONE_LEVEL</code> then no messages
     * will be logged.
     */
    public final static int NONE_LEVEL = Integer.MAX_VALUE;

  static {
      Assert.assertTrue( ALL_LEVEL == Level.ALL.intValue() );
      Assert.assertTrue( NONE_LEVEL == Level.OFF.intValue() );
      Assert.assertTrue( FINEST_LEVEL == Level.FINEST.intValue() );
      Assert.assertTrue( FINER_LEVEL == Level.FINER.intValue() );
      Assert.assertTrue( FINE_LEVEL == Level.FINE.intValue() );
      Assert.assertTrue( CONFIG_LEVEL == Level.CONFIG.intValue() );
      Assert.assertTrue( INFO_LEVEL == Level.INFO.intValue() );
      Assert.assertTrue( WARNING_LEVEL == Level.WARNING.intValue() );
      Assert.assertTrue( SEVERE_LEVEL == Level.SEVERE.intValue() );
      int logLevels = FINEST_LEVEL | 
                      FINER_LEVEL  | 
                      FINE_LEVEL   |
                      CONFIG_LEVEL |
                      INFO_LEVEL   |
                      WARNING_LEVEL|
                      SEVERE_LEVEL;
      Assert.assertTrue( logLevels == (logLevels & LOGGING_FLAGS_MASK) );
      Assert.assertTrue( 0 == (logLevels & SECURITY_LOGGING_FLAG) );
  }

    /**
     * The format string used to format the timestamp of log messages
     */
    public final static String FORMAT = "yyyy/MM/dd HH:mm:ss.SSS z";

    // Constructors
    protected LogWriterImpl() {
      this.timeFormatter = new SimpleDateFormat(FORMAT);
    }

    // Special Instance Methods on this class only
    /**
     * Gets the writer's level.
     */
    public abstract int getLevel();

    static final String[] levelNames = new String[] {
  "all", "finest", "finer", "fine", "config", "info", "warning",
  "error", "severe", "none"
    };

    public static final int[] allLevels = new int[] {
      ALL_LEVEL, FINEST_LEVEL, FINER_LEVEL, FINE_LEVEL, CONFIG_LEVEL, INFO_LEVEL,
      WARNING_LEVEL, ERROR_LEVEL, SEVERE_LEVEL, NONE_LEVEL
    };

    public static String allowedLogLevels() {
      StringBuilder b = new StringBuilder(64);
      for (int i=0; i < levelNames.length; i++) {
        if (i != 0) {
          b.append('|');
        }
        b.append(levelNames[i]);
      }
      return b.toString();
    }

    /**
     * Gets the string representation for the given <code>level</code> int code.
     */
    public static String levelToString(int level) {
      switch (level) {
      case ALL_LEVEL: return "all";
      case FINEST_LEVEL: return "finest";
      case FINER_LEVEL: return "finer";
      case FINE_LEVEL: return "fine";
      case CONFIG_LEVEL: return "config";
      case INFO_LEVEL: return "info";
      case WARNING_LEVEL: return "warning";
      case ERROR_LEVEL: return "error";
      case SEVERE_LEVEL: return "severe";
      case NONE_LEVEL: return "none";
      default: return levelToStringSpecialCase(level);
      }
    }
    /**
     * Handles the special cases for {@link #levelToString(int)} including
     * such cases as the SECURITY_LOGGING_PREFIX and an invalid log level.
     */
    private static String levelToStringSpecialCase(int levelWithFlags) {
      if((levelWithFlags & SECURITY_LOGGING_FLAG) != 0) {
        //We know the flag is set so XOR will zero it out.
        int level = levelWithFlags ^ SECURITY_LOGGING_FLAG;
        return SecurityLogWriter.SECURITY_PREFIX + levelToString(level);
      } else {
        //Needed to prevent infinite recursion
        //This signifies an unknown log level was used
        return "level-" + String.valueOf(levelWithFlags);
      }      
    }

    protected static int getRealLogLevel(int levelWithFlags) {
      if(levelWithFlags == NONE_LEVEL) {
        return levelWithFlags;
      }
      return levelWithFlags & LOGGING_FLAGS_MASK;
    }
    
    public static String join(Object[] a) {
      return join(a, " ");
    }
    public static String join(Object[] a, String joinString) {
      return join(Arrays.asList(a), joinString);
    }
    public static String join(List l) {
      return join(l, " ");
    }
    public static String join(List l, String joinString) {
      StringBuffer result = new StringBuffer(80);
      boolean firstTime = true;
      Iterator it = l.iterator();
      while (it.hasNext()) {
        if (firstTime) {
          firstTime = false;
        } else {
          result.append(joinString);
        }
        result.append(it.next());
      }
      return result.toString();
    }

    /**
     * Gets the level code for the given <code>levelName</code>.
     * @throws IllegalArgumentException if an unknown level name is given.
     */
    public static int levelNameToCode(String levelName) {
        if ("all".equalsIgnoreCase(levelName)) {
          return ALL_LEVEL;
        }
        if ("finest".equalsIgnoreCase(levelName)) {
          return FINEST_LEVEL;
        }
        if ("finer".equalsIgnoreCase(levelName)) {
          return FINER_LEVEL;
        }
        if ("fine".equalsIgnoreCase(levelName)) {
          return FINE_LEVEL;
        }
        if ("config".equalsIgnoreCase(levelName)) {
          return CONFIG_LEVEL;
        }
        if ("info".equalsIgnoreCase(levelName)) {
          return INFO_LEVEL;
        }
        if ("warning".equalsIgnoreCase(levelName)) {
          return WARNING_LEVEL;
        }
        if ("error".equalsIgnoreCase(levelName)) {
          return ERROR_LEVEL;
        }
        if ("severe".equalsIgnoreCase(levelName)) {
          return SEVERE_LEVEL;
        }
        if ("none".equalsIgnoreCase(levelName)) {
          return NONE_LEVEL;
        }
        try {
          if (levelName.startsWith("level-")) {
            String levelValue = levelName.substring("level-".length());
            return Integer.parseInt(levelValue);
          }
        } catch (NumberFormatException ignore) {
        } catch (NullPointerException ignore) {
        }
  throw new IllegalArgumentException("Unknown log-level \"" + levelName
                                           + "\". Valid levels are: "
                                           + join(levelNames)
                                           + ".");
    }

  /** A set of all created LoggingThreadGroups */
  private static final Collection loggingThreadGroups = new ArrayList();

  /**
   * Returns a <code>ThreadGroup</code> whose {@link
   * ThreadGroup#uncaughtException} method logs to both {#link
   * System#err} and the given <code>LogWriterI18n</code>.
   *
   * @param name
   *        The name of the <code>ThreadGroup</code>
   * @param logger
   *        A <code>LogWriterI18n</code> to log uncaught exceptions to.  It
   *        is okay for this argument to be <code>null</code>.
   *
   * author David Whitlock
   * @since 3.0
   */
  public static LoggingThreadGroup createThreadGroup(String name,
                                              final LogWriterI18n logger) {
    return createThreadGroup(name, logger, null);
  }
  
  public interface ThreadGroupCreator {
    LoggingThreadGroup create(String name, final LogWriterI18n logger);
  }
  
  public static LoggingThreadGroup createThreadGroup(String name,
        final LogWriterI18n logger, final ThreadGroupCreator creatorFactory ) {
    // Cache the LoggingThreadGroups so that we don't create a
    // gazillion of them.
    LoggingThreadGroup group = null;
    synchronized (loggingThreadGroups) {
      for (Iterator iter = loggingThreadGroups.iterator(); iter.hasNext(); ) {

        LoggingThreadGroup group2 = (LoggingThreadGroup) iter.next();
        if (group2.isDestroyed()) {
          // Clean is this guy out
          iter.remove();
          continue;
        }

        if (name.equals(group2.getName())) {
          // We already have one!
          // Change the underlying logger to point to new one (creating new
          //  thread groups for different loggers leaks groups for repeated
          //  connect/disconnects as in dunits for example)
          if (logger != group2.logger) {
            group2.logger = logger;
          }
          group = group2;
          break;
        }
      }

      if (group == null) {
        if(creatorFactory == null) {
          group = new LoggingThreadGroup(name, logger);
        }
        else {
          group = creatorFactory.create(name, logger);
        }
        // force autoclean to false and not inherit from parent group
        // (happens to be true for GemFireXD started threads as seen in #41438)
        group.setDaemon(false);
        loggingThreadGroups.add(group);
      }
    }

    Assert.assertTrue(!group.isDestroyed());
    return group;
  }

  public static void cleanUpThreadGroups() {
    synchronized (loggingThreadGroups) {
      LoggingThreadGroup group;
      Iterator<?> itr = loggingThreadGroups.iterator();
      while (itr.hasNext()) {
        group = (LoggingThreadGroup)itr.next();
        if (!group.getName().equals(
            InternalDistributedSystem.SHUTDOWN_HOOK_NAME)
            && !group.getName()
                .equals("GemFireConnectionFactory Shutdown Hook")) {
          group.cleanup();
          itr.remove();
        }
      }
    }
  }

    /**
     * Gets a String representation of the current time.
     * @return a String representation of the current time.
     */
    public String getTimeStamp() {
      return formatDate(new Date());
    }
    /**
     * Convert a Date to a timestamp String.
     * @param d a Date to format as a timestamp String.
     * @return a String representation of the current time.
     */
    public String formatDate(Date d) {
      try {
        synchronized (timeFormatter) {
          // Need sync: see bug 21858
          return timeFormatter.format(d);
        }
      } catch (Exception e1) {
        // Fix bug 21857
        try {
          return d.toString();
        } catch (Exception e2) {
          try {
            return Long.toString(d.getTime());
          } catch (Exception e3) {
            return "timestampFormatFailed";
          }
        }
      }
    }

    // Implementation of LogWriterI18n interface
    /**
     * Returns true if "severe" log messages are enabled.
     * Returns false if "severe" log messages are disabled.
     */
    public boolean severeEnabled() {
      return this.getLevel() <= SEVERE_LEVEL;
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "severe".
     */
    public void severe(String msg, Throwable ex) {
      if (this.severeEnabled()) {
        this.put(SEVERE_LEVEL, msg, ex);
      }
    }
    /**
     * Writes a message to this writer.
     * The message level is "severe".
     */
    public void severe(String msg) {
      this.severe(msg, null);
    }
    /**
     * Writes an exception to this writer.
     * The exception level is "severe".
     */
    public void severe(Throwable ex) {
      this.severe(LocalizedStrings.EMPTY, ex);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "severe".
     * @since 6.0 
     */
    public void severe(StringId msgID, Object[] params, Throwable ex) {
      if (this.severeEnabled()) {
        this.put(SEVERE_LEVEL, msgID, params, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "severe".
     * @since 6.0 
     */
    public void severe(StringId msgID, Object param, Throwable ex) {
      if (this.severeEnabled()) {
        this.put(SEVERE_LEVEL, msgID, new Object[] {param}, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "severe".
     * @since 6.0
     */
    public void severe(StringId msgID, Throwable ex) {
      severe(msgID, null, ex);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "severe".
     * @since 6.0 
     */
    public void severe(StringId msgID, Object[] params) {
      severe(msgID, params, null);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "severe".
     * @since 6.0
     */
    public void severe(StringId msgID, Object param) {
      severe(msgID, param, null);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "severe".
     * @since 6.0
     */
    public void severe(StringId msgID) {
      severe(msgID, null, null);
    }
    /**
     * @return true if "error" log messages are enabled.
     */
    public boolean errorEnabled() {
      return this.getLevel() <= ERROR_LEVEL;
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "error".
     */
    public void error(String msg, Throwable ex) {
      if (this.errorEnabled()) {
        this.put(ERROR_LEVEL, msg, ex);
      }
    }
    /**
     * Writes a message to this writer.
     * The message level is "error".
     */
    public void error(String msg) {
      this.error(msg, null);
    }
    /**
     * Writes an exception to this writer.
     * The exception level is "error".
     */
    public void error(Throwable ex) {
      this.error(LocalizedStrings.EMPTY, ex);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "error".
     * @since 6.0
     */
    public void error(StringId msgID, Object[] params, Throwable ex) {
      if (this.errorEnabled()) {
        this.put(ERROR_LEVEL, msgID, params, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "error".
     * @since 6.0
     */
    public void error(StringId msgID, Object param, Throwable ex) {
      if (this.errorEnabled()) {
        this.put(ERROR_LEVEL, msgID, new Object[] {param}, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "error".
     * @since 6.0
     */
    public void error(StringId msgID, Throwable ex) {
      error(msgID, null, ex); 
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "error".
     * @since 6.0
     */
    public void error(StringId msgID, Object[] params) {
      error(msgID, params, null); 
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "error".
     * @since 6.0
     */
    public void error(StringId msgID, Object param) {
      error(msgID, param, null); 
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "error".
     * @since 6.0
     */
    public void error(StringId msgID) {
      error(msgID, null, null); 
    }
    /**
     * @return true if "warning" log messages are enabled.
     */
    public boolean warningEnabled() {
      return this.getLevel() <= WARNING_LEVEL;
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "warning".
     */
    public void warning(String msg, Throwable ex) {
      if (this.warningEnabled()) {
        this.put(WARNING_LEVEL, msg, ex);
      }
    }
    /**
     * Writes a message to this writer.
     * The message level is "warning".
     */
    public void warning(String msg) {
      this.warning(msg, null);
    }
    /**
     * Writes an exception to this writer.
     * The exception level is "warning".
     */
    public void warning(Throwable ex) {
      this.warning(LocalizedStrings.EMPTY, ex);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "warning".
     * @since 6.0
     */
    public void warning(StringId msgID, Object[] params, Throwable ex) {
      if (this.warningEnabled()) {
        this.put(WARNING_LEVEL, msgID, params, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "warning".
     * @since 6.0
     */
    public void warning(StringId msgID, Object param, Throwable ex) {
      if (this.warningEnabled()) {
        this.put(WARNING_LEVEL, msgID, new Object[] {param}, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "warning".
     * @since 6.0
     */
    public void warning(StringId msgID, Throwable ex) {
      warning(msgID, null, ex);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "warning".
     * @since 6.0
     */
    public void warning(StringId msgID, Object[] params) {
      warning(msgID, params, null);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "warning".
     * @since 6.0
     */
    public void warning(StringId msgID, Object param) {
      warning(msgID, param, null);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "warning".
     * @since 6.0
     */
    public void warning(StringId msgID) {
      warning(msgID, null, null);
    }
    /**
     * @return true if "info" log messages are enabled.
     */
    public boolean infoEnabled() {
      return this.getLevel() <= INFO_LEVEL
          /* (bug 29581) && !SmHelper._memorySpaceLow() */;
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "information".
     */
    public void info(String msg, Throwable ex) {
      if (this.infoEnabled()) {
        this.put(INFO_LEVEL, msg, ex);
      }
    }
    /**
     * Writes a message to this writer.
     * The message level is "information".
     */
    public void info(String msg) {
      this.info(msg, null);
    }
    /**
     * Writes an exception to this writer.
     * The exception level is "information".
     */
    public void info(Throwable ex) {
      this.info(LocalizedStrings.EMPTY, ex);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "info".
     * @since 6.0
     */
    public void info(StringId msgID, Object[] params, Throwable ex) {
      if (this.infoEnabled()) {
        this.put(INFO_LEVEL, msgID, params, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "info".
     * @since 6.0
     */
    public void info(StringId msgID, Object param, Throwable ex) {
      if (this.infoEnabled()) {
        this.put(INFO_LEVEL, msgID, new Object[] {param}, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "info".
     * @since 6.0
     */
    public void info(StringId msgID, Throwable ex) {
      info(msgID, null, ex); 
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "info".
     * @since 6.0
     */
    public void info(StringId msgID, Object[] params) {
      info(msgID, params, null); 
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "info".
     * @since 6.0
     */
    public void info(StringId msgID, Object param) {
      info(msgID, param, null); 
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "info".
     * @since 6.0
     */
    public void info(StringId msgID) {
      info(msgID, null, null); 
    }
    /**
     * @return true if "config" log messages are enabled.
     */
    public boolean configEnabled() {
      return this.getLevel() <= CONFIG_LEVEL;
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "config".
     */
    public void config(String msg, Throwable ex) {
      if (this.configEnabled()) {
        this.put(CONFIG_LEVEL, msg, ex);
      }
    }
    /**
     * Writes a message to this writer.
     * The message level is "config".
     */
    public void config(String msg) {
      this.config(msg, null);
    }
    /**
     * Writes an exception to this writer.
     * The exception level is "config".
     */
    public void config(Throwable ex) {
      this.config(LocalizedStrings.EMPTY, ex);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "config".
     * @since 6.0
     */
    public void config(StringId msgID, Object[] params, Throwable ex) {
      if (this.configEnabled()) {
        this.put(CONFIG_LEVEL, msgID, params, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "config".
     * @since 6.0
     */
    public void config(StringId msgID, Object param, Throwable ex) {
      if (this.configEnabled()) {
        this.put(CONFIG_LEVEL, msgID, new Object[] {param}, ex);
      }
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "config".
     * @since 6.0
     */
    public void config(StringId msgID, Throwable ex) {
      config(msgID, null, ex);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "config".
     * @since 6.0
     */
    public void config(StringId msgID, Object[] params) {
      config(msgID, params, null);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "config".
     * @since 6.0
     */
    public void config(StringId msgID, Object param) {
      config(msgID, param, null);
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "config".
     * @since 6.0
     */
    public void config(StringId msgID) {
      config(msgID, null, null);
    }
    /**
     * @return true if "fine" log messages are enabled.
     */
    public boolean fineEnabled() {
      return this.getLevel() <= FINE_LEVEL
      /* (bug 29581) && !SmHelper._memorySpaceLow() */;
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "fine".
     */
    public void fine(String msg, Throwable ex) {
      if (this.fineEnabled()) {
        this.put(FINE_LEVEL, msg, ex);
      }
    }
    /**
     * Writes a message to this writer.
     * The message level is "fine".
     */
    public void fine(String msg) {
      this.fine(msg, null);
    }
    /**
     * Writes an exception to this writer.
     * The exception level is "fine".
     */
    public void fine(Throwable ex) {
      this.fine(null, ex);
    }
    /**
     * Returns true if "finer" log messages are enabled.
     * Returns false if "finer" log messages are disabled.
     */
    public boolean finerEnabled() {
      return this.getLevel() <= FINER_LEVEL
      /* (bug 29581) && !SmHelper._memorySpaceLow() */;
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "finer".
     */
    public void finer(String msg, Throwable ex) {
      if (this.finerEnabled()) {
        this.put(FINER_LEVEL, msg, ex);
      }
    }
    /**
     * Writes a message to this writer.
     * The message level is "finer".
     */
    public void finer(String msg) {
      this.finer(msg, null);
    }
    /**
     * Writes an exception to this writer.
     * The exception level is "finer".
     */
    public void finer(Throwable ex) {
      this.finer(null, ex);
    }
    /**
     * Log a method entry.
     * <p>The logging is done using the <code>finer</code> level.
     * The string message will start with <code>"ENTRY"</code> and
     * include the class and method names.
     * @param sourceClass Name of class that issued the logging request.
     * @param sourceMethod Name of the method that issued the logging request.
     */
    public void entering(String sourceClass, String sourceMethod) {
      if (this.finerEnabled()) {
        this.finer("ENTRY " + sourceClass + ":" + sourceMethod);
      }
    }
    /**
     * Log a method return.
     * <p>The logging is done using the <code>finer</code> level.
     * The string message will start with <code>"RETURN"</code> and
     * include the class and method names.
     * @param sourceClass Name of class that issued the logging request.
     * @param sourceMethod Name of the method that issued the logging request.
     */
    public void exiting(String sourceClass, String sourceMethod) {
      if (this.finerEnabled()) {
        this.finer("RETURN " + sourceClass + ":" + sourceMethod);
      }
    }
    /**
     * Log throwing an exception.
     * <p> Use to log that a method is
     * terminating by throwing an exception. The logging is done using
     * the <code>finer</code> level.
     * <p> This is a convenience method that could be done
     * instead by calling {@link #finer(String, Throwable)}.
     * The string message will start with <code>"THROW"</code> and
     * include the class and method names.
     * @param sourceClass Name of class that issued the logging request.
     * @param sourceMethod Name of the method that issued the logging request.
     * @param thrown The Throwable that is being thrown.
     */
    public void throwing(String sourceClass, String sourceMethod,
       Throwable thrown) {
      if (this.finerEnabled()) {
        this.finer("THROW " + sourceClass + ":" + sourceMethod, thrown);
      }
    }
    /**
     * Returns true if "finest" log messages are enabled.
     * Returns false if "finest" log messages are disabled.
     */
    public boolean finestEnabled() {
      return this.getLevel() <= FINEST_LEVEL
      /* (bug 29581) && !SmHelper._memorySpaceLow() */;
    }
    /**
     * Writes both a message and exception to this writer.
     * The message level is "finest".
     */
    public void finest(String msg, Throwable ex) {
      if (this.finestEnabled()) {
        this.put(FINEST_LEVEL, msg, ex);
      }
    }
    /**
     * Writes a message to this writer.
     * The message level is "finest".
     */
    public void finest(String msg) {
      this.finest(msg, null);
    }
    /**
     * Writes an exception to this writer.
     * The exception level is "finest".
     */
    public void finest(Throwable ex) {
      this.finest(null, ex);
    }

    // internal implemenation methods
    /**
     * Logs a message and an exception of the given level.
     * 
     * @param msgLevel
     *                the level code for the message to log
     * @param msg
     *                the actual message to log
     * @param exception
     *                the actual Exception to log
     */
    protected abstract void put(int msgLevel, String msg, Throwable exception);
    
    /**
     * Logs a message and an exception of the given level.
     * 
     * @param msgLevel
     *                the level code for the message to log
     * @param msgId
     *                A locale agnostic form of the message
     * @param params
     *                the Object arguments to plug into the message               
     * @param exception
     *                the actual Exception to log
     */
    protected abstract void put(int msgLevel, StringId msgId, Object[] params, Throwable exception);

    static void formatText(PrintWriter writer, String target,
                           int initialLength) {
      BreakIterator boundary = BreakIterator.getLineInstance();
      boundary.setText(target);
      int start = boundary.first();
      int end = boundary.next();
      int lineLength = initialLength;

      while (end != BreakIterator.DONE) {
        // Look at the end and only accept whitespace breaks
        char endChar = target.charAt(end-1);
        while (!Character.isWhitespace(endChar)) {
          int lastEnd = end;
          end = boundary.next();
          if (end == BreakIterator.DONE) {
            // give up. We are at the end of the string
            end = lastEnd;
            break;
          }
          endChar = target.charAt(end-1);
        }
        int wordEnd = end;
        if (endChar == '\n') {
          // trim off the \n since println will do it for us
          wordEnd--;
          if (wordEnd > 0 && target.charAt(wordEnd-1) == '\r') {
            wordEnd--;
          }
        } else if (endChar == '\t') {
          // figure tabs use 8 characters
          lineLength += 7;
        }
      String word = target.substring(start, wordEnd);
      lineLength += word.length();
      writer.print(word);
      if (endChar == '\n' || endChar == '\r') {
        // force end of line
        writer.println();
        writer.print("  ");
        lineLength = 2;
      }
      start = end;
      end = boundary.next();
    }
    if (lineLength != 0) {
      writer.println();
    }
  }

  /**
   * Check if a message of the given level would actually be logged by this
   * logger.
   * This check is based on the Logger effective level.
   * @param level a message logging level
   * @return true if the given message level is currently being logged.
   */
  public boolean isLoggable(int level) {
      return this.getLevel() <= level;
  }

  /**
   * Log a message, with associated Throwable information.
   * If the logger is currently enabled for the given message level then the
   * given message is logged.
   * @param level One of the message level identifiers, e.g. SEVERE
   * @param message The string message
   * @param thrown - Throwable associated with log message.
   */
  public void log(int level, String message, Throwable thrown) {
      if (isLoggable(level)) {
        this.put(level, message, thrown);
      }
  }

  /**
   * Log a message.
   * If the logger is currently enabled for the given message level then the
   * given message is logged.
   * @param level One of the message level identifiers, e.g. SEVERE
   * @param message The string message
   */
  public void log(int level, String message) {
      if (isLoggable(level)) {
        this.put(level, message, null);
      }
  }

  public Handler getHandler() {
    return new GemFireHandler(this);
  }

  /** Utility to get a stack trace as a string from a Throwable */
  public static String getStackTrace(Throwable aThrowable) {
   StringWriter sw = new StringWriter();
   aThrowable.printStackTrace(new PrintWriter(sw, true));
   return sw.toString();
  }
  
  /**
   * Utility to periodically log a stack trace for a thread.  The thread should
   * be alive when this method is invoked.
   * @param toStdout whether to log to stdout or to this log writer
   * @param targetThread the thread to snapshot
   * @param interval millis to wait betweeen snaps
   * @param done when to stop snapshotting (also ceases if targetThread dies)
   */
  public void logTraces(final boolean toStdout,
      final Thread targetThread, final int interval, final AtomicBoolean done) {
    if (targetThread == null) {
      return;
    }
    Thread watcherThread = new Thread("Stack Tracer for '" + targetThread.getName() + "'") {
      @Override
      public void run() {
        while (!done.get()) {
          try { Thread.sleep(500); } catch (InterruptedException e) { return; } 
          if (!done.get() && targetThread.isAlive()) {
            StringBuffer sb = new StringBuffer(500);
            if (toStdout) {
              sb.append("[trace ").append(getTimeStamp()).append("] ");
            }
            StackTraceElement[] els = targetThread.getStackTrace();
            sb.append("Stack trace for '").append(targetThread.toString()).append("'\n");
            if (els.length > 0) {
              for (int i=0; i<els.length; i++) {
                sb.append("\tat ").append(els[i].toString()).append("\n");
              }
            }
            else {
              sb.append("    no stack\n");
            }
            if (toStdout) {
              System.out.println(sb.toString());
            } else {
              info(LocalizedStrings.DEBUG, sb.toString());
            }
          }
        }
      }
    };
    watcherThread.start();
  }

  /** Utility to get a stack trace for a thread */
  public static StringBuilder getStackTrace(Thread targetThread) {
    StringBuilder sb = new StringBuilder(500);
    StackTraceElement[] els = targetThread.getStackTrace();
    sb.append("Stack trace for '").append(targetThread.toString()).append("'\n");
    if (els.length > 0) {
      for (int i=0; i<els.length; i++) {
        sb.append("\tat ").append(els[i].toString()).append("\n");
      }
    }
    else {
      sb.append("    no stack\n");
    }
    return sb;
  }


  // instance variables
  private final SimpleDateFormat timeFormatter;

  ///////////////////////  Inner Classes  ///////////////////////

  public static abstract class GemFireThreadGroup extends ThreadGroup {

    private boolean isInterruptible;

    public GemFireThreadGroup(String name) {
      super(name);
    }

    public GemFireThreadGroup(ThreadGroup parent, String name) {
      super(parent, name);
    }
 
    public void setInterruptible() {
      this.isInterruptible = true;
    }

    /**
     * Indicates that the thread can be safely interrupted by external layers
     * (e.g. GemFireXD). GemFire layer can always interrupt its threads.
     */
    public boolean isInterruptible() {
      return this.isInterruptible;
    }
  }

  /**
   * A <code>ThreadGroup</code> that logs all {@linkplain
   * #uncaughtException uncaught exceptions} to a GemFire
   * <code>LogWriterI18n</code>.   It also keeps track of the uncaught
   * exceptions that were thrown by its threads.  This is comes in
   * handy when a thread fails to initialize properly (see bug
   * 32550).
   *
   * @see LogWriterImpl#createThreadGroup
   *
   * @author David Whitlock
   * @since 4.0
   */
  public static class LoggingThreadGroup extends GemFireThreadGroup {

    /** A "local" log writer that logs exceptions to standard error */
    private static final LogWriterI18n local =
      new LocalLogWriter(ALL_LEVEL, System.err);

    ////////////////////  Instance Fields  ////////////////////

    /** A log writer that the user has specified for logging uncaught
     * exceptions. */
    protected volatile LogWriterI18n logger;

    /** The count uncaught exceptions that were thrown by threads in this
     * thread group. */
    private long uncaughtExceptionsCount;

    ///////////////////////  Constructors  ///////////////////////

    /**
     * Creates a new <code>LoggingThreadGroup</code> that logs
     * uncaught exceptions to the given log writer.
     *
     * @param name
     *        The name of the thread group
     * @param logger
     *        A logger to which uncaught exceptions are logged.  May
     *        be <code>null</code>.
     */
    public LoggingThreadGroup(String name, LogWriterI18n logger) {
      super(name);
      this.logger = logger;
    }
    
    private final Object dispatchLock = new Object();

    /**
     * Logs an uncaught exception to a log writer
     */
    @Override
    public void uncaughtException(Thread t, Throwable ex) {
      synchronized(this.dispatchLock) {
        if (ex instanceof Error && SystemFailure.isJVMFailureError((Error)ex)) {
          SystemFailure.setFailure((Error)ex); // don't throw
        }
        final LogWriterI18n logger = this.logger;
        // Hack for Adobe to treat the shutdown hook error as a special case.
        // Do not change the hook's thread name without also changing it here.
        String threadName = t.getName();  
        if ((ex instanceof NoClassDefFoundError) 
            && (threadName.equals(InternalDistributedSystem.SHUTDOWN_HOOK_NAME)))
        {
          final StringId msg = LocalizedStrings.UNCAUGHT_EXCEPTION_IN_THREAD_0_THIS_MESSAGE_CAN_BE_DISREGARDED_IF_IT_OCCURED_DURING_AN_APPLICATION_SERVER_SHUTDOWN_THE_EXCEPTION_MESSAGE_WAS_1;
          final Object[] msgArgs = new Object[] {t, ex.getLocalizedMessage()};
          local.info(msg, msgArgs);
          if (logger != null) {
            logger.info(msg, msgArgs);
          }
        } else { 
          local.severe(LocalizedStrings.UNCAUGHT_EXCEPTION_IN_THREAD_0, t, ex);
          if (logger != null) {
            logger.severe(LocalizedStrings.UNCAUGHT_EXCEPTION_IN_THREAD_0, t, ex);
          }
        }
        //if (!(ex instanceof RuntimeException) && (ex instanceof Exception)) {
        // something's fishy - checked exceptions shouldn't get here
        //  this.logger.severe("stack trace showing origin of uncaught checked exception", new Exception("stack trace"));
        //}
        this.uncaughtExceptionsCount++;
      }
    }


    /**
     * clear number of uncaught exceptions
     */
    public void clearUncaughtExceptionsCount() {
      synchronized(this.dispatchLock) {
        this.uncaughtExceptionsCount = 0;
      }
    }
    

    /**
     * Returns the number of uncaught exceptions that occurred in threads in
     * this thread group.
     */
    public long getUncaughtExceptionsCount() {
      synchronized(this.dispatchLock) {
        return uncaughtExceptionsCount;
      }
    }
    
    /**
     * clean up the threadgroup, releasing resources that could be problematic
     * (bug 35388)
     * @since 4.2.3
     */
    public synchronized void cleanup() {
      // the logwriter holds onto a distribution config, which holds onto
      // the InternalDistributedSystem, which holds onto the
      // DistributionManager, which holds onto ... you get the idea
      this.logger = null;
    }
  }

  /*
   * @see com.gemstone.gemfire.LogWriter
   * @since 6.0
   */
  public com.gemstone.gemfire.LogWriter convertToLogWriter() {
    return this;
  }
  
  /*
   * @see com.gemstone.gemfire.LogWriterI18n
   * @since 6.0
   */
  public LogWriterI18n convertToLogWriterI18n() {
    return this;
  }

  public boolean isClosed() {
    return false;
  }

  /**
   * Used to convert the given object to a String. If anything goes wrong in this conversion
   * put some info about what went wrong on the result string but do not throw an exception.
   * @param o the object to convert to a string
   * @return the string from of the given object.
   */
  public static String forceToString(Object o) {
    try {
      return o.toString();
    } catch (RuntimeException ex) {
      return "Conversion to a string failed because " + ex;
    }
  }

  /**
   * Note: Must be used for test purposes ONLY.
   *
   * @param threadGroupName
   * @return thread group with given name.
   */
  public static ThreadGroup getThreadGroup(String threadGroupName) {
    
    for (Object object: loggingThreadGroups) {
      LoggingThreadGroup threadGroup = (LoggingThreadGroup)object;
      if (threadGroup.getName().equals(threadGroupName)) {
        return threadGroup;
      }
    }
    return null;
  }
}
