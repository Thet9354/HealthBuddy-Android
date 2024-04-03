package com.example.healthbuddy.Model;

public class WorkoutModel {

    private int id;
    private int videoPic;
    private String videoTitle;
    private String videoLink;

    public WorkoutModel(int id, int videoPic, String videoTitle, String videoLink) {
        this.id = id;
        this.videoPic = videoPic;
        this.videoTitle = videoTitle;
        this.videoLink = videoLink;
    }

    public WorkoutModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(int videoPic) {
        this.videoPic = videoPic;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
