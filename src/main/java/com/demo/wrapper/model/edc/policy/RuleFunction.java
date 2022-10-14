package com.demo.wrapper.model.edc.policy;

@FunctionalInterface
public interface RuleFunction<RULE_TYPE extends Rule> {

    boolean evaluate(RULE_TYPE rule);

}
