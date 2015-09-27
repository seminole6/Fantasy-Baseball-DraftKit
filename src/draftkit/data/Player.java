package draftkit.data;

import java.util.ArrayList;

/**
 *
 * @author Devon
 */
public abstract class Player {
    
    // BASIC INFORMATION FOR EACH PLAYER
    String firstName;
    String lastName;
    String mlbTeam;
    String position;
    String chosenPosition;
    String chosenTeam;
    Contract contract;
    double salary;
    double estimatedValue;
    String notes;
    int yob;
    String nation;
    int rank;
    
    public Player() {
        // INITIALIZING ALL THE BASIC PLAYER VALUES
        this.firstName = "";
        this.lastName = "";
        this.mlbTeam = "";
        this.position = "";
        this.chosenPosition = "";
        this.chosenTeam = "";
        this.contract = null;
        this.salary = 0.0;
        this.estimatedValue = 0.0;
        this.notes = "";
        this.yob = 0;
        this.nation = "";
        this.rank = 0;
    }
    // NEED TO MAKE ACTUAL METHOD AND THEN LITTLE METHODS SHOULD TAKE PRECEDENCE
    public abstract void setFirstName(String firstName);
    public abstract String getFirstName();
    public abstract void setLastName(String lastName);
    public abstract String getLastName();
    public abstract void setMLBTeam(String mlbTeam);
    public abstract String getMLBTeam();
    public abstract void setPosition(String position);
    public abstract String getPosition();
    public abstract void setChosenPosition(String chosenPosition);
    public abstract String getChosenPosition();
    public abstract void setChosenTeam(String chosenTeam);
    public abstract String getChosenTeam();
    public abstract void setContract(Contract contract);
    public abstract Contract getContract();
    public abstract void setSalary(double salary);
    public abstract double getSalary();
    public abstract void setEValue(double estimatedValue);
    public abstract double getEValue();
    public abstract void setNotes(String notes);
    public abstract String getNotes();
    public abstract void setYearOfBirth(int yob);
    public abstract int getYearOfBirth();
    public abstract void setNation(String nob);
    public abstract String getNation();
    public abstract void setRW(int rw);
    public abstract int getRW();
    public abstract void setHRSV(int hrsv);
    public abstract int getHRSV();
    public abstract void setH(int h);
    public abstract int getH();
    public abstract void setRBIK(int rbik);
    public abstract int getRBIK();
    public abstract void setSBERA(double sbera);
    public abstract double getSBERA();
    public abstract void setBAWHIP(int h, int ab);
    public abstract double getBAWHIP();
    public abstract void setAB(int ab);
    public abstract int getAB();
    public abstract int getRank();
    public abstract void setRank(int rank);
}
