package com.iomirea.http;

import java.util.ArrayList;

public final class Channel extends APIObject {

    private final static int MAX_CHANNEL_NAME = 128;

    private ArrayList<Long> user_ids;

    private ArrayList<Long> pinned_ids;

    private ArrayList<Message> messages;

    private String name;

    public Channel(Long id, String name, ArrayList user_ids, ArrayList pinned_ids, ArrayList  messages)
    {
        super(id);
        this.name = name;
        this.user_ids = user_ids;
        this.pinned_ids = pinned_ids;
        this.messages = messages;
    }

    public Channel(Long id, String name, ArrayList user_ids, ArrayList pinned_ids)
    {
        super(id);
        this.name = name;
        this.user_ids = user_ids;
        this.pinned_ids = pinned_ids;
        this.messages = new ArrayList<Message>();
    }

    public Channel(Long id, String name, ArrayList user_ids)
    {
        super(id);
        this.name = name;
        this.user_ids = user_ids;
        this.pinned_ids = new ArrayList<Long>();
        this.messages = new ArrayList<Message>();
    }

    public Channel(Long id, String name)
    {
        super(id);
        this.name = name;
        this.user_ids = new ArrayList<Long>();
        this.pinned_ids = new ArrayList<Long>();
        this.messages = new ArrayList<Message>();
    }

    public final boolean removePinnedId(Long id)
    {
        try {
            pinned_ids.remove(id);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean addPinnedId(Long id)
    {
        try {
            pinned_ids.add(id);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean removeUserId(Long id)
    {
        try {
            user_ids.remove(id);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public final boolean addUserId(Long id)
    {
        try {
            user_ids.add(id);
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

    public ArrayList getMessages()
    {
        return this.messages;
    }


    public String getName()
    {
        return name;
    }

    public ArrayList getUserIds()
    {
        return user_ids;
    }

    public ArrayList getPinnedIds()
    {
        return pinned_ids;
    }

    public int getUserCount()
    {
        return user_ids.size();
    }

    public int getPinnedCount()
    {
        return pinned_ids.size();
    }
}