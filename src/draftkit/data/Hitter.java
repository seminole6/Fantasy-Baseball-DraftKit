package draftkit.data;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Devon Maguire
 */
public class Hitter extends Player {
    private int ab;
    private int rw;
    private int hrsv;
    private int h;
    private int rbik;
    private double sbera;
    private double bawhip;

    public Hitter() {
        firstName = "";
        lastName = "";
        mlbTeam = "";
        position = "";
        chosenPosition = "";
        chosenTeam = "";
        contract = null;
        salary = 0.0;
        notes = "";
        yob = 0;
        nation = "";
        ab = 0;
        rw = 0;
        hrsv = 0;
        h = 0;
        rbik = 0;
        sbera = 0.0;
        bawhip = 0.0;
    }

    @Override
    public void setFirstName(String firstName) {
        super.firstName = firstName;
    }

    @Override
    public String getFirstName() {
        return super.firstName;
    }

    @Override
    public void setLastName(String lastName) {
        super.lastName = lastName;
    }

    @Override
    public String getLastName() {
        return super.lastName;
    }

    @Override
    public void setMLBTeam(String mlbTeam) {
        super.mlbTeam = mlbTeam;
    }

    @Override
    public String getMLBTeam() {
        return super.mlbTeam;
    }

    @Override
    public void setPosition(String position) {
        super.position = position;
    }

    @Override
    public String getPosition() {
        return super.position;
    }

    @Override
    public void setContract(Contract contract) {
        super.contract = contract;
    }

    @Override
    public Contract getContract() {
        return super.contract;
    }

    @Override
    public void setSalary(double salary) {
        super.salary = salary;
    }

    @Override
    public double getSalary() {
        return super.salary;
    }

    @Override
    public void setNotes(String notes) {
        super.notes = notes;
    }

    @Override
    public String getNotes() {
        return super.notes;
    }

    @Override
    public void setYearOfBirth(int yob) {
        super.yob = yob;
    }

    @Override
    public int getYearOfBirth() {
        return super.yob;
    }

    @Override
    public void setNation(String nob) {
        super.nation = nob;
    }

    @Override
    public String getNation() {
        return super.nation;
    }
    
    @Override
    public void setAB(int ab) {
        this.ab = ab;
    }
    
    @Override
    public int getAB() {
        return ab;
    }
    
    @Override
    public void setRW(int rw) {
        this.rw = rw;
    }
    
    @Override
    public int getRW() {
        return rw;
    }
    
    @Override
    public void setHRSV(int hrsv) {
        this.hrsv = hrsv;
    }
    
    @Override
    public int getHRSV() {
        return hrsv;
    }
    
    @Override
    public void setH(int h) {
        this.h = h;
    }
    
    @Override
    public int getH() {
        return h;
    }
    
    @Override
    public void setRBIK(int rbik) {
        this.rbik = rbik;
    }
    
    @Override
    public int getRBIK() {
        return rbik;
    }
    
    @Override
    public void setSBERA(double sbera) {
        this.sbera = sbera;
    }
    
    @Override
    public double getSBERA() {
        return sbera;
    }
    
    @Override
    public void setBAWHIP(int h, int ab) {
        if (ab == 0)
            this.bawhip = 0;
        else
            this.bawhip = h/ab;
    }
    
    @Override
    public double getBAWHIP() {
        if (ab == 0)
            return 0;
        else
            return (double) Math.round(((double)h/ab)*1000)/1000;
    }

    @Override
    public void setChosenPosition(String chosenPosition) {
        super.chosenPosition = chosenPosition;
    }

    @Override
    public String getChosenPosition() {
        return super.chosenPosition;
    }

    @Override
    public void setChosenTeam(String chosenTeam) {
        super.chosenTeam = chosenTeam;
    }

    @Override
    public String getChosenTeam() {
        return super.chosenTeam;
    }

    @Override
    public void setEValue(double estimatedValue) {
        super.estimatedValue = estimatedValue;
    }

    @Override
    public double getEValue() {
        return super.estimatedValue;
    }

    @Override
    public int getRank() {
        return super.rank;
    }

    @Override
    public void setRank(int rank) {
        super.rank = rank;
    }

}
