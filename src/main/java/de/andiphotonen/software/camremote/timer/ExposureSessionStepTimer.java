package de.andiphotonen.software.camremote.timer;

import java.util.TimerTask;

/**
 * Created by Andreas Kieburg on 08.12.2016.
 */
public abstract class ExposureSessionStepTimer extends TimerTask {

    protected abstract void updateStatus();

    protected abstract void countdown(Integer counter);

    public abstract Boolean isTimerFinished();
}
