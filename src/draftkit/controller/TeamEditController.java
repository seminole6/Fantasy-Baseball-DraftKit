package draftkit.controller;

import static draftkit.DraftKit_PropertyType.REMOVE_TEAM_MESSAGE;
import draftkit.data.Draft;
import draftkit.data.DraftDataManager;
import draftkit.data.Player;
import draftkit.data.Team;
import draftkit.gui.DraftKit_GUI;
import draftkit.gui.MessageDialog;
import draftkit.gui.TeamDialog;
import draftkit.gui.YesNoCancelDialog;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 * 
 * @author Devon Maguire
 */
public class TeamEditController {
    TeamDialog td;
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    public TeamEditController(Stage initPrimaryStage, Draft draft, MessageDialog initMessageDialog, YesNoCancelDialog initYesNoCancelDialog) {
        td = new TeamDialog(initPrimaryStage, draft, initMessageDialog);
        messageDialog = initMessageDialog;
        yesNoCancelDialog = initYesNoCancelDialog;
    }

    // THESE ARE FOR TEAMS
    
    public Team handleAddTeamRequest(DraftKit_GUI gui) {
        DraftDataManager ddm = gui.getDataManager();
        Draft draft = ddm.getDraft();
        td.showAddTeamDialog();
        
        Team team = new Team();
        // DID THE USER CONFIRM?
        if (td.wasCompleteSelected() && !td.getTeam().getManagerName().equals("") && !td.getTeam().getTeamName().equals("")) {
            // GET THE TEAM
            team = td.getTeam();
            
            // AND THE TEAM TO THE DRAFT
            draft.addTeam(team);
            
            // THE DRAFT IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            gui.getFileController().markAsEdited(gui);
        }
        else if (td.wasCompleteSelected() && (td.getTeam().getManagerName().equals("") || td.getTeam().getTeamName().equals(""))) {
            messageDialog.show("Every team must have a name and an owner.");
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }
        return team;
    }
    
    public void handleEditTeamRequest(DraftKit_GUI gui, Team teamToEdit) {
        DraftDataManager ddm = gui.getDataManager();
        Draft draft = ddm.getDraft();
        td.showEditTeamDialog(teamToEdit);
        
        // DID THE USER CONFIRM?
        if (td.wasCompleteSelected() && !td.getTeam().getManagerName().equals("") && !td.getTeam().getTeamName().equals("")) {
            // UPDATE THE TEAM
            Team team = td.getTeam();
            teamToEdit.setTeamName(team.getTeamName());
            teamToEdit.setManagerName(team.getManagerName());
            
            // THE DRAFT IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            gui.getFileController().markAsEdited(gui);
        }
        else if (td.wasCompleteSelected() && (td.getTeam().getManagerName().equals("") || td.getTeam().getTeamName().equals(""))) {
            messageDialog.show("Every team must have a name and an owner.");
        }
        else {
            // THE USER MUST HAVE PRESSED CANCEL, SO
            // WE DO NOTHING
        }        
    }
    
    public void handleRemoveTeamRequest(DraftKit_GUI gui, Team teamToRemove) {
        // PROMPT THE USER TO SAVE UNSAVED WORK
        yesNoCancelDialog.show(PropertiesManager.getPropertiesManager().getProperty(REMOVE_TEAM_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoCancelDialog.getSelection();

        // IF THE USER SAID YES, THEN REMOVE IT
        if (selection.equals(YesNoCancelDialog.YES)) { 
            for (Player p : teamToRemove.getTeamList()) {
                gui.getDataManager().getDraft().getPlayerPool().add(p);
                gui.getDataManager().getDraft().getSortedPlayerPool().add(p);
                if (gui.getDataManager().getDraft().getDraftData().contains(p)) {
                    gui.getDataManager().getDraft().getDraftData().remove(p);
                }
            }
            for (Player p : teamToRemove.getTaxiList()) {
                gui.getDataManager().getDraft().getPlayerPool().add(p);
                gui.getDataManager().getDraft().getSortedPlayerPool().add(p);
                if (gui.getDataManager().getDraft().getDraftData().contains(p)) {
                    gui.getDataManager().getDraft().getDraftData().remove(p);
                }
            }
            
            gui.getDataManager().getDraft().removeTeam(teamToRemove);
            
            // THE DRAFT IS NOW DIRTY, MEANING IT'S BEEN 
            // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
            // THE SAVE BUTTON IS ENABLED
            gui.getFileController().markAsEdited(gui);
        }
    }
    
}