package draftkit;

import static draftkit.DraftKit_PropertyType.PROP_APP_TITLE;
import static draftkit.DraftKit_StartupConstants.JSON_FILE_PATH_HITTERS;
import static draftkit.DraftKit_StartupConstants.JSON_FILE_PATH_PITCHERS;
import static draftkit.DraftKit_StartupConstants.PATH_DATA;
import static draftkit.DraftKit_StartupConstants.PROPERTIES_FILE_NAME;
import static draftkit.DraftKit_StartupConstants.PROPERTIES_SCHEMA_FILE_NAME;
import draftkit.data.DraftDataManager;
import draftkit.data.Player;
import draftkit.error.ErrorHandler;
import draftkit.file.JsonDraftFileManager;
import javafx.application.Application;
import draftkit.gui.DraftKit_GUI;
import java.io.IOException;
import java.util.ArrayList;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 * DraftKit is a JavaFX application that can be used to build a database that
 *  aids in organizing Fantasy sports drafts. In this case this particular draft
 *  kit is built toward baseball.
 * 
 * @author Devon Maguire
 */
public class DraftKit extends Application {
    // THIS IS THE FULL USER INTERFACE, WHICH WILL BE INITIALIZED
    // AFTER THE PROPERTIES FILE IS LOADED
    DraftKit_GUI gui;
    private Object jsonFileManager;
    
    /**
    * This is where our Application begins its initialization, it will
    * create the GUI and initialize all of its components.
    * 
    * @param primaryStage This application's window.
    */
    @Override
    public void start(Stage primaryStage) {
        // LET'S START BY GIVING THE PRIMARY STAGE TO OUR ERROR HANDLER
        ErrorHandler eH = ErrorHandler.getErrorHandler();
        eH.initMessageDialog(primaryStage);
        
        // LOAD APP SETTINGS INTO THE GUI AND START IT UP
        boolean success = loadProperties();
        if (success) {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String appTitle = props.getProperty(PROP_APP_TITLE);
            try {
                // WE WILL SAVE OUR COURSE DATA USING THE JSON FILE
                // FORMAT SO WE'LL LET THIS OBJECT DO THIS FOR US
                JsonDraftFileManager jsonFileManager = new JsonDraftFileManager();
                
                // LOAD THE HITTERS AND PITCHERS AND ADD THEM TO THE PLAYERPOOL
                ArrayList<Player> hitters = jsonFileManager.loadHitters(JSON_FILE_PATH_HITTERS);
                ArrayList<Player> pitchers = jsonFileManager.loadPitchers(JSON_FILE_PATH_PITCHERS);
                
                // AND NOW GIVE ALL OF THIS STUFF TO THE GUI
                // INITIALIZE THE USER INTERFACE COMPONENTS
                gui = new DraftKit_GUI(primaryStage);
                gui.setDraftFileManager(jsonFileManager);
                
                // CONSTRUCT THE DATA MANAGER AND GIVE IT TO THE GUI
                DraftDataManager dataManager = new DraftDataManager(gui, hitters, pitchers);
                
                // GIVE THE DATA MANAGER TO THE GUI
                gui.setDataManager(dataManager);
                
                // FINALLY, START UP THE USER INTERFACE WINDOW AFTER ALL
                // REMAINING INITIALIZATION
                gui.initGUI(appTitle);
            }
            catch (IOException ioe) {
                eH = ErrorHandler.getErrorHandler();
                eH.handlePropertiesFileError();
            }
        }
    }
    
    /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     * 
     * @return true if the properties file was loaded successfully, false otherwise.
     */
    public boolean loadProperties() {
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            return true;
       } catch (InvalidXMLFileFormatException ixmlffe) {
            // SOMETHING WENT WRONG INITIALIZING THE XML FILE
            ErrorHandler eH = ErrorHandler.getErrorHandler();
            eH.handlePropertiesFileError();
            return false;
        }        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
