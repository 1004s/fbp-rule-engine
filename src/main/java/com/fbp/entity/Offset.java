package com.fbp.entity;

public class Offset {

    private final int offset;
    private final String name;
    private final String type;
    private final String unit;
    private final int size;
    private final int scale;
    private final String description;

    public Offset(int offset, String name, String type, String unit, int size, int scale, String description) {
        this.offset = offset;
        this.name = name;
        this.type = type;
        this.unit = unit;
        this.size = size;
        this.scale = scale;
        this.description = description;
    }

    public int getOffset() {
        return offset;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getUnit() {
        return unit;
    }

    public int getSize() {
        return size;
    }

    public int getScale() {
        return scale;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Offset{" +
                "offset=" + offset +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", unit='" + unit + '\'' +
                ", size=" + size +
                ", scale=" + scale +
                ", description='" + description + '\'' +
                '}';
    }
}
