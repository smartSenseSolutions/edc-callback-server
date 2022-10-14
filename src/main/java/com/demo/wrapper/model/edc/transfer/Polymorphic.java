package com.demo.wrapper.model.edc.transfer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "edctype")
public interface Polymorphic {
}
