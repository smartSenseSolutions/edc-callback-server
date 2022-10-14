package com.demo.wrapper.model.edc.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;


@JsonDeserialize(builder = NegotiationId.Builder.class)
public class NegotiationId {
    private String id;

    private NegotiationId() {
    }

    public String getId() {
        return id;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private final NegotiationId dto;

        private Builder() {
            dto = new NegotiationId();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(String id) {
            dto.id = id;
            return this;
        }

        public NegotiationId build() {
            return dto;
        }
    }
}
