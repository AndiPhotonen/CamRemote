package de.andiphotonen.software.camremote.timer;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.model.ExposureSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

/**
 * Created by Andreas Kieburg on 07.12.2016.
 */
public class DelayTimer extends ExposureSessionStepTimer {
    private Logger log = LogManager.getLogger(ExposureSessionStepTimer.class);
    private ExposureSession currentSession;
    private Timer timer;
    private Boolean isTimerFinished;

    public DelayTimer(ExposureSession currentSession, Timer timer) {
        this.currentSession = currentSession;
        this.timer = timer;
        isTimerFinished = false;
    }

    @Override
    public void run() {
        synchronized (this) {
            Integer delay = currentSession.getDelay().getDurationInSeconds();
            CamRemoteMainFormController.getCamRemoteMainForm().setExposureStatusPanelText(currentSession.getAmount().toString());
            if (delay > 1) {
                countdown(delay);
                updateStatus();
            } else {
                countdown(delay);
                updateStatus();
                isTimerFinished = true;
                timer.cancel();
                notify();
            }
        }
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

    @Override
    public Boolean isTimerFinished() {
        return isTimerFinished;
    }
}
