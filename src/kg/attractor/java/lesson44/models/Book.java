package kg.attractor.java.lesson44.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private boolean available;
    private String imagePath;

    public Book(int id, String title, String author, boolean available, String imagePath) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = available;
        this.imagePath = imagePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getImagePath() {
        return imagePath;
    }
}

