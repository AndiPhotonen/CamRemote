package de.andiphotonen.software.camremote.controller;

import de.andiphotonen.software.camremote.model.ExposureSession;
import de.andiphotonen.software.camremote.model.ExposureSessionStepDuration;
import de.andiphotonen.software.camremote.view.CamRemoteMainForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Andreas Kieburg on 13.11.2016.
 */
public class CamRemoteMainFormController {

    private static final Logger log = LogManager.getLogger(CamRemoteMainFormController.class);
    private static CamRemoteMainFormController instanceOfThisObject;
    private static CamRemoteMainForm camRemoteMainForm;



    public static void main(String args[]){
        camRemoteMainForm = new CamRemoteMainForm();
    }

    private CamRemoteMainFormController(){
    }

    public static CamRemoteMainForm getCamRemoteMainForm(){
        return camRemoteMainForm;
    }


    /**
     * Starts the countdown of a exposure session step.
     */
    public static void startTimer(final ExposureSession selectedSession){
        ExposureTimerController timer = new ExposureTimerController(selectedSession);
        timer.startExposureSession();
    }

    public static void updateStatusPanel(ExposureSessionStepDuration duration){
        camRemoteMainForm.updateStatusPanel(duration);
    }
}
