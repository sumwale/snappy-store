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

import java.util.*;
import java.io.*;
import com.gemstone.gemfire.CancelException;
import com.gemstone.gemfire.SystemFailure;
import com.gemstone.gemfire.distributed.internal.*;
import com.gemstone.gemfire.distributed.internal.DistributionAdvisor.Profile;
import com.gemstone.gemfire.DataSerializer;
import com.gemstone.gemfire.i18n.LogWriterI18n;
import com.gemstone.gemfire.internal.*;
import com.gemstone.gemfire.distributed.internal.membership.*;

/**
 * This class is a bit misnamed. It really has more with pushing
 * a DistributionAdvisee's profile out to others and,
 * optionally if <code>profileExchange</code>,
 * fetching the profile of anyone who excepts the pushed profile.
 * @author Eric Zoerner
 *
 */
public class UpdateAttributesProcessor {
  protected final DistributionAdvisee advisee;
  private boolean profileExchange = false;
  /**
   * If true then sender is telling receiver to remove the sender's profile.
   * No profile exchange is needed in this case.
   * @since 5.7
   */
  private boolean removeProfile = false;
  private ReplyProcessor21 processor;

  /** Creates a new instance of UpdateAttributesProcessor */
  public UpdateAttributesProcessor(DistributionAdvisee da) {
    this(da, false);
  }

  /**
   * Creates a new instance of UpdateAttributesProcessor
   * @since 5.7
   */
  public UpdateAttributesProcessor(DistributionAdvisee da, boolean removeProfile) {
    this.advisee = da;
    this.removeProfile = removeProfile;
  }

  /**
   * Distribute new profile version without exchange of profiles. Same as 
   * calling {@link #distribute(boolean)} with (false).
   */
  void distribute() {
    distribute(false);
  }

  /** 
   * Distribute with optional exchange of profiles but do not create new 
   * profile version.
   * @param exchangeProfiles true if we want to receive profile replies
   */
  public void distribute(boolean exchangeProfiles) {
    sendProfileUpdate(exchangeProfiles);
    waitForProfileResponse();
  }

  public void waitForProfileResponse() {
    if(processor == null) {
      return;
    }
    DM mgr = this.advisee.getDistributionManager();
    try {
      // bug 36983 - you can't loop on a reply processor
          mgr.getCancelCriterion().checkCancelInProgress(null);
          try {
            processor.waitForRepliesUninterruptibly();
          }
          catch (ReplyException e) {
            e.handleAsUnexpected();
          }
    } finally {
      processor.cleanup();
    }
  }

  public void sendProfileUpdate(boolean exchangeProfiles) {
    DM mgr = this.advisee.getDistributionManager();
    DistributionAdvisor advisor = this.advisee.getDistributionAdvisor();
    this.profileExchange = exchangeProfiles;

    // if this is not intended for the purpose of exchanging profiles but
    // the advisor is uninitialized, then just exchange profiles anyway
    // and be done with it (instead of exchanging profiles and then sending
    // an attributes update)

    if (!exchangeProfiles) {
      if (this.removeProfile) {
        if (!advisor.isInitialized()) {
          // no need to tell the other guy we are going away since
          // never got initialized.
          return;
        }
      } else if (advisor.initializationGate()) {
        // it just did the profile exchange so we are done
        return;
      }
    }

    final Set recipients;
    if (this.removeProfile) {
      recipients = advisor.adviseProfileRemove();
    } else if (exchangeProfiles) {
      recipients = advisor.adviseProfileExchange();
    } else {
      recipients = advisor.adviseProfileUpdate();
    }

//     {
//       DM dm = this.advisee.getDistributionManager();
//       dm.getLogger().info("DEBUG: sending profile to " + recipients);
//     }
    if (recipients.isEmpty()) {
      return;
    }

    ReplyProcessor21 processor = null;
//    Scope scope = this.region.scope;
    
    // always require an ack to prevent misordering of messages
    InternalDistributedSystem system = this.advisee.getSystem();
    processor = new UpdateAttributesReplyProcessor(system, recipients);
    UpdateAttributesMessage message = getUpdateAttributesMessage(processor, recipients);
    mgr.putOutgoing(message);
    this.processor = processor;
  }


