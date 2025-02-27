package kg.attractor.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.lesson44.models.Employee;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;
import kg.attractor.java.util.JsonUtil;
import kg.attractor.java.util.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson45Server extends Lesson44Server {
    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);
        registerGet("/register", this::handleRegisterPage);
        registerPost("/register", this::handleRegisterPost);
        registerGet("/register-error", this::registerErrorPage);
        registerGet("/register-success", this::registerSuccessPage);
    }

    private void loginGet(HttpExchange exchange) {
        Path path = makeFilePath("/auth/login.ftlh");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }

    private void loginPost(HttpExchange exchange) {

        String cType = exchange.getRequestHeaders()
                .getOrDefault("Content-Type", List.of())
                .get(0);

        String raw = getBody(exchange);

        Map<String, String> params = Utils.parseUrlEncoded(raw, "&");
        String fmt = "<p>Необработанные данные: <b>%s</b></p>" +
                "<p>Content-Type: <b>%s</b></p>" +
                "<p>После обработки: <b>%s</b></p>";
        String data = String.format(fmt, raw, cType, params);

        try {
            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        Map<String, Object> data = new HashMap<>();

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
