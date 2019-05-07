package com.iomirea.http;

import java.util.ArrayList;

public final class Channel extends APIObject {
    private ArrayList<Long> user_ids;

    private ArrayList<Long> pinned_ids;

    private ArrayList<Message> messages;

    private String name;

    public Channel(Long id, String name, ArrayList<Long> user_ids, ArrayList<Long> pinned_ids, ArrayList<Message> messages)
    {
        super(id);

        this.name = name;
        this.user_ids = user_ids;
        this.pinned_ids = pinned_ids;
        this.messages = messages;
    }

    public final boolean removePinnedID(Long id)
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

    public final boolean addPinnedID(Long id)
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

    public final boolean removeUserID(Long id)
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

    public final boolean addUserID(Long id)
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

    public ArrayList getUserIDs()
    {
        return user_ids;
    }

    public ArrayList getPinnedIDs()
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