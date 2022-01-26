package com.example.prana_000.stoichiometrycalculator;


public class Element implements Comparable{
    private String name;
    private double molarMass;
    private String symbol;
    private int atomicNumber;

    public Element(String name, double molarMass, String symbol, int atomicNumber) {
        this.name = name;
        this.molarMass = molarMass;
        this.symbol = symbol;
        this.atomicNumber = atomicNumber;
    }

    public String getName() {
        return name;
    }

    public double getMolarMass() {
        return molarMass;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getAtomicNumber() {
        return atomicNumber;
    }

    @Override
    public String toString() {
        return String.format("%d)%s - %s: %f", atomicNumber, name, symbol, molarMass);
    }

    @Override
    public int compareTo(Object another) {
        if(another instanceof Element) {
            return this.atomicNumber - ((Element) another).getAtomicNumber();
        }else{
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        return this.compareTo(o) == 0;
    }
}
