package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.jetbrains.annotations.Nullable;

import static java.util.stream.Collectors.joining;

@JsonDeserialize(builder = Duty.Builder.class)
@JsonTypeName("dataspaceconnector:duty")
public class Duty extends Rule {

    private Permission parentPermission;

    @Nullable
    private Duty consequence;

    public Duty getConsequence() {
        return consequence;
    }

    @Nullable
    public Permission getParentPermission() {
        return parentPermission;
    }

    void setParentPermission(Permission permission) {
        parentPermission = permission;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitDuty(this);
    }

    @Override
    public String toString() {
        return "Duty constraint: [" + getConstraints().stream().map(Object::toString).collect(joining(",")) + "]";
    }

    public Duty withTarget(String target) {
        return Builder.newInstance()
                .uid(uid)
                .assigner(assigner)
                .assignee(assignee)
                .action(action)
                .constraints(constraints)
                .parentPermission(parentPermission)
                .consequence(consequence == null ? null : consequence.withTarget(target))
                .target(target)
                .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends Rule.Builder<Duty, Builder> {

        private Builder() {
            rule = new Duty();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder uid(String uid) {
            rule.uid = uid;
            return this;
        }

        public Builder parentPermission(Permission parentPermission) {
            rule.parentPermission = parentPermission;
            return this;
        }

        public Builder consequence(Duty consequence) {
            rule.consequence = consequence;
            return this;
        }

        public Duty build() {
            return rule;
        }
    }

}
