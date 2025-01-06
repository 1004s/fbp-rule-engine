package com.fbp.message;

import java.time.LocalDateTime;
import java.util.UUID;

public class StatisticsMessage extends Message {

    public enum StatisticsDataType {
        INPUT,
        OUTPUT,
        ERROR
    }

    private final String senderNodeId;
    private final LocalDateTime createdAt;
    private final StatisticsDataType statisticsDataType;
    private String errorMessage;

    public StatisticsMessage(String senderNodeId, StatisticsDataType statisticsDataType) {
        this(senderNodeId, statisticsDataType, null);
    }

    public StatisticsMessage(String senderNodeId, StatisticsDataType statisticsDataType, String errorMessage) {
        super();
        this.senderNodeId = senderNodeId;
        this.createdAt = LocalDateTime.now();
        this.statisticsDataType = statisticsDataType;
        this.errorMessage = errorMessage;
    }

    public Message copy() {
        return new StatisticsMessage(this.senderNodeId, this.statisticsDataType, this.errorMessage);
    }

    public String getSenderNodeId() {
        return senderNodeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public StatisticsDataType getStatisticsDataType() {
        return statisticsDataType;
    }
}
