package main.java;

public interface MutableResultModel {
    void add(ResultRow item);
    void remove(ResultRow item);
    ResultRow getRow(int row);
}
