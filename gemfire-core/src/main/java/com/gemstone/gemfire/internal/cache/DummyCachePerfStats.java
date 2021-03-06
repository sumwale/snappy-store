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
package com.gemstone.gemfire.internal.cache;

import com.gemstone.gemfire.Statistics;
import com.gemstone.gemfire.distributed.internal.PoolStatHelper;

/**
 * @author ashetkar
 * @since 5.7
 */
public class DummyCachePerfStats extends CachePerfStats {

  // ////////////////////// Constructors ////////////////////////

  /**
   * Creates a new <code>DummyCachePerfStats</code>
   */
  public DummyCachePerfStats() {
  }

  // //////////////////// Accessing Stats //////////////////////

  @Override
  public int getLoadsInProgress() {
    return 0;
  }

  @Override
  public int getLoadsCompleted() {
    return 0;
  }

  @Override
  public long getLoadTime() {
    return 0;
  }

  @Override
  public int getNetloadsInProgress() {
    return 0;
  }

  @Override
  public int getNetloadsCompleted() {
    return 0;
  }

  @Override
  public long getNetloadTime() {
    return 0;
  }

  @Override
  public int getNetsearchesInProgress() {
    return 0;
  }

  @Override
  public int getNetsearchesCompleted() {
    return 0;
  }

  @Override
  public long getNetsearchTime() {
    return 0;
  }

  @Override
  public int getGetInitialImagesInProgress() {
    return 0;
  }

  @Override
  public int getGetInitialImagesCompleted() {
    return 0;
  }

  @Override
  public long getGetInitialImageTime() {
    return 0;
  }

  @Override
  public int getGetInitialImageKeysReceived() {
    return 0;
  }

  @Override
  public int getGetInitialImageTransactionsReceived() {
    return 0;
  }

  @Override
  public int getRegions() {
    return 0;
  }

  @Override
  public int getPartitionedRegions() {
    return 0;
  }

  @Override
  public int getDestroys() {
    return 0;
  }

  @Override
  public int getCreates() {
    return 0;
  }

  @Override
  public int getPuts() {
    return 0;
  }

  @Override
  public int getPutAlls() {
    return 0;
  }

  @Override
  public int getUpdates() {
    return 0;
  }

  @Override
  public int getInvalidates() {
    return 0;
  }

  @Override
  public int getGets() {
    return 0;
  }

  @Override
  public int getMisses() {
    return 0;
  }

  @Override
  public int getReliableQueuedOps() {
    return 0;
  }

  @Override
  public void incReliableQueuedOps(int inc) {
  }

  @Override
  public int getReliableQueueSize() {
    return 0;
  }

  @Override
  public void incReliableQueueSize(int inc) {
  }

  @Override
  public int getReliableQueueMax() {
    return 0;
  }

  @Override
  public void incReliableQueueMax(int inc) {
  }

  @Override
  public int getReliableRegions() {
    return 0;
  }

  @Override
  public void incReliableRegions(int inc) {
  }

  @Override
  public int getReliableRegionsMissing() {
    return 0;
  }

  @Override
  public void incReliableRegionsMissing(int inc) {
  }

  @Override
  public int getReliableRegionsQueuing() {
    return 0;
  }

  @Override
  public void incReliableRegionsQueuing(int inc) {
  }

  @Override
  public int getReliableRegionsMissingFullAccess() {
    return 0;
  }

  @Override
  public void incReliableRegionsMissingFullAccess(int inc) {
  }

  @Override
  public int getReliableRegionsMissingLimitedAccess() {
    return 0;
  }

  @Override
  public void incReliableRegionsMissingLimitedAccess(int inc) {
  }

  @Override
  public int getReliableRegionsMissingNoAccess() {
    return 0;
  }

  @Override
  public void incReliableRegionsMissingNoAccess(int inc) {
  }

  @Override
  public void incQueuedEvents(int inc) {
  }

  @Override
  public long getQueuedEvents() {
    return 0;
  }

  @Override
  public int getDeltaUpdates() {
    return 0;
  }

  @Override
  public long getDeltaUpdatesTime() {
    return 0;
  }

  @Override
  public int getDeltaFailedUpdates() {
    return 0;
  }

  @Override
  public int getDeltasPrepared() {
    return 0;
  }

  @Override
  public long getDeltasPreparedTime() {
    return 0;
  }

  @Override
  public int getDeltasSent() {
    return 0;
  }

  @Override
  public int getDeltaFullValuesSent() {
    return 0;
  }

  @Override
  public int getDeltaFullValuesRequested() {
    return 0;
  }

  @Override
  public long getTotalCompressionTime() {
    return 0;
  }

  @Override
  public long getTotalDecompressionTime() {
    return 0;
  }

