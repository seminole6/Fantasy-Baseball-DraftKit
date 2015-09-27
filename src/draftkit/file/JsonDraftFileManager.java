package draftkit.file;

import static draftkit.DraftKit_StartupConstants.PATH_DRAFTS;
import draftkit.data.Draft;
import draftkit.data.Hitter;
import draftkit.data.Pitcher;
import draftkit.data.Player;
import draftkit.data.Team;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonValue;

/**
 * This is a DraftFileManager that uses the JSON file format to 
 * implement the necessary functions for loading and saving different
 * data for our draft.
 * 
 * @author Devon Maguire
 */
public class JsonDraftFileManager implements DraftFileManager {
    // JSON FILE READING AND WRITING CONSTANTS
    String JSON_DRAFT = "Draft Name";
    String JSON_TEAMS = "Fantasy Teams";
    String JSON_PLAYER_POOL = "Player Pool";
    String JSON_HITTERS = "Hitters";
    String JSON_PITCHERS = "Pitchers";
    
    // VARIABLES FOR TEAM ARRAY
    String JSON_TEAM_NAME = "Team Name";
    String JSON_MANAGER = "Manager";
    String JSON_PLAYERS_NEEDED = "Players Needed";
    String JSON_TEAM_LIST = "Team Players";
    String JSON_TAXI_LIST = "Taxi Players";
    
    // VARIABLES FOR THE PLAYERS ARRAY
    String JSON_FIRST = "First Name";
    String JSON_LAST = "Last Name";
    String JSON_MLB = "MLB Team";
    String JSON_POSITIONS = "Positions";
    String JSON_CHOSEN_POSITION = "Chosen Position";
    String JSON_CHOSEN_TEAM = "Chosen Team";
    String JSON_CONTRACT = "Contract";
    String JSON_SALARY = "Salary";
    String JSON_EVALUE = "Estimated Value";
    String JSON_PLAYER_NOTES = "Notes";
    String JSON_YOB = "Year of Birth";
    String JSON_NATION = "Nation of Birth";
    
    String JSON_RW = "rw";
    String JSON_HRSV = "hrsv";
    String JSON_PLAYER_H = "h";
    String JSON_RBIK = "rbik";
    String JSON_SBERA = "sbera";
    String JSON_BAWHIP = "bawhip";
    
    // SHARED VARIABLES
    String JSON_TEAM = "TEAM";
    String JSON_LAST_NAME = "LAST_NAME";
    String JSON_FIRST_NAME = "FIRST_NAME";
    String JSON_H = "H";
    String JSON_NOTES = "NOTES";
    String JSON_YEAR_OF_BIRTH = "YEAR_OF_BIRTH";
    String JSON_NATION_OF_BIRTH = "NATION_OF_BIRTH";
    
    // HITTERS VARIABLES
    String JSON_QP = "QP";
    String JSON_AB = "AB";
    String JSON_R = "R";
    String JSON_HR = "HR";
    String JSON_RBI = "RBI";
    String JSON_SB = "SB";
    
    // PITCHERS VARIABLES
    String JSON_IP = "IP";
    String JSON_ER = "ER";
    String JSON_W = "W";
    String JSON_SV = "SV";
    String JSON_BB = "BB";
    String JSON_K = "K";
    
    // FILE STUFF VARIABLES
    String JSON_EXT = ".json";
    String SLASH = "/";
    
    // ARRAY LISTS FOR HITTERS AND PITCHERS
    ArrayList<Player> hitters;
    ArrayList<Player> pitchers;

    /**
     * This method saves all the data associated with a draft to
     * a JSON file.
     * 
     * @param draftToSave The draft whose data we are saving.
     * 
     * @throws IOException Thrown when there are issues writing
     * to the JSON file.
     */
    @Override
    public void saveDraft(Draft draftToSave) throws IOException {
        // BUILD THE FILE PATH
        String draftListing = "" + draftToSave.getDraftName();
        String jsonFilePath = PATH_DRAFTS + SLASH + draftListing + JSON_EXT;
        
        // INIT THE WRITER
        OutputStream os = new FileOutputStream(jsonFilePath);
        JsonWriter jsonWriter = Json.createWriter(os);
        
        // MAKE A JSON ARRAY FOR THE FANTASY TEAMS ARRAY
        JsonArray teamJsonArray = makeTeamJsonArray(draftToSave.getFantasyTeams());
        
        // AND ANOTHER ARRAY FOR THE PLAYERPOOL IN THE DRAFT
        JsonArray playerPoolJsonArray = makePlayerJsonArray(draftToSave.getPlayerPool());
        
        // NOW BUILD THE COURSE USING EVERYTHING WE'VE ALREADY MADE
        JsonObject draftJsonObject = Json.createObjectBuilder()
                                    .add(JSON_DRAFT, draftToSave.getDraftName())
                                    .add(JSON_TEAMS, teamJsonArray)
                                    .add(JSON_PLAYER_POOL, playerPoolJsonArray)
                .build();
        
        // AND SAVE EVERYTHING AT ONCE
        jsonWriter.writeObject(draftJsonObject);
    }
    
