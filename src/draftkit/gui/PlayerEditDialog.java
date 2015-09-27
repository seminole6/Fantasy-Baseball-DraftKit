package draftkit.gui;

import draftkit.data.Contract;
import draftkit.data.Draft;
import draftkit.data.Hitter;
import draftkit.data.Player;
import draftkit.data.Team;
import static draftkit.gui.DraftKit_GUI.CLASS_HEADING_LABEL;
import static draftkit.gui.DraftKit_GUI.CLASS_SUBHEADING_LABEL;
import static draftkit.gui.DraftKit_GUI.PRIMARY_STYLE_SHEET;
import java.util.Arrays;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Devon Maguire
 */
public class PlayerEditDialog extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    Player player;
    Draft editDraft;
    
    // GUI CONTROLS FOR OUR DIALOG
    GridPane gridPane;
    Scene dialogScene;
    Label headingLabel;
    Label nameLabel;
    Label positionsLabel;
    Image playerPic;
    Image playerFlag;
    ImageView img;
    ImageView img2;
    Label fantasyTeamLabel;
    ComboBox fantasyTeamBox;
    Label positionLabel;
    ComboBox positionBox;
    Label contractLabel;
    ComboBox contractBox;
    Label salaryLabel;
    TextField salaryTextField;
    Button completeButton;
    Button cancelButton;
    
    List<String> positionList;
    
    // CONSTANTS FOR THE PICTURES
    public static final String PATH_IMAGES = "./player_images/";
    public static final String PATH_HEADSHOTS = "players/";
    public static final String PATH_FLAGS = "flags/";
    
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
    public static final String EDIT_PLAYER_TITLE = "Edit Player";
    
    public PlayerEditDialog(Stage primaryStage, Draft draft,  MessageDialog messageDialog) {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        editDraft = draft;
        player = new Hitter();
        
        // FIRST OUR CONTAINER
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // PUT THE HEADING IN THE GRID
        headingLabel = new Label(PLAYER_HEADING);
        headingLabel.getStyleClass().add(CLASS_HEADING_LABEL);
        
        // PUT THE PICTURES IN THE GRID
        playerPic = new Image("file:./player_images/players/AAA_PhotoMissing.jpg");
        playerFlag = new Image("file:" + PATH_IMAGES + PATH_FLAGS + player.getNation() + ".png");
    
        img = new ImageView();
        img.setImage(playerPic);
        img.setFitWidth(100);
        img.setPreserveRatio(true);
        
        img2 = new ImageView();
        img2.setImage(playerFlag);
        img2.setFitWidth(50);
        img2.setPreserveRatio(true);
        
        // NOW THE NAME & POSITIONS
        nameLabel = new Label(player.getFirstName() + " " + player.getLastName());
        positionsLabel = new Label(player.getPosition());
        nameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        positionsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        
        // NOW THE FANTASY TEAM COMBOBOX
        fantasyTeamLabel = new Label("Fantasty Team: ");
        
        fantasyTeamBox = new ComboBox();
        for (Team t : draft.getFantasyTeams()) {
            fantasyTeamBox.getItems().add(t.getTeamName());
        }
        
        fantasyTeamBox.setOnAction(e -> {
            if (fantasyTeamBox.getSelectionModel().getSelectedItem() != null) {
                if (fantasyTeamBox.getSelectionModel().getSelectedItem().toString().equals("Free Agent")) {
                    player.setChosenTeam("Free Agent");
                } else {
                    player.setChosenTeam(fantasyTeamBox.getSelectionModel().getSelectedItem().toString());
                    Team newTeam = draft.findTeam(player.getChosenTeam());
                    // MAYBE CLEAR POSITIONS AND SET THEM IN THIS LISTENER???
                    positionBox.getItems().clear();
                    for (String p : positionList) {
                        int c = newTeam.emptyPosition(p);
                        if (c == 0) {
                            positionBox.getItems().remove(p);
                        }
                        else {
                            positionBox.getItems().add(p);
                        }
                    }
                }
            } else {
                // DO NOTHING
            }
            // NEED TO ADD TAKING AWAY UNAVAILABLE POSITIONS TO THE COMBOBOX
        });
        
        // NOW THE POSITION COMBOBOX
        positionLabel = new Label("Position: ");
        
        positionBox = new ComboBox();
        
        positionBox.setOnAction(e -> {
            if (positionBox.getSelectionModel().getSelectedItem() != null) {
                player.setChosenPosition(positionBox.getSelectionModel().getSelectedItem().toString());
            } else {
                // DO NOTHING
            }
        });
        
        // AND THE CONTRACT COMBOBOX
        contractLabel = new Label("Contract: ");
        
        contractBox = new ComboBox();
        contractBox.getItems().add(Contract.S1);
        contractBox.getItems().add(Contract.S2);
        contractBox.getItems().add(Contract.X);
        
        contractBox.setOnAction(e -> {
            player.setContract((Contract)contractBox.getSelectionModel().getSelectedItem());
        });
        
        // AND THE SALARY TEXT FIELD
        salaryLabel = new Label("Salary ($): ");
        salaryTextField = new TextField();
        salaryTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            player.setSalary(Double.parseDouble(newValue));
        });
        
        // AND FINALLY, THE BUTTONS
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);
        
        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            PlayerEditDialog.this.selection = sourceButton.getText();
            PlayerEditDialog.this.hide();
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);
        
        // NOW LET'S ARRANGE THEM ALL AT ONCE
        gridPane.add(headingLabel, 0, 0, 2, 1);
        gridPane.add(img, 0, 1, 1, 1);
        gridPane.add(img2, 1, 0, 1, 2);
        gridPane.add(nameLabel, 1, 1, 1, 5);
        gridPane.add(positionsLabel, 1, 1, 1, 7);
        gridPane.add(fantasyTeamLabel, 0, 6, 1, 1);
        gridPane.add(fantasyTeamBox, 1, 6, 1, 1);
        gridPane.add(positionLabel, 0, 7, 1, 1);
        gridPane.add(positionBox, 1, 7, 1, 1);
        gridPane.add(contractLabel, 0, 8, 1, 1);
        gridPane.add(contractBox, 1, 8, 1, 1);
        gridPane.add(salaryLabel, 0, 9, 1, 1);
        gridPane.add(salaryTextField, 1, 9, 1, 1);
        gridPane.add(completeButton, 0, 10, 1, 1);
        gridPane.add(cancelButton, 1, 10, 1, 1);

        // AND PUT THE GRID PANE IN THE WINDOW
        dialogScene = new Scene(gridPane);
        dialogScene.getStylesheets().add(PRIMARY_STYLE_SHEET);
        this.setScene(dialogScene);
        
        //draft.getPlayerPool().add(player);
        //draft.getSortedPlayerPool().add(player);
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
    
    public void loadGUIData() {
        // LOAD THE UI STUFF
        // LOAD THE NAME AND POSITIONS
        nameLabel.setText(player.getFirstName() + " " + player.getLastName());
        positionsLabel.setText(player.getPosition());
        
        // LOAD THE FANTASY TEAMS
        fantasyTeamBox.getItems().clear();
        for (Team t : editDraft.getFantasyTeams()) {
            fantasyTeamBox.getItems().add(t.getTeamName());
        }
        fantasyTeamBox.getItems().add("Free Agent");
        
        if (player.getChosenTeam().equals("")) {
            fantasyTeamBox.setValue("Choose a Fantasy Team");
        } else {
            fantasyTeamBox.setValue(player.getChosenTeam());
        }
        
        // LOAD THE POSITION CHOSEN BY THE TEAM OWNER
        positionBox.getItems().clear();
        positionList = Arrays.asList(player.getPosition().split("_"));
        for (String p : positionList) {
            positionBox.getItems().add(p);
        }
        
        if (player.getChosenPosition().equals("")) {
            positionBox.setValue("Choose a Position");
        } else {
            positionBox.setValue(player.getChosenPosition());
        }
        
        // LOAD THE CONTRACT SELECTED BY THE TEAM OWNER
        if (player.getContract() == null) {
            contractBox.setValue("Choose a Contract");
        } else {
            contractBox.setValue(player.getContract());
        }
        
        // LOAD THE SALARY GIVEN TO THE PLAYER
        salaryTextField.setText(Double.toString(player.getSalary()));
        
        // LOAD THE IMAGES FOR THE PLAYER
        String playerString = "file:" + PATH_IMAGES + PATH_HEADSHOTS + player.getLastName() + player.getFirstName() + ".jpg";
        playerPic = new Image(playerString);
        if (playerPic.errorProperty().getValue()) {
                playerPic = new Image("file:./player_images/players/AAA_PhotoMissing.jpg");
                img.setImage(playerPic);
        } else {
            playerPic = new Image(playerString);
            img.setImage(playerPic);
        }
        
        // LOAD THE NATION IMAGE FOR THE PLAYER
        String imageString = "file:" + PATH_IMAGES + PATH_FLAGS + player.getNation() + ".png";
        playerFlag = new Image(imageString);
        img2.setImage(playerFlag);
    }
    
    public boolean wasCompleteSelected() {
        return selection.equals(COMPLETE);
    }
    
    public void showEditPlayerDialog(Player playerToEdit) {
        // SET THE DIALOG TITLE
        setTitle(EDIT_PLAYER_TITLE);
        
        // LOAD THE PLAYER INTO OUR LOCAL OBJECT
        player = new Hitter();
        player.setFirstName(playerToEdit.getFirstName());
        player.setLastName(playerToEdit.getLastName());
        player.setMLBTeam(playerToEdit.getMLBTeam());
        player.setNation(playerToEdit.getNation());
        player.setPosition(playerToEdit.getPosition());
        player.setChosenTeam(playerToEdit.getChosenTeam());
        player.setChosenPosition(playerToEdit.getChosenPosition());
        player.setContract(playerToEdit.getContract());
        player.setSalary(playerToEdit.getSalary());
        
        // AND THEN INTO OUR GUI
        loadGUIData();
               
        // AND OPEN IT UP
        this.showAndWait();
    }
}