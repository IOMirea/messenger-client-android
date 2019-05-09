package com.iomirea.http;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.HashMap;


public class HttpClient {
    private final String API_URL = "https://iomirea.ml/api/";
    private final String API_VERSION = "v0";
    private final String BASE_URL = API_URL + API_VERSION;

    private String token;

    private RequestQueue queue;

    public HttpClient(Context context, String token) {
        this.queue = VolleyController.getInstance(context).getRequestQueue();
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void send_message(Long channel_id, String content, CallbackListener<Message> cl) {
        String url = BASE_URL + "/channels/" + channel_id + "/messages";

        HashMap<String, String> body = new HashMap<>();
        body.put("channel_id", channel_id.toString());
        body.put("content", content);

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, Message.class, token, body, cl);

        queue.add(request);
    }

    public void get_message(Long channel_id, Long message_id, CallbackListener<Message> cl) {
        String url = BASE_URL + "/channels/" + channel_id + "/messages/" + message_id;

        GenericRequest request = new GenericRequest<>(url, Message.class, token, cl);

        queue.add(request);
    }

    public void get_messages(Long channel_id, Long offset, CallbackListener<Message[]> cl) {
        String url = BASE_URL + "/channels/" + channel_id + "/messages";

        // TODO: support offset
        GenericRequest request = new GenericRequest<>(url, Message[].class, token, cl);

        queue.add(request);
    }

    public void create_channel(HashMap body, CallbackListener<Channel> cl) {
        String url = BASE_URL + "/channels";

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, Channel.class, token, body, cl);

        queue.add(request);
    }

    public void get_channel(Long channel_id, CallbackListener<Channel> cl) {
        String url = BASE_URL + "/channels/" + channel_id;

        GenericRequest request = new GenericRequest<>(url, Channel.class, token, cl);

        queue.add(request);
    }

    public void get_channels(CallbackListener<Channel[]> cl) {
        String url = BASE_URL + "/users/@me/channels";

        GenericRequest request = new GenericRequest<>(url, Channel[].class, token, cl);

        queue.add(request);
    }

    public void send_bugreport(HashMap body, CallbackListener<BugReport> cl) {
        String url = BASE_URL + "/bugreports";

        GenericRequest request = new GenericRequest<>(Request.Method.POST, url, null, token, body, cl);

        queue.add(request);
    }

    public void identify(CallbackListener<User> cl) {
        String url = BASE_URL + "/users/@me";

        GenericRequest request = new GenericRequest<>(url, User.class, token, cl);

        queue.add(request);
    }
}