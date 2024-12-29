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

    public StatisticsMessage(String senderNodeId, LocalDateTime createdAt, StatisticsDataType statisticsDataType) {
        super();
        this.senderNodeId = senderNodeId;
        this.createdAt = createdAt;
        this.statisticsDataType = statisticsDataType;
    }

    public Message copy() {
        return new StatisticsMessage(this.senderNodeId, this.createdAt, this.statisticsDataType);
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
