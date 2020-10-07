package com.ts.learning.sws101.model.simplechat.server;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ts.learning.sws101.model.simplechat.SimpleFromMessage;
import com.ts.learning.sws101.model.simplechat.ActiveUsersListMessage;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimpleFromMessage.class, name = SimpleFromMessage.TYPE),
        @JsonSubTypes.Type(value = ActiveUsersListMessage.class, name = ActiveUsersListMessage.TYPE),
})
public class AbstractServerMessage {
}
