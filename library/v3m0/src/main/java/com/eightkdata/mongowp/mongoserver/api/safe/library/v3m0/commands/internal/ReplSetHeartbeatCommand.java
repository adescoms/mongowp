package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.ErrorCode;
import com.eightkdata.mongowp.MongoConstants;
import com.eightkdata.mongowp.OpTime;
import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonNumber;
import com.eightkdata.mongowp.exceptions.*;
import com.eightkdata.mongowp.fields.*;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetHeartbeatCommand.ReplSetHeartbeatArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.ReplSetHeartbeatCommand.ReplSetHeartbeatReply;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberState;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplSetProtocolVersion;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.ReplicaSetConfig;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.net.HostAndPort;
import java.util.Locale;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 */
public class ReplSetHeartbeatCommand extends AbstractCommand<ReplSetHeartbeatArgument, ReplSetHeartbeatReply>{

    public static final ReplSetHeartbeatCommand INSTANCE = new ReplSetHeartbeatCommand();

    private ReplSetHeartbeatCommand() {
        super("replSetHeartbeat");
    }

    @Override
    public boolean isReadyToReplyResult(ReplSetHeartbeatReply r) {
        return !r.errorCode.isOk();
    }

    @Override
    public Class<? extends ReplSetHeartbeatArgument> getArgClass() {
        return ReplSetHeartbeatArgument.class;
    }

