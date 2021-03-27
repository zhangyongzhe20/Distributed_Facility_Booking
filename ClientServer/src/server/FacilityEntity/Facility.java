package server.FacilityEntity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Facility {
    String FacilityName;
    int FacilityType;
    boolean availability[][];
    String slotsString;
    private int facilityID;

    // ------------------------------------------ Constructor ------------------------------------------
    public Facility(String facilityName, int facilityID) {
        this.FacilityName = facilityName;
        if (facilityName.substring(0, 2).equals("LT"))
            setFacilityType(0);
        else
            setFacilityType(1);

        this.availability = new boolean[10][7];
        for (int i = 0; i < 10; i++) {
            java.util.Arrays.fill(this.availability[i], true);
        }
        this.facilityID = facilityID;
    }


    // ------------------------------------------ Set Methods ------------------------------------------
    public void setFacilityName(String facilityName) {
        this.FacilityName = facilityName;
    }

    public void setFacilityType(int facilityType) {
        this.FacilityType = facilityType;
    }

    public void setPrintSlot(int interval){
        this.slotsString = "";

        this.slotsString += "\n";
        this.slotsString += this.FacilityName;
        this.slotsString += "\n";

        for (int j = 0; j < 10; j++) {
            String s = "";
            if ((8+j)<10){
                s+=Integer.toString(j)+"        "+"0"+Integer.toString(8+j);
            }else {
                s+=Integer.toString(j)+"        "+Integer.toString(8+j);
            }
            if ((8+j+1)<10){
                s+="-0"+Integer.toString(8+j+1);
            }else {
                s+="-"+Integer.toString(8+j+1);
            }
            for (int i = 0; i < interval; i++) {
                if (this.availability[j][i])
                    s += "    -           ";
                else
                    s += "    B           ";
            }
            s += "\n";
            this.slotsString += s;
        }
    }

    // ------------------------------------------   Get Method  ------------------------------------------
    public String getPrintResult() {
        return slotsString;
    }

    public String getFacilityName() {
        return FacilityName;
    }

    public int getFacilityID() {
        return facilityID;
    }

    public boolean checkAvailability(int day, int slot){
        return this.availability[slot-1][day-1];
    }

    // ------------------------------------------ Functional Methods  ------------------------------------------
    public void bookAvailability(int day, int slot) {
        this.availability[slot-1][day-1]=false;

    }

    public void cancelBooking(int day, int slot){
        this.availability[slot-1][day-1]=true;
    }

    public boolean changeBooking(int day, int offset, int startIndex, int endIndex){
        System.out.println("Facility:       "+offset);
        if (offset>0)
        {
            for (int i = 0; i < (endIndex-startIndex); i++) {
                if (!this.availability[endIndex+offset-1][day-1]){ // check if endindex + offset has collision
                    return false; // cannot change slot
                }
            }
            for (int i = 0; i < (endIndex-startIndex); i++) {
                this.availability[startIndex+i-1][day-1]=true; //set availability of previous slot to true
                this.availability[endIndex+offset+i-1][day-1]=false; // set availability of new slot to false
            }
        }else
        {// offset<0
            for (int i = 0; i < (endIndex-startIndex); i++) {
                if (!this.availability[startIndex+offset-1][day-1]){ // check if startindex - offset has collision
                    return false; // cannot change slot
                }
            }
            for (int i = 0; i < (endIndex-startIndex); i++) {
                this.availability[startIndex+i-1][day-1]=true; //set availability of previous slot to true
                this.availability[startIndex+offset+i-1][day-1]=false; // set availability of new slot to false
            }
        }
        return true;
    }


}
