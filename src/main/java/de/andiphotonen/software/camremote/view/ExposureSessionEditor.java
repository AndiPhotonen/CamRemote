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
     * CamRemoteMainFormController method for this Dialog. Opens a new dialog.
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
        setTooltipTexts();

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

    /**
     * Creates custom UI components.
     */
    private void createUIComponents() {
        //default values
        Integer initValue = 0;
        Integer minValue = 0;
        Integer step = 1;

        amountSpinner = setupSpinnerNumber(initValue, minValue, 9999, step);

        delayHourSpinner = setupSpinnerNumber(initValue, minValue, 99, step);
        delayMinutesSpinner = setupSpinnerNumber(initValue, minValue, 59, step);
        delaySecondsSpinner = setupSpinnerNumber(initValue, minValue, 59, step);

        durationHourSpinner = setupSpinnerNumber(initValue, minValue, 99, step);
        durationMinutesSpinner = setupSpinnerNumber(initValue, minValue, 59, step);
        durationSecondsSpinner = setupSpinnerNumber(initValue, minValue, 59, step);

        intervalHourSpinner = setupSpinnerNumber(initValue, minValue, 99, step);
        intervalMinutesSpinner = setupSpinnerNumber(initValue, minValue, 59, step);
        intervalSecondsSpinner = setupSpinnerNumber(initValue, minValue, 59, step);

    }

    /**
     * Setting up a Jspinner for numbers.
     * @param initValue the current (non <code>null</code>) value of the model
     * @param min the first number in the sequence or <code>null</code>
     * @param max the last number in the sequence or <code>null</code>
     * @param step the difference between elements of the sequence
     * @return A new instance of {@link JSpinner}
     */
    private JSpinner setupSpinnerNumber(Integer initValue, Integer min, Integer max, Integer step){
        SpinnerModel spinnerModel = new SpinnerNumberModel(initValue, min, max, step);
        return new JSpinner(spinnerModel);
    }

    /**
     * Setting up the tooltip texts.
     */
    private void setTooltipTexts(){
        amountSpinner.setToolTipText("Amount of exposures");
        delayHourSpinner.setToolTipText("Delay duration hours");
        delayMinutesSpinner.setToolTipText("Delay duration minutes");
        delaySecondsSpinner.setToolTipText("Delay duration seconds");
        durationHourSpinner.setToolTipText("Exposure duration hours");
        durationMinutesSpinner.setToolTipText("Exposure duration minutes");
        durationSecondsSpinner.setToolTipText("Exposure duration seconds");
        intervalHourSpinner.setToolTipText("Interval duration hours");
        intervalMinutesSpinner.setToolTipText("Interval duration minutes");
        intervalSecondsSpinner.setToolTipText("Interval duration seconds");
        exposureSessionNameTxt.setToolTipText("Name of exposure session");
    }
}
