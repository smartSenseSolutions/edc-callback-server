package com.demo.wrapper.model.edc.policy;

@FunctionalInterface
public interface AtomicConstraintFunction<RIGHT_VALUE, RULE_TYPE extends Rule, RESULT> {

    RESULT evaluate(Operator operator, RIGHT_VALUE rightValue, RULE_TYPE rule);

}
