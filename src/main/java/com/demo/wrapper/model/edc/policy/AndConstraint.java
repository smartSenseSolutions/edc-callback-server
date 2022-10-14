package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

import static java.util.stream.Collectors.joining;

@JsonDeserialize(builder = AndConstraint.Builder.class)
public class AndConstraint extends MultiplicityConstraint {

    private AndConstraint() {
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitAndConstraint(this);
    }

    @Override
    public AndConstraint create(List<Constraint> constraints) {
        return Builder.newInstance().constraints(constraints).build();
    }

    @Override
    public String toString() {
        return "And constraint: [" + constraints.stream().map(Object::toString).collect(joining(",")) + "]";
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends MultiplicityConstraint.Builder<AndConstraint, Builder> {

        private Builder() {
            constraint = new AndConstraint();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public AndConstraint build() {
            return constraint;
        }
    }
}
