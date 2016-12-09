package de.andiphotonen.software.camremote.timer;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.model.ExposureSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

/**
 * Created by Andreas Kieburg on 08.12.2016.
 */
public class IntervalTimer extends ExposureSessionStepTimer {
    private Logger log = LogManager.getLogger(ExposureSessionStepTimer.class);
    private ExposureSession currentSession;
    private Timer timer;
    private Boolean isTimerFinished;

    public IntervalTimer(ExposureSession currentSession, Timer timer){
        this.currentSession = currentSession;
        this.timer = timer;
        isTimerFinished = false;
    }

    @Override
    public void run() {
        synchronized (this) {
            Integer interval = currentSession.getInterval().getDurationInSeconds();
            CamRemoteMainFormController.getCamRemoteMainForm().setExposureStatusPanelText(currentSession.getAmount().toString());
            if (interval > 1) {
                countdown(interval);
                updateStatus();
            } else {
                countdown(interval);
                updateStatus();
                isTimerFinished = true;
                timer.cancel();
                notify();
            }
        }
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

    @Override
    public Boolean isTimerFinished() {
        return isTimerFinished;
    }
}
