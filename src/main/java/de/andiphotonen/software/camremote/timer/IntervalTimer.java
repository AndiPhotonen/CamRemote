package de.andiphotonen.software.camremote.timer;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.model.ExposureSession;

/**
 * Created by Andreas Kieburg on 08.12.2016.
 */

/**
 * The implementation of the interval timer.
 * Well... it is not really a timer.
 */
public class IntervalTimer extends ExposureSessionStepTimer{
    private static final String INTERVAL = "Interval";

    /**
     * Creates a new instance of this class.
     * @param currentSession The currently running {@link ExposureSession}
     */
    public IntervalTimer(ExposureSession currentSession) {
        super(currentSession);
    }

    @Override
    public Boolean startTimer() {
        if(!isTimerFinished) {
            CamRemoteMainFormController.getCamRemoteMainForm().setExposureStepLblText(INTERVAL);
            Integer interval = currentSession.getInterval().getDurationInSeconds();
            //The interval duration is one second shorter then set, because reducing the amount takes an extra second
            if (interval > 1) {
                countdown(interval);
                updateStatus();
            } else {
                countdown(interval);
                updateStatus();
                isTimerFinished = true;
            }
        }
        return isTimerFinished;
    }

    @Override
    protected void updateStatus() {
        CamRemoteMainFormController.updateStatusPanel(currentSession.getInterval());
    }

    @Override
    protected void countdown(Integer counter) {
        currentSession.getInterval().setDurationInSeconds(--counter);
        log.info("interval countdown: " + currentSession.getInterval().getDurationInSeconds().toString());
    }
}
