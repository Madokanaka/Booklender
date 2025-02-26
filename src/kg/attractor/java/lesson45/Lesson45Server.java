package kg.attractor.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;
import kg.attractor.java.util.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Lesson45Server extends Lesson44Server {
    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/auth/login", this::loginGet);
        registerPost("/auth/login", this::loginPost);
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
}
