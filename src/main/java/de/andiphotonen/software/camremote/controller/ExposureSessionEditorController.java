package de.andiphotonen.software.camremote.controller;

/**
 * Created by Andreas Kieburg on 26.10.2016.
 */

import de.andiphotonen.software.camremote.model.ExposureSession;
import de.andiphotonen.software.camremote.model.ExposureSessionStepDuration;
import de.andiphotonen.software.camremote.view.ExposureSessionEditor;

import java.util.HashMap;

/**
 * The Controller for editing or creating a exposureSession.
 */
public class ExposureSessionEditorController {

    /** The singleton instance of this class */
    private static ExposureSessionEditorController instanceOfThisObject;

    /** The map which contains the added exposureSessions */
    private HashMap<String, ExposureSession> exposureSessionMap;

    /** The last added exposure session to exposureSessionMap */
    private ExposureSession lastAddedExposureSession;

    /** This variable should be set true if a new exposure session is successfully created
     * and false if the new exposure session is added to the ExposureSessionList in Frontend. <br>
     * The value "false" signals that a new exposure session can be created now.
     */
    private Boolean isCreatingNewExposureSessionFinished;

    /**
     * This is a Singleton Constructor. <br>
     * To get a instance of this Object, use the Getter <code>getInstance()</code>.
     */
    private ExposureSessionEditorController(){}

    /**
     * Opens the {@link ExposureSessionEditor} dialog. <br>
     * If exposureSessionName is not null, the dialog will be initialized with the associated {@link ExposureSession}
     * @param exposureSessionName The name of the exposureSession.
     */
    public void openExposureEditorForm(final String exposureSessionName){
        ExposureSession selectedExposureSession = getExposureSessionMap().get(exposureSessionName);
        ExposureSessionEditor.main(selectedExposureSession);
    }

    /**
     * Opens the ExposureSessionEditor dialog
     */
    public void openExposureEditorForm(){
        openExposureEditorForm(null);
    }

    /**
     * Adds a new exposure session to the map. <br>
     * Existing exposure session will be replaced, if exposure session name already exists in the map.
     * @param name The name of the exposure session.
     * @param delay The time to wait until the session starts.
     * @param duration The duration of each exposure.
     * @param interval The time between each exposure.
     * @param amount The number of exposures.
     * @throws IllegalStateException If a field has an invalid value.
     */
    public void addExposureSession(final String name, final ExposureSessionStepDuration delay, final ExposureSessionStepDuration duration,
                                   final ExposureSessionStepDuration interval, final Integer amount) throws IllegalArgumentException{

        ExposureSession newExposureSession = new ExposureSession(name, delay, duration, interval, amount);
        validateExposureSession(newExposureSession);
        getExposureSessionMap().put(newExposureSession.getSessionName(), newExposureSession);
        setLastAddedExposureSession(newExposureSession);
    }

    /**
     * Removes a exposureSession from the exposureSessionMap.
     * @param exposureSessionName The exposure name as key.
     */
    public void removeExposureSession(final String exposureSessionName){
        getExposureSessionMap().remove(exposureSessionName);
    }

    /**
     * Opens editor dialog
     * @param sessionName
     */
    public void editExposureSession(final String sessionName){
        openExposureEditorForm(sessionName);
    }

    /**
     * Checks if the required fields of the {@link ExposureSession} are set.
     * @param session The {@link ExposureSession} to validate
     * @throws IllegalStateException If a field has an invalid value.
     */
    private void validateExposureSession(final ExposureSession session) throws IllegalArgumentException {

        if(session.getSessionName() == null || session.getSessionName().isEmpty()){
            throw new IllegalArgumentException ("Exposure session has no name");
        }
        if(session.getAmount() <= 0){
            throw new IllegalArgumentException (String.format("The number of exposures of the session '%s' have to be at least 1", session.getSessionName()));
        }
        if(!session.getDuration().isDurationgreaterZero()){
            throw new IllegalArgumentException (String.format("The duration of the session '%s' have to be longer than at least 1 second.", session.getSessionName()));
        }
    }



    /*-----------Getter & Setter-------------*/

    public static ExposureSessionEditorController getInstance() {
        if (ExposureSessionEditorController.instanceOfThisObject == null){
            ExposureSessionEditorController.instanceOfThisObject = new ExposureSessionEditorController();
        }
        return instanceOfThisObject;
    }

    public Boolean getIsCreatingNewExposureSessionFinished() {
        if(isCreatingNewExposureSessionFinished == null){
            isCreatingNewExposureSessionFinished = false;
        }
        return isCreatingNewExposureSessionFinished;
    }

    /**
     * IsCreatingNewExposureSessionFinished should be set true if a new exposure session is successfully created
     * and false if the new exposure session is added to the ExposureSessionList in Frontend. <br>
     * The value "false" signals that a new exposure session can be created now.
     * @param isCreatingNewExposureSessionFinished The actual status.
     */
    public void setIsCreatingNewExposureSessionFinished(Boolean isCreatingNewExposureSessionFinished) {
        this.isCreatingNewExposureSessionFinished = isCreatingNewExposureSessionFinished;
    }

    public HashMap<String, ExposureSession> getExposureSessionMap() {
        if(exposureSessionMap == null){
            exposureSessionMap = new HashMap<String, ExposureSession>();
        }
        return exposureSessionMap;
    }

    public void setExposureSessionMap(HashMap<String, ExposureSession> exposureSessionMap) {
        this.exposureSessionMap = exposureSessionMap;
    }

    public ExposureSession getLastAddedExposureSession() {
        if(lastAddedExposureSession == null){
            lastAddedExposureSession = new ExposureSession();
        }
        return lastAddedExposureSession;
    }

    public void setLastAddedExposureSession(final ExposureSession lastAddedExposureSession) {
        this.lastAddedExposureSession = lastAddedExposureSession;
    }
}
