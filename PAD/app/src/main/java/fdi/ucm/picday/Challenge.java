package fdi.ucm.picday;

import java.util.ArrayList;

public class Challenge {

    private int id;
    private String name;
    private String description;

    public Challenge() {

    }

    public Challenge(String name, String desc) {
        this.name = name;
        description = desc;
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

}
