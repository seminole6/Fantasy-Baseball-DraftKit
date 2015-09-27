package draftkit.gui;

import draftkit.DraftKit_PropertyType;
import draftkit.data.Draft;
import draftkit.data.Hitter;
import draftkit.data.Pitcher;
import draftkit.data.Player;
import static draftkit.gui.DraftKit_GUI.CLASS_HEADING_LABEL;
import static draftkit.gui.DraftKit_GUI.CLASS_PROMPT_LABEL;
import static draftkit.gui.DraftKit_GUI.PRIMARY_STYLE_SHEET;
import static java.awt.MediaTracker.COMPLETE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Devon Maguire
 */
public class PlayerDialog extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    Hitter player;
    
    ArrayList<String> proTeams = new ArrayList();
    ArrayList<String> checkBoxes;
    
    // GUI CONTROLS FOR OUR DIALOG
    GridPane gridPane;
    Scene dialogScene;
    Label headingLabel;
    Label firstNameLabel;
    TextField firstNameTextField;
    Label lastNameLabel;
    TextField lastNameTextField;
    Label proTeamLabel;
    ComboBox proTeamComboBox;
    CheckBox c;
    CheckBox oneB;
    CheckBox threeB;
    CheckBox twoB;
    CheckBox ss;
    CheckBox of;
    CheckBox p;
    Button completeButton;
    Button cancelButton;
    
    // THIS IS FOR KEEPING TRACK OF WHICH BUTTON THE USER PRESSED
    String selection;
    
    // CONSTANTS FOR THE UI
    public static final String COMPLETE = "Complete";
    public static final String CANCEL = "Cancel";
    
    // NEEDS EDITING
    public static final String FIRST_NAME_PROMPT = "First Name: ";
    public static final String LAST_NAME_PROMPT = "Last Name: ";
    public static final String PRO_TEAM_PROMPT = "Pro Team: ";
    public static final String PLAYER_HEADING = "Player Details";
    public static final String ADD_PLAYER_TITLE = "Add New Player";
    public static final String EDIT_PLAYER_TITLE = "Edit Player";
    
    public PlayerDialog(Stage primaryStage, Draft draft,  MessageDialog messageDialog) {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        player = new Hitter();
        
        // FIRST OUR CONTAINER
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // PUT THE HEADING IN THE GRID, NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(PLAYER_HEADING);
        headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
    
        // NOW THE FIRST AND LAST NAMES
        firstNameLabel = new Label(FIRST_NAME_PROMPT);
        firstNameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        firstNameTextField = new TextField();
        firstNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            player.setFirstName(newValue);
        });
        
        lastNameLabel = new Label(LAST_NAME_PROMPT);
        lastNameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        lastNameTextField = new TextField();
        lastNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            player.setLastName(newValue);
        });
        
        // AND MLB TEAM COMBOBOX
        proTeams.add("ATL");
        proTeams.add("AZ");
        proTeams.add("CHC");
        proTeams.add("CIN");
        proTeams.add("COL");
        proTeams.add("LAD");
        proTeams.add("MIA");
        proTeams.add("MIL");
        proTeams.add("NYM");
        proTeams.add("PHI");
        proTeams.add("PIT");
        proTeams.add("SD");
        proTeams.add("SF");
        proTeams.add("STL");
        proTeams.add("WAS");
        
        proTeamComboBox = new ComboBox();
        for (String s : proTeams) {
            proTeamComboBox.getItems().add(s);
        }
        
        proTeamComboBox.setValue("ATL");
        
        proTeamLabel = new Label(PRO_TEAM_PROMPT);
        proTeamLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        proTeamComboBox.setOnAction(e -> {
            player.setMLBTeam(proTeamComboBox.getSelectionModel().getSelectedItem().toString());
        });
        
        // AND THE CHECKBOXES
        checkBoxes = new ArrayList<>();
        
        c = new CheckBox("C");
        c.setOnAction(e -> {
            updatePlayerUsingCheckBox(c, "C");
        });
        oneB = new CheckBox("1B");
        oneB.setOnAction(e -> {
            updatePlayerUsingCheckBox(oneB, "1B");
        });
        threeB = new CheckBox("3B");
        threeB.setOnAction(e -> {
            updatePlayerUsingCheckBox(threeB, "3B");
        });
        twoB = new CheckBox("2B");
        twoB.setOnAction(e -> {
            updatePlayerUsingCheckBox(twoB, "2B");
        });
        ss = new CheckBox("SS");
        ss.setOnAction(e -> {
            updatePlayerUsingCheckBox(ss, "SS");
        });
        of = new CheckBox("OF");
        of.setOnAction(e -> {
            updatePlayerUsingCheckBox(of, "OF");
        });
        p = new CheckBox("P");
        p.setOnAction(e -> {
            updatePlayerUsingCheckBox(p, "P");
        });
        
        // NOW ADDING DATA TO THE PLAYER OBJECT
        if (p.isSelected()) {
            draft.getPitchers().add(player);
        }
        else {
            draft.getHitters().add(player);
        }
        
        // AND FINALLY, THE BUTTONS
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);
        
        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            PlayerDialog.this.selection = sourceButton.getText();
            PlayerDialog.this.hide();
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);

        HBox checks = new HBox();
        checks.getChildren().addAll(c,oneB,threeB,twoB,ss,of,p);
        
        // NOW LET'S ARRANGE THEM ALL AT ONCE
        gridPane.add(headingLabel, 0, 0, 2, 1);
        gridPane.add(firstNameLabel, 0, 1, 1, 1);
        gridPane.add(firstNameTextField, 1, 1, 1, 1);
        gridPane.add(lastNameLabel, 0, 2, 1, 1);
        gridPane.add(lastNameTextField, 1, 2, 1, 1);
        gridPane.add(proTeamLabel, 0, 3, 1, 1);
        gridPane.add(proTeamComboBox, 1, 3, 1, 1);
        gridPane.add(checks, 1, 4, 1, 1);
        gridPane.add(completeButton, 0, 5, 1, 1);
        gridPane.add(cancelButton, 1, 5, 1, 1);

        // AND PUT THE GRID PANE IN THE WINDOW
        dialogScene = new Scene(gridPane);
        dialogScene.getStylesheets().add(PRIMARY_STYLE_SHEET);
        this.setScene(dialogScene);
        
        draft.getPlayerPool().add(player);
        draft.getSortedPlayerPool().add(player);
        
        // NEED TO UPDATE THE DRAFT AND FORCE IT TO RELOAD
    }
    
    public ArrayList<String> getCheckBoxes() {
        return checkBoxes;
    }
    
    private void loadTeamComboBox(ArrayList<String> subjects) {
        for (String s : subjects) {
            proTeamComboBox.getItems().add(s);
        }
    }
    
    private void updatePlayerUsingCheckBox(CheckBox cB, String pos) {
        if (cB.isSelected()) {
            if (!checkBoxes.contains(pos))
                checkBoxes.add(pos);
        } else {
            checkBoxes.remove(pos);
        }
    }
    
    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either YES, NO, or CANCEL, depending on which
     * button the user selected when this dialog was presented.
     */
    public String getSelection() {
        return selection;
    }

    public Player getPlayer() {
        return player;
    }
    
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param message Message to appear inside the dialog.
     */
    public Player showAddPlayerDialog() {
        // SET THE DIALOG TITLE
        setTitle(ADD_PLAYER_TITLE);
        
        // RESET THE SCHEDULE ITEM OBJECT WITH DEFAULT VALUES
        player = new Hitter();
        
        // LOAD THE UI STUFF
        firstNameTextField.setText("");
        lastNameTextField.setText("");
        proTeamComboBox.setValue("ATL");
        c.setSelected(false);
        oneB.setSelected(false);
        threeB.setSelected(false);
        twoB.setSelected(false);
        ss.setSelected(false);
        of.setSelected(false);
        p.setSelected(false);
        checkBoxes = new ArrayList<>();
        
        // AND OPEN IT UP
        this.showAndWait();
        
        return player;
    }
    
    public void loadGUIData() {
        // LOAD THE UI STUFF
        firstNameTextField.setText(player.getFirstName());
        lastNameTextField.setText(player.getLastName());
        proTeamComboBox.setValue(player.getMLBTeam());
    }
    
    public boolean wasCompleteSelected() {
        return selection.equals(COMPLETE);
    }
    
}
