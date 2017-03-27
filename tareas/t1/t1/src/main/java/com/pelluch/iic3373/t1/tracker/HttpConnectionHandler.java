package com.pelluch.iic3373.t1.tracker;

import com.pelluch.iic3373.t1.Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by pablo on 3/25/17.
 */
public class HttpConnectionHandler extends  ConnectionHandler
        implements HttpHandler {

    HttpConnectionHandler(Tracker tracker) {
        super(tracker);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String query = httpExchange.getRequestURI().getQuery();
        Map<String, String> paramsMap = Utils.queryToMap(query);

        int port = httpExchange.getRemoteAddress().getPort();
        ByteArrayOutputStream os =
                generateResponse(paramsMap, httpExchange.getRemoteAddress().getPort());

        httpExchange.getResponseHeaders()
                .set("Content-Type", "text/plain; charset=UTF-8");
        httpExchange.sendResponseHeaders(200, os.size());
        os.writeTo(httpExchange.getResponseBody());
        httpExchange.getResponseBody().close();
        os.close();
    }
}
