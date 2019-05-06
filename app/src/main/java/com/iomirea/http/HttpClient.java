package com.iomirea.http;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;

public class HttpClient {

    private Context context;

    private static final String API = "https://iomirea.ml/api/";

    private static final String APIVersion = "v0";

    private RequestQueue queue;

    public interface Method {
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
    }

    public HttpClient(Context context)
    {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void send_message(String token, Long channel_id, String content)
    {
        String url = API + APIVersion + "/channels/" + channel_id + "/messages";

        HashMap<String, String> body = new HashMap<>();
        body.put("channel_id", channel_id.toString());
        body.put("content", content);

        GenericRequest request = new GenericRequest(Method.POST, url,null, body, null, null, token);
        queue.add(request);
    }

    public void get_message(String token, Long channel_id, Long message_id)
    {
        String url = API + APIVersion + "/channels/" + channel_id + "/messages/" + message_id;

        //Тестирование получения сообщения
        Response.Listener listner = new Response.Listener() {
            @Override
            public void onResponse(Object response)
            {
                Message message = (Message) response;
                System.out.println("id: " + message.getId() + "\nedit_id: " + message.getEditID() + "\nchannel_id: " +
                                    message.getChannelId() + "\ncontent: " + message.getContent() + "\npinned: " + message.isPinned() +
                                    "\nauthor_id: " + message.getAuthorId() + "\nauthor_name:" + message.getAuthorName() +
                                    "\nis_bot: " + message.fromBot());
            }
        };

        GenericRequest request = new GenericRequest(url, Message.class, listner, null, token);
        queue.add(request);
    }


    public void create_channel(String token, HashMap body)
    {
        String url = API + APIVersion + "/channels";

        GenericRequest request = new GenericRequest(Method.POST, url,null, body, null, null, token);
        queue.add(request);
    }

    public void send_bugreport(String token, HashMap body)
    {
        String url = API + APIVersion + "/bugreports";

        GenericRequest request = new GenericRequest(Method.POST, url,null, body, null, null, token);
        queue.add(request);
    }
}