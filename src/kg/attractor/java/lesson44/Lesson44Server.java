package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.lesson44.models.Book;
import kg.attractor.java.lesson44.models.Employee;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.Cookie;
import kg.attractor.java.server.ResponseCodes;
import kg.attractor.java.util.JsonUtil;
import kg.attractor.java.util.Utils;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    protected final Map<String, Employee> sessions = new ConcurrentHashMap<>();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/sample", this::freemarkerSampleHandler);
        registerGet("/employees", this::employeesHandler);
        registerGet("/book", this::bookDetailsHandler);
        registerGet("/books", this::booksHandler);
        registerGet("/employee", this::employeeDetailsHandler);
        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);
        registerGet("/login-error", this::loginError);
        registerGet("/register", this::handleRegisterPage);
        registerPost("/register", this::handleRegisterPost);
        registerGet("/register-error", this::registerErrorPage);
        registerGet("/register-success", this::registerSuccessPage);
        registerGet("/profile", this::profileGet);
        registerGet("/cookie", this::cookieHandler);
        registerGet("/takeBooks", this::takeBooksHandler);
        registerGet("/takeBook/.*", this::takeBookHandler);
        registerGet("/returnBooks", this::returnBooksHandler);
        registerGet("/returnBook/.*", this::returnBookHandler);
        registerGet("/logout", this::logoutHandler);
        registerGet("/query", this::handleQueryRequest);
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            // путь к каталогу в котором у нас хранятся шаблоны
            // это может быть совершенно другой путь, чем тот, откуда сервер берёт файлы
            // которые отправляет пользователю
            cfg.setDirectoryForTemplateLoading(new File("data"));

            // прочие стандартные настройки о них читать тут
            // https://freemarker.apache.org/docs/pgui_quickstart_createconfiguration.html
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void employeesHandler(HttpExchange exchange) {
        List<Employee> employees = JsonUtil.readEmployees();
        List<Book> books = JsonUtil.readBooks();
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("employees", employees);
        dataModel.put("books", books);
        renderTemplate(exchange, "employees.ftlh", dataModel);
    }


    private void bookDetailsHandler(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
        Map<String, Object> dataModel = new HashMap<>();
        if (!params.containsKey("bookId")) {
            dataModel.put("errorMessage", "ID книги не указан");
            renderTemplate(exchange, "error.ftlh", dataModel);
            return;
        }

        int bookId;
        try {
            bookId = Integer.parseInt(params.get("bookId"));
        } catch (NumberFormatException e) {
            dataModel.put("errorMessage", "ID книги не является цифровым значением");
            renderTemplate(exchange, "error.ftlh", dataModel);
            return;
        }

        Book book = JsonUtil.getBookById(bookId);

        if (book == null) {
            dataModel.put("errorMessage", "Книга с указанным ID не найдена");
            renderTemplate(exchange, "error.ftlh", dataModel);
            return;
        }
        dataModel.put("book", book);
        renderTemplate(exchange, "book.ftlh", dataModel);
    }


    private void freemarkerSampleHandler(HttpExchange exchange) {
        renderTemplate(exchange, "sample.ftlh", getSampleDataModel());
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            // Загружаем шаблон из файла по имени.
            // Шаблон должен находится по пути, указанном в конфигурации
            Template temp = freemarker.getTemplate(templateFile);

            // freemarker записывает преобразованный шаблон в объект класса writer
            // а наш сервер отправляет клиенту массивы байт
            // по этому нам надо сделать "мост" между этими двумя системами

            // создаём поток, который сохраняет всё, что в него будет записано в байтовый массив
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // создаём объект, который умеет писать в поток и который подходит для freemarker
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {

                // обрабатываем шаблон заполняя его данными из модели
                // и записываем результат в объект "записи"
                temp.process(dataModel, writer);
                writer.flush();

                // получаем байтовый поток
                byte[] data = stream.toByteArray();

                // отправляем результат клиенту
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private void booksHandler(HttpExchange exchange) {
        List<Book> books = JsonUtil.readBooks();
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("books", books);
        renderTemplate(exchange, "books.ftlh", dataModel);
    }


    private Map<String, Object> getSampleDataModel() {
        // возвращаем экземпляр тестовой модели-данных
        // которую freemarker будет использовать для наполнения шаблона
//        return new SampleDataModel();
        Map<String, Object> data = new HashMap<>();
        data.put("user", new SampleDataModel.User("Alex", "Attractor"));
        data.put("currentDateTime", LocalDateTime.now());
        data.put("customers", List.of(
                new SampleDataModel.User("Marco"),
                new SampleDataModel.User("Winston", "Duarte"),
                new SampleDataModel.User("Amos", "Burton", "'Timmy'")
        ));
        return data;
    }

    private void employeeDetailsHandler(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
        Map<String, Object> dataModel = new HashMap<>();
        if (!params.containsKey("employeeId")) {
            dataModel.put("errorMessage", "ID пользователя не указан");
            renderTemplate(exchange, "error.ftlh", dataModel);
            return;
        }

        int employeeId;
        try {
            employeeId = Integer.parseInt(params.get("employeeId"));
        } catch (NumberFormatException e) {
            dataModel.put("errorMessage", "ID пользователя не является цифровым значением");
            renderTemplate(exchange, "error.ftlh", dataModel);
            return;
        }

        Employee employee = JsonUtil.getEmployeeById(employeeId);

        if (employee == null) {
            dataModel.put("errorMessage", "Пользователь с указанным ID не найден");
            renderTemplate(exchange, "error.ftlh", dataModel);
            return;
        }
        List<Book> currentBooks = employee.getCurrentBooks().stream()
                .map(JsonUtil::getBookById)
                .toList();

        List<Book> pastBooks = employee.getPastBooks().stream()
                .map(JsonUtil::getBookById)
                .toList();

        dataModel.put("employee", employee);
        dataModel.put("currentBooks", currentBooks);
        dataModel.put("pastBooks", pastBooks);

        renderTemplate(exchange, "employee.ftlh", dataModel);
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
        Map<String, Object> data = new HashMap<>();
        data.put("error", null);
        renderTemplate(exchange, "register/register.ftlh", data);
    }

    private void handleRegisterPost(HttpExchange exchange) {

        String raw = getBody(exchange);

        Map<String, String> params = Utils.parseUrlEncoded(raw, "&");
        String email = params.get("email");
        String name = params.get("name");
        String position = params.get("position");
        String password = params.get("password");

        if (email == null || email.isEmpty() ||
                name == null || name.isEmpty() ||
                position == null || position.isEmpty() ||
                password == null || password.isEmpty()) {
            Map<String, Object> data = new HashMap<>();
            data.put("error", "Все поля должны быть заполнены!");
            renderTemplate(exchange, "register/register.ftlh", data);
            return;
        }

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
            renderTemplate(exchange, "bookActions/actionBookResult.ftlh", data);
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
            JsonUtil.writeBooksToFile();
        }

        renderTemplate(exchange, "bookActions/actionBookResult.ftlh", data);
    }

    private void returnBooksHandler(HttpExchange exchange) {
        String cookieString = getCookie(exchange);
        Map<String, String> cookies = Cookie.parse(cookieString);
        String sessionId = cookies.get("sessionId");
        Employee emp = sessionId != null ? sessions.get(sessionId) : null;
        Map<String, Object> data = new HashMap<>();
        if (emp == null) {
            data.put("isAuthorized", false);
        } else {
            List<Integer> currentBookIds = emp.getCurrentBooks();
            List<Book> books = currentBookIds.stream()
                    .map(id -> JsonUtil.getBookById(id))
                    .filter(book -> book != null)
                    .collect(Collectors.toList());
            data.put("isAuthorized", true);
            data.put("hasBooks", !emp.getCurrentBooks().isEmpty());
            data.put("books", books);
        }

        renderTemplate(exchange, "bookActions/returnBook.ftlh", data);
    }

    private void returnBookHandler(HttpExchange exchange) {
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
            renderTemplate(exchange, "bookActions/actionBookResult.ftlh", data);
            return;
        }

        int bookId = Integer.parseInt(bookIdString);
        Book book = JsonUtil.getBookById(bookId);

        if (book == null) {
            data.put("isAuthorized", true);
            data.put("message", "Книга не найдена.");
        } else if (emp.getCurrentBooks().isEmpty()) {
            data.put("isAuthorized", true);
            data.put("message", "У вас нет книг для возврата.");
        } else {
            emp.getCurrentBooks().remove((Integer)bookId);
            if (!emp.getPastBooks().contains(bookId))
                emp.getPastBooks().add(bookId);

            book.setAvailable(true);
            data.put("isAuthorized", true);
            data.put("message", "Книга успешно возвращено!");
            JsonUtil.writeEmployeesToFile();
            JsonUtil.writeBooksToFile();
        }

        renderTemplate(exchange, "bookActions/actionBookResult.ftlh", data);
    }

    private void logoutHandler(HttpExchange exchange) {
        String cookieString = getCookie(exchange);
        Map<String, String> cookies = Cookie.parse(cookieString);
        String sessionId = cookies.get("sessionId");

        if(sessionId != null) {
            sessions.remove(sessionId);
        }

        Cookie expiredCookie = Cookie.make("sessionId", "");
        expiredCookie.setMaxAge(0);
        expiredCookie.setHttpOnly(true);
        setCookie(exchange, expiredCookie);

        redirect303(exchange, "/login");
    }

    private void handleQueryRequest(HttpExchange exchange) {
        String query = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(query, "&");

        Map<String, Object> data = new HashMap<>();
        data.put("query", params);
        renderTemplate(exchange, "query.ftlh", data);

    }

}