    @Override
    public ReplSetHeartbeatArgument unmarshallArg(BsonDocument requestDoc)
            throws TypesMismatchException, NoSuchKeyException, BadValueException {
        return ReplSetHeartbeatArgument.fromDocument(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(ReplSetHeartbeatArgument request) {
        return request.toBSON();
    }

    @Override
    public Class<? extends ReplSetHeartbeatReply> getResultClass() {
        return ReplSetHeartbeatReply.class;
    }

    @Override
    public BsonDocument marshallResult(ReplSetHeartbeatReply reply) {
        return reply.toBSON();
    }

    @Override
    public ReplSetHeartbeatReply unmarshallResult(BsonDocument replyDoc) throws NoSuchKeyException, TypesMismatchException, FailedToParseException, MongoException {
        return ReplSetHeartbeatReply.fromDocument(replyDoc);
    }

    public static class ReplSetHeartbeatArgument {

        private static final BooleanField CHECK_EMPTY_FIELD_NAME = new BooleanField("checkEmpty");
        private static final LongField PROTOCOL_VERSION_FIELD = new LongField("pv");
        private static final LongField CONFIG_VERSION_FIELD = new LongField("v");
        private static final LongField SENDER_ID_FIELD = new LongField("fromId");
        private static final StringField SET_NAME_FIELD = new StringField("replSetHeartbeat");
        private static final HostAndPortField SENDER_HOST_FIELD = new HostAndPortField("from");

        private static final Set<String> VALID_FIELD_NAMES = Sets.newHashSet(
                CHECK_EMPTY_FIELD_NAME.getFieldName(),
                PROTOCOL_VERSION_FIELD.getFieldName(),
                CONFIG_VERSION_FIELD.getFieldName(),
                SENDER_ID_FIELD.getFieldName(),
                SET_NAME_FIELD.getFieldName(),
                SENDER_HOST_FIELD.getFieldName()
        );

        private final @Nonnull ReplSetProtocolVersion protocolVersion;
        private final long configVersion;
        private final @Nullable Integer senderId;
        private final String setName;
        private final @Nullable HostAndPort senderHost;
        private final boolean checkEmpty;

        public ReplSetHeartbeatArgument(
                boolean checkEmpty,
                @Nonnull ReplSetProtocolVersion protocolVersion,
                long configVersion,
                @Nullable Integer senderId,
                String setName,
                @Nullable HostAndPort senderHost) {
            this.checkEmpty = checkEmpty;
            this.protocolVersion = protocolVersion;
            this.configVersion = configVersion;
            this.senderId = senderId;
            this.setName = setName;
            this.senderHost = senderHost;
        }

        /**
         *
         * @return the protocol version the sender is using
         */
        @Nonnull
        public ReplSetProtocolVersion getProtocolVersion() {
            return protocolVersion;
        }

        /**
         *
         * @return the replica set configuration version the sender is using
         */
        public long getConfigVersion() {
            return configVersion;
        }

        /**
         *
         * @return the id of the sender on the replica set configuration
         */
        @Nullable
        public Integer getSenderId() {
            return senderId;
        }

        /**
         *
         * @return the replica set name of the sender
         */
        public String getSetName() {
            return setName;
        }

        /**
         *
         * @return the host and port of the sender node
         */
        @Nullable
        public HostAndPort getSenderHost() {
            return senderHost;
        }

        public BsonDocument toBSON() {
            BsonDocumentBuilder result = new BsonDocumentBuilder();

            result.append(SET_NAME_FIELD, setName);
            result.append(PROTOCOL_VERSION_FIELD, protocolVersion.getVersionId());
            result.append(CONFIG_VERSION_FIELD, configVersion);
            if (senderHost == null) {
                result.append(SENDER_HOST_FIELD, "");
            }
            else {
                result.append(SENDER_HOST_FIELD, senderHost);
            }

            if (senderId != null) {
                result.append(SENDER_ID_FIELD, senderId.longValue());
            }
            if (checkEmpty) {
                result.append(CHECK_EMPTY_FIELD_NAME, true);
            }
            return result.build();
        }

        /**
         *
         * @param bson
         * @param command
         * @return
         * @throws MongoException
         */
        public static ReplSetHeartbeatArgument fromDocument(BsonDocument bson) 
                throws BadValueException, TypesMismatchException, NoSuchKeyException {

            BsonReaderTool.checkOnlyHasFields("ReplSetHeartbeatArgs", bson, VALID_FIELD_NAMES);

            boolean checkEmpty = BsonReaderTool.getBoolean(bson, CHECK_EMPTY_FIELD_NAME, false);

            ReplSetProtocolVersion protocolVersion = ReplSetProtocolVersion.fromVersionId(
                    BsonReaderTool.getLong(bson, PROTOCOL_VERSION_FIELD)
            );

            long configVersion = BsonReaderTool.getLong(bson, CONFIG_VERSION_FIELD);

            Integer senderId;
            long senderIdLong = BsonReaderTool.getLong(bson, SENDER_ID_FIELD, -1);
            assert senderIdLong < Integer.MAX_VALUE;
            senderId = senderIdLong == -1 ? null : (int) senderIdLong;

            String setName = BsonReaderTool.getString(bson, SET_NAME_FIELD);

            HostAndPort senderHost = BsonReaderTool.getHostAndPort(bson, SENDER_HOST_FIELD, null);
            
            return new ReplSetHeartbeatArgument(checkEmpty, protocolVersion, configVersion,
                    senderId, setName, senderHost);
        }

        public static class Builder {
            private @Nonnull ReplSetProtocolVersion protocolVersion;
            private long configVersion;
            private @Nullable Integer senderId;
            private String setName;
            private @Nullable HostAndPort senderHost;
            private boolean checkEmpty;
            private boolean built = false;

            public Builder(@Nonnull ReplSetProtocolVersion protocolVersion) {
                this.protocolVersion = protocolVersion;
            }

            public Builder setProtocolVersion(@Nonnull ReplSetProtocolVersion protocolVersion) {
                Preconditions.checkState(!built);
                this.protocolVersion = protocolVersion;
                return this;
            }

            public Builder setConfigVersion(long configVersion) {
                Preconditions.checkState(!built);
                this.configVersion = configVersion;
                return this;
            }

            public Builder setSenderId(@Nullable Integer senderId) {
                Preconditions.checkState(!built);
                this.senderId = senderId;
                return this;
            }

            public Builder setSetName(String setName) {
                Preconditions.checkState(!built);
                this.setName = setName;
                return this;
            }

            public Builder setSenderHost(@Nullable HostAndPort senderHost) {
                Preconditions.checkState(!built);
                this.senderHost = senderHost;
                return this;
            }

            public Builder setCheckEmpty(boolean checkEmpty) {
                Preconditions.checkState(!built);
                this.checkEmpty = checkEmpty;
                return this;
            }

            public ReplSetHeartbeatArgument build() {
                Preconditions.checkState(!built);
                built = true;
                return new ReplSetHeartbeatArgument(checkEmpty, protocolVersion, configVersion, senderId, setName, senderHost);
            }
        }

    }

    public static class ReplSetHeartbeatReply {

        private static final DocField CONFIG_FIELD_NAME = new DocField("config");
        private static final LongField CONFIG_VERSION_FIELD_NAME = new LongField("v");
        private static final TimestampField ELECTION_TIME_FIELD_NAME = new TimestampField("electionTime");
        private static final StringField ERR_MSG_FIELD_NAME = new StringField("errmsg");
        private static final IntField ERROR_CODE_FIELD_NAME = new IntField("code");
        private static final BooleanField HAS_DATA_FIELD_NAME = new BooleanField("hasData");
        private static final BooleanField HAS_STATE_DISAGREEMENT_FIELD_NAME = new BooleanField("stateDisagreement");
        private static final StringField HB_MESSAGE_FIELD_NAME = new StringField("hbmsg");
        private static final BooleanField IS_ELECTABLE_FIELD_NAME = new BooleanField("e");
        private static final BooleanField IS_REPL_SET_FIELD_NAME = new BooleanField("rs");
        private static final IntField MEMBER_STATE_FIELD_NAME = new IntField("state");
        private static final BooleanField MISMATCH_FIELD_NAME = new BooleanField("mismatch");
        private static final DoubleField OK_FIELD_NAME = new DoubleField("ok");
        private static final TimestampField OP_TIME_FIELD = new TimestampField("opTime");
        private static final StringField REPL_SET_FIELD_NAME = new StringField("set");
        private static final HostAndPortField SYNC_SOURCE_FIELD_NAME = new HostAndPortField("syncingTo");
        private static final LongField TIME_FIELD_NAME = new LongField("time");

        private final @Nonnull ErrorCode errorCode;
        private final @Nullable String errMsg;
        private final @Nonnull OpTime electionTime;
        private final @Nullable Long time;
        private final @Nullable OpTime opTime;
        private final boolean electable;
        private final @Nullable Boolean hasData;
        private final boolean mismatch;
        private final boolean isReplSet;
        private final boolean stateDisagreement;
        private final @Nullable MemberState state;
        private final long configVersion;
        private final @Nonnull String setName;
        private final @Nonnull String hbmsg;
        private final @Nullable HostAndPort syncingTo;
        private final @Nullable ReplicaSetConfig config;

        public ReplSetHeartbeatReply(
                @Nonnull OpTime electionTime,
                @Nullable Long time,
                @Nullable OpTime opTime,
                boolean electable,
                @Nullable Boolean hasData,
                boolean mismatch,
                boolean isReplSet,
                boolean stateDisagreement,
                @Nullable MemberState state,
                long configVersion,
                @Nonnull String setName,
                @Nonnull String hbmsg,
                @Nullable HostAndPort syncingTo,
                @Nullable ReplicaSetConfig config) {
            this.errorCode = ErrorCode.OK;
            this.errMsg = null;
            this.electionTime = electionTime;
            this.time = time;
            this.opTime = opTime;
            this.electable = electable;
            this.hasData = hasData;
            this.mismatch = mismatch;
            this.isReplSet = isReplSet;
            this.stateDisagreement = stateDisagreement;
            this.state = state;
            this.configVersion = configVersion;
            this.setName = setName;
            this.hbmsg = hbmsg;
            this.syncingTo = syncingTo;
            this.config = config;
        }

        public ReplSetHeartbeatReply(ErrorCode errorCode, String errMsg, boolean mismatch, String setName) {
            this.errorCode = errorCode;
            this.errMsg = errMsg;
            this.electionTime = OpTime.EPOCH;
            this.time = null;
            this.opTime = null;
            this.electable = false;
            this.hasData = false;
            this.mismatch = mismatch;
            this.isReplSet = false;
            this.stateDisagreement = false;
            this.state = null;
            this.configVersion = -1;
            this.setName = setName;
            this.hbmsg = "";
            this.syncingTo = null;
            this.config = null;
        }
        
        public ErrorCode getErrorCode() {
        	return errorCode;
        }
        
        public String getErrMsg() {
        	return errMsg;
        }

        public OpTime getElectionTime() {
            return electionTime;
        }

        public Long getTime() {
            return time;
        }

        @Nullable
        public OpTime getOpTime() {
            return opTime;
        }

        public boolean isElectable() {
            return electable;
        }

        public Boolean getHasData() {
            return hasData;
        }

        public boolean isMismatch() {
            return mismatch;
        }

        public boolean isIsReplSet() {
            return isReplSet;
        }

        public boolean isStateDisagreement() {
            return stateDisagreement;
        }

        @Nullable
        public MemberState getState() {
            return state;
        }

        public long getConfigVersion() {
            return configVersion;
        }

        @Nonnull
        public String getSetName() {
            return setName;
        }

        @Nonnull
        public String getHbMsg() {
            return hbmsg;
        }

        @Nullable
        public HostAndPort getSyncingTo() {
            return syncingTo;
        }

        @Nullable
        public ReplicaSetConfig getConfig() {
            return config;
        }

        public BsonDocument toBSON() {
            BsonDocumentBuilder doc = new BsonDocumentBuilder();
            if (mismatch) {
                doc.append(OK_FIELD_NAME, MongoConstants.KO)
                        .append(MISMATCH_FIELD_NAME, true);
                return doc.build();
            }

            if (errorCode.isOk()) {
                doc.append(OK_FIELD_NAME, MongoConstants.OK);
            } else {
                doc.append(OK_FIELD_NAME, MongoConstants.KO);
                doc.append(ERROR_CODE_FIELD_NAME, errorCode.getErrorCode());
                if (errMsg != null) {
                    doc.append(ERR_MSG_FIELD_NAME, errMsg);
                }
            }

            if (opTime != null) {
                doc.append(OP_TIME_FIELD, opTime);
            }
            if (time != null) {
                doc.append(TIME_FIELD_NAME, time);
            }
            doc.append(ELECTION_TIME_FIELD_NAME, electionTime);
            if (config != null) {
                doc.append(CONFIG_FIELD_NAME, config.toBSON());
            }

            doc.append(IS_ELECTABLE_FIELD_NAME, electable);
            doc.append(IS_REPL_SET_FIELD_NAME, isReplSet);
            doc.append(HAS_STATE_DISAGREEMENT_FIELD_NAME, stateDisagreement);
            if (state != null) {
                doc.append(MEMBER_STATE_FIELD_NAME, state.getId());
            }
            doc.append(CONFIG_VERSION_FIELD_NAME, configVersion);
            doc.append(HB_MESSAGE_FIELD_NAME, hbmsg);
            doc.append(REPL_SET_FIELD_NAME, setName);
            if (syncingTo != null) {
                doc.append(SYNC_SOURCE_FIELD_NAME, syncingTo);
            }
            if (hasData != null) {
                doc.append(HAS_DATA_FIELD_NAME, hasData);
            }

            return doc.build();
        }

        public static ReplSetHeartbeatReply fromDocument(BsonDocument bson) 
                throws TypesMismatchException, NoSuchKeyException, BadValueException, FailedToParseException, MongoException {

            // Old versions set this even though they returned not "ok"
            boolean mismatch = BsonReaderTool.getBoolean(bson, MISMATCH_FIELD_NAME, false);
            if (mismatch) {
                throw new InconsistentReplicaSetNamesException();
            }

            // Old versions sometimes set the replica set name ("set") but ok:0
            // That means that setName != null => ok == true
            String setName;
            try {
                setName = BsonReaderTool.getString(bson, REPL_SET_FIELD_NAME, null);
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + REPL_SET_FIELD_NAME
                        + "\" field in response to replSetHeartbeat to have type "
                        + "String, but found " + ex.getFoundType()
                );
            }

            boolean ok = MongoConstants.OK.equals(BsonReaderTool.getDouble(bson, OK_FIELD_NAME)) || setName != null;
            if (!ok) {
                String errMsg = BsonReaderTool.getString(bson, ERR_MSG_FIELD_NAME, "");

                ErrorCode errorCode;
                try {
                    errorCode = ErrorCode.fromErrorCode(
                            BsonReaderTool.getNumeric(bson, ERROR_CODE_FIELD_NAME).intValue()
                    );
                } catch (TypesMismatchException ex) {
                    throw new BadValueException(ERROR_CODE_FIELD_NAME + " is not a number");
                } catch (NoSuchKeyException ex) {
                    throw new UnknownErrorException();
                }
                return new ReplSetHeartbeatReply(errorCode, errMsg, mismatch, setName);
            }

            Boolean hasData;
            if (!bson.containsKey(HAS_DATA_FIELD_NAME.getFieldName())) {
                hasData = null;
            }
            else {
                hasData = BsonReaderTool.getBoolean(bson, HAS_DATA_FIELD_NAME);
            }

            OpTime electionTime;
            try {
                electionTime = BsonReaderTool.getOpTime(bson, ELECTION_TIME_FIELD_NAME);
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + ELECTION_TIME_FIELD_NAME
                        + "\" field in response to replSetHeartbeat command to "
                        + "have type Date or Timestamp, but found type " + ex.getFoundType()
                );
            }

            Long time;
            BsonNumber timeNumber = BsonReaderTool.getNumeric(bson, TIME_FIELD_NAME, null);
            time = timeNumber == null ? null : timeNumber.longValue();

            boolean isReplSet = BsonReaderTool.getBoolean(bson, IS_REPL_SET_FIELD_NAME, false);

            OpTime opTime;
            if (!bson.containsKey(OP_TIME_FIELD.getFieldName())) {
                opTime = null;
            }
            else {
                try {
                    opTime = BsonReaderTool.getOpTime(bson, OP_TIME_FIELD);
                } catch (TypesMismatchException ex) {
                    throw ex.newWithMessage("Expected \"" + OP_TIME_FIELD
                            + "\" field in response to replSetHeartbeat "
                            + "command to have type Date or Timestamp, but found "
                            + "type " + ex.getFoundType().toString().toLowerCase(Locale.ROOT));
                }
            }

            boolean electable = BsonReaderTool.getBoolean(bson, IS_ELECTABLE_FIELD_NAME, false);

            MemberState state;
            if (bson.containsKey(MEMBER_STATE_FIELD_NAME.getFieldName())) {
                state = null;
            } else {
                int memberId = BsonReaderTool.getNumeric(bson, MEMBER_STATE_FIELD_NAME).intValue();
                try {
                    state = MemberState.fromId(memberId);
                } catch (IllegalArgumentException ex) {
                    throw new BadValueException("Value for \""
                            + MEMBER_STATE_FIELD_NAME + "\" in response to "
                            + "replSetHeartbeat is out of range; legal values are "
                            + "non-negative and no more than " + MemberState.RS_MAX.getId()
                    );
                }
            }

            boolean stateDisagreement = BsonReaderTool.getBoolean(bson, HAS_STATE_DISAGREEMENT_FIELD_NAME, false);

            long configVersion;
            try {
                configVersion = BsonReaderTool.getNumeric(bson, CONFIG_VERSION_FIELD_NAME).longValue();
            } catch (NoSuchKeyException ex) {
                if (opTime != null) {
                    throw new NoSuchKeyException(
                            CONFIG_VERSION_FIELD_NAME.getFieldName(),
                            "Response to replSetHeartbeat missing required \""
                            + CONFIG_VERSION_FIELD_NAME + "\" field even though "
                            + "initialized"
                    );
                }
                else {
                    configVersion = 0;
                }
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + CONFIG_VERSION_FIELD_NAME
                        + "\" field in response to replSetHeartbeat to have "
                        + "type NumberInt, but found " + ex.getFoundType().toString().toLowerCase(Locale.ROOT));
            }

            String hbMsg;
            try {
                hbMsg = BsonReaderTool.getString(bson, HB_MESSAGE_FIELD_NAME, "");
                assert hbMsg != null;
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + HB_MESSAGE_FIELD_NAME + "\" field in "
                        + "response to replSetHeartbeat to have type String, but "
                        + "found " + ex.getFoundType()
                );
            }

