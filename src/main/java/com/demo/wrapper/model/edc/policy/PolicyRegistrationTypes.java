package com.demo.wrapper.model.edc.policy;

import java.util.List;

public final class PolicyRegistrationTypes {

    public static final List<Class<?>> TYPES = List.of(
            Action.class,
            AndConstraint.class,
            AtomicConstraint.class,
            Duty.class,
            LiteralExpression.class,
            OrConstraint.class,
            Permission.class,
            Policy.class,
            PolicyType.class,
            Prohibition.class,
            XoneConstraint.class);

    private PolicyRegistrationTypes() {
    }
}
