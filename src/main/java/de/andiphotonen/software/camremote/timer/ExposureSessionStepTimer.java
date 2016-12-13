package de.andiphotonen.software.camremote.timer;

import de.andiphotonen.software.camremote.model.ExposureSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Andreas Kieburg on 11.12.2016.
 */

/**
 * The super class of the exposure session step timer.
 */
public class ExposureSessionStepTimer {

    protected Logger log = LogManager.getLogger(ExposureSessionStepTimer.class);
    /** The current {@link ExposureSession} */
    protected ExposureSession currentSession;
    /** Is true if the {@link ExposureSession} is finished. */
    protected Boolean isTimerFinished;

    /**
     * Creates a new instance of this class.
     *
     * @param currentSession The currently running {@link ExposureSession}
     */
    public ExposureSessionStepTimer(ExposureSession currentSession) {
        setCurrentSession(currentSession);
        isTimerFinished = false;
    }

    /**
     * Executes one step of the timer.
     *
     * @return True if the
     */
    public Boolean startTimer() {
        return isTimerFinished;
    }

    /**
     * Updates the current time status label of the main form.
     */
    protected void updateStatus() {
    }

    /**
     * Decrements the duration of the exposure timer.
     *
     * @param counter The current interval time in seconds
     */
    protected void countdown(Integer counter) {
    }

    /**
     * Resets the timer.
     *
     * @param currentSession The currently running {@link ExposureSession}
     */
    public void resetTimer(ExposureSession currentSession) {
        setCurrentSession(currentSession);
        isTimerFinished = false;
    }

    /**
     * Sets the {@link ExposureSession}.
     *
     * @param currentSession The currently running {@link ExposureSession}
     */
    protected void setCurrentSession(ExposureSession currentSession) {
        this.currentSession = currentSession;
    }
}