            HostAndPort syncincTo;
            try {
                syncincTo = BsonReaderTool.getHostAndPort(bson, SYNC_SOURCE_FIELD_NAME, null);
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + SYNC_SOURCE_FIELD_NAME
                        + "\" field in response to replSetHeartbeat to have type "
                        + "String, but found " + ex.getFoundType()
                );
            }

            ReplicaSetConfig config;
            BsonDocument uncastedConf;
            try {
                uncastedConf = BsonReaderTool.getDocument(bson, CONFIG_FIELD_NAME, null);
            } catch (TypesMismatchException ex) {
                throw ex.newWithMessage("Expected \"" + CONFIG_FIELD_NAME + "\" in "
                        + "response to replSetHeartbeat to have type Object, but "
                        + "found " + ex.getFoundType());
            }
            if (uncastedConf == null) {
                config = null;
            }
            else {
                config = ReplicaSetConfig.fromDocument(uncastedConf);
            }

            return new ReplSetHeartbeatReply(
                    electionTime,
                    time,
                    opTime,
                    electable,
                    hasData,
                    mismatch,
                    isReplSet,
                    stateDisagreement,
                    state,
                    configVersion,
                    setName,
                    hbMsg,
                    syncincTo,
                    config
            );
        }

        public static class Builder {

            private @Nonnull OpTime electionTime;
            private @Nullable Long time;
            private @Nullable OpTime opTime;
            private boolean electable;
            private @Nullable Boolean hasData;
            private boolean mismatch;
            private boolean isReplSet;
            private boolean stateDisagreement;
            private @Nullable MemberState state;
            private long configVersion;
            private @Nonnull String setName;
            private @Nonnull String hbmsg;
            private @Nullable HostAndPort syncingTo;
            private @Nullable ReplicaSetConfig config;

            public Builder(@Nonnull OpTime electionTime, @Nonnull String setName, @Nonnull String hbmsg) {
                this.electionTime = electionTime;
                this.setName = setName;
                this.hbmsg = hbmsg;
            }

            public Builder(ReplSetHeartbeatReply other) {
                this.electionTime = other.getElectionTime();
                this.time = other.getTime();
                this.opTime = other.getOpTime();
                this.electable = other.isElectable();
                this.hasData = other.getHasData();
                this.mismatch = other.isMismatch();
                this.isReplSet = other.isReplSet;
                this.stateDisagreement = other.isStateDisagreement();
                this.state = other.getState();
                this.configVersion = other.getConfigVersion();
                this.setName = other.getSetName();
                this.hbmsg = other.getHbMsg();
                this.syncingTo = other.syncingTo;
                this.config = other.config;
            }

            public Builder setElectionTime(@Nonnull OpTime electionTime) {
                this.electionTime = electionTime;
                return this;
            }

            public Builder setTime(@Nullable Long time) {
                this.time = time;
                return this;
            }

            public Builder setOpTime(@Nullable OpTime opTime) {
                this.opTime = opTime;
                return this;
            }

            public Builder setElectable(boolean electable) {
                this.electable = electable;
                return this;
            }

            public Builder setHasData(@Nullable Boolean hasData) {
                this.hasData = hasData;
                return this;
            }

            public Builder setMismatch(boolean mismatch) {
                this.mismatch = mismatch;
                return this;
            }

            public Builder setIsReplSet(boolean isReplSet) {
                this.isReplSet = isReplSet;
                return this;
            }

            public Builder setStateDisagreement(boolean stateDisagreement) {
                this.stateDisagreement = stateDisagreement;
                return this;
            }

            public Builder setState(@Nullable MemberState state) {
                this.state = state;
                return this;
            }

            public Builder setConfigVersion(long configVersion) {
                this.configVersion = configVersion;
                return this;
            }

            public Builder setSetName(@Nonnull String setName) {
                this.setName = setName;
                return this;
            }

            public Builder setHbmsg(@Nonnull String hbmsg) {
                this.hbmsg = hbmsg;
                return this;
            }

            public Builder setSyncingTo(@Nullable HostAndPort syncingTo) {
                this.syncingTo = syncingTo;
                return this;
            }

            public Builder setConfig(@Nullable ReplicaSetConfig config) {
                this.config = config;
                return this;
            }

            public ReplSetHeartbeatReply build() {
                return new ReplSetHeartbeatReply(electionTime, time, opTime, electable, hasData,
                        mismatch, isReplSet, stateDisagreement, state, configVersion, setName,
                        hbmsg, syncingTo, config);
            }

        }
    }

}
