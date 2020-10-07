package com.ts.learning.sws101.model.simplechat.client;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ts.learning.sws101.model.simplechat.SimpleFromMessage;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JoinMessage.class, name = JoinMessage.TYPE),
        @JsonSubTypes.Type(value = SimpleToMessage.class, name = SimpleToMessage.TYPE),
        @JsonSubTypes.Type(value = SimpleFromMessage.class, name = SimpleFromMessage.TYPE),
        @JsonSubTypes.Type(value = LeaveMessage.class, name = LeaveMessage.TYPE),
        @JsonSubTypes.Type(value = GetActiveUsersMessage.class, name = GetActiveUsersMessage.TYPE),
})
public abstract class AbstractClientMessage {
}
