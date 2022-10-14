package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = Action.Builder.class)
public class Action {
    String type;
    String includedIn;
    Constraint constraint;

    private Action() {
    }

    public String getIncludedIn() {
        return includedIn;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public String getType() {
        return type;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final Action action;

        private Builder() {
            action = new Action();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder type(String type) {
            action.type = type;
            return this;
        }

        public Builder includedIn(String includedIn) {
            action.includedIn = includedIn;
            return this;
        }

        public Builder constraint(Constraint constraint) {
            action.constraint = constraint;
            return this;
        }

        public Action build() {
            Objects.requireNonNull(action.type, "type");
            return action;
        }
    }

}
