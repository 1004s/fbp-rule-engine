package com.fbp.message;

public class ModbusDataMessage extends Message {

    private final int offset;
    private final String name;
    private final String unit;
    private final int scale;
    private final int value;

    public ModbusDataMessage(int offset, String name, String unit, int scale, int value) {
        this.offset = offset;
        this.name = name;
        this.unit = unit;
        this.scale = scale;
        this.value = value;
    }

    @Override
    public String toString() {
        return "ModbusDataMessage{" +
                "offset=" + offset +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", scale=" + scale +
                ", value=" + value +
                '}';
    }

    @Override
    public Message copy() {
        return new ModbusDataMessage(offset, name, unit, scale, value);
    }
}
