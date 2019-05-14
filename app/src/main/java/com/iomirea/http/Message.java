package com.iomirea.http;

public final class Message extends APIObject {
    private String edit_id;
    private String channel_id;

    private String content;

    private User author;

    private boolean pinned;

    public Message(String id, String edit_id, String channel_id, String content,
                   boolean pinned, User author)
    {
        super(id);

        this.edit_id = edit_id;
        this.channel_id = channel_id;
        this.author = author;
        this.content = content;
        this.pinned = pinned;
    }

    public User getAuthor() {
        return author;
    }

    public void setPinned(boolean pinned)
    {
        this.pinned = pinned;
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
        return Long.valueOf(edit_id);
    }

    public Long getChannelID()
    {
        return Long.valueOf(channel_id);
    }

    public String getContent()
    {
        return content;
    }

    boolean isPinned()
    {
        return pinned;
    }

    @Override
    public String toString() {
        return "Message{" +
                "edit_id=" + edit_id +
                ", channel_id=" + channel_id +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", pinned=" + pinned +
                ", id=" + id +
                '}';
    }
}