  UpdateAttributesMessage getUpdateAttributesMessage(ReplyProcessor21 processor,
                                                     Set recipients) {

    UpdateAttributesMessage msg = new UpdateAttributesMessage();
    msg.adviseePath = this.advisee.getFullPath();
    msg.setRecipients(recipients);
    if (processor != null) {
      msg.processorId = processor.getProcessorId();
    }
    msg.profile = this.advisee.getProfile();
    msg.exchangeProfiles = this.profileExchange;
    msg.removeProfile = this.removeProfile;
    return msg;
  }

  class UpdateAttributesReplyProcessor extends ReplyProcessor21 {

    UpdateAttributesReplyProcessor(InternalDistributedSystem system, Set members) {
      super(system, members);
    }
    
    /**
     * Registers this processor as a membership listener and
     * returns a set of the current members.
     * @return a Set of the current members
     * @since 5.7
     */
    @Override
    protected Set addListenerAndGetMembers() {
      DistributionAdvisor da = UpdateAttributesProcessor.this.advisee.getDistributionAdvisor();
      if (da.useAdminMembersForDefault()) {
        return getDistributionManager()
          .addAllMembershipListenerAndGetAllIds(this);
      } else {
        return super.addListenerAndGetMembers();
      }
    }
    /**
     * Unregisters this processor as a membership listener
     * @since 5.7
     */
    @Override
    protected void removeListener() {
      DistributionAdvisor da = UpdateAttributesProcessor.this.advisee.getDistributionAdvisor();
      if (da.useAdminMembersForDefault()) {
        getDistributionManager().removeAllMembershipListener(this);
      } else {
        super.removeListener();
      }
    }
    /**
     * If this processor being used by controller then return
     * ALL members; otherwise defer to super.
     * @return a Set of the current members
     * @since 5.7
     */
    @Override
    protected Set getDistributionManagerIds() {
      DistributionAdvisor da = UpdateAttributesProcessor.this.advisee.getDistributionAdvisor();
      if (da.useAdminMembersForDefault()) {
        return getDistributionManager().getDistributionManagerIdsIncludingAdmin();
      } else {
        return super.getDistributionManagerIds();
      }
    }
    
    @Override
    public void process(DistributionMessage msg) {
      try {
        if (msg instanceof ProfilesReplyMessage) {
          ProfilesReplyMessage reply =
            (ProfilesReplyMessage)msg;
          if (reply.profiles != null) {
            for (int i=0; i < reply.profiles.length; i++) {
              // @todo Add putProfiles to DistributionAdvisor to do this
              //       with one call atomically?
              UpdateAttributesProcessor.this.advisee.
                getDistributionAdvisor().putProfile(reply.profiles[i]);
            }
          }
        }
        // msg can be a ReplyMessage in case of unexpected exceptions
        else if (msg instanceof ProfileReplyMessage) {
          ProfileReplyMessage reply =
            (ProfileReplyMessage)msg;
          if (reply.profile != null) {
            UpdateAttributesProcessor.this.advisee.
              getDistributionAdvisor().putProfile(reply.profile);
          }
        }
      } finally {
        super.process(msg);
      }
    }
  }

