package server;

import Facility.FacilityFactoryImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class FacilityFactoryServer {
    public static void main(String[] args) {
        try{
            LocateRegistry.createRegistry(5099);
            FacilityFactoryImpl myFacilityFactory = new FacilityFactoryImpl();
            Naming.rebind("rmi://localhost:5099/Facility", myFacilityFactory);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
