package com.fbp.entity;

public class Channel {

    private final int channelId;
    private final int channelStartAddress;
    private final String location;

    public Channel(int channel, int channelStartAddress, String location) {
        this.channelId = channel;
        this.channelStartAddress = channelStartAddress;
        this.location = location;
    }

    public int getChannelId() {
        return channelId;
    }

    public int getChannelStartAddress() {
        return channelStartAddress;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channel=" + channelId +
                ", channelStartAddress=" + channelStartAddress +
                ", location='" + location +
                '}';
    }

}
