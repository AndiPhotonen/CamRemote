package de.andiphotonen.software.camremote.timer;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.model.ExposureSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

/**
 * Created by Andreas Kieburg on 08.12.2016.
 */
public class ExposureTimer extends ExposureSessionStepTimer {
    private Logger log = LogManager.getLogger(ExposureSessionStepTimer.class);
    private ExposureSession currentSession;
    private Timer timer;
    private Boolean isTimerFinished;

    public ExposureTimer(ExposureSession currentSession, Timer timer){
        this.currentSession = currentSession;
        this.timer = timer;
        isTimerFinished = false;
    }

    @Override
    public void run() {
        synchronized (this) {
            Integer duration = currentSession.getDuration().getDurationInSeconds();

            if (duration > 1) {
                countdown(duration);
                updateStatus();
            } else {
                countdown(duration);
                updateStatus();
                isTimerFinished = true;
                timer.cancel();
                notify();
            }
        }
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

    @Override
    public Boolean isTimerFinished() {
        return isTimerFinished;
    }
}
