package com.demo.wrapper.model.edc.transfer;

import com.demo.wrapper.model.edc.negotiation.TraceCarrier;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Map;
import java.util.Objects;

@JsonTypeName("dataspaceconnector:dataflowrequest")
@JsonDeserialize(builder = DataFlowRequest.Builder.class)
public class DataFlowRequest implements Polymorphic, TraceCarrier {
    private String id;
    private String processId;

    private DataAddress sourceDataAddress;
    private DataAddress destinationDataAddress;

    private boolean trackable;

    private Map<String, String> properties = Map.of();
    private Map<String, String> traceContext = Map.of();

    private DataFlowRequest() {
    }

    public String getId() {
        return id;
    }

    public String getProcessId() {
        return processId;
    }

    public DataAddress getSourceDataAddress() {
        return sourceDataAddress;
    }

    public DataAddress getDestinationDataAddress() {
        return destinationDataAddress;
    }

    public boolean isTrackable() {
        return trackable;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public Map<String, String> getTraceContext() {
        return traceContext;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final DataFlowRequest request;

        private Builder() {
            this(new DataFlowRequest());
        }

        private Builder(DataFlowRequest request) {
            this.request = request;
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(String id) {
            request.id = id;
            return this;
        }

        public Builder processId(String id) {
            request.processId = id;
            return this;
        }

        public Builder destinationType(String type) {
            if (request.destinationDataAddress == null) {
                request.destinationDataAddress = DataAddress.Builder.newInstance().type(type).build();
            } else {
                request.destinationDataAddress.setType(type);
            }
            return this;
        }

        public Builder sourceDataAddress(DataAddress destination) {
            request.sourceDataAddress = destination;
            return this;
        }

        public Builder destinationDataAddress(DataAddress destination) {
            request.destinationDataAddress = destination;
            return this;
        }

        public Builder trackable(boolean value) {
            request.trackable = value;
            return this;
        }

        public Builder properties(Map<String, String> value) {
            request.properties = value == null ? null : Map.copyOf(value);
            return this;
        }

        public Builder traceContext(Map<String, String> value) {
            request.traceContext = value;
            return this;
        }

        public DataFlowRequest build() {
            Objects.requireNonNull(request.processId, "processId");
            Objects.requireNonNull(request.sourceDataAddress, "sourceDataAddress");
            Objects.requireNonNull(request.destinationDataAddress, "destinationDataAddress");
            Objects.requireNonNull(request.traceContext, "traceContext");
            return request;
        }

    }
}