  @Override
  public long getTotalCompressions() {
    return 0;
  }

  @Override
  public long getTotalDecompressions() {
    return 0;
  }

  @Override
  public long getTotalPreCompressedBytes() {
    return 0;
  }

  @Override
  public long getTotalPostCompressedBytes() {
    return 0;
  }

  @Override
  public long getTotalCompressionsSkipped() {
    return 0;
  }

  // //////////////////// Updating Stats //////////////////////

  @Override
  public long startCompression() {
    return 0;
  }

  @Override
  public void endCompression(long startTime, long startSize, long endSize) {
  }

  @Override
  public void endCompressionSkipped(long startTime, long startSize) {
  }

  @Override
  public void incDecompressedReplaced() {
  }

  @Override
  public void incDecompressedReplaceSkipped() {
  }

  @Override
  public void incCompressedReplaceSkipped() {
  }

  @Override
  public long startDecompression() {
    return 0;
  }

  @Override
  public void endDecompression(long startTime) {
  }

  @Override
  public long startLoad() {
    return 0;
  }

  @Override
  public void endLoad(long start) {
  }

  @Override
  public long startNetload() {
    return 0;
  }

  @Override
  public void endNetload(long start) {
  }

  @Override
  public long startNetsearch() {
    return 0;
  }

  @Override
  public void endNetsearch(long start) {
  }

  @Override
  public long startCacheWriterCall() {
    return 0;
  }

  @Override
  public void endCacheWriterCall(long start) {
  }

  @Override
  public long startCacheListenerCall() {
    return 0;
  }

  @Override
  public void endCacheListenerCall(long start) {}

  @Override
  public long startGetInitialImage() {
    return 0;
  }

  @Override
  public void endGetInitialImage(long start) {
  }
  
  @Override
  public void endNoGIIDone(long start) {
  }

  @Override
  public void incGetInitialImageKeysReceived(int inc) {
  }

  @Override
  public void incGetInitialImageTransactionsReceived(int inc) {
  }

  @Override
  public void incRegions(int inc) {
  }

  @Override
  public void incPartitionedRegions(int inc) {
  }

  @Override
  public void incDestroys() {
  }

  @Override
  public void incCreates() {
  }

  @Override
  public void incInvalidates() {
  }

  @Override
  public long startGet() {
    return 0;
  }

  @Override
  public void endGet(long start, boolean miss) {
  }

  @Override
  public long endPut(long start, boolean isUpdate) {
    return 0;
  }

  @Override
  public void endPutAll(long start) {
  }

  @Override
  public void endQueryExecution(long executionTime) {
  }

  @Override
  public int getTxCommits() {
    return 0;
  }

  @Override
  public int getTxCommitChanges() {
    return 0;
  }

  @Override
  public long getTxCommitTime() {
    return 0;
  }

  @Override
  public long getTxSuccessLifeTime() {
    return 0;
  }

  @Override
  public int getTxFailures() {
    return 0;
  }

  @Override
  public int getTxFailureChanges() {
    return 0;
  }

  @Override
  public long getTxFailureTime() {
    return 0;
  }

  @Override
 public long getTxFailedLifeTime() {
    return 0;
  }

  @Override
  public int getTxRollbacks() {
    return 0;
  }

  @Override
  public int getTxRollbackChanges() {
    return 0;
  }

  @Override
  public long getTxRollbackTime() {
    return 0;
  }

  @Override
  public long getTxRollbackLifeTime() {
    return 0;
  }

  @Override
  public void incTxConflictCheckTime(long delta) {
  }

  @Override
  public void txSuccess(long opTime, long txLifeTime, int txChanges) {
  }

  @Override
  public void txFailure(long opTime, long txLifeTime, int txChanges) {
  }

  @Override
  public void txRollback(long opTime, long txLifeTime, int txChanges) {
  }

  // //// Special Instance Methods /////

  @Override
  void close() {
  }

  @Override
  public boolean isClosed() {
    return false;
  }

  @Override
  public int getEventQueueSize() {
    return 0;
  }

  @Override
  public void incEventQueueSize(int items) {
  }

  @Override
  public void incEventQueueThrottleCount(int items) {
  }

  @Override
  protected void incEventQueueThrottleTime(long nanos) {
  }

  @Override
  protected void incEventThreads(int items) {
  }

  @Override
  public void incEntryCount(int delta) {
  }

  @Override
  public long getEntries() {
    return 0;
  }

  @Override
  public void incRetries() {
  }

  @Override
  public Statistics getStats() {
    return null;
  }

  @Override
  public PoolStatHelper getEventPoolHelper() {
    return new PoolStatHelper() {
      public void startJob() {
      }

      public void endJob() {
      }
    };
  }

}
