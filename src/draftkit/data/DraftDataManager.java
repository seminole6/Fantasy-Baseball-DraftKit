package draftkit.data;

import draftkit.file.DraftFileManager;
import java.util.ArrayList;

/**
 * This class manages a Draft, which means it knows how to
 * reset one with default values.
 * 
 * @author Devon Maguire
 */
public class DraftDataManager {
    // THIS IS THE COURSE BEING EDITED
    Draft draft;
    
    // THIS IS THE UI, WHICH MUST BE UPDATED
    // WHENEVER OUR MODEL'S DATA CHANGES
    DraftDataView view;
    
    // THIS HELPS US LOAD THINGS FOR OUR COURSE
    DraftFileManager fileManager;
    
    // DEFAULT INITIALIZATION VALUES FOR NEW DRAFT
    
    public DraftDataManager(DraftDataView initView, ArrayList<Player> hitters, ArrayList<Player> pitchers) {
        view = initView;
        draft = new Draft();
        draft.playerPool.addAll(hitters);
        draft.playerPool.addAll(pitchers);
        draft.setHitters(hitters);
        draft.setPitchers(pitchers);
    }
    
    /**
     * Accessor method for getting the Course that this class manages.
     */
    public Draft getDraft() {
        return draft;
    }
    
    /**
     * Accessor method for getting the file manager, which knows how
     * to read and write course data from/to files.
     */
    public DraftFileManager getFileManager() {
        return fileManager;
    }

    /**
     * Resets the course to its default initialized settings, triggering
     * the UI to reflect these changes.
     */
    public void reset() {
        // CLEAR ALL THE DRAFT VALUES
        draft.getFantasyTeams().clear();
        
        // AND THEN FORCE THE UI TO RELOAD THE UPDATED DRAFT
        view.reloadDraft(draft);
    }
}
