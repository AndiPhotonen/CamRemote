package de.andiphotonen.software.camremote.controller;

/**
 * Created by Andreas Kieburg on 08.11.2016.
 */
public class ExposureController {

    /** The singleton instance of this class */
    private static ExposureController instanceOfThisObject;



    private ExposureController(){}

    public void startExposure(){
        //start exposure
    }

    private void pauseExposure(){
        //pause exposure
    }

    private void resumeExposure(){
        //resume exposure
    }

    private void stopExposure(){
        //stop exposure
    }

    public static ExposureController getInstanceOfThisObject() {
        if (instanceOfThisObject == null) {
            instanceOfThisObject = new ExposureController();
        }
        return instanceOfThisObject;
    }
}
