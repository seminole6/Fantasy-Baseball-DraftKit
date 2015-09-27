package draftkit.gui;

import draftkit.DraftKit_PropertyType;
import static draftkit.DraftKit_PropertyType.AUTO_DRAFT_TOOLTIP;
import static draftkit.DraftKit_PropertyType.AUTO_PAUSE_TOOLTIP;
import static draftkit.DraftKit_PropertyType.PAUSE_ICON;
import static draftkit.DraftKit_PropertyType.PICK_ICON;
import static draftkit.DraftKit_PropertyType.PICK_PLAYER_TOOLTIP;
import static draftkit.DraftKit_PropertyType.PLAY_ICON;
import static draftkit.DraftKit_StartupConstants.CLOSE_BUTTON_LABEL;
import static draftkit.DraftKit_StartupConstants.PATH_CSS;
import static draftkit.DraftKit_StartupConstants.PATH_IMAGES;
import draftkit.controller.DraftEditController;
import draftkit.controller.FileController;
import draftkit.controller.PlayerEditController;
import draftkit.controller.TeamEditController;
import draftkit.data.Contract;
import draftkit.data.Draft;
import draftkit.data.DraftDataManager;
import draftkit.data.DraftDataView;
import draftkit.data.Player;
import draftkit.data.PositionCompare;
import draftkit.data.Team;
import draftkit.file.DraftFileManager;
import draftkit.file.DraftSiteExporter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import static javafx.scene.control.TabPane.TabClosingPolicy.UNAVAILABLE;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import properties_manager.PropertiesManager;

/**
 * This class provides the Graphical User Interface for this application,
 * managing all the UI components for editing a Draft and exporting it to a
 * site.
 * 
 * @author Devon Maguire
 */
public class DraftKit_GUI implements DraftDataView {
    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS GUI'S COMPONENTS TO A STYLE SHEET THAT IT USES

    static final String PRIMARY_STYLE_SHEET = PATH_CSS + "draftKit_style.css";
    static final String CLASS_BORDERED_PANE = "bordered_pane";
    static final String CLASS_DRAFT_PANE = "subject_pane";
    static final String CLASS_HEADING_LABEL = "heading_label";
    static final String CLASS_SUBHEADING_LABEL = "subheading_label";
    static final String CLASS_PROMPT_LABEL = "prompt_label";
    static final String EMPTY_TEXT = "";
    static final int LARGE_TEXT_FIELD_LENGTH = 20;
    static final int SMALL_TEXT_FIELD_LENGTH = 5;
    
    // THIS MANAGES ALL OF THE APPLICATION'S DATA
    DraftDataManager dataManager;

    // THIS MANAGES COURSE FILE I/O
    DraftFileManager draftFileManager;

    // THIS MANAGES EXPORTING OUR SITE PAGES
    DraftSiteExporter siteExporter;

    // THIS HANDLES INTERACTIONS WITH FILE-RELATED CONTROLS
    FileController fileController;

    // THIS HANDLES INTERACTIONS WITH DRAFT CONTROLS
    DraftEditController draftController;
    
    // THIS HANDLES INTERACTIONS WITH TEAM CONTROLS
    TeamEditController teamController;
    
    // THIS HANDLES INTERACTIONS WITH PLAYER CONTROLS
    PlayerEditController playerController;
    
    // THIS IS FOR SORTING THE TEAM TABLE
    PositionCompare positionCompare;

    // THIS IS THE APPLICATION WINDOW
    Stage primaryStage;

    // THIS IS THE STAGE'S SCENE GRAPH
    Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION GUI
    BorderPane draftPane;
    
    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    FlowPane fileToolbarPane;
    Button newDraftButton;
    Button loadDraftButton;
    Button saveDraftButton;
    Button exportDraftButton;
    Button exitButton;

    // WE'LL ORGANIZE OUR WORKSPACE COMPONENTS USING A BORDER PANE
    BorderPane workspacePane;
    boolean workspaceActivated;

    // WE'LL CREATE THE MAIN WORKSPACE PANE
    TabPane mainWorkspacePane;
    
    // WE'LL MAKE A TAB FOR EACH PAGE
    Tab teamTab;
    Tab playerTab;
    Tab standingsTab;
    Tab draftTab;
    Tab mlbTeamTab;
    
    // SCROLLING PANES FOR THE PAGES THAT NEED SCROLLING
    ScrollPane teamScroll;
    
    // WE'LL MAKE A SEPERATE PANE AND LABEL FOR EACH PAGE
    Label teamHeadingLabel;
    VBox teamWorkspacePane;
    Label playerHeadingLabel;
    VBox playerWorkspacePane;
    Label standingHeadingLabel;
    VBox standingsWorkspacePane;
    Label draftHeadingLabel;
    VBox draftWorkspacePane;
    Label mlbTeamHeadingLabel;
    VBox mlbTeamWorkspacePane;
    
    // CONTROLS ON THE FANTASY TEAMS PAGE
    Label draftName;
    TextField draftNameText;
    
    Button addTeam;
    Button removeTeam;
    Button editTeam;
    
    ComboBox selectTeam;
    
    // TABLE FOR THE FANTASY TEAMS PAGE
    TableView<Player> teamTable;
    ObservableList<Player> teamList;
    TableColumn<Player,String> posCol;
    TableColumn<Player,String> firstCol;
    TableColumn<Player,String> lastCol;
    TableColumn<Player,String> teamCol;
    TableColumn<Player,String> positionsCol;
    TableColumn<Player,Integer> rwCol;
    TableColumn<Player,Integer> hrsvCol;
    TableColumn<Player,Integer> rbikCol;
    TableColumn<Player,Double> sberaCol;
    TableColumn<Player,Double> bawhipCol;
    TableColumn<Player,Double> eValueCol;
    TableColumn<Player,Contract> contractCol;
    TableColumn<Player,Double> salaryCol;
    
    TableView<Player> teamTaxiTable;
    ObservableList<Player> teamTaxiList;
    TableColumn<Player,String> posCol2;
    TableColumn<Player,String> firstCol2;
    TableColumn<Player,String> lastCol2;
    TableColumn<Player,String> teamCol2;
    TableColumn<Player,String> positionsCol2;
    TableColumn<Player,Integer> rwCol2;
    TableColumn<Player,Integer> hrsvCol2;
    TableColumn<Player,Integer> rbikCol2;
    TableColumn<Player,Double> sberaCol2;
    TableColumn<Player,Double> bawhipCol2;
    TableColumn<Player,Double> eValueCol2;
    TableColumn<Player,Contract> contractCol2;
    TableColumn<Player,Double> salaryCol2;
    
    // SEARCH TEXT FIELD FOR THE AVAILABLE PLAYERS SCREEN
    Label search;
    TextField searchText;
    
    Button addPlayer;
    Button removePlayer;
    
    // RADIO BUTTONS FOR THE AVAILABLE PLAYERS SCREEN
    ToggleGroup group;
    RadioButton all;
    RadioButton c;
    RadioButton oneB;
    RadioButton ci;
    RadioButton threeB;
    RadioButton twoB;
    RadioButton mi;
    RadioButton ss;
    RadioButton of;
    RadioButton u;
    RadioButton p;
    
    // TABLE AND COLUMNS TO HOLD ALL THE PLAYERS ON THE AVAILABLE PLAYERS SCREEN
    TableView<Player> playerTable;
    ObservableList<Player> playerList;
    TableColumn<Player,String> first;
    TableColumn<Player,String> last;
    TableColumn<Player,String> proTeam;
    TableColumn<Player,String> positions;
    TableColumn<Player,Integer> yob;
    TableColumn<Player,Integer> rw;
    TableColumn<Player,Integer> hrsv;
    TableColumn<Player,Integer> rbik;
    TableColumn<Player,Double> sbera;
    TableColumn<Player,Double> bawhip;
    TableColumn<Player,Double> estimatedValue;
    TableColumn<Player,String> notes;
    
