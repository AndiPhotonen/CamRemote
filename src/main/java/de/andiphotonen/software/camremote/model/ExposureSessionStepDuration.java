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

    public ExposureSessionStepDuration(Integer hours, Integer minutes, Integer seconds){
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    /**
     * Returns this duration in seconds
     * @return duration in seconds
     */
    public Integer getDurationInSeconds(){
        Integer secondsOfHours = getHours() * 60 *60;
        Integer secondsOfMinute = getMinutes() * 60;
        return secondsOfHours + secondsOfMinute + getSeconds();
    }

    /**
     * Checks if the duration is at least one second.
     * @return true if the duration is > 0, false if <= 0
     */
    public Boolean isDurationgreaterZero(){
        Boolean isDurationGreaterZero = false;
        if(getHours() > 0 || getMinutes() > 0 || getSeconds() > 0){
            isDurationGreaterZero = true;
        }
        return isDurationGreaterZero;
    }

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
