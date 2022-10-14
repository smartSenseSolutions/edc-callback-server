package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "edctype")
public abstract class MultiplicityConstraint extends Constraint {
    protected List<Constraint> constraints = new ArrayList<>();

    public List<Constraint> getConstraints() {
        return constraints;
    }

    public abstract MultiplicityConstraint create(List<Constraint> constraints);

    protected abstract static class Builder<T extends MultiplicityConstraint, B extends Builder<T, B>> {
        protected T constraint;

        public B constraint(Constraint constraint) {
            this.constraint.constraints.add(constraint);
            return (B) this;
        }

        public B constraints(List<Constraint> constraints) {
            constraint.constraints.addAll(constraints);
            return (B) this;
        }

    }

}
