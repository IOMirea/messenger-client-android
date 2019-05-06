package com.iomirea.http;

public final class Message extends APIObject {

    private final static int MAX_MESSAGE_LENGTH = 2048;

    private Long edit_id, channel_id;

    private String content;

    private User author;

    private boolean toUser, pinned, deleted;

    public Message (Long id, Long edit_id, Long channel_id, String content,
                    boolean toUser , boolean pinned ,boolean deleted, User author)
    {
        super(id);
        this.id = id;
        this.edit_id = edit_id;
        this.channel_id = channel_id;
        this.author = author;
        this.content = content;
        this.toUser = toUser;
        this.pinned = pinned;
        this.deleted = deleted;
    }

    public Message (Long id, Long channel_id, String content, boolean toUser, User author)
    {
        super(id);
        this.id = id;
        this.edit_id = null;
        this.channel_id = channel_id;
        this.author = author;
        this.content = content;
        this.toUser = toUser;
        this.pinned = false;
        this.deleted = false;
    }

    public void setPinned(boolean pinned)
    {
        this.pinned = pinned;
    }

    public Long getId()
    {
        return this.id;
    }

    public String getAuthorName()
    {
        return author.getName();
    }

    public boolean messageSendedToUser()
    {
        return toUser;
    }

    public int getMessageLength()
    {
        return content.length();
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Long getEditID()
    {
        return edit_id;
    }

    public Long getChannelId()
    {
        return channel_id;
    }

    public Long getAuthorId()
    {
        return author.getId();
    }

    public String getContent()
    {
        return content;
    }

    public boolean isPinned()
    {
        return pinned;
    }

    public boolean isDeleted()
    {
        return deleted;
    }

    public boolean fromBot()
    {
        return author.isBot();
    }
}