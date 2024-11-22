package org.example;

public enum Command {

    EXIT("종료"),
    LIST("목록"),
    ADD("등록"),
    DELETE("삭제"),
    BUILD("빌드");

    private final String command;

    Command(String command) {
        this.command = command;
    }
}
