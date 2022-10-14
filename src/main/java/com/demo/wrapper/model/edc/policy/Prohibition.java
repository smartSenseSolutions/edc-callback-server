package com.demo.wrapper.model.edc.policy;

import static java.util.stream.Collectors.joining;

public class Prohibition extends Rule {

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitProhibition(this);
    }

    @Override
    public String toString() {
        return "Prohibition constraints: [" + getConstraints().stream().map(Object::toString).collect(joining(",")) + "]";
    }

    public Prohibition withTarget(String target) {
        return Builder.newInstance()
                .uid(uid)
                .assigner(assigner)
                .assignee(assignee)
                .action(action)
                .constraints(constraints)
                .target(target)
                .build();
    }

    public static class Builder extends Rule.Builder<Prohibition, Builder> {

        private Builder() {
            rule = new Prohibition();
        }

        public static Builder newInstance() {
            return new Builder();
        }

        public Builder uid(String uid) {
            rule.uid = uid;
            return this;
        }

        public Prohibition build() {
            return rule;
        }
    }
}
