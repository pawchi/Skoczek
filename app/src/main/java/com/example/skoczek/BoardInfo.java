package com.example.skoczek;

public class BoardInfo {
    private String size;
    private String numberOfFields;

    public BoardInfo(String size, String numberOfFields) {
        this.size = size;
        this.numberOfFields = numberOfFields;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getNumberOfFields() {
        return numberOfFields;
    }

    public void setNumberOfFields(String numberOfFields) {
        this.numberOfFields = numberOfFields;
    }
}
