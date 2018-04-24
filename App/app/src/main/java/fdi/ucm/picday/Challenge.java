package fdi.ucm.picday;

import java.util.ArrayList;

public class Challenge {

    private int id;
    private String name;
    private String description;
    private ArrayList<Picture> pictures;

    public Challenge() {
        pictures = new ArrayList<Picture>();
    }

    public Challenge(String name, String desc) {
        this.name = name;
        description = desc;
        pictures = new ArrayList<Picture>();
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictures() {
        return "transformacion";
    }

    public void setPictures(ArrayList<Picture> pictures) {
        this.pictures = pictures;
    }
}
