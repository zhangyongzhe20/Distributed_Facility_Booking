package server.FacilityEntity;

public class Facility {
    String FacilityName;
    int FacilityType;
    boolean availability[][];
    String printResult;
    public Facility(String facilityName) {
        this.FacilityName = facilityName;
        if (facilityName.substring(0, 2).equals("LT"))
            setFacilityType(0);
        else
            setFacilityType(1);

        this.availability = new boolean[10][7];
        for (int i = 0; i < 10; i++) {
            java.util.Arrays.fill(this.availability[i], true);
        }
    }

    public void setFacilityName(String facilityName) {
        this.FacilityName = facilityName;
    }

    public void setFacilityType(int facilityType) {
        this.FacilityType = facilityType;
    }

    public void bookAvailability(int day, int slot) {
        this.availability[slot-1][day-1]=false;
    }

    public void printFacility(int interval){
        this.printResult = "";

        this.printResult += "\n";
        this.printResult += this.FacilityName;
        this.printResult += "   ";

        for (int j = 0; j < interval; j++) {
            String s = "[";
            for (int i = 0; i < 10; i++) {
                if (this.availability[i][j])
                    s += "-";
                else
                    s += "B";
            }
            s += "]";
            this.printResult += s;
        }
    }

    public String getPrintResult() {
        return printResult;
    }
}
