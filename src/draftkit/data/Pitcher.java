package draftkit.data;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Devon Maguire
 */
public class Pitcher extends Player {
    private double ip;
    private int er;
    private int rw;
    private int hrsv;
    private int h;
    private int bb;
    private int rbik;
    private double sbera;
    private double bawhip;

    public Pitcher() {
        firstName = "";
        lastName = "";
        mlbTeam = "";
        position = "P";
        chosenPosition = "";
        chosenTeam = "";
        contract = null;
        salary = 0.0;
        notes = "";
        yob = 0;
        nation = "";
        ip = 0.0;
        er = 0;
        rw = 0;
        hrsv = 0;
        h = 0;
        bb = 0;
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
    
    public void setIP(double ip) {
        this.ip = ip;
    }
    
    public double getIP() {
        return ip;
    }
    
    public void setER(int er) {
        this.er = er;
    }
    
    public int getER() {
        return er;
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
    
    public void setBB(int bb) {
        this.bb = bb;
    }
    
    public int getBB() {
        return bb;
    }
    
    @Override
    public void setRBIK(int rbik) {
        this.rbik = rbik;
    }
    
    @Override
    public int getRBIK() {
        return rbik;
    }
    
    public void setSBERA(int er, double ip) {
        if (ip == 0)
            this.sbera = 0;
        else
            this.sbera = (er * 9)/ip;
    }
    
    @Override
    public double getSBERA() {
        if (ip == 0)
            return this.sbera = 0;
        else
            return Math.round((this.sbera = (er * 9)/ip)*100.0)/100.0;
    }
    
    public void setBAWHIP(int rw, int h) {
        if (ip == 0)
            this.bawhip = 0;
        else
            this.bawhip = (rw + h)/this.ip;
    }
    
    @Override
    public double getBAWHIP() {
        if (ip == 0)
            return this.bawhip = 0;
        else
            return Math.round((this.bawhip = (rw + h)/ip)*100.0)/100.0;
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
        super.estimatedValue = estimatedValue;}

    @Override
    public double getEValue() {
        return super.estimatedValue;
    }

    @Override
    public void setSBERA(double sbera) {
        this.sbera = sbera;
    }

    @Override
    public void setAB(int ab) {
        // not implemented for pitchers
    }

    @Override
    public int getAB() {
        return 0;
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
