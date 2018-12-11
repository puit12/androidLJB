package com.example.ljb.jbapp.ChatListFrag;

public class Listitem {
    private int profile;
    private String name;
    private String chat;

    public Listitem(int profile, String name) {
        this.profile = profile;
        this.name = name;
    }

    public int getProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }

}