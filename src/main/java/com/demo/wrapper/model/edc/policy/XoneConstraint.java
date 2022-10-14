package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

import static java.util.stream.Collectors.joining;

@JsonDeserialize(builder = XoneConstraint.Builder.class)
@JsonTypeName("dataspaceconnector:xone")
public class XoneConstraint extends MultiplicityConstraint {

    @Override
    public List<Constraint> getConstraints() {
        return constraints;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitXoneConstraint(this);
    }

    @Override
    public XoneConstraint create(List<Constraint> constraints) {
        return Builder.newInstance().constraints(constraints).build();
    }

    @Override
    public String toString() {
        return "Xone constraint: [" + constraints.stream().map(Object::toString).collect(joining(",")) + "]";
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends MultiplicityConstraint.Builder<XoneConstraint, Builder> {

        private Builder() {
            constraint = new XoneConstraint();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public XoneConstraint build() {
            return constraint;
        }
    }

}
