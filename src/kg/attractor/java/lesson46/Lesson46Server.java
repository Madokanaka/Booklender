package kg.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson45.Lesson45Server;
import kg.attractor.java.server.Cookie;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Lesson46Server extends Lesson45Server {
    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/cookie", this::cookieHandler);
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
}