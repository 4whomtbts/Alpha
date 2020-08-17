package com.dna.rna;

public class Pair<T> {
    Integer y;
    T x;
    public Pair(Integer y, T x) {
        this.y = y;
        this.x = x;
    }
    public Integer first() {
        return y;
    }
    public T second() {
        return x;
    }
}