    // AND TABLE COLUMNS TO AVAILABLE PLAYERS PAGE
    static final String COL_FIRST = "First";
    static final String COL_LAST = "Last";
    static final String COL_PROTEAM = "Pro Team";
    static final String COL_POSITIONS = "Positions";
    static final String COL_YOB = "Year of Birth";
    static final String COL_RW = "R/W";
    static final String COL_HRSV = "HR/SV";
    static final String COL_RBIK = "RBI/K";
    static final String COL_SBERA = "SB/ERA";
    static final String COL_BAWHIP = "BA/WHIP";
    static final String COL_ESTIMATED_VALUE = "Estimated Value";
    static final String COL_NOTES = "Notes";
    
    // JUST THE HITTER TABLE
    static final String COL_R = "R";
    static final String COL_HR = "HR";
    static final String COL_RBI = "RBI";
    static final String COL_SB = "SB";
    static final String COL_BA = "BA";
    
    // JUST THE PITCHER TABLE
    static final String COL_W = "W";
    static final String COL_SV = "SV";
    static final String COL_K = "K";
    static final String COL_ERA = "ERA";
    static final String COL_WHIP = "WHIP";
    
    // ITEMS USED IN THE STANDINGS ESTIMATE PAGE
    TableView<Team> standingTable;
    ObservableList<Team> standingList;
    
    TableColumn<Team,String> teamName;
    TableColumn<Team,Integer> pNeeded;
    TableColumn<Team,Integer> moneyLeft;
    TableColumn<Team,Integer> moneyPP;
    TableColumn<Team,Integer> rEst;
    TableColumn<Team,Integer> hrEst;
    TableColumn<Team,Integer> rbiEst;
    TableColumn<Team,Integer> sbEst;
    TableColumn<Team,Double> baEst;
    TableColumn<Team,Double> wEst;
    TableColumn<Team,Integer> svEst;
    TableColumn<Team,Integer> kEst;
    TableColumn<Team,Double> eraEst;
    TableColumn<Team,Double> whipEst;
    TableColumn<Team,Integer> totalPoints;
    
    // VAIRABLES TO PUT THINGS IN THE DRAFT PAGE
    Button pickPlayer;
    Button autoPickPlayer;
    Button autoPickPause;
    
    TableView<Player> draftTable;
    ObservableList<Player> draftList;
    ArrayList<Player> tempDraftList;
    
    TableColumn<Player,Integer> draftPick;
    TableColumn<Player,String> draftFirst;
    TableColumn<Player,String> draftLast;
    TableColumn<Player,String> draftTeam;
    TableColumn<Player,Contract> draftContract;
    TableColumn<Player,Integer> draftSalary;
    
    // COMBOBOX AND TABLE FOR THE MLB TEAM PAGE
    ComboBox mlbTeamBox;
    ArrayList<String> proTeams;
    
    TableView<Player> mlbTable;
    ObservableList<Player> mlbList;
    TableColumn<Player,String> mlbFirst;
    TableColumn<Player,String> mlbLast;
    TableColumn<Player,String> mlbPositions;
    
    // HERE ARE OUR DIALOGS
    MessageDialog messageDialog;
    YesNoCancelDialog yesNoCancelDialog;
    
    /**
     * Constructor for making this GUI, note that it does not initialize the UI
     * controls. To do that, call initGUI.
     *
     * @param initPrimaryStage Window inside which the GUI will be displayed.
     */
    public DraftKit_GUI(Stage initPrimaryStage) {
        primaryStage = initPrimaryStage;
    }

    /**
     * Accessor method for the data manager.
     *
     * @return The DraftDataManager used by this UI.
     */
    public DraftDataManager getDataManager() {
        return dataManager;
    }

    /**
     * Accessor method for the file controller.
     *
     * @return The FileController used by this UI.
     */
    public FileController getFileController() {
        return fileController;
    }

    /**
     * Accessor method for the draft file manager.
     *
     * @return The DraftFileManager used by this UI.
     */
    public DraftFileManager getDraftFileManager() {
        return draftFileManager;
    }

    /**
     * Accessor method for the site exporter.
     *
     * @return The DraftSiteExporter used by this UI.
     */
    public DraftSiteExporter getSiteExporter() {
        return siteExporter;
    }

    /**
     * Accessor method for the window (i.e. stage).
     *
     * @return The window (i.e. Stage) used by this UI.
     */
    public Stage getWindow() {
        return primaryStage;
    }
    
    public MessageDialog getMessageDialog() {
        return messageDialog;
    }
    
    public YesNoCancelDialog getYesNoCancelDialog() {
        return yesNoCancelDialog;
    }
    
    public TableView getPlayerTable() {
        return playerTable;
    }
    
    public TableView getStandingTable() {
        return standingTable;
    }
    
    public TableView getDraftTable() {
        return draftTable;
    }

    /**
     * Mutator method for the data manager.
     *
     * @param initDataManager The DraftDataManager to be used by this UI.
     */
    public void setDataManager(DraftDataManager initDataManager) {
        dataManager = initDataManager;
    }

    /**
     * Mutator method for the draft file manager.
     *
     * @param initDraftFileManager The DraftFileManager to be used by this UI.
     */
    public void setDraftFileManager(DraftFileManager initDraftFileManager) {
        draftFileManager = initDraftFileManager;
    }

    /**
     * Mutator method for the site exporter.
     *
     * @param initSiteExporter The DraftSiteExporter to be used by this UI.
     */
    public void setSiteExporter(DraftSiteExporter initSiteExporter) {
        siteExporter = initSiteExporter;
    }

    /**
     * This method fully initializes the user interface for use.
     *
     * @param windowTitle The text to appear in the UI window's title bar.
     * @throws IOException Thrown if any initialization files fail to load.
     */
    public void initGUI(String windowTitle) throws IOException {
        // INIT THE DIALOGS
        initDialogs();
        
        // INIT THE TOOLBAR
        initFileToolbar();

        // INIT THE CENTER WORKSPACE CONTROLS BUT DON'T ADD THEM
        // TO THE WINDOW YET
        initWorkspace();

        // NOW SETUP THE EVENT HANDLERS
        initEventHandlers();

        // AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
        initWindow(windowTitle);
    }

    /**
     * When called this function puts the workspace into the window,
     * revealing the controls for editing a Draft.
     */
    public void activateWorkspace() {
        if (!workspaceActivated) {
            // PUT THE WORKSPACE IN THE GUI
            draftPane.setCenter(workspacePane);
            workspaceActivated = true;
        }
    }
    
    /**
     * This function takes all of the data out of the draftToReload 
     * argument and loads its values into the user interface controls.
     * 
     * @param draftToReload The Draft whose data we'll load into the GUI.
     */
    @Override
    public void reloadDraft(Draft draftToReload) {
        // FIRST ACTIVATE THE WORKSPACE IF NECESSARY
        if (!workspaceActivated) {
            activateWorkspace();
        }

        // WE DON'T WANT TO RESPOND TO EVENTS FORCED BY
        // OUR INITIALIZATION SELECTIONS
        draftController.enable(false);

        // LOAD ALL THE DRAFT INFO
        //playerList.setAll(FXCollections.observableArrayList(dataManager.getDraft().getPlayerPool()));

        // NOW WE DO WANT TO RESPOND WHEN THE USER INTERACTS WITH OUR CONTROLS
        draftController.enable(true);
    }

