package kg.attractor.java.util;

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
    private static final String filePath = "info/books.json";
    private static final String filePathToEmployees = "info/employee.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Employee vagueEmp = new Employee("Некий пользователь", "Некая должность", "some@mail.some", "somePassword");

    private static List<Book> books;
    private static List<Employee> employees;

    static {
        loadBooks();
        loadEmployees();
    }

    private static void loadBooks() {
        try (FileReader reader = new FileReader(filePath)) {
            Type bookListType = new TypeToken<List<Book>>(){}.getType();
            books = gson.fromJson(reader, bookListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadEmployees() {
        try (FileReader reader = new FileReader(filePathToEmployees)) {
            Type employeeListType = new TypeToken<List<Employee>>(){}.getType();
            employees = gson.fromJson(reader, employeeListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> readBooks() {
        return books;
    }

    public static List<Employee> readEmployees() {
        return employees;
    }

    public static void writeBooksToFile() {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(books, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeEmployeesToFile() {
        try (FileWriter writer = new FileWriter(filePathToEmployees)) {
            gson.toJson(employees, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Book getBookById(int id) {
        return books.stream().filter(book -> book.getId() == id).findFirst().orElse(null);
    }

    public static Employee getEmployeeById(int id) {
        return employees.stream().filter(emp -> emp.getId() == id).findFirst().orElse(null);
    }

    public static Employee getEmployeeByEmail(String email) {
        return employees.stream().filter(emp -> emp.getEmail().equals(email.trim())).findFirst().orElse(null);
    }

    public static void addEmployee(Employee employee) {
        employee.setId(getMaxEmployeeId() + 1);
        employees.add(employee);
        writeEmployeesToFile();
    }

    public static int getMaxEmployeeId() {
        return employees.stream().mapToInt(Employee::getId).max().orElse(0);
    }

    public static Employee getVagueEmp (){
        return vagueEmp;
    }
}
