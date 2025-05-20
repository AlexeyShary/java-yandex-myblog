package ru.yandex.practicum.util;

public enum TemplateNamesUtil {
    EDIT("edit"),
    POST("post"),
    POSTS("posts"),
    REDIRECT_POSTS("redirect:/posts");

    public final String name;

    TemplateNamesUtil(String name) {
        this.name = name;
    }
}
