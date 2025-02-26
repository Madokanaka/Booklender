package kg.attractor.java.lesson44.models;

import java.util.List;

public class Employee {
    private int id;
    private String name;
    private String position;
    private String email;
    private String password;
    private List<Integer> currentBooks;
    private List<Integer> pastBooks;

    public Employee(int id, String name, String position, String email, String password) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.email = email;
        this.password = password;
    }

    public Employee(int id, String name, String position, String email, String password,
                    List<Integer> currentBooks, List<Integer> pastBooks) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.email = email;
        this.password = password;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
