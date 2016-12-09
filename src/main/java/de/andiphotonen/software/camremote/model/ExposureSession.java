package de.andiphotonen.software.camremote.model;

/**
 * Created by Andreas Kieburg on 26.10.2016.
 */

/**
 * This Class represents a exposure session.
 */
public class ExposureSession {

    /** The name of this session.*/
    private String sessionName;
    /** The time to wait until the session starts. */
    private ExposureSessionStepDuration delay;
    /** The duration of each exposure. */
    private ExposureSessionStepDuration duration;
    /** The time between each exposure. */
    private ExposureSessionStepDuration interval;
    /** The number of exposures. */
    private Integer amount;

    /**
     * The constructor.
     * @param sessionName The name of this session.
     * @param delay The time to wait until the session starts.
     * @param duration The duration of each exposure.
     * @param interval The time between each exposure.
     * @param amount The number of exposures.
     */
    public ExposureSession(String sessionName, ExposureSessionStepDuration delay, ExposureSessionStepDuration duration,
                           ExposureSessionStepDuration interval, Integer amount){

        this.sessionName = sessionName;
        this.delay = delay;
        this.duration = duration;
        this.interval = interval;
        this.amount = amount;
    }

    /**
     * Copy Constructor.
     * @param newSession {@link ExposureSession} to be copied.
     */
    public ExposureSession(final ExposureSession newSession){
        this.sessionName = new String(newSession.sessionName);
        this.delay = new ExposureSessionStepDuration(newSession.delay);
        this.duration = new ExposureSessionStepDuration(newSession.duration);
        this.interval = new ExposureSessionStepDuration(newSession.interval);
        this.amount = new Integer(newSession.amount);
    }

    /**
     * Default constructor
     */
    public ExposureSession(){}

    /* -------Getter & Setter------- */

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public ExposureSessionStepDuration getDelay() {
        return delay;
    }

    public void setDelay(ExposureSessionStepDuration delay) {
        this.delay = delay;
    }

    public void setDelayValue(ExposureSessionStepDuration delay){
        this.delay = new ExposureSessionStepDuration(delay);
    }

    public ExposureSessionStepDuration getDuration() {
        return duration;
    }

    public void setDuration(ExposureSessionStepDuration duration) {
        this.duration = duration;
    }

    public void setDurationValue(ExposureSessionStepDuration duration){
        this.duration = new ExposureSessionStepDuration(duration);
    }

    public ExposureSessionStepDuration getInterval() {
        return interval;
    }

    public void setInterval(ExposureSessionStepDuration interval) {
        this.interval = interval;
    }

    public void setIntervalValue(ExposureSessionStepDuration interval){
        this.interval = new ExposureSessionStepDuration(interval);
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
