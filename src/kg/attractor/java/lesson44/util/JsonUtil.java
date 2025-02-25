package kg.attractor.java.lesson44.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import kg.attractor.java.lesson44.models.Book;
import kg.attractor.java.lesson44.models.Employee;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {
    private static String filePath = "src/info/books.json";
    private static String filePathToEmployees = "src/info/employee.json";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static List<Book> readBooksFromFile() {
        try (FileReader reader = new FileReader(filePath)) {
            Type bookListType = new TypeToken<List<Book>>(){}.getType();
            return gson.fromJson(reader, bookListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Employee> readEmployeesFromFile() {
        try (FileReader reader = new FileReader(filePathToEmployees)) {
            Type employeeListType = new TypeToken<List<Employee>>(){}.getType();
            return gson.fromJson(reader, employeeListType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeBooksToFile(List<Book> books) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(books, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeEmployeesToFile(List<Employee> employees) {
        try (FileWriter writer = new FileWriter(filePathToEmployees)) {
            gson.toJson(employees, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Book getBookById(int id) {
        List<Book> books = readBooksFromFile();
        return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }
}

