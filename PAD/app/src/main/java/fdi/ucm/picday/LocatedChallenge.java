package fdi.ucm.picday;

public class LocatedChallenge extends Challenge{

    private int id;
    private String name;
    private String description;
    private double longitude;
    private double latitude;

    public LocatedChallenge() {

    }

    public LocatedChallenge(int id,String name, String desc, double x, double y) {
        super(id,name,desc);
        this.longitude = x;
        this.latitude = y;
    }

    public double getLongitude() {return this.longitude;}

    public void setLongitude(double x) {this.longitude = x;}

    public double getLatitude() {return this.latitude;}

    public void setLatitude(double y) {this.latitude = y;}

}
