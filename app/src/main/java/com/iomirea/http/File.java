package com.iomirea.http;

public final class File extends APIObject {

    private final static int MAX_FILE_NAME = 128;

    private Long message_id, channel_id;

    private String name;

    public File(Long id, Long message_id, Long channel_id, String name)
    {
        super(id);
        this.message_id = message_id;
        this.channel_id = channel_id;
        this.name = name;
    }

    public Long getMessage_id()
    {
        return message_id;
    }

    public Long getChannel_id()
    {
        return  channel_id;
    }

    public String getName()
    {
        return name;
    }
}