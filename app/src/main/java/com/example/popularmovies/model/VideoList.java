package com.example.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VideoList {

    @SerializedName("results")
    private ArrayList<Video> videoList;
    @SerializedName("id")
    private int id;

//    public VideoList(int id, ArrayList<Video> videoList) {
//        this.videoList = videoList;
//        this.id = id;
//    }

    public ArrayList<Video> getVideoArrayList() {
        return videoList;
    }

    public void setVideoArrayList(ArrayList<Video> videoArrayList) {
        this.videoList = videoArrayList;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

}
