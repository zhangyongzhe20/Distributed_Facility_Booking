package server.FacilityEntity;

public class Facility {
    String FacilityName;
    int FacilityType;
    boolean availability[][];
    String slots;
    private int facilityID;

    public String getFacilityName() {
        return FacilityName;
    }

    public int getFacilityID() {
        return facilityID;
    }

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

    public void setFacilityName(String facilityName) {
        this.FacilityName = facilityName;
    }

    public void setFacilityType(int facilityType) {
        this.FacilityType = facilityType;
    }

    public boolean bookAvailability(int day, int slot) {
        if (!this.availability[slot-1][day-1])
            return false; // the slot is booked already
        else
        {
            this.availability[slot-1][day-1]=false;
            return true;
        }
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


    public void setPrintSlot(int interval){
        this.slots = "";

        this.slots += "\n";
        this.slots += this.FacilityName;
        this.slots += "   ";

        for (int j = 0; j < interval; j++) {
            String s = "[";
            for (int i = 0; i < 10; i++) {
                if (this.availability[i][j])
                    s += "-";
                else
                    s += "B";
            }
            s += "]";
            this.slots += s;
        }
    }

    public String getPrintResult() {
        return slots;
    }
}
