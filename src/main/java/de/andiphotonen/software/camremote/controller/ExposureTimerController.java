package de.andiphotonen.software.camremote.controller;

import de.andiphotonen.software.camremote.model.ExposureSession;
import de.andiphotonen.software.camremote.timer.DelayTimer;
import de.andiphotonen.software.camremote.timer.ExposureSessionStepTimer;
import de.andiphotonen.software.camremote.timer.ExposureTimer;
import de.andiphotonen.software.camremote.timer.IntervalTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Timer;

/**
 * Created by Andreas Kieburg on 03.12.2016.
 */
public class ExposureTimerController {

    private Logger log = LogManager.getLogger(ExposureTimerController.class);

    private static Timer timer;

    private static final Long TIMER_DELAY = 0L;
    private static final Long TIMER_PERIOD = 1000L;

    private static final String DELAY = "Delay";
    private static final String EXPOSURE = "Duration";
    private static final String INTERVAL = "Interval";

    ExposureSessionStepTimer delayDuration;
    ExposureSessionStepTimer exposureDuration;
    ExposureSessionStepTimer intervalDuration;

    private ExposureSession tempSession;
    private ExposureSession currentSession;
    private ExposureSession selectedSession;

    private Integer amountOfExposures;

    public ExposureTimerController(final ExposureSession selectedSession) {
        this.currentSession = new ExposureSession(selectedSession);
        this.selectedSession = new ExposureSession(selectedSession);

        this.amountOfExposures = currentSession.getAmount();
    }


    public void startExposureSession() {
        updateExposureAmount(currentSession.getAmount());
        startDelayTimer();
        while (currentSession.getAmount() > 0) {
            startExposureTimer();
            startIntervalTimer();
            currentSession.setAmount(currentSession.getAmount() - 1);
            updateExposureAmount(currentSession.getAmount());
        }
//        log.debug("initialize timer");
//        timer = new Timer(1000, new ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Integer delay = currentSession.getDelay().getDurationInSeconds();
//                Integer duration = currentSession.getDuration().getDurationInSeconds();
//                Integer interval = currentSession.getInterval().getDurationInSeconds();
//                Integer amount = currentSession.getAmount();
//
//                CamRemoteMainFormController.getCamRemoteMainForm().setExposureStatusPanelText(currentSession.getAmount().toString());
//
//                if(delay > 1){
//                    currentSession.getDelay().setDurationInSeconds(--delay);
//                    CamRemoteMainFormController.getCamRemoteMainForm().updateStatusPanel(currentSession.getDelay());
//                    CamRemoteMainFormController.getCamRemoteMainForm().setexposureStepLblText(DELAY);
//                }else if(amount > 0){
//                    if(duration > 1){
//                        currentSession.getDuration().setDurationInSeconds(--duration);
//                        CamRemoteMainFormController.getCamRemoteMainForm().updateStatusPanel(currentSession.getDuration());
//                        CamRemoteMainFormController.getCamRemoteMainForm().setexposureStepLblText(EXPOSURE);
//                    }else if(interval > 1){
//                        currentSession.getInterval().setDurationInSeconds(--interval);
//                        CamRemoteMainFormController.getCamRemoteMainForm().updateStatusPanel(currentSession.getInterval());
//                        CamRemoteMainFormController.getCamRemoteMainForm().setexposureStepLblText(INTERVAL);
//                    }else{
//                        //reduce amount
//                        currentSession.setAmount(--amount);
//                        //reset duration and interval
//                        currentSession.setDurationValue(selectedSession.getDuration());
//                        currentSession.setIntervalValue(selectedSession.getInterval());
//                    }
//                }else{
//                    timer.stop();
//                    log.info("exposure finished");
//                }
//            }
//        });
//        timer.start();
//        log.info("exposure started");
    }

    private Boolean startDelayTimer() {
        Boolean finished = false;
        log.info("start delay timer");
        timer = new Timer();

        delayDuration = new DelayTimer(new ExposureSession(currentSession), timer);
        CamRemoteMainFormController.getCamRemoteMainForm().setexposureStepLblText(DELAY);
        timer.schedule(delayDuration, TIMER_DELAY, TIMER_PERIOD);
        synchronized (delayDuration){
            try {
                delayDuration.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return finished;
    }

    private Boolean startExposureTimer() {
        Boolean finished = false;
        timer = new Timer();
        exposureDuration = new ExposureTimer(new ExposureSession(currentSession), timer);
        log.info("start exposure timer");
        CamRemoteMainFormController.getCamRemoteMainForm().setexposureStepLblText(EXPOSURE);
        timer.schedule(exposureDuration, TIMER_DELAY, TIMER_PERIOD);
        synchronized (exposureDuration){
            try {
                exposureDuration.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return finished;
    }

    private Boolean startIntervalTimer() {
        Boolean finished = false;
        timer = new Timer();
        intervalDuration = new IntervalTimer(new ExposureSession(currentSession), timer);
        log.info("start interval timer");
        CamRemoteMainFormController.getCamRemoteMainForm().setexposureStepLblText(INTERVAL);
        timer.schedule(intervalDuration, TIMER_DELAY, TIMER_PERIOD);
        synchronized (intervalDuration){
            try {
                intervalDuration.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return finished;
    }

    private Boolean isTimerFinished(ExposureSessionStepTimer timer) {
        return timer.isTimerFinished();
    }

    private void updateExposureAmount(Integer amountOfExposures){
        CamRemoteMainFormController.getCamRemoteMainForm().setExposureStatusPanelText(amountOfExposures.toString());
    }
}
