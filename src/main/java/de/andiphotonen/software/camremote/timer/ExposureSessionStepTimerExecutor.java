package de.andiphotonen.software.camremote.timer;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.model.ExposureSession;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Andreas Kieburg on 11.12.2016.
 */

/**
 * The implementation of the sequence of the {@link ExposureSessionStepTimer}
 */
public class ExposureSessionStepTimerExecutor extends TimerTask {
    private Timer timer;

    ExposureSession currentSession;

    ExposureSessionStepTimer delayDuration;
    ExposureSessionStepTimer exposureDuration;
    ExposureSessionStepTimer intervalDuration;

    /**
     * Creates a new instance of ExposureSessionStepTimerExecutor
     * @param currentSession The current running {@link ExposureSession}
     * @param timer The instance of the scheduled {@link Timer}
     */
    public ExposureSessionStepTimerExecutor(ExposureSession currentSession, Timer timer) {
        this.currentSession = new ExposureSession(currentSession);
        this.delayDuration = new DelayTimer(new ExposureSession(currentSession));
        this.exposureDuration = new ExposureTimer(new ExposureSession(currentSession));
        this.intervalDuration = new IntervalTimer(new ExposureSession(currentSession));
        this.timer = timer;
    }

    @Override
    public void run() {
        CamRemoteMainFormController.getCamRemoteMainForm().setExposureNumberText(currentSession.getAmount().toString());
        if (delayDuration.startTimer()) {
            if (currentSession.getAmount() > 0) {
                if(exposureDuration.startTimer()){
                    if(intervalDuration.startTimer()){
                        //currently it takes an extra second
                        currentSession.setAmount(currentSession.getAmount() - 1);
                        CamRemoteMainFormController.getCamRemoteMainForm().setExposureNumberText(currentSession.getAmount().toString());
                        exposureDuration.resetTimer(new ExposureSession(currentSession));
                        intervalDuration.resetTimer(new ExposureSession(currentSession));
                    }
                }
            }else{
                timer.cancel();
            }
        }
    }
}
