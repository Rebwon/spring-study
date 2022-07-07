package com.rebwon.toby.springbook.domain;

public enum Type {
    ADMIN(1, "관리자"), MEMBER(2, "회원"), GUEST(3, "비회원");

    private int value;
    private String name;

    Type(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Type valueOf(int value) {
        switch (value) {
            case 1:
                return ADMIN;
            case 2:
                return MEMBER;
            case 3:
                return GUEST;
        }
        throw new IllegalArgumentException("파라미터로 전달된 값에 대응되는 타입이 없습니다.");
    }
}
