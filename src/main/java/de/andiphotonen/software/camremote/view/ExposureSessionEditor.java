package de.andiphotonen.software.camremote.view;

import de.andiphotonen.software.camremote.controller.ExposureSessionEditorController;
import de.andiphotonen.software.camremote.model.ExposureSession;
import de.andiphotonen.software.camremote.model.ExposureSessionStepDuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;

public class ExposureSessionEditor extends JDialog {
    private JPanel contentPane;
    private JButton addExposureBtn;
    private JButton cancelBtn;
    private JPanel textLabelPanel;
    private JLabel exposureNameLbl;
    private JLabel exposureDelayLbl;
    private JLabel exposureDurationLbl;
    private JLabel exposureIntervalLbl;
    private JLabel exposureAmountLbl;
    private JPanel parameterPanel;
    private JPanel nameParamPanel;
    private JTextField exposureSessionNameTxt;
    private JPanel delayParamPanel;
    private JSpinner delayHourSpinner;
    private JSpinner delayMinutesSpinner;
    private JSpinner delaySecondsSpinner;
    private JPanel durationParameterPanel;
    private JSpinner durationHourSpinner;
    private JSpinner durationMinutesSpinner;
    private JSpinner durationSecondsSpinner;
    private JPanel intervalParameterPanel;
    private JSpinner intervalHourSpinner;
    private JSpinner intervalMinutesSpinner;
    private JSpinner intervalSecondsSpinner;
    private JPanel controlPnl;
    private JLabel hourLbl;
    private JLabel minutesLbl;
    private JLabel secondsLbl;
    private JPanel legendPnl;
    private JPanel amountPanel;
    private JSpinner amountSpinner;

    private ExposureSessionEditorController editorController = ExposureSessionEditorController.getInstance();
    private final static Logger log = LogManager.getLogger(ExposureSessionEditor.class);
    private static final String WINDOW_TITLE = "CamRemote";

    /**
     * Main method for this Dialog. Opens a new dialog.
     * @param exposureSession
     */
    public static void main(final ExposureSession exposureSession) {
        ExposureSessionEditor dialog = new ExposureSessionEditor(exposureSession);
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * The Constructor
     */
    private ExposureSessionEditor(final ExposureSession exposureSession) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(addExposureBtn);

        if (exposureSession != null){
            fillForm(exposureSession);
        }

        addExposureBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Adds a Exposure Session to to the Exposure Session List
     */
    private void onAdd() {
        String warningDialogTitle = WINDOW_TITLE + " Cannot add Exposure Session";
        String sessionName = exposureSessionNameTxt.getText();
        Integer amount = (Integer) amountSpinner.getValue();

        ExposureSessionStepDuration delayDuration = getDurationForExposureStep(delayHourSpinner, delayMinutesSpinner, delaySecondsSpinner);
        ExposureSessionStepDuration exposureDuration = getDurationForExposureStep(durationHourSpinner, durationMinutesSpinner, durationSecondsSpinner);
        ExposureSessionStepDuration intervalDuration = getDurationForExposureStep(intervalHourSpinner, intervalMinutesSpinner, intervalSecondsSpinner);

        try{
            editorController.addExposureSession(sessionName, delayDuration, exposureDuration, intervalDuration, amount);
            editorController.setIsCreatingNewExposureSessionFinished(true);
            dispose();
        }catch (IllegalArgumentException iae){
            log.warn(iae.getMessage());
            JOptionPane.showMessageDialog(null, iae.getMessage(), warningDialogTitle, JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Cancels progress and closes dialog.
     */
    private void onCancel() {
        dispose();
    }

    /**
     * Instantiates a {@link ExposureSessionStepDuration}.
     * @param hoursSpinner Spinner for hours.
     * @param minutesSpinner Spinner for minutes.
     * @param secondsSpinner Spinner for seconds.
     * @return ExposureSessionStepDuration
     */
    private ExposureSessionStepDuration getDurationForExposureStep(final JSpinner hoursSpinner, final JSpinner minutesSpinner, final JSpinner secondsSpinner){
        Integer hours = (Integer) hoursSpinner.getValue();
        Integer minutes = (Integer) minutesSpinner.getValue();
        Integer seconds = (Integer) secondsSpinner.getValue();
        return new ExposureSessionStepDuration(hours, minutes, seconds);
    }

    /**
     * Fills the duration JSpinner with the value of the values of the {@link ExposureSessionStepDuration}.
     * @param hoursSpinner Spinner for hours.
     * @param minutesSpinner Spinner for minutes.
     * @param secondsSpinner Spinner for seconds.
     * @param stepDuration The {@link ExposureSessionStepDuration} object with the values to set.
     */
    private void setDurationForExposureStep(final JSpinner hoursSpinner, final JSpinner minutesSpinner, final JSpinner secondsSpinner, final ExposureSessionStepDuration stepDuration){
        hoursSpinner.setValue(stepDuration.getHours());
        minutesSpinner.setValue(stepDuration.getMinutes());
        secondsSpinner.setValue(stepDuration.getSeconds());
    }

    /**
     * Fills form with selected exposure session.
     * @param exposureSession The selected exposure session.
     */
    private void fillForm(final ExposureSession exposureSession){
        exposureSessionNameTxt.setText(exposureSession.getSessionName());
        amountSpinner.setValue(exposureSession.getAmount());

        setDurationForExposureStep(delayHourSpinner, delayMinutesSpinner, delaySecondsSpinner, exposureSession.getDelay());
        setDurationForExposureStep(durationHourSpinner, durationMinutesSpinner, durationSecondsSpinner, exposureSession.getDuration());
        setDurationForExposureStep(intervalHourSpinner, intervalMinutesSpinner, intervalSecondsSpinner, exposureSession.getInterval());
    }
}
