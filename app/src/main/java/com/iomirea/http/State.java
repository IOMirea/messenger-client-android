package com.iomirea.http;

import java.util.ArrayList;

public class State {

    private User me;

    private ArrayList<Channel> channels;

    private ArrayList<Message> messages;

    private ArrayList<User> users;

    private ArrayList<File> files;

    public State(User me, ArrayList<Channel> channels, ArrayList<Message> messages, ArrayList<User> users, ArrayList<File> files) {
        this.me = me;
        this.channels = channels;
        this.messages = messages;
        this.users = users;
        this.files = files;
    }

    public State() {
        this.channels = new ArrayList<Channel>();
        this.messages = new ArrayList<Message>();
        this.users = new ArrayList<User>();
        this.files = new ArrayList<File>();
    }

    //TODO: выгрузка контейнеров при выходе из приложения и загрузка при входе.

    public final boolean removeChannel(Channel channel)
    {
        try {
            channels.remove(channel);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean removeMessage(Message message)
    {
        try {
            messages.remove(message);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean removeFile(File file)
    {
        try {
            files.remove(file);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean removeUser(User user)
    {
        try {
            users.remove(user);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean addUser(User user)
    {
        try {
            users.add(user);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean addMessage(Message message)
    {
        try {
            messages.add(message);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean addChannel(Channel channel)
    {
        try {
            channels.add(channel);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean addFile(File file)
    {
        try {
            files.add(file);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        this.me = me;
    }

    public ArrayList getChannels() {
        return channels;
    }

    public ArrayList getMessages() {
        return messages;

    }

    public ArrayList getUsers() {
        return users;
    }

    public ArrayList getFiles() {
        return files;
    }
}