package fdi.ucm.picday;

import android.media.Image;

public class Picture {

    private Image pic;
    private User owner;
    private double score;
    private int times_scored;

    public Picture(Image pic, User user) {
        this.pic = pic;
        this.owner = user;
        score = 0;
        times_scored = 0;
    }

    public Picture(Image pic, User user, double score, int times_scored) {
        this.pic = pic;
        this.owner = user;
        this.score = score;
        this.times_scored = times_scored;
    }

    public Image getPic(){
        return pic;
    }

    public void setPic(Image image) {
        pic = image;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User user) {
        owner = user;
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