    /**
     * Loads the draftToLoad argument using the data found in the json file.
     * 
     * @param draftToLoad Draft to load.
     * @param jsonFilePath File containing the data to load.
     * 
     * @throws IOException Thrown when IO fails.
     */
    @Override
    public void loadDraft(Draft draftToLoad, String jsonFilePath) throws IOException {
        
    }
    
    /**
     * Loads the Hitters objects using the data found in the json file.
     * 
     * @param jsonFilePath File containing the data to load.
     * 
     * @throws IOException Thrown when IO fails.
     */
    @Override
    public ArrayList<Player> loadHitters(String jsonFilePath) throws IOException {
        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(jsonFilePath);
        
        hitters = new ArrayList<Player>();
        
        // GET EACH HITTERS INFORMATION
        JsonArray jsonHitterArray = json.getJsonArray(JSON_HITTERS);
        for (int i = 0; i < jsonHitterArray.size(); i++) {
            JsonObject jso = jsonHitterArray.getJsonObject(i);
            Hitter hitter = new Hitter();
            hitter.setMLBTeam(jso.getString(JSON_TEAM));
            hitter.setLastName(jso.getString(JSON_LAST_NAME));
            hitter.setFirstName(jso.getString(JSON_FIRST_NAME));
            hitter.setPosition(jso.getString(JSON_QP).concat("_U"));
            hitter.setAB(Integer.parseInt(jso.getString(JSON_AB)));
            hitter.setRW(Integer.parseInt(jso.getString(JSON_R)));
            hitter.setH(Integer.parseInt(jso.getString(JSON_H)));
            hitter.setHRSV(Integer.parseInt(jso.getString(JSON_HR)));
            hitter.setRBIK(Integer.parseInt(jso.getString(JSON_RBI)));
            hitter.setSBERA(Integer.parseInt(jso.getString(JSON_SB)));
            hitter.setNotes(jso.getString(JSON_NOTES));
            hitter.setYearOfBirth(Integer.parseInt(jso.getString(JSON_YEAR_OF_BIRTH)));
            hitter.setNation(jso.getString(JSON_NATION_OF_BIRTH));
            hitters.add(hitter);
            if (hitter.getPosition().contains("1B") || hitter.getPosition().contains("3B"))
                hitter.setPosition(hitter.getPosition().concat("_CI"));
            if (hitter.getPosition().contains("2B") || hitter.getPosition().contains("SS"))
                hitter.setPosition(hitter.getPosition().concat("_MI"));
        }
        return hitters;
    }

    /**
     * Loads the Pitchers objects using the data found in the json file.
     * 
     * @param jsonFilePath File containing the data to load.
     * 
     * @throws IOException Thrown when IO fails.
     */
    @Override
    public ArrayList<Player> loadPitchers(String jsonFilePath) throws IOException {
        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(jsonFilePath);
        
        pitchers = new ArrayList<Player>();
        
        // GET EACH HITTERS INFORMATION
        JsonArray jsonPitcherArray = json.getJsonArray(JSON_PITCHERS);
        for (int i = 0; i < jsonPitcherArray.size(); i++) {
            JsonObject jso = jsonPitcherArray.getJsonObject(i);
            Pitcher pitcher = new Pitcher();
            pitcher.setMLBTeam(jso.getString(JSON_TEAM));
            pitcher.setLastName(jso.getString(JSON_LAST_NAME));
            pitcher.setFirstName(jso.getString(JSON_FIRST_NAME));
            pitcher.setIP(Double.parseDouble(jso.getString(JSON_IP)));
            pitcher.setER(Integer.parseInt(jso.getString(JSON_ER)));
            pitcher.setRW(Integer.parseInt(jso.getString(JSON_W)));
            pitcher.setHRSV(Integer.parseInt(jso.getString(JSON_SV)));
            pitcher.setH(Integer.parseInt(jso.getString(JSON_H)));
            pitcher.setBB(Integer.parseInt(jso.getString(JSON_BB)));
            pitcher.setRBIK(Integer.parseInt(jso.getString(JSON_K)));
            pitcher.setNotes(jso.getString(JSON_NOTES));
            pitcher.setYearOfBirth(Integer.parseInt(jso.getString(JSON_YEAR_OF_BIRTH)));
            pitcher.setNation(jso.getString(JSON_NATION_OF_BIRTH));
            pitchers.add(pitcher);
        }
        
        return pitchers;
    }
    
