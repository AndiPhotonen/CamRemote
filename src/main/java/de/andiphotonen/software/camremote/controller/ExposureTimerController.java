package de.andiphotonen.software.camremote.controller;

import de.andiphotonen.software.camremote.model.ExposureSession;
import de.andiphotonen.software.camremote.timer.ExposureSessionStepTimerExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

/**
 * Created by Andreas Kieburg on 03.12.2016.
 */

/**
 * The controller for the exposure session timer.
 */
public class ExposureTimerController {

    private Logger log = LogManager.getLogger(ExposureTimerController.class);

    private static Timer timer;

    /** Delay for the timer in ms. */
    private static final Long TIMER_DELAY = 0L;
    /** Period for the timer in ms. */
    private static final Long TIMER_PERIOD = 1000L;

    /** A temporary session to store the current running if timer is paused. */
    private ExposureSession tempSession;
    /** The current running exposure session. */
    private ExposureSession currentSession;
    /** The original selected exposure session. This one will be not modified. */
    private ExposureSession selectedSession;

    /** The timer instance for the exposure session steps */
    private ExposureSessionStepTimerExecutor sessionStepTimer;

    /**
     * Default constructor.
     */
    public ExposureTimerController(){}

    /**
     * Setups the timer.
     * @param selectedSession The selected session
     */
    public void setupTimer(final ExposureSession selectedSession){
        this.currentSession = new ExposureSession(selectedSession);
        this.selectedSession = new ExposureSession(selectedSession);
        timer = new Timer();
        this.sessionStepTimer = new ExposureSessionStepTimerExecutor(selectedSession, timer);
    }

    /**
     * Starts the timer.
     */
    public void startExposureSession() {
        timer.schedule(sessionStepTimer, TIMER_DELAY, TIMER_PERIOD);
    }

    /**
     * Stops the timer resets the exposure session
     */
    public void stopExposureSession(){
        log.info("Exposure session stopped");
        timer.cancel();
        tempSession = selectedSession;
    }

    /**
     * Pauses the timer and saves the current session status.
     */
    public void pauseExposureSession(){
        log.info("Exposure session paused");
        //save current running session
        tempSession = sessionStepTimer.getCurrentSession();
        //stop the timer
        timer.cancel();
    }

    /**
     * Resumes current session. Restarts exposure if it was interrupted.
     */
    public void resumeExposureSession(){
        log.info("Exposure session resumed");
        //prepare temp session for resuming
        prepareSessionForResuming();

        timer = new Timer();
        sessionStepTimer = new ExposureSessionStepTimerExecutor(tempSession, timer);
        startExposureSession();
    }

    private void prepareSessionForResuming(){
        //if exposure has already begun when paused, restart this exposure
        if (tempSession.getDuration() != selectedSession.getDuration() ){
            tempSession.setDurationValue(selectedSession.getDuration());
        }

    }
}
