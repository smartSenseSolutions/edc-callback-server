package com.demo.wrapper.model.edc.policy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "edctype")
public abstract class Expression {

    public abstract <R> R accept(Visitor<R> visitor);

    public interface Visitor<R> {

        R visitLiteralExpression(LiteralExpression expression);

    }

}
