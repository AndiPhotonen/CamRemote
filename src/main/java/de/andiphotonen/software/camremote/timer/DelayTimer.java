package de.andiphotonen.software.camremote.timer;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.model.ExposureSession;

/**
 * Created by Andreas Kieburg on 07.12.2016.
 */
/**
 * The implementation of the delay timer.
 * Well... it is not really a timer.
 */
public class DelayTimer extends ExposureSessionStepTimer{
    private static final String DELAY = "Delay";

    /**
     * Creates a new instance of this class.
     * @param currentSession The currently running {@link ExposureSession}
     */
    public DelayTimer(ExposureSession currentSession) {
        super(currentSession);
    }

    @Override
    public Boolean startTimer() {
        if(!isTimerFinished) {
            Integer delay = currentSession.getDelay().getDurationInSeconds();
            CamRemoteMainFormController.getCamRemoteMainForm().setExposureStepLblText(DELAY);
            if (delay >= 1) {
                countdown(delay);
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
        CamRemoteMainFormController.updateStatusPanel(currentSession.getDelay());
    }

    @Override
    protected void countdown(Integer counter) {
        currentSession.getDelay().setDurationInSeconds(--counter);
        log.info("delay countdown: " + currentSession.getDelay().getDurationInSeconds().toString());
    }
}
