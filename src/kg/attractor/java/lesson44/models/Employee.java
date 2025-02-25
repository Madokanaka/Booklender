package kg.attractor.java.lesson44.models;

import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String position;
    private List<Integer> currentBooks;
    private List<Integer> pastBooks;

    public Employee(int id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public Employee(int id, String name, String position, List<Integer> currentBooks, List<Integer> pastBooks) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.currentBooks = currentBooks;
        this.pastBooks = pastBooks;
    }

    public List<Integer> getCurrentBooks() {
        return currentBooks;
    }

    public void setCurrentBooks(List<Integer> currentBooks) {
        this.currentBooks = currentBooks;
    }

    public List<Integer> getPastBooks() {
        return pastBooks;
    }

    public void setPastBooks(List<Integer> pastBooks) {
        this.pastBooks = pastBooks;
    }
}