  public static final class UpdateAttributesMessage
    extends HighPriorityDistributionMessage implements MessageWithReply {

    protected String adviseePath;
    protected int processorId = 0;
    protected Profile profile;
    protected boolean exchangeProfiles = false;
    protected boolean removeProfile = false;
    private transient LogWriterI18n logger;

    @Override
    public int getProcessorId() {
      return this.processorId;
    }

    @Override
    protected void process(DistributionManager dm) {
      Throwable thr = null;
      this.logger = dm.getLoggerI18n();
      boolean sendReply = this.processorId != 0;
      List<Profile> replyProfiles = null;
      try {
        if (this.profile != null) {
          if (this.exchangeProfiles) {
            replyProfiles = new ArrayList<Profile>();
          }
          this.profile.processIncoming(dm, this.adviseePath, this.removeProfile,
              this.exchangeProfiles, replyProfiles, this.logger);
        }
      }
      catch (CancelException e) {
        if (logger.fineEnabled())
          logger.fine("<cache closed> ///" + this);
      }
      catch (Throwable t) {
        Error err;
        if (t instanceof Error && SystemFailure.isJVMFailureError(
            err = (Error)t)) {
          SystemFailure.initiateFailure(err);
          // If this ever returns, rethrow the error. We're poisoned
          // now, so don't let this thread continue.
          throw err;
        }
        // Whenever you catch Error or Throwable, you must also
        // check for fatal JVM error (see above).  However, there is
        // _still_ a possibility that you are dealing with a cascading
        // error condition, so you also need to check to see if the JVM
        // is still usable:
        SystemFailure.checkFailure();
        thr = t;
      }
      finally {
        if (sendReply) {
          ReplyException rex = null;
          if (thr != null) {
            rex = new ReplyException(thr);
          }
          if (replyProfiles == null || replyProfiles.size() <= 1) {
            Profile p = null;
            if (replyProfiles != null && replyProfiles.size() == 1) {
              p = replyProfiles.get(0);
            }
            ProfileReplyMessage.send(getSender(), this.processorId, rex, dm, p);
          }
          else {
            Profile[] profiles = new Profile[replyProfiles.size()];
            replyProfiles.toArray(profiles);
            ProfilesReplyMessage.send(getSender(), this.processorId, rex, dm,
                profiles);
          }
        }
      }
    }

    @Override
    public String toString() {
      StringBuilder buff = new StringBuilder();
      buff.append("UpdateAttributesMessage (adviseePath=");
      buff.append(this.adviseePath);
      buff.append("; processorId=");
      buff.append(this.processorId);
      buff.append("; profile=");
      buff.append(this.profile);
      if (this.exchangeProfiles) {
        buff.append("; exchangeProfiles");
      }
      if (this.removeProfile) {
        buff.append("; removeProfile");
      }
      buff.append(")");
      return buff.toString();
    }

    public int getDSFID() {
      return UPDATE_ATTRIBUTES_MESSAGE;
    }

    @Override
    public void fromData(DataInput in)
    throws IOException, ClassNotFoundException {
      super.fromData(in);
      this.adviseePath = DataSerializer.readString(in);
      this.processorId = in.readInt();
      // set the processor ID to be able to send reply to sender in case of any
      // unexpected exception during deserialization etc.
      ReplyProcessor21.setMessageRPId(this.processorId);
      try {
        this.profile = DataSerializer.readObject(in);
      } catch (DSFIDFactory.GfxdSerializationException ex) {
        // Ignore GemFireXD serialization errors and reply with nothing.
        // This can happen even during normal startup of all GemFireXD VMs
        // when DS connect is complete but GemFireXD boot is still in progress.
        this.profile = null;
        // bug #49206 - don't leave extra bytes sitting in the stream
        try {
          int i = 0;
          while (i < 100  &&  in.skipBytes(10000) == 10000) {
            i++;
          };
        } catch (IOException e) {
          // the DataInput impls we use don't throw IOException
        }
        return;
      }
      this.exchangeProfiles = in.readBoolean();
      this.removeProfile = in.readBoolean();
    }

    @Override
    public void toData(DataOutput out) throws IOException {
      super.toData(out);
      DataSerializer.writeString(this.adviseePath, out);
      out.writeInt(this.processorId);
      DataSerializer.writeObject(this.profile, out);
      out.writeBoolean(this.exchangeProfiles);
      out.writeBoolean(this.removeProfile);
    }
  }


  public final static class ProfileReplyMessage extends ReplyMessage {
    Profile profile;

    public static void send(InternalDistributedMember recipient, int processorId,
                            ReplyException exception,
                            DistributionManager dm, Profile profile) {
      Assert.assertTrue(recipient != null, "Sending a ProfileReplyMessage to ALL");
      ProfileReplyMessage m = new ProfileReplyMessage();

      m.processorId = processorId;
      m.profile = profile;
      if (exception != null) {
       m.setException(exception);
       dm.getLoggerI18n().fine("Replying with exception: " + m, exception);
      }
      m.setRecipient(recipient);
      dm.putOutgoing(m);
    }

