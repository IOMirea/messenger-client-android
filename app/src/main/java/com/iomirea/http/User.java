package com.iomirea.http;

public final class User extends APIObject {

    private final static int MAX_USER_NAME = 128;

    private String  name;

    private boolean bot;

    public User (Long id, String name, boolean bot)
    {
        super(id);

        this.name = name;
        this.bot = bot;
    }

    public User (Long id, String name)
    {
        super(id);

        this.name = name;
        this.bot = false;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public boolean isBot()
    {
        return bot;
    }
}