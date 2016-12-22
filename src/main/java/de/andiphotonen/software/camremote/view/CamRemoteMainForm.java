package de.andiphotonen.software.camremote.view;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.controller.ExposureSessionEditorController;
import de.andiphotonen.software.camremote.model.ExposureSession;
import de.andiphotonen.software.camremote.model.ExposureSessionStepDuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Andreas Kieburg on 26.10.2016.
 */
public class CamRemoteMainForm extends JFrame{

    private JPanel contentPanel;
    private JPanel exposureListPanel;
    private JPanel exposureManagerPanel;
    private JPanel exposureControllerPanel;
    private JPanel exposureStatusPanel;
    private JButton addExposureBtn;
    private JButton removeExposureBtn;
    private JButton moveUpExposureBtn;
    private JButton moveDownExposureBtn;
    private JScrollPane exposureListPane;
    private JButton startExposureBtn;
    private JToggleButton pauseToggleExposureBtn;
    private JButton stopExposureBtn;
    private JButton editExposureBtn;
    private JTextField exposureStatusTimerTxt;
    private JLabel exposureStepLbl;
    private JTextField exposureNrTxt;
    private JLabel exposureNumberLbl;
    private JList<String> exposureList;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem menuItem;

    private static final Logger log = LogManager.getLogger(CamRemoteMainForm.class);
    private static final String WINDOW_TITLE = "CamRemote";

    private ExposureSessionEditorController editorController = ExposureSessionEditorController.getInstance();
    private DefaultListModel<String> exposureSessionListModel;