    // AND HERE ARE THE PRIVATE HELPER METHODS TO HELP THE PUBLIC ONES
    
    // LOADS A JSON FILE AS A SINGLE OBJECT AND RETURNS IT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }    
    
    // LOADS AN ARRAY OF A SPECIFIC NAME FROM A JSON FILE AND
    // RETURNS IT AS AN ArrayList FULL OF THE DATA FOUND
    private ArrayList<String> loadArrayFromJSONFile(String jsonFilePath, String arrayName) throws IOException {
        JsonObject json = loadJSONFile(jsonFilePath);
        ArrayList<String> items = new ArrayList();
        JsonArray jsonArray = json.getJsonArray(arrayName);
        for (JsonValue jsV : jsonArray) {
            items.add(jsV.toString());
        }
        return items;
    }

    // BUILDS AND RETURNS A JsonArray CONTAINING THE PROVIDED DATA
    public JsonArray buildJsonArray(List<Object> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Object d : data) {
           jsb.add(d.toString());
        }
        JsonArray jA = jsb.build();
        return jA;
    }

    // BUILDS AND RETURNS A JsonObject CONTAINING A JsonArray
    // THAT CONTAINS THE PROVIDED DATA
    public JsonObject buildJsonArrayObject(List<Object> data) {
        JsonArray jA = buildJsonArray(data);
        JsonObject arrayObject = Json.createObjectBuilder().add(JSON_HITTERS, jA).build();
        return arrayObject;
    }
    
    // BUILDS AND RETURNS A JsonArray CONTAINING ALL THE TEAMS FOR THIS DRAFT
    public JsonArray makeTeamJsonArray(ArrayList<Team> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Team cP : data) {
           jsb.add(makeTeamJsonObject(cP));
        }
        JsonArray jA = jsb.build();
        return jA;
    }
    
    // BUILDS AND RETURNS A JsonArray CONTAINING ALL THE PLAYERS FOR THIS DRAFT
    public JsonArray makePlayerJsonArray(ArrayList<Player> data) {
        JsonArrayBuilder jsb = Json.createArrayBuilder();
        for (Player cP : data) {
            JsonObject jso = Json.createObjectBuilder().add(JSON_FIRST, cP.getFirstName())
                                                       .add(JSON_LAST, cP.getLastName())
                                                       .add(JSON_MLB, cP.getMLBTeam())
                                                       .add(JSON_POSITIONS, cP.getPosition())
                                                       .add(JSON_CHOSEN_POSITION, cP.getChosenPosition())
                                                       .add(JSON_CHOSEN_TEAM, cP.getChosenTeam())
                                                       //.add(JSON_CONTRACT, cP.getContract().toString())
                                                       .add(JSON_SALARY, cP.getSalary())
                                                       .add(JSON_EVALUE, cP.getEValue())
                                                       .add(JSON_PLAYER_NOTES, cP.getNotes())
                                                       .add(JSON_YOB, cP.getYearOfBirth())
                                                       .add(JSON_NATION, cP.getNation())
                                                       .add(JSON_RW, cP.getRW())
                                                       .add(JSON_HRSV, cP.getHRSV())
                                                       .add(JSON_PLAYER_H, cP.getH())
                                                       .add(JSON_RBIK, cP.getRBIK())
                                                       .add(JSON_SBERA, cP.getSBERA())
                                                       .add(JSON_BAWHIP, cP.getBAWHIP())
                    .build();
           jsb.add(jso);
        }
        JsonArray jA = jsb.build();
        return jA;
    }
    
    // MAKES AND RETURNS A JSON OBJECT FOR THE PROVIDED INSTRUCTOR
    private JsonObject makeTeamJsonObject(Team team) {
        JsonArray jTeam = makePlayerJsonArray(team.getTeamList());
        JsonArray jTaxi = makePlayerJsonArray(team.getTaxiList());
        JsonObject jso = Json.createObjectBuilder().add(JSON_TEAM_NAME, team.getTeamName())
                                                   .add(JSON_MANAGER, team.getManagerName())
                                                   .add(JSON_PLAYERS_NEEDED, team.getPlayersNeeded())
                                                   .add(JSON_TEAM_LIST, jTeam)
                                                   .add(JSON_TAXI_LIST, jTaxi)
                                    .build();
        return jso;
    }
}
// LOAD EVERYTHING INTO A JSON ARRAY -- create a method for loading the json array, load json from file path
// take array from json object you just made, make a new arraylist and then continuously read