package kg.attractor.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.lesson44.models.Book;
import kg.attractor.java.lesson44.models.Employee;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.Cookie;
import kg.attractor.java.util.JsonUtil;
import kg.attractor.java.util.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Lesson45Server extends Lesson44Server {

    private final Map<String, Employee> sessions = new ConcurrentHashMap<>();
    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);
        registerGet("/login-error", this::loginError);
        registerGet("/register", this::handleRegisterPage);
        registerPost("/register", this::handleRegisterPost);
        registerGet("/register-error", this::registerErrorPage);
        registerGet("/register-success", this::registerSuccessPage);
        registerGet("/profile", this::profileGet);
    }

    private void loginGet(HttpExchange exchange) {
        Path path = makeFilePath("/auth/login.ftlh");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    private void loginPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(raw, "&");
        String email = params.get("email");
        String password = params.get("user-password");

        Employee employee = JsonUtil.getEmployeeByEmail(email);
        if (employee != null && employee.getPassword().equals(password)) {
            String sessionId = UUID.randomUUID().toString();
            sessions.put(sessionId, employee);

            Cookie sessionCookie = Cookie.make("sessionId", sessionId);
            sessionCookie.setMaxAge(600);
            sessionCookie.setHttpOnly(true);
            setCookie(exchange, sessionCookie);

            redirect303(exchange, "/profile");
        } else {
            redirect303(exchange, "/login-error");
        }
    }

    private void loginError(HttpExchange exchange) {
        Path path = makeFilePath("auth/login_error.ftlh");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    private void profileGet(HttpExchange exchange) {
        String cookieString = getCookie(exchange);
        Map<String, String> cookies = Cookie.parse(cookieString);
        String sessionId = cookies.get("sessionId");

        Employee loggedInUser = sessionId != null ? sessions.get(sessionId) : null;


        Map<String, Object> data = new HashMap<>();
        if (loggedInUser != null) {
            List<Book> currentBooks = loggedInUser.getCurrentBooks().stream()
                    .map(JsonUtil::getBookById)
                    .toList();

            List<Book> pastBooks = loggedInUser.getPastBooks().stream()
                    .map(JsonUtil::getBookById)
                    .toList();

            data.put("employee", loggedInUser);
            data.put("currentBooks", currentBooks);
            data.put("pastBooks", pastBooks);

        } else {
            data.put("employee", JsonUtil.getVagueEmp());
            data.put("currentBooks", new ArrayList());
            data.put("pastBooks", new ArrayList());

        }
        renderTemplate(exchange, "employee.ftlh", data);
    }

    private void handleRegisterPage(HttpExchange exchange) {
        sendFile(exchange, makeFilePath("/register/register.ftlh"), ContentType.TEXT_HTML);
    }

    private void handleRegisterPost(HttpExchange exchange) {

        String raw = getBody(exchange);

        Map<String, String> params = Utils.parseUrlEncoded(raw, "&");
        String email = params.get("email");
        String name = params.get("name");
        String position = params.get("position");
        String password = params.get("password");

        if (JsonUtil.getEmployeeByEmail(email) != null) {
            redirect303(exchange, "/register-error");
        } else {
            Employee newEmployee = new Employee(name, position, email, password);
            JsonUtil.addEmployee(newEmployee);

            redirect303(exchange, "/register-success");
        }
    }

    private void registerSuccessPage(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("message", "Регистрация успешна! Теперь вы можете войти.");

        renderTemplate(exchange, "register/register_result.ftlh", data);
    }

    private void registerErrorPage(HttpExchange exchange) {
        Map<String, Object> data = new HashMap<>();
        data.put("success", false);
        data.put("message", "Пользователь с таким email уже зарегистрирован!");

        renderTemplate(exchange, "register/register_result.ftlh", data);
    }




}
