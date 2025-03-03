package kg.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.models.Book;
import kg.attractor.java.lesson44.models.Employee;
import kg.attractor.java.lesson45.Lesson45Server;
import kg.attractor.java.server.Cookie;
import kg.attractor.java.util.JsonUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson46Server extends Lesson45Server {
    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/cookie", this::cookieHandler);
        registerGet("/takeBooks", this::takeBooksHandler);
        registerGet("/takeBook/.*", this::takeBookHandler);

    }

    private void cookieHandler(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();

        Cookie sessionCookie = Cookie.make("userId", "!@#$%ˆ&*())_+");
        setCookie(exchange, sessionCookie);

        Cookie c1 = Cookie.make("user%Id", "456");
        setCookie(exchange, c1);

        Cookie c2 = Cookie.make("user-mail", "qwe@qwe.qwe");
        setCookie(exchange, c2);

        Cookie c3 = Cookie.make("restricted()<>#,;:\\\"/[]?={}", "()<>#,;:\\\"/[]?={}");
        setCookie(exchange, c3);

        String cookieString = getCookie(exchange);

        Map<String, String> cookies = Cookie.parse(cookieString);

        String timesName = "times";
        String cookieVal = cookies.getOrDefault(timesName, "0");
        int timesVal = Integer.parseInt(cookieVal) + 1;
        Cookie times = Cookie.make(timesName, timesVal);
        setCookie(exchange, times);
        data.put(timesName, timesVal);

        data.put("cookies", cookies);
        renderTemplate(exchange, "cookie/cookie.ftlh", data);
    }

    private void takeBooksHandler(HttpExchange exchange) {
        String cookieString = getCookie(exchange);
        Map<String, String> cookies = Cookie.parse(cookieString);
        String sessionId = cookies.get("sessionId");
        Employee emp = sessionId != null ? sessions.get(sessionId) : null;
        Map<String, Object> data = new HashMap<>();
        List<Book> books= JsonUtil.getAvailableBooks();
        if (emp == null) {
            data.put("isAuthorized", false);
        } else {
            data.put("isAuthorized", true);
            if (emp.getCurrentBooks().size() > 1) {
                data.put("canTakeBooks", false);
            } else {
                data.put("canTakeBooks", true);
            }
        }

        data.put("booksAvailable", !books.isEmpty());

        data.put("books", books);
        renderTemplate(exchange, "bookActions/takeBook.ftlh", data);
    }

    private void takeBookHandler(HttpExchange exchange) {
        String cookieString = getCookie(exchange);
        Map<String, String> cookies = Cookie.parse(cookieString);
        String sessionId = cookies.get("sessionId");
        Employee emp = sessionId != null ? sessions.get(sessionId) : null;
        Map<String, Object> data = new HashMap<>();
        String path = exchange.getRequestURI().getPath();
        String bookIdString = path.split("/")[2];

        if (emp == null) {
            data.put("isAuthorized", false);
            data.put("message", "Вы не авторизованы. Пожалуйста, войдите в систему.");
            renderTemplate(exchange, "bookActions/takeBookResult.ftlh", data);
            return;
        }

        int bookId = Integer.parseInt(bookIdString);
        Book book = JsonUtil.getBookById(bookId);

        if (book == null) {
            data.put("isAuthorized", true);
            data.put("message", "Книга не найдена.");
        } else if (emp.getCurrentBooks().size() >= 2) {
            data.put("isAuthorized", true);
            data.put("message", "Вы не можете взять больше двух книг.");
        } else if (!book.isAvailable()) {
            data.put("isAuthorized", true);
            data.put("message", "Эта книга уже была взята.");
        } else {
            emp.getCurrentBooks().add(bookId);
            book.setAvailable(false);
            data.put("isAuthorized", true);
            data.put("message", "Книга успешно взята!");
            JsonUtil.writeEmployeesToFile();
        }

        renderTemplate(exchange, "bookActions/takeBookResult.ftlh", data);
    }

}