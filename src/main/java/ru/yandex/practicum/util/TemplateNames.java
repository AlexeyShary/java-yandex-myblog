package ru.yandex.practicum.util;

public enum TemplateNames {
    EDIT("edit"),
    POST("post"),
    POSTS("posts"),
    REDIRECT_POSTS("redirect:/posts");

    public final String name;

    TemplateNames(String name) {
        this.name = name;
    }
}
