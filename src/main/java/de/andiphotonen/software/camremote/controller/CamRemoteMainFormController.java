package de.andiphotonen.software.camremote.controller;

import de.andiphotonen.software.camremote.model.ExposureSession;
import de.andiphotonen.software.camremote.model.ExposureSessionStepDuration;
import de.andiphotonen.software.camremote.view.CamRemoteMainForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Andreas Kieburg on 13.11.2016.
 */

/**
 * The controller of the main form.
 */
public class CamRemoteMainFormController {

    private static final Logger log = LogManager.getLogger(CamRemoteMainFormController.class);
    private static CamRemoteMainFormController instanceOfThisObject;
    private static CamRemoteMainForm camRemoteMainForm;
    private static ExposureTimerController timer;

    /**
     * The main method.
     * @param args
     */
    public static void main(String args[]){
        timer = new ExposureTimerController();
        camRemoteMainForm = new CamRemoteMainForm();
    }

    /**
     * Default constructor. It is private because of singleton pattern.
     */
    private CamRemoteMainFormController(){
    }

    public static CamRemoteMainForm getCamRemoteMainForm(){
        return camRemoteMainForm;
    }


    /**
     * Starts the exposure session.
     */
    public static void startTimer(final ExposureSession selectedSession){

        timer.setupTimer(selectedSession);
        timer.startExposureSession();
    }

    /**
     * Stops the exposure session.
     */
    public static void stopTimer(){
        timer.stopExposureSession();
    }

    /**
     * Updates the timer display.
     * @param duration
     */
    public static void updateStatusPanel(ExposureSessionStepDuration duration){
        camRemoteMainForm.updateStatusPanel(duration);
    }
}
