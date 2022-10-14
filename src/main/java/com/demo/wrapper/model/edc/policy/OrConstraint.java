package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

import static java.util.stream.Collectors.joining;

@JsonDeserialize(builder = OrConstraint.Builder.class)
@JsonTypeName("dataspaceconnector:orconstraint")
public class OrConstraint extends MultiplicityConstraint {

    private OrConstraint() {
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitOrConstraint(this);
    }

    @Override
    public MultiplicityConstraint create(List<Constraint> constraints) {
        return Builder.newInstance().constraints(constraints).build();
    }

    @Override
    public String toString() {
        return "Or constraint: [" + constraints.stream().map(Object::toString).collect(joining(",")) + "]";
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends MultiplicityConstraint.Builder<OrConstraint, Builder> {

        private Builder() {
            constraint = new OrConstraint();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public OrConstraint build() {
            return constraint;
        }
    }

}
