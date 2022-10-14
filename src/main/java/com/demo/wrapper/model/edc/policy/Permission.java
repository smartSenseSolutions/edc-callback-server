package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@JsonDeserialize(builder = Permission.Builder.class)
@JsonTypeName("dataspaceconnector:permission")
public class Permission extends Rule {
    private final List<Duty> duties = new ArrayList<>();

    public List<Duty> getDuties() {
        return duties;
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitPermission(this);
    }

    @Override
    public String toString() {
        return "Permission constraints: [" + getConstraints().stream().map(Object::toString).collect(joining(",")) + "]";
    }

    public Permission withTarget(String target) {
        return Builder.newInstance()
                .uid(uid)
                .assigner(assigner)
                .assignee(assignee)
                .action(action)
                .constraints(constraints)
                .duties(duties.stream().map(d -> d.withTarget(target)).collect(Collectors.toList()))
                .target(target)
                .build();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder extends Rule.Builder<Permission, Builder> {

        private Builder() {
            rule = new Permission();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder duty(Duty duty) {
            duty.setParentPermission(rule);
            rule.duties.add(duty);
            return this;
        }

        public Builder uid(String uid) {
            rule.uid = uid;
            return this;
        }

        public Builder duties(List<Duty> duties) {
            for (var duty : duties) {
                duty.setParentPermission(rule);
            }
            rule.duties.addAll(duties);
            return this;
        }

        public Permission build() {
            return rule;
        }
    }
}
