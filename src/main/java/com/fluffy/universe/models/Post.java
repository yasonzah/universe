package com.fluffy.universe.models;

public class Post {
    private Integer id;
    private Integer userId;
    private String title;
    private String description;
    private String publicationDateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublicationDateTime() {
        return publicationDateTime;
    }

    public void setPublicationDateTime(String publicationDateTime) {
        this.publicationDateTime = publicationDateTime;
    }
}
