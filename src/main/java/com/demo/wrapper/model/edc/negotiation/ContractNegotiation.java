package com.demo.wrapper.model.edc.negotiation;

import com.demo.wrapper.model.edc.agreement.ContractAgreement;
import com.demo.wrapper.model.edc.offer.ContractOffer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;

import static com.demo.wrapper.model.edc.negotiation.ContractNegotiationStates.*;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@JsonTypeName("dataspaceconnector:contractnegotiation")
@JsonDeserialize(builder = ContractNegotiation.Builder.class)
public class ContractNegotiation implements TraceCarrier {
    private String id;
    private String correlationId;
    private String counterPartyId;
    private String counterPartyAddress;
    private String protocol;
    private Type type = Type.CONSUMER;
    private int state = UNSAVED.code();
    private int stateCount;
    private long stateTimestamp;
    private String errorDetail;
    private ContractAgreement contractAgreement;
    private List<ContractOffer> contractOffers = new ArrayList<>();
    private Map<String, String> traceContext = new HashMap<>();

    public Type getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getCounterPartyId() {
        return counterPartyId;
    }

    public String getCounterPartyAddress() {
        return counterPartyAddress;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    @NotNull
    public String getProtocol() {
        return protocol;
    }

    @Override
    public Map<String, String> getTraceContext() {
        return Collections.unmodifiableMap(traceContext);
    }

    public int getState() {
        return state;
    }

    public int getStateCount() {
        return stateCount;
    }

    public long getStateTimestamp() {
        return stateTimestamp;
    }

    public List<ContractOffer> getContractOffers() {
        return contractOffers;
    }

    public void addContractOffer(ContractOffer offer) {
        contractOffers.add(offer);
    }

    public String getErrorDetail() {
        return errorDetail;
    }

    public void setErrorDetail(String errorDetail) {
        this.errorDetail = errorDetail;
    }

    public ContractOffer getLastContractOffer() {
        var size = contractOffers.size();
        if (size == 0) {
            return null;
        }
        return contractOffers.get(size - 1);
    }

    public ContractAgreement getContractAgreement() {
        return contractAgreement;
    }

    public void setContractAgreement(ContractAgreement agreement) {
        contractAgreement = agreement;
    }

    public void transitionInitial() {
        transition(ContractNegotiationStates.INITIAL, REQUESTING, UNSAVED);
    }

    public void transitionRequesting() {
        if (Type.PROVIDER == type) {
            throw new IllegalStateException("Provider processes have no REQUESTING state");
        }
        transition(REQUESTING, REQUESTING, INITIAL);
    }

    public void transitionRequested() {
        if (Type.PROVIDER == type) {
            transition(REQUESTED, UNSAVED);
        } else {
            transition(REQUESTED, REQUESTED, REQUESTING);
        }
    }

    public void transitionOffering() {
        if (Type.CONSUMER == type) {
            transition(CONSUMER_OFFERING, CONSUMER_OFFERING, REQUESTED);
        } else {
            transition(PROVIDER_OFFERING, PROVIDER_OFFERING, PROVIDER_OFFERED, REQUESTED);
        }
    }

    public void transitionOffered() {
        if (Type.CONSUMER == type) {
            transition(CONSUMER_OFFERED, PROVIDER_OFFERED, CONSUMER_OFFERING);
        } else {
            transition(PROVIDER_OFFERED, PROVIDER_OFFERED, PROVIDER_OFFERING);
        }
    }

    public void transitionApproving() {
        if (Type.PROVIDER == type) {
            throw new IllegalStateException("Provider processes have no CONSUMER_APPROVING state");
        }
        transition(CONSUMER_APPROVING, CONSUMER_APPROVING, CONSUMER_OFFERED, REQUESTED);
    }

    public void transitionApproved() {
        if (Type.PROVIDER == type) {
            throw new IllegalStateException("Provider processes have no CONSUMER_APPROVED state");
        }
        transition(CONSUMER_APPROVED, CONSUMER_APPROVED, CONSUMER_APPROVING, PROVIDER_OFFERED);
    }

    public void transitionDeclining() {
        if (Type.CONSUMER == type) {
            transition(DECLINING, DECLINING, REQUESTED, CONSUMER_OFFERED, CONSUMER_APPROVED);
        } else {
            transition(DECLINING, DECLINING, REQUESTED, PROVIDER_OFFERED, CONSUMER_APPROVED);
        }
    }

    public void transitionDeclined() {
        if (Type.CONSUMER == type) {
            transition(DECLINED, DECLINING, CONSUMER_OFFERED, REQUESTED);
        } else {
            transition(DECLINED, DECLINING, PROVIDER_OFFERED, CONFIRMED, REQUESTED);
        }

    }

    public void transitionConfirming() {
        if (Type.CONSUMER == type) {
            throw new IllegalStateException("Consumer processes have no CONFIRMING state");
        }
        transition(CONFIRMING, CONFIRMING, REQUESTED, PROVIDER_OFFERED);
    }

    public void transitionConfirmed() {
        if (Type.CONSUMER == type) {
            transition(CONFIRMED, CONFIRMING, CONSUMER_APPROVED, REQUESTED, CONSUMER_OFFERED, CONFIRMED);
        } else {
            transition(CONFIRMED, CONFIRMING);
        }

    }

    public void transitionError(@Nullable String errorDetail) {
        state = ContractNegotiationStates.ERROR.code();
        this.errorDetail = errorDetail;
        stateCount = 1;
        updateStateTimestamp();
    }

    public void rollbackState(ContractNegotiationStates state) {
        this.state = state.code();
        stateCount = 1;
        updateStateTimestamp();
    }

    public ContractNegotiation copy() {
        return Builder.newInstance().id(id).correlationId(correlationId).counterPartyId(counterPartyId)
                .counterPartyAddress(counterPartyAddress).protocol(protocol).type(type).state(state).stateCount(stateCount)
                .stateTimestamp(stateTimestamp).errorDetail(errorDetail).contractAgreement(contractAgreement)
                .contractOffers(contractOffers).traceContext(traceContext).build();
    }

    public void updateStateTimestamp() {
        stateTimestamp = Instant.now().toEpochMilli();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContractNegotiation that = (ContractNegotiation) o;
        return state == that.state && stateCount == that.stateCount && stateTimestamp == that.stateTimestamp && Objects.equals(id, that.id) &&
                Objects.equals(correlationId, that.correlationId) && Objects.equals(counterPartyId, that.counterPartyId) &&
                Objects.equals(protocol, that.protocol) && Objects.equals(traceContext, that.traceContext) &&
                type == that.type && Objects.equals(contractAgreement, that.contractAgreement) && Objects.equals(contractOffers, that.contractOffers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, correlationId, counterPartyId, protocol, traceContext, type, state, stateCount, stateTimestamp, contractAgreement, contractOffers);
    }

    private void checkState(int... legalStates) {
        for (var legalState : legalStates) {
            if (state == legalState) {
                return;
            }
        }
        var values = Arrays.stream(legalStates).mapToObj(String::valueOf).collect(joining(","));
        throw new IllegalStateException(format("Illegal state: %s. Expected one of: %s.", state, values));
    }

    private void transition(ContractNegotiationStates end, ContractNegotiationStates... starts) {
        if (Arrays.stream(starts).noneMatch(s -> s.code() == state)) {
            throw new IllegalStateException(format("Cannot transition from state %s to %s", ContractNegotiationStates.from(state), ContractNegotiationStates.from(end.code())));
        }
        stateCount = state == end.code() ? stateCount + 1 : 1;
        state = end.code();
        updateStateTimestamp();
    }

    public enum Type {
        CONSUMER, PROVIDER
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private final ContractNegotiation negotiation;

        private Builder() {
            negotiation = new ContractNegotiation();
        }

        @JsonCreator
        public static Builder newInstance() {
            return new Builder();
        }

        public Builder id(String id) {
            negotiation.id = id;
            return this;
        }

        public Builder protocol(String protocol) {
            negotiation.protocol = protocol;
            return this;
        }

        public Builder state(int state) {
            negotiation.state = state;
            return this;
        }

        public Builder stateCount(int stateCount) {
            negotiation.stateCount = stateCount;
            return this;
        }

        public Builder stateTimestamp(long stateTimestamp) {
            negotiation.stateTimestamp = stateTimestamp;
            return this;
        }

        public Builder counterPartyId(String id) {
            negotiation.counterPartyId = id;
            return this;
        }

        public Builder counterPartyAddress(String address) {
            negotiation.counterPartyAddress = address;
            return this;
        }

        public Builder correlationId(String id) {
            negotiation.correlationId = id;
            return this;
        }

        public Builder contractAgreement(ContractAgreement agreement) {
            negotiation.contractAgreement = agreement;
            return this;
        }

        //used mainly for JSON deserialization
        public Builder contractOffers(List<ContractOffer> contractOffers) {
            negotiation.contractOffers = contractOffers;
            return this;
        }

        public Builder contractOffer(ContractOffer contractOffer) {
            negotiation.contractOffers.add(contractOffer);
            return this;
        }

        public Builder type(Type type) {
            negotiation.type = type;
            return this;
        }

        public Builder errorDetail(String errorDetail) {
            negotiation.errorDetail = errorDetail;
            return this;
        }

        public Builder traceContext(Map<String, String> traceContext) {
            negotiation.traceContext = traceContext;
            return this;
        }

        public ContractNegotiation build() {
            Objects.requireNonNull(negotiation.id);
            Objects.requireNonNull(negotiation.counterPartyId);
            Objects.requireNonNull(negotiation.counterPartyAddress);
            Objects.requireNonNull(negotiation.protocol);
            if (Type.PROVIDER == negotiation.type) {
                Objects.requireNonNull(negotiation.correlationId);
            }
            return negotiation;
        }
    }
}
