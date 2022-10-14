package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "edctype")
public abstract class Constraint {

    public abstract <R> R accept(Visitor<R> visitor);

    public interface Visitor<R> {

        R visitAndConstraint(AndConstraint constraint);

        R visitOrConstraint(OrConstraint constraint);

        R visitXoneConstraint(XoneConstraint constraint);

        R visitAtomicConstraint(AtomicConstraint constraint);

    }


}
