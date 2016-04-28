
package com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal;

import com.eightkdata.mongowp.bson.BsonDocument;
import com.eightkdata.mongowp.bson.BsonObjectId;
import com.eightkdata.mongowp.exceptions.BadValueException;
import com.eightkdata.mongowp.exceptions.NoSuchKeyException;
import com.eightkdata.mongowp.exceptions.TypesMismatchException;
import com.eightkdata.mongowp.fields.DocField;
import com.eightkdata.mongowp.fields.IntField;
import com.eightkdata.mongowp.fields.LongField;
import com.eightkdata.mongowp.fields.ObjectIdField;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.commands.internal.HandshakeCommand.HandshakeArgument;
import com.eightkdata.mongowp.mongoserver.api.safe.library.v3m0.pojos.MemberConfig;
import com.eightkdata.mongowp.server.api.impl.AbstractCommand;
import com.eightkdata.mongowp.server.api.tools.Empty;
import com.eightkdata.mongowp.utils.BsonDocumentBuilder;
import com.eightkdata.mongowp.utils.BsonReaderTool;
import javax.annotation.Nullable;

/**
 *
 */
public class HandshakeCommand extends AbstractCommand<HandshakeArgument, Empty>{

    public static final HandshakeCommand INSTANCE = new HandshakeCommand();

    private static final String RID_FIELD_NAME = "handshake";

    protected HandshakeCommand() {
        super(RID_FIELD_NAME);
    }

    @Override
    public Class<? extends HandshakeArgument> getArgClass() {
        return HandshakeArgument.class;
    }

    @Override
    public HandshakeArgument unmarshallArg(BsonDocument requestDoc)
            throws BadValueException, TypesMismatchException, NoSuchKeyException {
        return HandshakeArgument.unmarshall(requestDoc);
    }

    @Override
    public BsonDocument marshallArg(HandshakeArgument request) {
        return request.marshall();
    }

    @Override
    public Class<? extends Empty> getResultClass() {
        return Empty.class;
    }

    @Override
    public BsonDocument marshallResult(Empty reply) {
        return null;
    }

    @Override
    public Empty unmarshallResult(BsonDocument resultDoc) {
        return Empty.getInstance();
    }

    public static class HandshakeArgument {

        private static final IntField REPL_SET_UPDATE_POSITION_FIELD = new IntField("replSetUpdatePosition");
        private static final DocField HANDSHAKE_OBJ_FIELD = new DocField("handshake");
        private static final ObjectIdField RID_FIELD = new ObjectIdField("handshake");
        private static final LongField MEMBER_FIELD = new LongField("member");
        private static final DocField CONFIG_FIELD = new DocField("config");

        private final BsonObjectId rid;
        private final Integer memberId;
        /**
         * This is not used on MongoDB 3.0.0 and higher, but it is required in
         * older versions.
         */
        private final @Nullable MemberConfig config;

        public HandshakeArgument(
                BsonObjectId rid,
                @Nullable Integer memberId,
                @Nullable MemberConfig config) {
            this.rid = rid;
            this.memberId = memberId;
            this.config = config;
        }

        public BsonObjectId getRid() {
            return rid;
        }

        public Integer getMemberId() {
            return memberId;
        }

        public MemberConfig getConfig() {
            return config;
        }

        @Override
        public String toString() {
            return HandshakeCommand.INSTANCE.marshallArg(this).toString();
        }

        private static HandshakeArgument unmarshall(BsonDocument requestDoc) 
                throws TypesMismatchException, NoSuchKeyException, BadValueException {
            TODO: CHECK UNMARSHALLING;

            BsonObjectId rid = BsonReaderTool.getObjectId(requestDoc, RID_FIELD);
            Integer memberId;
            if (!requestDoc.containsKey(MEMBER_FIELD.getFieldName())) {
                memberId = null;
            }
            else {
                memberId = BsonReaderTool.getLong(requestDoc, MEMBER_FIELD);
            }
            BsonDocument configBson = BsonReaderTool.getDocument(
                    requestDoc,
                    CONFIG_FIELD,
                    null
            );
            MemberConfig memberConfig = null;
            if (configBson != null) {
                memberConfig = MemberConfig.fromDocument(configBson);
            }

            return new HandshakeArgument(rid, memberId, memberConfig);
        }

        private BsonDocument marshall() {
            return new BsonDocumentBuilder()
                .append(RID_FIELD, rid)
                .append(MEMBER_FIELD, memberId)
                .append(CONFIG_FIELD, config.toBSON())
                .build();
        }

        public BsonDocument marshallAsReplSetUpdate() {
            BsonDocumentBuilder builder = new BsonDocumentBuilder();
            builder.append(REPL_SET_UPDATE_POSITION_FIELD, 1);
            builder.append(HANDSHAKE_OBJ_FIELD, marshall());
            return builder.build();
        }
    }

}
