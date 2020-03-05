package com.example.trailsafe;
import android.location.Location;
public class Clocation extends Location {

    private boolean bUseMetricUnits = false;

    public Clocation(Location location){
        this(location, true);
    }

    public Clocation(Location location, boolean bUseMetricUnits){
        super(location);
        this.bUseMetricUnits = bUseMetricUnits;
    }

    public boolean getUseMetricUnits(){
        return this.bUseMetricUnits;
    }

    public void setbUseMetricUnits(boolean bUseMetricUnits){
        this.bUseMetricUnits = bUseMetricUnits;
    }


    @Override
    public float distanceTo(Location dest) {
        float Distance = super.distanceTo(dest);

        return Distance;
    }

    @Override
    public double getAltitude() {
        double Altitude = super.getAltitude();

        return Altitude;
    }

    @Override
    public float getSpeed() {
        float Speed = super.getSpeed();

        return Speed;
    }

    @Override
    public float getAccuracy() {
        float Accuracy = super.getAccuracy();

        return Accuracy;
    }
}
