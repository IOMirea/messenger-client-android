package com.iomirea.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import java.util.HashMap;

public class HttpClient {
    private final String API_URL = "https://iomirea.ml/api/";
    private final String API_VERSION = "v0";
    private final String BASE_URL = API_URL + API_VERSION;

    private String token;

    private RequestQueue queue;

    public HttpClient(Context context, String token)
    {
        this.queue = VolleyController.getInstance(context).getRequestQueue();
        this.token = token;
    }

    public void send_message(String token, Long channel_id, String content)
    {
        String url = BASE_URL + "/channels/" + channel_id + "/messages";

        HashMap<String, String> body = new HashMap<>();
        body.put("channel_id", channel_id.toString());
        body.put("content", content);

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, Message.class, body, null, null, token);
        queue.add(request);
    }

    public void get_message(Long channel_id, Long message_id)
    {
        String url = BASE_URL + "/channels/" + channel_id + "/messages/" + message_id;

        //Тестирование получения сообщения
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object response)
            {
                Message message = (Message) response;
                System.out.println(message);
            }
        };

        GenericRequest<Message> request = new GenericRequest<>(url, Message.class, listener, null, token);
        queue.add(request);
    }


    public void create_channel(HashMap body)
    {
        String url = BASE_URL + "/channels";

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, Channel.class, body, null, null, token);
        queue.add(request);
    }

    public void send_bugreport(HashMap body)
    {
        String url = BASE_URL + "/bugreports";

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, null, body, null, null, token);
        queue.add(request);
    }
}