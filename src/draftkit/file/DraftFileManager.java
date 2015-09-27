package draftkit.file;

import draftkit.data.Draft;
import draftkit.data.Player;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This interface provides an abstraction of what a file manager should do. Note
 * that file managers know how to read and write drafts, hitters, and pitchers,
 * but now how to export sites.
 * 
 * @author Devon Maguire
 */
public interface DraftFileManager {
    public void                             saveDraft(Draft draftToSave) throws IOException;
    public void                             loadDraft(Draft draftToLoad, String draftPath) throws IOException;
    public ArrayList<Player>                loadHitters(String filePath) throws IOException;
    public ArrayList<Player>                loadPitchers(String filePath) throws IOException;
}
