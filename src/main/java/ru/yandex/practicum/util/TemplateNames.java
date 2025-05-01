package ru.yandex.practicum.util;

public enum TemplateNames {
    ADD_POST("post-add"),
    POST("post"),
    POSTS("posts");

    public final String name;

    TemplateNames(String name) {
        this.name = name;
    }
}
