package de.andiphotonen.software.camremote.view;

import de.andiphotonen.software.camremote.controller.CamRemoteMainFormController;
import de.andiphotonen.software.camremote.controller.ExposureSessionEditorController;
import de.andiphotonen.software.camremote.model.ExposureSessionStepDuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    private JButton pauseToggleExposureBtn;
    private JButton stopExposureBtn;
    private JButton editExposureBtn;
    private JTextField exposureStatusTimerTxt;
    private JLabel exposureStepLbl;
    private JTextField exposureNrTxt;
    private JLabel exposureNumberLbl;
    private JList<String> exposureList;

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


        /*---------Event Listener------------*/
        addExposureBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onAdd();
            }
        });
        removeExposureBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onRemove();
            }
        });
        moveUpExposureBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onMoveUp();
            }
        });
        moveDownExposureBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onMoveDown();
            }
        });
        startExposureBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onStart();
            }
        });
        pauseToggleExposureBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onPauseResume();
            }
        });
        stopExposureBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                onStop();
            }
        });
        editExposureBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
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
        }
    }

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
            }
        }else{
            JOptionPane.showMessageDialog(null, "No exposure session selected", title, JOptionPane.ERROR_MESSAGE);
        }

    }

    private void onMoveUp() {
        Integer selectedIndex = exposureList.getSelectedIndex();
        if(selectedIndex >= 1){
            swapExposureSessions(selectedIndex, selectedIndex -1);
            selectedIndex = selectedIndex -1;
            exposureList.setSelectedIndex(selectedIndex);
            updateExposureSessionList();
        }
    }

    private void onMoveDown() {
        Integer selectedIndex = exposureList.getSelectedIndex();
        if(selectedIndex < exposureSessionListModel.size()){
            swapExposureSessions(selectedIndex, selectedIndex +1);
            selectedIndex = selectedIndex +1;
            exposureList.setSelectedIndex(selectedIndex);
            updateExposureSessionList();
        }
    }

    private void onStart() {
        CamRemoteMainFormController.startTimer(editorController.getExposureSessionMap().get(exposureList.getSelectedValue()));
    }

    private void onPauseResume() {
        System.out.println("pause/resume");
    }

    private void onStop() {
        System.out.println("stop");
    }

    private void onEdit() {
        editorController.editExposureSession(exposureList.getSelectedValue());
    }

    public void updateExposureNrTxt(Integer exposureNr){
        exposureNrTxt.setText(exposureNr.toString());
    }

    public void updateExposureStepLbl(String exposureStep){
        exposureStepLbl.setText(exposureStep);
    }

    /*-------------helper methods------------------*/

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
    public void setExposureStatusPanelText(String number){
        exposureNrTxt.setText(number);
    }

    public void setexposureStepLblText(String step){
        exposureStepLbl.setText(step);
    }
}
