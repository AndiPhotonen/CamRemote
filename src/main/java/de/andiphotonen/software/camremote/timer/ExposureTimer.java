package de.andiphotonen.software.camremote.timer;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.model.ExposureSession;

/**
 * Created by Andreas Kieburg on 08.12.2016.
 */

/**
 * The implementation of the exposure timer.
 * Well... it is not really a timer.
 */
public class ExposureTimer extends ExposureSessionStepTimer{
    private static final String EXPOSURE = "Duration";

    /**
     * Creates a new instance of this class.
     * @param currentSession The currently running {@link ExposureSession}
     */
    public ExposureTimer(ExposureSession currentSession) {
        super(currentSession);
    }


    @Override
    public Boolean startTimer() {
        if(!isTimerFinished) {
            CamRemoteMainFormController.getCamRemoteMainForm().setExposureStepLblText(EXPOSURE);
            Integer duration = currentSession.getDuration().getDurationInSeconds();
            if (duration >= 1) {
                countdown(duration);
                updateStatus();
            } else {
                updateStatus();
                isTimerFinished = true;
            }
        }
        return isTimerFinished;
    }

    @Override
    protected void updateStatus() {
        CamRemoteMainFormController.updateStatusPanel(currentSession.getDuration());
    }

    @Override
    protected void countdown(Integer counter) {
        currentSession.getDuration().setDurationInSeconds(--counter);
        log.info("exposure countdown: " + currentSession.getDuration().getDurationInSeconds().toString());
    }
}
