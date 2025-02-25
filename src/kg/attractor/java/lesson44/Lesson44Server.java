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
import kg.attractor.java.server.ResponseCodes;
import kg.attractor.java.lesson44.util.JsonUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/sample", this::freemarkerSampleHandler);
        registerGet("/employees", this::employeesHandler);
        registerGet("/book/.*", this::bookDetailsHandler);
        registerGet("/library", this::booksHandler);
        registerGet("/employee/1", this::employeeDetailsHandler);
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
        List<Employee> employees = JsonUtil.readEmployeesFromFile();
        List<Book> books = JsonUtil.readBooksFromFile();
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("employees", employees);
        dataModel.put("books", books);
        renderTemplate(exchange, "employees.ftlh", dataModel);
    }


    private void bookDetailsHandler(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        int bookId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
        Book book = JsonUtil.getBookById(bookId);
        Map<String, Object> dataModel = new HashMap<>();
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
        List<Book> books = JsonUtil.readBooksFromFile();
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
        String path = exchange.getRequestURI().getPath();
        int employeeId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

        Employee employee = JsonUtil.getEmployeeById(employeeId);
        List<Book> currentBooks = employee.getCurrentBooks().stream()
                .map(JsonUtil::getBookById)
                .toList();

        List<Book> pastBooks = employee.getPastBooks().stream()
                .map(JsonUtil::getBookById)
                .toList();

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("employee", employee);
        dataModel.put("currentBooks", currentBooks);
        dataModel.put("pastBooks", pastBooks);

        renderTemplate(exchange, "employee.ftlh", dataModel);
    }

}