    public CamRemoteMainForm(){
        super("CamRemote");

        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setContentPane(this.contentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        exposureSessionListModel = new DefaultListModel<>();

        setTooltipTexts();
        //disable exposure control buttons when no exposure session created
        setStartExposureBtnEnabled(false);
        setStopExposureBtnEnabled(false);
        setPauseToggleExposureBtnEnabled(false);
        setEditExposureBtnEnabled(false);

        /*---------Event Listener------------*/
        addExposureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onAdd();
            }
        });
        removeExposureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRemove();
            }
        });
        moveUpExposureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMoveUp();
            }
        });
        moveDownExposureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onMoveDown();
            }
        });
        startExposureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStart();
            }
        });
        pauseToggleExposureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPauseResume();
            }
        });
        stopExposureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onStop();
            }
        });
        editExposureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onEdit();
            }
        });
        exposureList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });
    }

    /*-----------Listener Methods------------*/

    /**
     * Action if Add button was pressed.
     */
    private void onAdd() {
        log.trace("addExposureBtn clicked");
        editorController.setIsCreatingNewExposureSessionFinished(false);
        editorController.openExposureEditorForm();
        //If successfully added a new exposureSession
        if(editorController.getIsCreatingNewExposureSessionFinished()) {
            String exposureSessionName = editorController.getLastAddedExposureSession().getSessionName();
            //add this name to the exposureList
            exposureSessionListModel.addElement(exposureSessionName);
            log.debug(String.format("Added exposure session: %s", exposureSessionName));
            updateExposureSessionList();
            setStartExposureBtnEnabled(true);
            editExposureBtn.setEnabled(true);
        }
    }

    /**
     * Action if Remove button was pressed.
     */
    private void onRemove() {
        String confirmMessage = "Delete exposure session?";
        Integer selectedSession = exposureList.getSelectedIndex();
        String title = WINDOW_TITLE + "Delete exposure session";
        //show error dialog if no sessions selected
        if (selectedSession >= 0){
            //if confirmed
            if(JOptionPane.showConfirmDialog(null, confirmMessage, title, JOptionPane.YES_NO_OPTION) == 0){
                //remove selected session from Map
                editorController.removeExposureSession(exposureList.getSelectedValue());
                //remove selected element from exposureList
                exposureSessionListModel.removeElement(exposureList.getSelectedValue());
                updateExposureSessionList();
                if(exposureSessionListModel.isEmpty()){
                    setStartExposureBtnEnabled(false);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "No exposure session selected", title, JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Action if Move up button was pressed.
     */
    private void onMoveUp() {
        Integer selectedIndex = exposureList.getSelectedIndex();
        if(selectedIndex >= 1){
            swapExposureSessions(selectedIndex, selectedIndex -1);
            selectedIndex = selectedIndex -1;
            exposureList.setSelectedIndex(selectedIndex);
            updateExposureSessionList();
        }
    }

    /**
     * Action if Move down button was pressed.
     */
    private void onMoveDown() {
        Integer selectedIndex = exposureList.getSelectedIndex();
        if(selectedIndex < exposureSessionListModel.size()){
            swapExposureSessions(selectedIndex, selectedIndex +1);
            selectedIndex = selectedIndex +1;
            exposureList.setSelectedIndex(selectedIndex);
            updateExposureSessionList();
        }
    }

    /**
     * Action if Start button was pressed.
     */
    private void onStart() {
        ExposureSession selectedSession = editorController.getExposureSessionMap().get(exposureList.getSelectedValue());
        if(selectedSession != null) {
            CamRemoteMainFormController.startTimer(selectedSession);
            setStartExposureBtnEnabled(false);
            setStopExposureBtnEnabled(true);
            setPauseToggleExposureBtnEnabled(true);
        }
    }

    /**
     * Action if pause or resumed toggle button was pressed.
     */
    private void onPauseResume() {
        if(pauseToggleExposureBtn.isSelected()){
            pauseToggleExposureBtn.setText("Resume");
            CamRemoteMainFormController.pauseTimer();
            setStartExposureBtnEnabled(false);
        }else {
            pauseToggleExposureBtn.setText("Pause");
            CamRemoteMainFormController.resumeTimer();
            setStartExposureBtnEnabled(true);
        }
    }

    /**
     * Action if Stop button was pressed.
     */
    private void onStop() {
        CamRemoteMainFormController.stopTimer();

        setStartExposureBtnEnabled(true);
        setStopExposureBtnEnabled(false);
        setPauseToggleExposureBtnEnabled(false);

        setExposureNumberText("");
        exposureStatusTimerTxt.setText("");
    }

    /**
     * Action if Edit button was pressed.
     */
    private void onEdit() {
        editorController.editExposureSession(exposureList.getSelectedValue());
    }

    /*-------------helper methods------------------*/

    private void createUIComponents() {
        //the menu
        createMenu();
    }

    /**
     * Creates the menu of CamRemoteMainForm.
     */
    private void createMenu(){
        //create the menuBar
        menuBar = new JMenuBar();
        //create the menu
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menu.getAccessibleContext().setAccessibleDescription("The File Menu");
        menuBar.add(menu);
        //create the menuItem
        menuItem = new JMenuItem("Test Menu Item", KeyEvent.VK_T);
        menu.add(menuItem);
    }

    /**
     * Setting up the tooltip texts.
     */
    private void setTooltipTexts(){
        addExposureBtn.setToolTipText("Add new exposure session");
        removeExposureBtn.setToolTipText("Delete selected exposure session");
        moveUpExposureBtn.setToolTipText("Move up selected exposure session");
        moveDownExposureBtn.setToolTipText("Move down selected exposure session");
        startExposureBtn.setToolTipText("Start exposure session");
        stopExposureBtn.setToolTipText("Stop exposure session");
        pauseToggleExposureBtn.setToolTipText("Pause / Resume exposure session");
        editExposureBtn.setToolTipText("Edit selected exposure session");
    }

    /**
     * Updates the Countdown Panel.
     * @param duration The current step duration.
     */
    public void updateStatusPanel(final ExposureSessionStepDuration duration){
        String countdownString = String.format("%02d:%02d:%02d", duration.getHours(), duration.getMinutes(), duration.getSeconds());
        exposureStatusTimerTxt.setText(countdownString);
    }

    /**
     * Updates the list with the Sessions, which is displayed in the form.
     */
    private void updateExposureSessionList(){
        exposureList.setModel(exposureSessionListModel);
    }

    /**
     * Swaps two sessions inside the list in the form.
     * @param pastPosition The actual position of the selected session.
     * @param newPosition The new position of the selected session.
     */
    private void swapExposureSessions(final Integer pastPosition, final Integer newPosition){
        String tempExposureSession = exposureSessionListModel.get(newPosition);
        exposureSessionListModel.setElementAt(exposureSessionListModel.get(pastPosition), newPosition);
        exposureSessionListModel.setElementAt(tempExposureSession, pastPosition);
    }

/*----------------Getter & Setter----------------*/
    public void setExposureNumberText(String number){
        exposureNrTxt.setText(number);
    }

    public void setExposureStepLblText(String step){
        exposureStepLbl.setText(step);
    }

    public void setStartExposureBtnEnabled(Boolean enabled){
        startExposureBtn.setEnabled(enabled);
    }

    public void setPauseToggleExposureBtnEnabled(Boolean enabled){
        pauseToggleExposureBtn.setEnabled(enabled);
    }

    public void setStopExposureBtnEnabled(Boolean enabled){
        stopExposureBtn.setEnabled(enabled);
    }

    public void setEditExposureBtnEnabled(Boolean enabled){
        editExposureBtn.setEnabled(enabled);
    }


}