    @Override
    public int getDSFID() {
      return PROFILE_REPLY_MESSAGE;
    }

    @Override
    public void fromData(DataInput in)
    throws IOException, ClassNotFoundException {
      super.fromData(in);
      this.profile = (Profile)DataSerializer.readObject(in);
    }

    @Override
    public void toData(DataOutput out) throws IOException {
      super.toData(out);
      DataSerializer.writeObject(this.profile, out);
    }

    @Override
    public String toString() {
      final StringBuilder buff = new StringBuilder();
      buff.append("ProfileReplyMessage");
      buff.append(" (processorId=");
      buff.append(super.processorId);
      buff.append("; profile=");
      buff.append(this.profile);
      buff.append(")");
      return buff.toString();
    }

    /* (non-Javadoc)
     * @see com.gemstone.gemfire.distributed.internal.ReplyMessage#getInlineProcess()
     * ProfileReplyMessages must be processed in-line and not in a pool to
     * keep partitioned region bucket profile exchange from swamping the
     * high priority pool and not allowing other profile exchanges through.
     * This is safe as long as ProfileReplyMessage obtains no extra synchronization
     * locks.
     */
    @Override
    public boolean getInlineProcess() {
      final Profile p = this.profile;
      return (p == null || p.getInlineProcess());
    }

  }
  /**
   * Used to return multiple profiles
   * @since 5.7
   */
  public static class ProfilesReplyMessage extends ReplyMessage {
    Profile[] profiles;

    public static void send(InternalDistributedMember recipient, int processorId,
                            ReplyException exception,
                            DistributionManager dm, Profile[] profiles) {
      Assert.assertTrue(recipient != null, "Sending a ProfilesReplyMessage to ALL");
      ProfilesReplyMessage m = new ProfilesReplyMessage();

      m.processorId = processorId;
      m.profiles = profiles;
      if (exception != null) {
        m.setException(exception);
       dm.getLoggerI18n().fine("Replying with exception: " + m, exception);
      }
      m.setRecipient(recipient);
      dm.putOutgoing(m);
    }
    
    

    @Override
    public int getDSFID() {
     return PROFILES_REPLY_MESSAGE;
    }



    @Override
    public void fromData(DataInput in)
    throws IOException, ClassNotFoundException {
      super.fromData(in);
      int length = in.readInt();
      if (length == -1) {
        this.profiles = null;
      } else {
        Profile[] array = new Profile[length];
        for (int i = 0; i < length; i++) {
          array[i] = (Profile)DataSerializer.readObject(in);
        }
        this.profiles = array;
      }
    }

    @Override
    public void toData(DataOutput out) throws IOException {
      super.toData(out);
      if (this.profiles == null) {
        out.writeInt(-1);
      } else {
        int length = this.profiles.length;
        out.writeInt(length);
        for (int i = 0; i < length; i++) {
          DataSerializer.writeObject(this.profiles[i], out);
        }
      }
    }

    @Override
    public String toString() {
      final StringBuilder buff = new StringBuilder();
      buff.append("ProfilesReplyMessage");
      buff.append(" (processorId=");
      buff.append(super.processorId);
      if (this.profiles != null) {
        buff.append("; profiles=");
        buff.append(Arrays.asList(this.profiles));
      }
      buff.append(")");
      return buff.toString();
    }

    /* (non-Javadoc)
     * @see com.gemstone.gemfire.distributed.internal.ReplyMessage#getInlineProcess()
     * ProfilesReplyMessages must be processed in-line and not in a pool to
     * keep partitioned region bucket profile exchange from swamping the
     * high priority pool and not allowing other profile exchanges through.
     * This is safe as long as ProfilesReplyMessage obtains no extra synchronization
     * locks.
     */
    @Override
    public boolean getInlineProcess() {
      final Profile[] ps = this.profiles;
      if (ps != null) {
        for (Profile p : ps) {
          if (p != null && !p.getInlineProcess()) {
            return false;
          }
        }
      }
      return true;
    }
  }
}