    /**
     * This method is used to activate/deactivate toolbar buttons when
     * they can and cannot be used so as to provide foolproof design.
     * 
     * @param saved Describes whether the loaded Course has been saved or not.
     */
    public void updateToolbarControls(boolean saved) {
        // THIS TOGGLES WITH WHETHER THE CURRENT COURSE
        // HAS BEEN SAVED OR NOT
        saveDraftButton.setDisable(saved);

        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING THAT FIRST COURSE BEGINS
        loadDraftButton.setDisable(false);
        exportDraftButton.setDisable(false);

        // NOTE THAT THE NEW, LOAD, AND EXIT BUTTONS
        // ARE NEVER DISABLED SO WE NEVER HAVE TO TOUCH THEM
    }
    
    /**
     * This function loads all the values currently in the user interface
     * into the draft argument.
     * 
     * @param draft The draft to be updated using the data from the UI controls.
     */
    public void updateDraftInfo(Draft draft) {
        // add necessary code here ********
    }

    /****************************************************************************/
    /* BELOW ARE ALL THE PRIVATE HELPER METHODS WE USE FOR INITIALIZING OUR GUI */
    /****************************************************************************/
    
    private void initDialogs() {
        messageDialog = new MessageDialog(primaryStage, CLOSE_BUTTON_LABEL);
        yesNoCancelDialog = new YesNoCancelDialog(primaryStage);
    }
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
        fileToolbarPane = new FlowPane();

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
        // START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        newDraftButton = initChildButton(fileToolbarPane, DraftKit_PropertyType.NEW_DRAFT_ICON, DraftKit_PropertyType.NEW_DRAFT_TOOLTIP, false);
        loadDraftButton = initChildButton(fileToolbarPane, DraftKit_PropertyType.LOAD_DRAFT_ICON, DraftKit_PropertyType.LOAD_DRAFT_TOOLTIP, false);
        saveDraftButton = initChildButton(fileToolbarPane, DraftKit_PropertyType.SAVE_DRAFT_ICON, DraftKit_PropertyType.SAVE_DRAFT_TOOLTIP, true);
        exportDraftButton = initChildButton(fileToolbarPane, DraftKit_PropertyType.EXPORT_DRAFT_ICON, DraftKit_PropertyType.EXPORT_DRAFT_TOOLTIP, true);
        exitButton = initChildButton(fileToolbarPane, DraftKit_PropertyType.EXIT_ICON, DraftKit_PropertyType.EXIT_TOOLTIP, false);
    }

    // CREATES AND SETS UP ALL THE CONTROLS TO GO IN THE APP WORKSPACE
    private void initWorkspace() throws IOException {
        // THE MAIN WORKSPACE HOLDS BOTH THE BASIC INFO
        // CONTROLS AS WELL AS THE PAGE SELECTION CONTROLS
        initMainWorkspace();

        // THIS HOLDS ALL OUR WORKSPACE COMPONENTS, SO NOW WE MUST
        // ADD THE COMPONENTS WE'VE JUST INITIALIZED
        workspacePane = new BorderPane();
        workspacePane.setCenter(mainWorkspacePane);
        workspacePane.getStyleClass().add(CLASS_BORDERED_PANE);

        // NOTE THAT WE HAVE NOT PUT THE WORKSPACE INTO THE WINDOW,
        // THAT WILL BE DONE WHEN THE USER EITHER CREATES A NEW
        // COURSE OR LOADS AN EXISTING ONE FOR EDITING
        workspaceActivated = false;
    }
    
    // INITIALIZES THE MAIN PORTION OF THE WORKPLACE UI
    private void initMainWorkspace() throws IOException {
        // THE TOP WORKSPACE PANE WILL ONLY DIRECTLY HOLD 2 THINGS, A LABEL
        // AND A SPLIT PANE, WHICH WILL HOLD 2 ADDITIONAL GROUPS OF CONTROLS
        mainWorkspacePane = new TabPane();
        mainWorkspacePane.setSide(Side.BOTTOM);
        mainWorkspacePane.setTabClosingPolicy(UNAVAILABLE);
        
        initTeamsTab();
        initPlayersTab();
        initStandingTab();
        initDraftTab();
        initMLBTeamTab();
    }
    
    // INITIALIZES THE CONTROLS IN THE TEAM TAB
    private void initTeamsTab() throws IOException {
        // THESE ARE THE CONTROLS FOR THE BASIC SCHEDULE PAGE HEADER INFO
        // WE'LL ARRANGE THEM IN A VBox
        teamTab = new Tab();
        teamWorkspacePane = new VBox();
        teamWorkspacePane.getStyleClass().add(CLASS_DRAFT_PANE);
        
        teamScroll = new ScrollPane();
        
        // PAGE LABEL & BUTTON
        Label teams = new Label();
        teams = initChildLabel(teamWorkspacePane, DraftKit_PropertyType.TEAMS_HEADING_LABEL, CLASS_HEADING_LABEL);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_IMAGES + props.getProperty(DraftKit_PropertyType.TEAM_ICON);
        Image buttonImage = new Image(imagePath);
        teamTab.setGraphic(new ImageView(buttonImage));
        
        // ADD THINGS TO VBOX HERE
        HBox draftNameControls = new HBox();
        initHBoxLabel(draftNameControls, DraftKit_PropertyType.DRAFT_NAME_LABEL, CLASS_PROMPT_LABEL);
        draftNameText = initHBoxTextField(draftNameControls, "", true);

        HBox teamControls = new HBox();
        addTeam = initHBoxButton(teamControls, DraftKit_PropertyType.ADD_ICON, DraftKit_PropertyType.ADD_TEAM_TOOLTIP, false);
        removeTeam = initHBoxButton(teamControls, DraftKit_PropertyType.MINUS_ICON, DraftKit_PropertyType.REMOVE_TEAM_TOOLTIP, true);
        editTeam = initHBoxButton(teamControls, DraftKit_PropertyType.EDIT_ICON, DraftKit_PropertyType.EDIT_TEAM_TOOLTIP, true);

        initHBoxLabel(teamControls, DraftKit_PropertyType.TEAM_SELECT_LABEL, CLASS_PROMPT_LABEL);
        selectTeam = new ComboBox();
        selectTeam.getItems().clear();
        for (int n = 0; n < dataManager.getDraft().getFantasyTeams().size(); n++) {
            selectTeam.getItems().add(dataManager.getDraft().getFantasyTeams().get(n).getTeamName());
        }
        selectTeam.setValue("Choose a team");
        
        selectTeam.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                ArrayList<Player> tempTeam = new ArrayList<>();
                ArrayList<Player> tempTaxi = new ArrayList<>();
                teamTable.getItems().clear();
                for (Team tt : dataManager.getDraft().getFantasyTeams()) {
                    if (tt.getTeamName().equals(selectTeam.getSelectionModel().getSelectedItem())) {
                        tempTeam = tt.getTeamList();
                        tempTaxi = tt.getTaxiList();
                    }
                }
                teamTable = resetTeamTable(tempTeam);
                teamTaxiTable = resetTaxiTable(tempTaxi);
            }
        });
        
        teamControls.getChildren().add(selectTeam);

        // TABLE FOR MAIN PLAYERS IN THE FANTASY TEAMS PAGE
        VBox teamTableBox = new VBox();
        initVBoxLabel(teamTableBox, DraftKit_PropertyType.TEAM_TABLE_LABEL, CLASS_SUBHEADING_LABEL);
        positionCompare = new PositionCompare();
        teamTable = new TableView<>();
        
        posCol = new TableColumn<>("Position");
        posCol.setCellValueFactory(new PropertyValueFactory<>("ChosenPosition"));
        posCol.setComparator(positionCompare);
        posCol.setSortType(SortType.ASCENDING);
        posCol.setSortable(true);
        firstCol = new TableColumn<>("First");
        firstCol.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        firstCol.setSortable(false);
        lastCol = new TableColumn<>("Last");
        lastCol.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        lastCol.setSortable(false);
        teamCol = new TableColumn<>("Pro Team");
        teamCol.setCellValueFactory(new PropertyValueFactory<>("MLBTeam"));
        teamCol.setSortable(false);
        positionsCol = new TableColumn<>("Positions");
        positionsCol.setCellValueFactory(new PropertyValueFactory<>("position"));
        positionsCol.setSortable(false);
        rwCol = new TableColumn<>("R/W");
        rwCol.setCellValueFactory(new PropertyValueFactory<>("RW"));
        rwCol.setSortable(false);
        hrsvCol = new TableColumn<>("HR/SV");
        hrsvCol.setCellValueFactory(new PropertyValueFactory<>("HRSV"));
        hrsvCol.setSortable(false);
        rbikCol = new TableColumn<>("RBI/K");
        rbikCol.setCellValueFactory(new PropertyValueFactory<>("RBIK"));
        rbikCol.setSortable(false);
        sberaCol = new TableColumn<>("SB/ERA");
        sberaCol.setCellValueFactory(new PropertyValueFactory<>("SBERA"));
        sberaCol.setSortable(false);
        bawhipCol = new TableColumn<>("BA/WHIP");
        bawhipCol.setCellValueFactory(new PropertyValueFactory<>("BAWHIP"));
        bawhipCol.setSortable(false);
        eValueCol = new TableColumn<>("Estimated Value");
        eValueCol.setCellValueFactory(new PropertyValueFactory<>("estimatedValue"));
        eValueCol.setSortable(false);
        contractCol = new TableColumn<>("Contract");
        contractCol.setCellValueFactory(new PropertyValueFactory<>("Contract"));
        contractCol.setSortable(false);
        salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        salaryCol.setSortable(false);
        
        teamTable.getColumns().setAll(posCol, firstCol, lastCol, positionsCol,
                rwCol, hrsvCol, rbikCol, sberaCol, bawhipCol, eValueCol, contractCol, salaryCol);
        
        teamTable.getSortOrder().add(posCol);
        
        teamTable.setItems(teamList);
        
        // TABLE FOR TAXI PLAYERS IN THE FANTASY TEAMS PAGE
        VBox teamTaxiBox = new VBox();
        initVBoxLabel(teamTaxiBox, DraftKit_PropertyType.TEAM_TAXI_LABEL, CLASS_SUBHEADING_LABEL);
        teamTaxiTable = new TableView<>();
        
        firstCol2 = new TableColumn<>("First");
        firstCol2.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        lastCol2 = new TableColumn<>("Last");
        lastCol2.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        teamCol2 = new TableColumn<>("Pro Team");
        teamCol2.setCellValueFactory(new PropertyValueFactory<>("MLBTeam"));
        positionsCol2 = new TableColumn<>("Positions");
        positionsCol2.setCellValueFactory(new PropertyValueFactory<>("position"));
        rwCol2 = new TableColumn<>("R/W");
        rwCol2.setCellValueFactory(new PropertyValueFactory<>("RW"));
        hrsvCol2 = new TableColumn<>("HR/SV");
        hrsvCol2.setCellValueFactory(new PropertyValueFactory<>("HRSV"));
        rbikCol2 = new TableColumn<>("RBI/K");
        rbikCol2.setCellValueFactory(new PropertyValueFactory<>("RBIK"));
        sberaCol2 = new TableColumn<>("SB/ERA");
        sberaCol2.setCellValueFactory(new PropertyValueFactory<>("SBERA"));
        bawhipCol2 = new TableColumn<>("BA/WHIP");
        bawhipCol2.setCellValueFactory(new PropertyValueFactory<>("BAWHIP"));
        eValueCol2 = new TableColumn<>("Estimated Value");
        eValueCol2.setCellValueFactory(new PropertyValueFactory<>("EValue"));
        contractCol2 = new TableColumn<>("Contract");
        contractCol2.setCellValueFactory(new PropertyValueFactory<>("Contract"));
        salaryCol2 = new TableColumn<>("Salary");
        salaryCol2.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        
        teamTaxiTable.getColumns().setAll(firstCol2, lastCol2, positionsCol2,
                rwCol2, hrsvCol2, rbikCol2, sberaCol2, bawhipCol2, eValueCol2, contractCol2, salaryCol2);
        
        teamTaxiTable.setItems(teamTaxiList);
        
        // SET TOOLTIP
        Tooltip t = new Tooltip("Fantasy Teams Page");
        teamTab.setTooltip(t);
        
        // PUT IN TAB PANE
        teamTableBox.getChildren().add(teamTable);
        teamTaxiBox.getChildren().add(teamTaxiTable);
        teamWorkspacePane.getChildren().add(draftNameControls);
        teamWorkspacePane.getChildren().add(teamControls);
        teamWorkspacePane.getChildren().add(teamTableBox);
        teamWorkspacePane.getChildren().add(teamTaxiBox);
        teamScroll.setContent(teamWorkspacePane);
        teamTab.setContent(teamScroll);
        mainWorkspacePane.getTabs().add(teamTab);
        
        ArrayList<Player> tempTeam = new ArrayList<>();
        for (Team tt : dataManager.getDraft().getFantasyTeams()) {
            if (tt.getTeamName().equals(selectTeam.getSelectionModel().getSelectedItem()))
                tempTeam = tt.getTeamList();
        }
        teamTable = resetTeamTable(tempTeam);
    }
    
    // INITIALIZES THE CONTROLS IN THE PLAYER TAB
    private void initPlayersTab() throws IOException {
        // THESE ARE THE CONTROLS FOR THE BASIC SCHEDULE PAGE HEADER INFO
        // WE'LL ARRANGE THEM IN A VBox
        playerTab = new Tab();
        playerWorkspacePane = new VBox();
        playerWorkspacePane.getStyleClass().add(CLASS_DRAFT_PANE);
        
        // PAGE LABEL & BUTTON
        Label playersHeading = new Label();
        playersHeading = initChildLabel(playerWorkspacePane, DraftKit_PropertyType.PLAYER_HEADING_LABEL, CLASS_HEADING_LABEL);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_IMAGES + props.getProperty(DraftKit_PropertyType.PLAYER_ICON);
        Image buttonImage = new Image(imagePath);
        playerTab.setGraphic(new ImageView(buttonImage));
        
        // SEARCH AREA
        HBox search = new HBox();
        addPlayer = initHBoxButton(search, DraftKit_PropertyType.ADD_ICON, DraftKit_PropertyType.ADD_PLAYER_TOOLTIP, false);
        removePlayer = initHBoxButton(search, DraftKit_PropertyType.MINUS_ICON, DraftKit_PropertyType.REMOVE_PLAYER_TOOLTIP, false);
        initHBoxLabel(search, DraftKit_PropertyType.SEARCH_LABEL, CLASS_PROMPT_LABEL);
        searchText = initHBoxTextField(search, "", true);
        
        // RADIO BUTTONS AREA FUNESS
        group = new ToggleGroup();
        all = new RadioButton("All");
        all.setToggleGroup(group);
        c = new RadioButton("C");
        c.setToggleGroup(group);
        oneB = new RadioButton("1B");
        oneB.setToggleGroup(group);
        ci = new RadioButton("CI");
        ci.setToggleGroup(group);
        threeB = new RadioButton("3B");
        threeB.setToggleGroup(group);
        twoB = new RadioButton("2B");
        twoB.setToggleGroup(group);
        mi = new RadioButton("MI");
        mi.setToggleGroup(group);
        ss = new RadioButton("SS");
        ss.setToggleGroup(group);
        of = new RadioButton("OF");
        of.setToggleGroup(group);
        u = new RadioButton("U");
        u.setToggleGroup(group);
        p = new RadioButton("P");
        p.setToggleGroup(group);
        
        ToolBar radioTool = new ToolBar(
                all, c, oneB, ci, threeB, twoB, mi, ss, of, u, p
        );
        
        // CREATE TABLE FOR PLAYER PAGE
        playerTable = new TableView<>();
        
        first = new TableColumn<>("First");
        first.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        last = new TableColumn<>("Last");
        last.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        proTeam = new TableColumn<>("Pro Team");
        proTeam.setCellValueFactory(new PropertyValueFactory<>("MLBTeam"));
        positions = new TableColumn<>("Positions");
        positions.setCellValueFactory(new PropertyValueFactory<>("position"));
        yob = new TableColumn<>("Year of Birth");
        yob.setCellValueFactory(new PropertyValueFactory<>("YearOfBirth"));
        rw = new TableColumn<>("R/W");
        rw.setCellValueFactory(new PropertyValueFactory<>("RW"));
        hrsv = new TableColumn<>("HR/SV");
        hrsv.setCellValueFactory(new PropertyValueFactory<>("HRSV"));
        rbik = new TableColumn<>("RBI/K");
        rbik.setCellValueFactory(new PropertyValueFactory<>("RBIK"));
        sbera = new TableColumn<>("SB/ERA");
        sbera.setCellValueFactory(new PropertyValueFactory<>("SBERA"));
        bawhip = new TableColumn<>("BA/WHIP");
        bawhip.setCellValueFactory(new PropertyValueFactory<>("BAWHIP"));
        estimatedValue = new TableColumn<>("Estimated Value");
        estimatedValue.setCellValueFactory(new PropertyValueFactory<>("EValue"));
        // MAKE SURE TO CHANGE ESTIMATED VALUE TO MATCH GETTERS/ SETTERS
        notes = new TableColumn<>("Notes");
        notes.setCellValueFactory(new PropertyValueFactory<>("notes"));
        notes.setCellFactory(TextFieldTableCell.forTableColumn());
        playerTable.setEditable(true);
        notes.setOnEditCommit((CellEditEvent<Player, String> event) -> {
            ((Player) event.getTableView().getItems().get(event.getTablePosition().getRow())).setNotes(event.getNewValue());
        });
        
        // ADD ALL THE COLUMNS TO THE TABLE
        playerTable.getColumns().setAll(first, last, proTeam, positions, yob, rw, hrsv, rbik, sbera, bawhip, estimatedValue, notes);
        
        // SET TOOLTIP
        Tooltip t = new Tooltip("Available Players Page");
        playerTab.setTooltip(t);
        
        // PUT IN TAB PANE
        playerWorkspacePane.getChildren().add(search);
        playerWorkspacePane.getChildren().add(radioTool);
        playerWorkspacePane.getChildren().add(playerTable);
        playerTab.setContent(playerWorkspacePane);
        mainWorkspacePane.getTabs().add(playerTab);
        
        playerTable = resetTable(dataManager.getDraft().getPlayerPool());
    }
    
    // INITIALIZES THE CONTROLS IN THE STANDING TAB
    private void initStandingTab() throws IOException {
        // THESE ARE THE CONTROLS FOR THE BASIC SCHEDULE PAGE HEADER INFO
        // WE'LL ARRANGE THEM IN A VBox
        standingsTab = new Tab();
        standingsWorkspacePane = new VBox();
        standingsWorkspacePane.getStyleClass().add(CLASS_DRAFT_PANE);
        
        // ADD PAGE LABEL & BUTTON
        Label standing = new Label();
        standing = initChildLabel(standingsWorkspacePane, DraftKit_PropertyType.STANDINGS_HEADING_LABEL, CLASS_HEADING_LABEL);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_IMAGES + props.getProperty(DraftKit_PropertyType.STANDINGS_ICON);
        Image buttonImage = new Image(imagePath);
        standingsTab.setGraphic(new ImageView(buttonImage));
        
        // CREATE THE STANDINGS TABLE
        standingTable = new TableView();

        teamName = new TableColumn<>("Team Name");
        teamName.setCellValueFactory(new PropertyValueFactory<>("TeamName"));
        pNeeded = new TableColumn<>("Players Needed");
        pNeeded.setCellValueFactory(new PropertyValueFactory<>("PlayersNeeded"));
        moneyLeft = new TableColumn<>("$ Left");
        moneyLeft.setCellValueFactory(new PropertyValueFactory<>("MoneyLeft"));
        moneyPP = new TableColumn<>("$ PP");
        moneyPP.setCellValueFactory(new PropertyValueFactory<>("MoneyPP"));
        rEst = new TableColumn<>("R");
        rEst.setCellValueFactory(new PropertyValueFactory<>("TotalR"));
        hrEst = new TableColumn<>("HR");
        hrEst.setCellValueFactory(new PropertyValueFactory<>("TotalHR"));
        rbiEst = new TableColumn<>("RBI");
        rbiEst.setCellValueFactory(new PropertyValueFactory<>("TotalRBI"));
        sbEst = new TableColumn<>("SB");
        sbEst.setCellValueFactory(new PropertyValueFactory<>("TotalSB"));
        baEst = new TableColumn<>("BA");
        baEst.setCellValueFactory(new PropertyValueFactory<>("TotalBA"));
        wEst = new TableColumn<>("W");
        wEst.setCellValueFactory(new PropertyValueFactory<>("TotalW"));
        svEst = new TableColumn<>("SV");
        svEst.setCellValueFactory(new PropertyValueFactory<>("TotalSV"));
        kEst = new TableColumn<>("K");
        kEst.setCellValueFactory(new PropertyValueFactory<>("TotalK"));
        eraEst = new TableColumn<>("ERA");
        eraEst.setCellValueFactory(new PropertyValueFactory<>("TotalERA"));
        whipEst = new TableColumn<>("WHIP");
        whipEst.setCellValueFactory(new PropertyValueFactory<>("TotalWHIP"));
        totalPoints = new TableColumn<>("Total Points");
        totalPoints.setCellValueFactory(new PropertyValueFactory<>("TotalPoints"));
        
        standingTable.getColumns().addAll(teamName, pNeeded, moneyLeft, moneyPP, rEst,
                hrEst, rbiEst, sbEst, baEst, wEst, svEst, kEst, eraEst, whipEst, totalPoints);
        
        standingTable = resetStandingTable(dataManager.getDraft().getFantasyTeams());
        
        // SET TOOLTIP
        Tooltip t = new Tooltip("Fantasy Standings Page");
        standingsTab.setTooltip(t);
        
        // PUT IN TAB PANE
        standingsWorkspacePane.getChildren().add(standingTable);
        standingsTab.setContent(standingsWorkspacePane);
        mainWorkspacePane.getTabs().add(standingsTab);
    }
    
    // INITIALIZES THE CONTROLS IN THE DRAFT TAB
    private void initDraftTab() throws IOException {
        // THESE ARE THE CONTROLS FOR THE BASIC SCHEDULE PAGE HEADER INFO
        // WE'LL ARRANGE THEM IN A VBox
        draftTab = new Tab();
        draftWorkspacePane = new VBox();
        draftWorkspacePane.getStyleClass().add(CLASS_DRAFT_PANE);
        
        // ADD PAGE LABEL & BUTTON
        Label draftLabel = new Label();
        draftLabel = initChildLabel(draftWorkspacePane, DraftKit_PropertyType.DRAFT_HEADING_LABEL, CLASS_HEADING_LABEL);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_IMAGES + props.getProperty(DraftKit_PropertyType.DRAFT_ICON);
        Image buttonImage = new Image(imagePath);
        draftTab.setGraphic(new ImageView(buttonImage));
        
        // ADD BUTTONS TO THE DRAFT PAGE
        HBox draftButtons = new HBox();
        
        pickPlayer = initHBoxButton(draftButtons, PICK_ICON, PICK_PLAYER_TOOLTIP, false);
        autoPickPlayer = initHBoxButton(draftButtons, PLAY_ICON, AUTO_DRAFT_TOOLTIP, false);
        autoPickPause = initHBoxButton(draftButtons, PAUSE_ICON, AUTO_PAUSE_TOOLTIP, false);
        
        pickPlayer.setMinHeight(10);
        autoPickPlayer.setMinHeight(34);
        autoPickPause.setMinHeight(34);
        pickPlayer.setMinWidth(8);
        autoPickPlayer.setMinWidth(38);
        autoPickPause.setMinWidth(38);
        
        ToolBar draftToolBar = new ToolBar(
                pickPlayer,
                autoPickPlayer,
                autoPickPause
        );
        
        // CREATE THE DRAFT TABLE
        draftTable = new TableView();

        draftPick = new TableColumn<>("Pick #");
        draftPick.setCellValueFactory(new Callback<CellDataFeatures<Player, Integer>, ObservableValue<Integer>>() {
          @Override
          public ObservableValue<Integer> call(CellDataFeatures<Player, Integer> p) {
            return new ReadOnlyObjectWrapper(draftTable.getItems().indexOf(p.getValue()) + 1);
          }
        });   
        draftPick.setSortable(false);
        draftFirst = new TableColumn<>("First");
        draftFirst.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        draftLast = new TableColumn<>("Last");
        draftLast.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        draftTeam = new TableColumn<>("Team");
        draftTeam.setCellValueFactory(new PropertyValueFactory<>("ChosenTeam"));
        draftContract = new TableColumn<>("Contract");
        draftContract.setCellValueFactory(new PropertyValueFactory<>("Contract"));
        draftSalary = new TableColumn<>("Salary ($)");
        draftSalary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        
        draftTable.getColumns().addAll(draftPick, draftFirst, draftLast, draftTeam, draftContract, draftSalary);
        
        // NEED TO CREATE THE WAY TO ADD THE TABLE (REFRESH IT)
        
        // SET TOOLTIP
        Tooltip t = new Tooltip("Draft Summary Page");
        draftTab.setTooltip(t);
        
        // PUT IN TAB PANE
        draftWorkspacePane.getChildren().add(draftToolBar);
        draftWorkspacePane.getChildren().add(draftTable);
        draftTab.setContent(draftWorkspacePane);
        mainWorkspacePane.getTabs().add(draftTab);
    }
    
    // INITIALIZES THE CONTROLS IN THE MLB TEAM TAB
    private void initMLBTeamTab() throws IOException {
        // THESE ARE THE CONTROLS FOR THE BASIC SCHEDULE PAGE HEADER INFO
        // WE'LL ARRANGE THEM IN A VBox
        mlbTeamTab = new Tab();
        mlbTeamWorkspacePane = new VBox();
        mlbTeamWorkspacePane.getStyleClass().add(CLASS_DRAFT_PANE);
        
        // ADD LABEL & BUTTON
        Label mlbTeams = new Label();
        mlbTeams = initChildLabel(mlbTeamWorkspacePane, DraftKit_PropertyType.MLB_HEADING_LABEL, CLASS_HEADING_LABEL);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_IMAGES + props.getProperty(DraftKit_PropertyType.MLB_ICON);
        Image buttonImage = new Image(imagePath);
        mlbTeamTab.setGraphic(new ImageView(buttonImage));
        
        // CREATE THE COMBOBOX
        HBox mlbTeamHBox = new HBox();
        initHBoxLabel(mlbTeamHBox, DraftKit_PropertyType.MLB_TEAM_LABEL, CLASS_PROMPT_LABEL);
        
        mlbTeamBox = new ComboBox();
        proTeams = new ArrayList<>();
        
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
        
        for (String s : proTeams) {
            mlbTeamBox.getItems().add(s);
        }
        
        mlbTeamBox.setValue("Choose a MLB Team");
        
        mlbTeamHBox.getChildren().add(mlbTeamBox);
        
        mlbTeamBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
                ArrayList<Player> tempTeam = new ArrayList<>();
                teamTable.getItems().clear();
                for (Player p : dataManager.getDraft().getPlayerPool()) {
                    if (p.getMLBTeam().equals(mlbTeamBox.getSelectionModel().getSelectedItem())) {
                        tempTeam.add(p);
                    }
                }
                teamTable = resetMLBTable(tempTeam);
            }
        });
        
        // CREATE THE TABLE VIEW FOR THE MLB TEAMS PAGE
        mlbTable = new TableView();
        
        mlbFirst = new TableColumn<>("First");
        mlbFirst.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        mlbFirst.setSortType(SortType.ASCENDING);
        mlbFirst.setSortable(true);
        mlbLast = new TableColumn<>("Last");
        mlbLast.setSortType(SortType.ASCENDING);
        mlbLast.setSortable(true);
        mlbLast.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        mlbPositions = new TableColumn<>("Positions");
        mlbPositions.setCellValueFactory(new PropertyValueFactory<>("Position"));
        
        mlbTable.getColumns().setAll(mlbFirst, mlbLast, mlbPositions);
        
        mlbTable = resetMLBTable(dataManager.getDraft().getPlayerPool());
        
        // SET TOOL TIP
        Tooltip t = new Tooltip("MLB Teams Page");
        mlbTeamTab.setTooltip(t);
        
        // PUT IN TAB PANE
        mlbTeamWorkspacePane.getChildren().add(mlbTeamHBox);
        mlbTeamWorkspacePane.getChildren().add(mlbTable);
        mlbTeamTab.setContent(mlbTeamWorkspacePane);
        mainWorkspacePane.getTabs().add(mlbTeamTab);
    }
    
    public void updateTeamTables() {
        // UPDATES THE TEAM TABLE IN REAL TIME
        ArrayList<Player> tempTeam = new ArrayList<>();
        ArrayList<Player> tempTaxi = new ArrayList<>();
        teamTable.getItems().clear();
        for (Team tt : dataManager.getDraft().getFantasyTeams()) {
            if (tt.getTeamName().equals(selectTeam.getSelectionModel().getSelectedItem())) {
                tempTeam = tt.getTeamList();
                tempTaxi = tt.getTaxiList();
            }
        }
        teamTable = resetTeamTable(tempTeam);
        teamTaxiTable = resetTaxiTable(tempTaxi);
        
        teamTable.getSortOrder().add(posCol);
        posCol.setComparator(positionCompare);
        posCol.setSortType(SortType.ASCENDING);
        posCol.setSortable(true);
        posCol.setSortable(false);
    }
    
    public TableView resetTable(ArrayList<Player> sortedPlayers) {
        playerList = FXCollections.observableArrayList(sortedPlayers);
        FilteredList<Player> filteredData = new FilteredList<>(playerList, p -> true);

        filteredData.setPredicate(player -> {
            if (searchText.getText() == null) {
                return true;
            }

            String filter = searchText.getText().toLowerCase();
                
            String fullName = player.getFirstName() + " " + player.getLastName();
                
            String nameFull = player.getLastName() + " " + player.getFirstName();

            if (fullName.toLowerCase().contains(filter)) {
                return true;
            } else if (nameFull.toLowerCase().contains(filter)) {
                return true;
            }
            return false;
        });
        
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(player -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String filter = newValue.toLowerCase();
                
                String fullName = player.getFirstName() + " " + player.getLastName();
                
                String nameFull = player.getLastName() + " " + player.getFirstName();

                if (fullName.toLowerCase().contains(filter)) {
                    return true;
                } else if (nameFull.toLowerCase().contains(filter)) {
                    return true;
                }
                return false;
            });
        });
 
        SortedList<Player> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(playerTable.comparatorProperty());

        playerTable.setItems(sortedData);
        
        return playerTable;
    }
    
    public TableView resetTeamTable(ArrayList<Player> teamPlayers) {
        teamList = FXCollections.observableArrayList(teamPlayers);
        teamTable.setItems(teamList);
        
        return teamTable;
    }
    
    public TableView resetTaxiTable(ArrayList<Player> teamPlayers) {
        teamTaxiList = FXCollections.observableArrayList(teamPlayers);
        teamTaxiTable.setItems(teamTaxiList);
        
        return teamTaxiTable;
    }
    
    public TableView resetStandingTable(ArrayList<Team> teams) {
        for (Team t : teams) {
            t.calculateStats();
        }
        
        dataManager.getDraft().calculateTotalPoints(teams);
        
        standingTable.getColumns().clear();
        standingTable.getColumns().addAll(teamName, pNeeded, moneyLeft, moneyPP, rEst,
                hrEst, rbiEst, sbEst, baEst, wEst, svEst, kEst, eraEst, whipEst, totalPoints);
        
        standingList = FXCollections.observableArrayList(teams);
        standingTable.setItems(standingList);
        
        return standingTable;
    }
    
    public TableView resetDraftTable(ArrayList<Player> players) {
        draftList = FXCollections.observableArrayList(players);
        draftTable.setItems(draftList);
        
        return draftTable;
    }
    
    public TableView resetMLBTable(ArrayList<Player> players) {
        mlbList = FXCollections.observableArrayList(players);
        mlbTable.setItems(mlbList);
        
        mlbLast.setSortType(SortType.ASCENDING);
        mlbLast.setSortable(true);
        mlbFirst.setSortType(SortType.ASCENDING);
        mlbFirst.setSortable(true);
        
        mlbTable.getSortOrder().clear();
        mlbTable.getSortOrder().add(mlbLast);
        mlbTable.getSortOrder().add(mlbFirst);
        
        mlbFirst.setSortable(false);
        mlbLast.setSortable(false);
        mlbPositions.setSortable(false);
        
        return mlbTable;
    }

    // INITIALIZE THE WINDOW (i.e. STAGE) PUTTING ALL THE CONTROLS
    // THERE EXCEPT THE WORKSPACE, WHICH WILL BE ADDED THE FIRST
    // TIME A NEW Course IS CREATED OR LOADED
    private void initWindow(String windowTitle) {
        // SET THE WINDOW TITLE
        primaryStage.setTitle(windowTitle);

        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        // AND USE IT TO SIZE THE WINDOW
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        // ADD THE TOOLBAR ONLY, NOTE THAT THE WORKSPACE
        // HAS BEEN CONSTRUCTED, BUT WON'T BE ADDED UNTIL
        // THE USER STARTS EDITING A COURSE
        draftPane = new BorderPane();
        draftPane.setTop(fileToolbarPane);
        primaryScene = new Scene(draftPane);

        // NOW TIE THE SCENE TO THE WINDOW, SELECT THE STYLESHEET
        // WE'LL USE TO STYLIZE OUR GUI CONTROLS, AND OPEN THE WINDOW
        primaryScene.getStylesheets().add(PRIMARY_STYLE_SHEET);
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }

    // INIT ALL THE EVENT HANDLERS
    private void initEventHandlers() throws IOException {
        // FIRST THE FILE CONTROLS
        fileController = new FileController(messageDialog, yesNoCancelDialog, draftFileManager, siteExporter);
        newDraftButton.setOnAction(e -> {
            fileController.handleNewDraftRequest(this);
        });
        loadDraftButton.setOnAction(e -> {
            fileController.handleLoadDraftRequest(this);
        });
        saveDraftButton.setOnAction(e -> {
            fileController.handleSaveDraftRequest(this, dataManager.getDraft());
        });
        exportDraftButton.setOnAction(e -> {
            fileController.handleExportDraftRequest(this);
        });
        exitButton.setOnAction(e -> {
            fileController.handleExitRequest(this);
        });
        
        // THEN THE ADD, EDIT, REMOVE TEAM CONTROLS
        teamController = new TeamEditController(primaryStage, dataManager.getDraft(), messageDialog, yesNoCancelDialog);
        
        draftNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            dataManager.getDraft().setDraftName(newValue);
        });
        
        addTeam.setOnAction(e -> {
            Team team = teamController.handleAddTeamRequest(this);
            if (dataManager.getDraft().getFantasyTeams() != null) {
                removeTeam.setDisable(false);
                editTeam.setDisable(false);
            }
            else {
                removeTeam.setDisable(true);
                editTeam.setDisable(true);
            }
            selectTeam.getItems().clear();
            for (int n = 0; n < dataManager.getDraft().getFantasyTeams().size(); n++) {
               selectTeam.getItems().add(dataManager.getDraft().getFantasyTeams().get(n).getTeamName());
            }
            selectTeam.setValue(team.getTeamName());
            teamTable = resetTeamTable(team.getTeamList());
            standingTable = resetStandingTable(dataManager.getDraft().getFantasyTeams());
        });
        
        editTeam.setOnAction(e -> {
            Team tempTeam = dataManager.getDraft().findTeam((String)selectTeam.getSelectionModel().getSelectedItem());
            teamController.handleEditTeamRequest(this, tempTeam);
            updateTeamBox(tempTeam);
        });
        
        removeTeam.setOnAction(e -> {
            Team tempTeam = dataManager.getDraft().findTeam((String)selectTeam.getSelectionModel().getSelectedItem());
            teamController.handleRemoveTeamRequest(this, tempTeam);
            tempTeam = new Team();
            updateTeamBox(tempTeam);
            playerTable = resetTable(dataManager.getDraft().getPlayerPool());
            draftTable = resetDraftTable(dataManager.getDraft().getDraftData());
        });
        
        teamTable.setOnMouseClicked((MouseEvent e) -> {
            Player player = null;
            if (e.getClickCount() == 2) {
                // OPEN UP THE PLAYER EDITOR
                Team tempTeam = dataManager.getDraft().findTeam((String)selectTeam.getSelectionModel().getSelectedItem());
                player = teamTable.getSelectionModel().getSelectedItem();
                playerController.handleEditPlayerRequest(this, player);
                playerTable = resetTable(dataManager.getDraft().getPlayerPool());
                this.updateTeamTables();
                teamTable = resetTeamTable(tempTeam.getTeamList());
                standingTable = resetStandingTable(dataManager.getDraft().getFantasyTeams());
                draftTable = resetDraftTable(dataManager.getDraft().getDraftData());
            }
        });
        
        teamTaxiTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                // OPEN UP THE PLAYER EDITOR
                Player player = teamTaxiTable.getSelectionModel().getSelectedItem();
                playerController.handleEditPlayerRequest(this, player);
                this.updateTeamTables();
                playerTable = resetTable(dataManager.getDraft().getPlayerPool());
                standingTable = resetStandingTable(dataManager.getDraft().getFantasyTeams());
                draftTable = resetDraftTable(dataManager.getDraft().getDraftData());
            }
        });
        
        // THEN THE ADD, EDIT, REMOVE PLAYER CONTROLS
        draftController = new DraftEditController();
        playerController = new PlayerEditController(primaryStage, dataManager.getDraft(), messageDialog, yesNoCancelDialog);
        
        addPlayer.setOnAction(e -> {
            playerController.handleAddPlayerRequest(this);
        });
        
        playerTable.setOnMouseClicked(e -> {
            Player player;
            if (e.getClickCount() == 2) {
                // OPEN UP THE PLAYER EDITOR
                player = playerTable.getSelectionModel().getSelectedItem();
                playerController.handleEditPlayerRequest(this, player);
                this.updateTeamTables();
                playerTable = resetTable(dataManager.getDraft().getPlayerPool());
                standingTable = resetStandingTable(dataManager.getDraft().getFantasyTeams());
                draftTable = resetDraftTable(dataManager.getDraft().getDraftData());
            }
        });
        
        removePlayer.setOnAction(e -> {
            playerController.handleRemovePlayerRequest(this, playerTable.getSelectionModel().getSelectedItem());
        });
        
        
        // THEN THE SORTING CONTROLS FOR THE PLAYER PAGE
        all.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("ALL"));
            setAllColumns();
        });
        c.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("C"));
            setHitterColumns();
        });
        oneB.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("1B"));
            setHitterColumns();
        });
        ci.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("CI"));
            setHitterColumns();
        });
        threeB.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("3B"));
            setHitterColumns();
        });
        twoB.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("2B"));
            setHitterColumns();
        });
        mi.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("MI"));
            setHitterColumns();
        });
        ss.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("SS"));
            setHitterColumns();
        });
        of.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPositions("OF"));
            setHitterColumns();
        });
        u.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getHitters());
            setHitterColumns();
        });
        p.setOnAction(e -> {
            playerTable = resetTable(dataManager.getDraft().getPitchers());
            setPitcherColumns();
        });
        
        // CONTROLS FOR THE DRAFT PAGE
        tempDraftList = dataManager.getDraft().getDraftData();
        pickPlayer.setOnAction(e -> {
            Player player = draftController.addPlayerToDraft(dataManager);
            if (player == null) {
                messageDialog.show("All teams in the draft are full.");
            } else {
                tempDraftList.add(player);
                draftTable = resetDraftTable(tempDraftList);
            }
            this.updateTeamTables();
            playerTable = resetTable(dataManager.getDraft().getPlayerPool());
            standingTable = resetStandingTable(dataManager.getDraft().getFantasyTeams());
        });
        
        autoPickPlayer.setOnAction(e -> {
            draftController.autoDraftPlayer(dataManager, this);
            this.updateTeamTables();
            standingTable = resetStandingTable(dataManager.getDraft().getFantasyTeams());
        });
        
        autoPickPause.setOnAction(e -> {
            draftController.autoDraftPause();
            this.updateTeamTables();
            standingTable = resetStandingTable(dataManager.getDraft().getFantasyTeams());
        });
        
    }
    
    private void setAllColumns() {
        rw.setText("R/W");
        hrsv.setText("HR/SV");
        rbik.setText("RBI/K");
        sbera.setText("SB/ERA");
        bawhip.setText("BA/WHIP");
    }
    
    private void setPitcherColumns() {
        rw.setText("W");
        hrsv.setText("SV");
        rbik.setText("K");
        sbera.setText("ERA");
        bawhip.setText("WHIP");
    }
    
    private void setHitterColumns() {
        rw.setText("R");
        hrsv.setText("HR");
        rbik.setText("RBI");
        sbera.setText("SB");
        bawhip.setText("BA");
    }
    
    private void updateTeamBox(Team name) {
        selectTeam.getItems().clear();
        for (int n = 0; n < dataManager.getDraft().getFantasyTeams().size(); n++) {
            selectTeam.getItems().add(dataManager.getDraft().getFantasyTeams().get(n).getTeamName());
        }
        if (name.getTeamName().equals(""))
            selectTeam.setValue("Choose a team");
        else
            selectTeam.setValue(name.getTeamName());
        
        if (dataManager.getDraft().getFantasyTeams().isEmpty()) {
            removeTeam.setDisable(true);
            editTeam.setDisable(true);
        }
        else {
            removeTeam.setDisable(false);
            editTeam.setDisable(false);
        }
    }

    // REGISTER THE EVENT LISTENER FOR A TEXT FIELD
    private void registerTextFieldController(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            //draftController.handleDraftChangeRequest(this);
        });
    }
    
    // INIT A BUTTON AND ADD IT TO A CONTAINER IN A TOOLBAR
    private Button initChildButton(Pane toolbar, DraftKit_PropertyType icon, DraftKit_PropertyType tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_IMAGES + props.getProperty(icon.toString());
        Image buttonImage = new Image(imagePath);
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip.toString()));
        button.setTooltip(buttonTooltip);
        toolbar.getChildren().add(button);
        return button;
    }
    
    // INIT A BUTTON AND ADD IT TO AN HBOX
    private Button initHBoxButton(HBox toolbar, DraftKit_PropertyType icon, DraftKit_PropertyType tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imagePath = "file:" + PATH_IMAGES + props.getProperty(icon.toString());
        Image buttonImage = new Image(imagePath);
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip.toString()));
        button.setTooltip(buttonTooltip);
        toolbar.getChildren().add(button);
        return button;
    }
    
    // INIT A LABEL AND SET IT'S STYLESHEET CLASS
    private Label initLabel(DraftKit_PropertyType labelProperty, String styleClass) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String labelText = props.getProperty(labelProperty);
        Label label = new Label(labelText);
        label.getStyleClass().add(styleClass);
        return label;
    }

    // INIT A LABEL AND PLACE IT IN A GridPane INIT ITS PROPER PLACE
    private Label initGridLabel(GridPane container, DraftKit_PropertyType labelProperty, String styleClass, int col, int row, int colSpan, int rowSpan) {
        Label label = initLabel(labelProperty, styleClass);
        container.add(label, col, row, colSpan, rowSpan);
        return label;
    }

    // INIT A LABEL AND PUT IT IN A TOOLBAR
    private Label initChildLabel(Pane container, DraftKit_PropertyType labelProperty, String styleClass) {
        Label label = initLabel(labelProperty, styleClass);
        container.getChildren().add(label);
        return label;
    }
    
    // INIT A LABEL AND PUT IT IN A HBox
    private Label initHBoxLabel(HBox container, DraftKit_PropertyType labelProperty, String styleClass) {
        Label label = initLabel(labelProperty, styleClass);
        container.getChildren().add(label);
        return label;
    }
    
    // INIT A LABEL AND PUT IT IN A VBox
    private Label initVBoxLabel(VBox container, DraftKit_PropertyType labelProperty, String styleClass) {
        Label label = initLabel(labelProperty, styleClass);
        container.getChildren().add(label);
        return label;
    }

    // INIT A TEXT FIELD AND PUT IT IN A Pane
    private TextField initTextField(Pane container, String initText, boolean editable) {
        TextField tf = new TextField();
        tf.setText(initText);
        tf.setEditable(editable);
        container.getChildren().add(tf);
        return tf;
    }
    
    // INIT A TEXT FIELD AND PUT IT IN A Pane
    private TextField initHBoxTextField(HBox container, String initText, boolean editable) {
        TextField tf = new TextField();
        tf.setText(initText);
        tf.setEditable(editable);
        container.getChildren().add(tf);
        return tf;
    }

    // INIT A TEXT FIELD AND PUT IT IN A GridPane
    private TextField initHBoxTextField(GridPane container, int size, String initText, boolean editable, int col, int row, int colSpan, int rowSpan) {
        TextField tf = new TextField();
        tf.setPrefColumnCount(size);
        tf.setText(initText);
        tf.setEditable(editable);
        container.add(tf, col, row, colSpan, rowSpan);
        return tf;
    }
    
}
