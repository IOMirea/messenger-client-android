package com.iomirea.http;

public final class Message extends APIObject {
    private Long edit_id, channel_id;

    private String content;

    private User author;

    private boolean pinned;

    public Message (Long id, Long edit_id, Long channel_id, String content,
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
        return edit_id;
    }

    public Long getChannelID()
    {
        return channel_id;
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