package com.example.testfinalproject;

public class Image {
    String imagePath;

    public Image(String imagePath) {
        this.imagePath = imagePath;
    }

    public Image(){}

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
