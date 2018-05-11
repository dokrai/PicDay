package fdi.ucm.picday;

import android.graphics.Bitmap;
import android.media.Image;

public class Picture {

    private int id;
    private Bitmap pic;
    private String owner;
    private String challenge;
    private double score;
    private int times_scored;

    public Picture(Bitmap pic, String user) {
        this.pic = pic;
        this.owner = user;
        score = 0;
        times_scored = 0;
    }

    public Picture(int id, Bitmap pic, String user, String challenge, double score, int times_scored) {
        this.id = id;
        this.pic = pic;
        this.owner = user;
        this.challenge = challenge;
        this.score = score;
        this.times_scored = times_scored;
    }

    public int getId(){return this.id;}

    public Bitmap getPic(){
        return pic;
    }

    public void setPic(Bitmap image) {
        pic = image;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String user) {
        owner = user;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String chall) {
        challenge = chall;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        times_scored++;
        this.score = (this.score + score)/times_scored;
    }

    public int getTimes_scored() {
        return times_scored;
    }

    public void setTimes_scored(int times_scored) {
        this.times_scored = times_scored;
    }
}
