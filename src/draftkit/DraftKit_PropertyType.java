package draftkit;

/**
 * These are properties that are to be loaded from properties.xml. They
 * will provide custom labels and other UI details for our Draft Kit
 * application. The reason for doing this is to swap out UI text and icons
 * easily without having to touch our code. It also allows for language
 * independence.
 * 
 * @author Devon Maguire
 */
public enum DraftKit_PropertyType {
        // LOADED FROM properties.xml
        PROP_APP_TITLE,
        
        // APPLICATION ICONS
        NEW_DRAFT_ICON,
        LOAD_DRAFT_ICON,
        SAVE_DRAFT_ICON,
        EXPORT_DRAFT_ICON,
        DELETE_ICON,
        EXIT_ICON,
        ADD_ICON,
        MINUS_ICON,
        EDIT_ICON,
        MOVE_UP_ICON,
        MOVE_DOWN_ICON,
        TEAM_ICON,
        PLAYER_ICON,
        STANDINGS_ICON,
        DRAFT_ICON,
        MLB_ICON,
        PICK_ICON,
        PLAY_ICON,
        PAUSE_ICON,
        
        // APPLICATION TOOLTIPS FOR BUTTONS
        NEW_DRAFT_TOOLTIP,
        LOAD_DRAFT_TOOLTIP,
        SAVE_DRAFT_TOOLTIP,
        EXPORT_DRAFT_TOOLTIP,
        DELETE_TOOLTIP,
        EXIT_TOOLTIP,
        ADD_PLAYER_TOOLTIP,
        REMOVE_PLAYER_TOOLTIP,
        ADD_TEAM_TOOLTIP,
        REMOVE_TEAM_TOOLTIP,
        EDIT_TEAM_TOOLTIP,
        PICK_PLAYER_TOOLTIP,
        AUTO_DRAFT_TOOLTIP,
        AUTO_PAUSE_TOOLTIP,

        // FOR DRAFT EDIT WORKSPACE
        TEAMS_HEADING_LABEL,
        PLAYER_HEADING_LABEL,
        STANDINGS_HEADING_LABEL,
        DRAFT_HEADING_LABEL,
        MLB_HEADING_LABEL,
        SEARCH_LABEL,
        DRAFT_NAME_LABEL,
        TEAM_SELECT_LABEL,
        TEAM_TABLE_LABEL,
        TEAM_TAXI_LABEL,
        MLB_TEAM_LABEL,
        
        // ERROR DIALOG MESSAGES
        CLOSE_BUTTON_TEXT,
        
        // AND VERIFICATION MESSAGES
        NEW_DRAFT_CREATED_MESSAGE,
        DRAFT_LOADED_MESSAGE,
        DRAFT_SAVED_MESSAGE,
        DRAFT_EXPORTED_MESSAGE,
        SAVE_UNSAVED_WORK_MESSAGE,
        REMOVE_PLAYER_MESSAGE,
        REMOVE_TEAM_MESSAGE
}