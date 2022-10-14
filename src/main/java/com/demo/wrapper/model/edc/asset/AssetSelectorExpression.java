package com.demo.wrapper.model.edc.asset;

import com.demo.wrapper.model.edc.query.Criterion;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonDeserialize(builder = AssetSelectorExpression.Builder.class)
public final class AssetSelectorExpression {


    public static final AssetSelectorExpression SELECT_ALL = new AssetSelectorExpression();
    private List<Criterion> criteria;

    private AssetSelectorExpression() {
        criteria = new ArrayList<>();
    }

    public List<Criterion> getCriteria() {
        return criteria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AssetSelectorExpression that = (AssetSelectorExpression) o;
        return criteria == that.criteria || criteria.equals(that.criteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(criteria);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
        private final AssetSelectorExpression expression;

        private Builder() {
            expression = new AssetSelectorExpression();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder criteria(List<Criterion> criteria) {
            expression.criteria = criteria;
            return this;
        }

        @JsonIgnore
        public Builder constraint(String left, String op, Object right) {
            expression.criteria.add(new Criterion(left, op, right));
            return this;
        }

        @JsonIgnore
        public Builder whenEquals(String key, String value) {
            expression.criteria.add(new Criterion(key, "=", value));
            return this;
        }

        public AssetSelectorExpression build() {
            return expression;
        }
    }

}
