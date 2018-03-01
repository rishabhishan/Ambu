package com.example.rishabhk.ambu;

/**
 * Created by rishabhk on 01/03/18.
 */

public class RowItemForBill {

    private String lastUpdated;
    private String startletter;
    private String room_name;
    private String litres_count;

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getStartletter() {
        return startletter;
    }

    public void setStartletter(String startletter) {
        this.startletter = startletter;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getLitres_count() {
        return litres_count;
    }

    public void setLitres_count(String litres_count) {
        this.litres_count = litres_count;
    }

    public RowItemForBill(String room_name, String storedCount, String currentCount, String lastUpdated) {

        this.lastUpdated = lastUpdated;
        this.startletter = String.valueOf(room_name.charAt(0));
        this.room_name = room_name;
        this.litres_count = String.valueOf((Float.parseFloat(storedCount) + Float.parseFloat(currentCount))/10) + " L";
    }

}