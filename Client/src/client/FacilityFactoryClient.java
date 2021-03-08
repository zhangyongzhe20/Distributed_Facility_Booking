package client;

import Facility.FacilityFactory;
import Facility.FacilityIF;
import callback.CallBack;
import callback.CallBackImpl;

import java.rmi.Naming;

public class FacilityFactoryClient {
    public static void main(String[] args) {
        try{
            CallBack cb = new CallBackImpl();
            FacilityFactory myFacilityFactory = (FacilityFactory) Naming.lookup("rmi://localhost:5099/Facility");

            myFacilityFactory.register(cb);
            FacilityIF query_results= myFacilityFactory.query("LT", 1);
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
}
