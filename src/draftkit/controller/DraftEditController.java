package draftkit.controller;

import draftkit.data.Contract;
import draftkit.data.Draft;
import draftkit.data.DraftDataManager;
import draftkit.data.Player;
import draftkit.data.Team;
import draftkit.error.ErrorHandler;
import draftkit.gui.DraftKit_GUI;
import draftkit.gui.MessageDialog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.TableView;

/**
 * This controller class handles the responses to all draft
 * editing input, including verification of data and binding of
 * entered data to the Draft object.
 * 
 * @author Devon Maguire
 */
public class DraftEditController {
    // WE USE THIS TO MAKE SURE OUR PROGRAMMED UPDATES OF UI
    // VALUES DON'T THEMSELVES TRIGGER EVENTS
    private boolean enabled;
    private boolean run = true;

    /**
     * Constructor that gets this controller ready, not much to
     * initialize as the methods for this function are sent all
     * the objects they need as arguments.
     */
    public DraftEditController() {
        enabled = true;
    }

    /**
     * This mutator method lets us enable or disable this controller.
     * 
     * @param enableSetting If false, this controller will not respond to
     * Draft editing. If true, it will.
     */
    public void enable(boolean enableSetting) {
        enabled = enableSetting;
    }

    /**
     * This controller function is called in response to the user changing
     * draft details in the UI. It responds by updating the bound Draft
     * object using all the UI values, including the verification of that
     * data.
     * 
     * @param gui The user interface that requested the change.
     */
    public void handleDraftChangeRequest(DraftKit_GUI gui) {
        if (enabled) {
            try {
                // UPDATE THE COURSE, VERIFYING INPUT VALUES
                gui.updateDraftInfo(gui.getDataManager().getDraft());
                
                // THE COURSE IS NOW DIRTY, MEANING IT'S BEEN 
                // CHANGED SINCE IT WAS LAST SAVED, SO MAKE SURE
                // THE SAVE BUTTON IS ENABLED
                gui.getFileController().markAsEdited(gui);
            } catch (Exception e) {
                // SOMETHING WENT WRONG
                ErrorHandler eH = ErrorHandler.getErrorHandler();
                eH.handleUpdateDraftError();
            }
        }
    }
    
    public Player addPlayerToDraft(DraftDataManager dataManager) {
        Contract contract = Contract.S2;
        Contract contractT = Contract.X;
        
        // FIND PLAYER TO ADD TO THE DRAFT
        Draft draft = dataManager.getDraft();
        ArrayList<Player> playerList = draft.getPlayerPool();
        for (int t = 0; t < draft.getFantasyTeams().size(); t++) {
            Team team = draft.getFantasyTeams().get(t);
            for (int p = 0; p < playerList.size(); p++) {
                Player player = playerList.get(p);
                List<String> posList = Arrays.asList(player.getPosition().split("_"));
                for (int pp = 0; pp < posList.size(); pp++) {
                    int pCount = team.emptyPosition(posList.get(pp));
                    if (!draft.teamsFull() && pCount > 0) {
                        player.setChosenTeam(team.getTeamName());
                        player.setContract(contract);
                        player.setChosenPosition(posList.get(pp));
                        player.setSalary(1);
                        team.getTeamList().add(player);
                        team.minusPlayer();
                        team.minusPosition(posList.get(pp));
                        team.subtractSalary(player);
                        draft.getPlayerPool().remove(player);
                        return player;
                    } else if (draft.teamsFull() && team.getPlayersNeeded() > 0) {
                        player.setChosenTeam(team.getTeamName());
                        player.setContract(contractT);
                        player.setChosenPosition(posList.get(pp));
                        player.setSalary(1);
                        team.getTaxiList().add(player);
                        team.minusPlayer();
                        draft.getPlayerPool().remove(player);
                        return player;
                    }
                }
            }
        }
        return null;
    }
    
    public void autoDraftPlayer(DraftDataManager dataManager, DraftKit_GUI gui) {
        DraftEditController controller = this;
        this.run = true;
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                TableView tempPlayerTable = gui.getPlayerTable();
                TableView tempStandingTable = gui.getStandingTable();
                TableView tempDraftTable = gui.getDraftTable();
                while (run) {
                    Player player = controller.addPlayerToDraft(dataManager);
                    Thread.sleep(100);
                    if (player == null) {
                        return null;
                    } else {
                        dataManager.getDraft().getDraftData().add(player);
                        tempDraftTable = gui.resetDraftTable(dataManager.getDraft().getDraftData());
                    }
                    tempPlayerTable = gui.resetTable(dataManager.getDraft().getPlayerPool());
                    //tempStandingTable = gui.resetStandingTable(dataManager.getDraft().getFantasyTeams());
                    //gui.updateTeamTables();
                }
                return null;
            }
        };
        
        Thread thread = new Thread(task);
        thread.start();
    }
    
    public void autoDraftPause() {
        this.run = false;
    }
    
}
