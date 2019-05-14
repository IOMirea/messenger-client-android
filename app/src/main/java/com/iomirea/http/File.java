package com.iomirea.http;

public final class File extends APIObject {
    private Long message_id, channel_id;

    private String name;

    public File(String id, Long message_id, Long channel_id, String name)
    {
        super(id);

        this.message_id = message_id;
        this.channel_id = channel_id;
        this.name = name;
    }

    public Long getMessageID()
    {
        return message_id;
    }

    public Long getChannelID()
    {
        return channel_id;
    }

    public String getName()
    {
        return name;
    }
}