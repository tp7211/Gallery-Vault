package com.example.testfinalproject;

public class Package {
    String packg_name;
    String img;
    int downloads;

    public Package(String packg_name, String img, int downloads) {
        this.packg_name = packg_name;
        this.img = img;
        this.downloads = downloads;
    }

    public Package(){}

    public String getPackg_name() {
        return packg_name;
    }

    public void setPackg_name(String packg_name) {
        this.packg_name = packg_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }
}
