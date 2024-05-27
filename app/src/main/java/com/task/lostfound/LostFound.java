package com.task.lostfound;

public class LostFound {
    private Integer id;
    private String name;
    private String postType;
    private String phone;
    private String description;
    private String location;
    private String date;
    private String lat;
    private String lng;

    public LostFound() {
    }

    public LostFound(String name, String postType, String phone, String description, String location, String date, String lat, String lng) {
        this.name = name;
        this.postType = postType;
        this.phone = phone;
        this.description = description;
        this.location = location;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
