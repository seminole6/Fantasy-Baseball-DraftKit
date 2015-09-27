package draftkit.controller;

import static draftkit.DraftKit_PropertyType.REMOVE_PLAYER_MESSAGE;
import draftkit.data.Contract;
import draftkit.data.Draft;
import draftkit.data.DraftDataManager;
import draftkit.data.Player;
import draftkit.data.Team;
import draftkit.gui.DraftKit_GUI;
import draftkit.gui.MessageDialog;
import draftkit.gui.PlayerDialog;
import draftkit.gui.PlayerEditDialog;
import draftkit.gui.YesNoCancelDialog;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author Devon Maguire
 */
public class PlayerEditController {
    PlayerDialog pd;
    PlayerEditDialog pe;
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    public PlayerEditController(Stage initPrimaryStage, Draft draft, MessageDialog initMessageDialog, YesNoCancelDialog initYesNoCancelDialog) {
        pd = new PlayerDialog(initPrimaryStage, draft, initMessageDialog);
        pe = new PlayerEditDialog(initPrimaryStage, draft, initMessageDialog);
        messageDialog = initMessageDialog;
        yesNoCancelDialog = initYesNoCancelDialog;
    }
    
    public void handleAddPlayerRequest(DraftKit_GUI gui) {
        DraftDataManager ddm = gui.getDataManager();
        Draft draft = ddm.getDraft();
        pd.showAddPlayerDialog();
        
        if (!pd.getCheckBoxes().isEmpty()) {
            for (int n = 0; n < pd.getCheckBoxes().size(); n++) {
                if (n == pd.getCheckBoxes().size() - 1)
                    pd.getPlayer().setPosition(pd.getPlayer().getPosition()
                            + pd.getCheckBoxes().get(n));
                else
                    pd.getPlayer().setPosition(pd.getPlayer().getPosition()
                            + pd.getCheckBoxes().get(n) + "_");
            }
        }
        
        // DID THE USER CONFIRM?
        if (pd.wasCompleteSelected()) {
            // GET THE PLAYER
            Player newPlayer = pd.getPlayer();
            
            // AND ADD IT AS A ROW TO THE TABLE
            draft.getPlayerPool().add(newPlayer);

            // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            gui.getFileController().markAsEdited(gui);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
        
    }

    public void handleEditPlayerRequest(DraftKit_GUI gui, Player playerToEdit) {
        DraftDataManager ddm = gui.getDataManager();
        Draft draft = ddm.getDraft();
        pe.showEditPlayerDialog(playerToEdit);
        
        // DID THE USER CONFIRM?
        if (pe.wasCompleteSelected() && pe.getPlayer().getSalary() != 0.0 && pe.getPlayer().getContract() != null) {
            // UPDATE THE PLAYER
            
            Player player = pe.getPlayer();
            
            Team oldTeam = draft.findTeam(playerToEdit.getChosenTeam());
            Team newTeam = draft.findTeam(player.getChosenTeam());
            
            playerToEdit.setFirstName(player.getFirstName());
            playerToEdit.setLastName(player.getLastName());
            playerToEdit.setMLBTeam(player.getMLBTeam());
            playerToEdit.setPosition(player.getPosition());
            playerToEdit.setChosenTeam(player.getChosenTeam());
            playerToEdit.setChosenPosition(player.getChosenPosition());
            if (newTeam.getPlayersNeeded() > 8) {
                playerToEdit.setContract(player.getContract());
                playerToEdit.setSalary(player.getSalary());
            } else {
                playerToEdit.setContract(Contract.X);
                playerToEdit.setSalary(1);
            }
            
            // ADD AND REMOVE PLAYER TO/FROM CORRECT PLACE
            if (oldTeam == null) {
                draft.removePlayer(playerToEdit);
                newTeam.minusPlayer();
                if (newTeam.getPlayersNeeded() > 8 && playerToEdit.getContract() == Contract.S2) {
                    newTeam.getTeamList().add(playerToEdit);
                    draft.getDraftData().add(playerToEdit);
                } else if (newTeam.getPlayersNeeded() <= 8 && playerToEdit.getContract() == Contract.X) {
                    newTeam.getTaxiList().add(playerToEdit);
                    draft.getDraftData().add(playerToEdit);
                }
            }
            else if (player.getChosenTeam().equals("Free Agent")) {
                oldTeam.addedPlayer();
                draft.getPlayerPool().add(playerToEdit);
                draft.getSortedPlayerPool().add(playerToEdit);
                draft.removePlayerFromTeam(oldTeam, playerToEdit);
                draft.getDraftData().remove(playerToEdit);
            }
            else {
                draft.removePlayerFromTeam(oldTeam, playerToEdit);
                newTeam.getTeamList().add(playerToEdit);
                oldTeam.addedPlayer();
                newTeam.minusPlayer();
                draft.getDraftData().remove(playerToEdit);
                if (newTeam.getPlayersNeeded() > 8 && playerToEdit.getContract() == Contract.S2) {
                    draft.getDraftData().add(playerToEdit);
                } else if (newTeam.getPlayersNeeded() <= 8 && playerToEdit.getContract() == Contract.X) {
                    draft.getDraftData().add(playerToEdit);
                }
            }
            
            // SUBTRACT FROM THE POSITION COUNT (THIS MAY NOT BE THE RIGHT PLACE)
            if (oldTeam != null) {
                oldTeam.restorePosition(player.getChosenPosition());
            }
            if (!player.getChosenTeam().equals("Free Agent")) {
                newTeam.minusPosition(player.getChosenPosition());
            }
            
            // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            gui.getFileController().markAsEdited(gui);
        }
        else if (pe.wasCompleteSelected() && pe.getPlayer().getSalary() == 0.0 && pe.getPlayer().getContract() == null) {
            String invalid = "Player must have a contract and salary to be added to a team.";
            messageDialog.show(invalid);
            
        }
        else if (pe.wasCompleteSelected() && pe.getPlayer().getSalary() == 0.0) {
            String invalid = "Player must have a salary to be added to a team.";
            messageDialog.show(invalid);
        }
        else if (pe.wasCompleteSelected() && pe.getPlayer().getContract() == null) {
            String invalid = "Player must have a contract to be added to a team.";
            messageDialog.show(invalid);
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
    }
    
    public void handleRemovePlayerRequest(DraftKit_GUI gui, Player playerToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_PLAYER_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();

        // IF THE USER SAID YES, THEN REMOVE IT
        if (selection.equals(YesNoCancelDialog.YES)) { 
            gui.getDataManager().getDraft().removePlayer(playerToRemove);
            
            // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            gui.getFileController().markAsEdited(gui);
        }
    }
}
