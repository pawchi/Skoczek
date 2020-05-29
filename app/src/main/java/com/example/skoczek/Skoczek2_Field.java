package com.example.skoczek;

import java.util.ArrayList;

public class Skoczek2_Field {

    private int fieldStatus;
    private ArrayList<Integer> possibleMoves;

    public Skoczek2_Field(int fieldStatus, ArrayList<Integer> possibleMoves) {
        this.fieldStatus = fieldStatus;
        this.possibleMoves = possibleMoves;
    }

    public int getFieldStatus() {
        return fieldStatus;
    }

    public void setFieldStatus(int fieldStatus) {
        this.fieldStatus = fieldStatus;
    }

    public ArrayList<Integer> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(ArrayList<Integer> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

}
