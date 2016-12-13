package de.andiphotonen.software.camremote.model;

/**
 * Created by Andreas Kieburg on 27.10.2016.
 */

/**
 * This class represents the duration of a step of the exposure session
 */
public class ExposureSessionStepDuration {
    private Integer hours;
    private Integer minutes;
    private Integer seconds;

    public ExposureSessionStepDuration(Integer hours, Integer minutes, Integer seconds) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * Copy Constructor
     *
     * @param stepDuration The {@link ExposureSessionStepDuration} to copy
     */
    public ExposureSessionStepDuration(ExposureSessionStepDuration stepDuration) {
        this.hours = new Integer(stepDuration.getHours());
        this.minutes = new Integer(stepDuration.getMinutes());
        this.seconds = new Integer(stepDuration.getSeconds());
    }

    /**
     * Returns this duration in seconds
     *
     * @return duration in seconds
     */
    public Integer getDurationInSeconds() {
        Integer secondsOfHours = getHours() * 60 * 60;
        Integer secondsOfMinute = getMinutes() * 60;
        return secondsOfHours + secondsOfMinute + getSeconds();
    }

    /**
     * Converts duration in seconds to hours, minutes and seconds.
     *
     * @param durationInSeconds The duration in seconds
     */
    public void setDurationInSeconds(Integer durationInSeconds) {
        Integer seconds = durationInSeconds % 60;
        Integer minutes = (durationInSeconds * 60) % 60;
        Integer hours = (durationInSeconds * 60 * 60) % 24;

        setHours(hours);
        setMinutes(minutes);
        setSeconds(seconds);
    }

    /**
     * Checks if the duration is at least one second.
     *
     * @return true if the duration is > 0, false if <= 0
     */
    public Boolean isDurationGreaterZero() {
        Boolean isDurationGreaterZero = false;
        if (getHours() > 0 || getMinutes() > 0 || getSeconds() > 0) {
            isDurationGreaterZero = true;
        }
        return isDurationGreaterZero;
    }

    /*--------Getter & Setter-----------------*/

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

}
