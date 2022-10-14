package com.demo.wrapper.model.edc.negotiation.command;

import java.util.UUID;

public abstract class Command {
    private final String commandId;
    private int errorCount = 0;

    protected Command() {
        this(UUID.randomUUID().toString());
    }

    protected Command(String commandId) {
        this.commandId = commandId;
    }

    public String getCommandId() {
        return commandId;
    }

    public void increaseErrorCount() {
        errorCount++;
    }


    public boolean canRetry() {
        return errorCount < getMaxRetry();
    }

    protected int getMaxRetry() {
        return 5;
    }
}
