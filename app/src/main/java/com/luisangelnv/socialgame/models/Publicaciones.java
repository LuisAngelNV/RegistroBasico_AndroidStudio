package com.luisangelnv.socialgame.models;

public class Publicaciones {
    private String id;
    private String title;
    private String description;
    private String image1;
    private String image2;
    private String category;
    private String IdUser;

    public Publicaciones(){

    }

    public Publicaciones(String id, String title, String description, String image1, String image2, String category, String idUser) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.category = category;
        IdUser = idUser;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIdUser() {
        return IdUser;
    }

    public void setIdUser(String idUser) {
        IdUser = idUser;
    }
}
