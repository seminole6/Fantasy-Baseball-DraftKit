package draftkit.data;

import java.util.ArrayList;

/**
 * The Team class creates the Team object for the
 * Draft Kit and holds the values of all the players
 * in each fantasy league team.
 * 
 * @author Devon Maguire
 */
public class Team {
    
    // PLAYERS PICKED TO BE ON THIS TEAM
    ArrayList<Player> teamList;
    ArrayList<Player> taxiList;
    
    // MANAGEMENT INFORMATION
    String teamName;
    String managerName;
    int playersNeeded;
    int moneyLeft;
    int moneyPP; // money per player
    
    // COUNTERS FOR POSITIONS
    int cCount = 2;
    int oneBCount = 1;
    int ciCount = 1;
    int threeBCount = 1;
    int twoBCount = 1;
    int miCount = 1;
    int ssCount = 1;
    int uCount = 1;
    int ofCount = 5;
    int pCount = 9;
    
    // TOTALS FOR ALL THE HITTERS ON A TEAM
    int totalR;
    int totalHR;
    int totalRBI;
    int totalSB;
    double totalBA;
    
    // TOTALS FOR ALL THE PITCHERS ON A TEAM
    int totalW;
    int totalK;
    int totalSV;
    double totalERA;
    double totalWHIP;
    
    // TOTAL POINTS FOR THIS TEAM
    int totalPoints;
    
    public Team() {
        // INITIALIZE ALL THE ITEMS IN A TEAM
        teamList = new ArrayList<Player>();
        taxiList = new ArrayList<Player>();
    
        this.teamName = "";
        this.managerName = "";
        playersNeeded = 31;
        moneyLeft = 260;
        moneyPP = 0; // money per player
    
        totalR = 0;
        totalHR = 0;
        totalRBI = 0;
        totalSB = 0;
        totalBA = 0.0;
    
        totalW = 0;
        totalK = 0;
        totalSV = 0;
        totalERA = 0.0;
        totalWHIP = 0.0;
    
        totalPoints = 1;
    }

    public int getMoneyLeft() {
        return moneyLeft;
    }

    public void setMoneyLeft(int moneyLeft) {
        this.moneyLeft = moneyLeft;
    }

    public int getMoneyPP() {
        if (playersNeeded != 0) {
            return (int) moneyLeft / playersNeeded;
        } else {
            return 0;
        }
    }

    public void setMoneyPP(int moneyPP) {
        if (playersNeeded != 0) {
            this.moneyPP = (int) moneyLeft / playersNeeded;
        } else {
            this.moneyPP = 0;
        }
    }

    public int getTotalR() {
        return totalR;
    }

    public void setTotalR(int totalR) {
        this.totalR = totalR;
    }

    public int getTotalHR() {
        return totalHR;
    }

    public void setTotalHR(int totalHR) {
        this.totalHR = totalHR;
    }

    public int getTotalRBI() {
        return totalRBI;
    }

    public void setTotalRBI(int totalRBI) {
        this.totalRBI = totalRBI;
    }

    public int getTotalSB() {
        return totalSB;
    }

    public void setTotalSB(int totalSB) {
        this.totalSB = totalSB;
    }

    public double getTotalBA() {
        return totalBA;
    }

    public void setTotalBA(double totalBA) {
        this.totalBA = totalBA;
    }

    public int getTotalW() {
        return totalW;
    }

    public void setTotalW(int totalW) {
        this.totalW = totalW;
    }

    public int getTotalK() {
        return totalK;
    }

    public void setTotalK(int totalK) {
        this.totalK = totalK;
    }

    public int getTotalSV() {
        return totalSV;
    }

    public void setTotalSV(int totalSV) {
        this.totalSV = totalSV;
    }

    public double getTotalERA() {
        return totalERA;
    }

    public void setTotalERA(double totalERA) {
        this.totalERA = totalERA;
    }

    public double getTotalWHIP() {
        return totalWHIP;
    }

    public void setTotalWHIP(double totalWHIP) {
        this.totalWHIP = totalWHIP;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public String getTeamName() {
        return this.teamName;
    }
    
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    public String getManagerName() {
        return this.managerName;
    }
    
    public void setPlayersNeeded(int playersNeeded) {
        this.playersNeeded = playersNeeded;
    }
    
    public int getPlayersNeeded() {
        return this.playersNeeded;
    }
    
    public void setTeamList(ArrayList<Player> teamList) {
        this.teamList = teamList;
    }
    
    public ArrayList<Player> getTeamList() {
        return this.teamList;
    }
    
    public void setTaxiList(ArrayList<Player> taxiList) {
        this.taxiList = taxiList;
    }
    
    public ArrayList<Player> getTaxiList() {
        return this.taxiList;
    }
    
    public void minusPlayer() {
        this.playersNeeded = this.playersNeeded - 1;
    }
    
    public void addedPlayer() {
        this.playersNeeded = this.playersNeeded + 1;
    }
    
    public void subtractSalary(Player player) {
        int sal = (int) player.getSalary();
        this.moneyLeft = this.moneyLeft - sal;
    }
    
    public void restoreSalary(Player player) {
        int sal = (int) player.getSalary();
        this.moneyLeft = this.moneyLeft + sal;
    }
    
    public void calculateStats() {
        // RESET ALL TOTALS
        moneyPP = 0; // money per player
    
        totalR = 0;
        totalHR = 0;
        totalRBI = 0;
        totalSB = 0;
        totalBA = 0.0;
    
        totalW = 0;
        totalK = 0;
        totalSV = 0;
        totalERA = 0.0;
        totalWHIP = 0.0;
        
        // CALCULATE ALL THE TOTAL AGAIN
        if (this.playersNeeded == 0) {
            this.moneyPP = 0;
        } else {
            this.moneyPP = this.moneyLeft / this.playersNeeded;
        }
        for (Player p : this.teamList) {
            if (p instanceof Hitter) {
                this.totalR += p.getRW();
                this.totalHR += p.getHRSV();
                this.totalRBI += p.getRBIK();
                this.totalSB += p.getSBERA();
            }
            else if (p instanceof Pitcher) {
                this.totalW += p.getRW();
                this.totalK += p.getRBIK();
                this.totalSV += p.getHRSV();
            }
        }
        int hits = 0;
        int atBats = 0;
        for (Player p : this.teamList) {
            if (p instanceof Hitter) {
                hits += p.getH();
                atBats += p.getAB();
            }
        }
        if (atBats != 0) {
            this.totalBA = Math.round(((double) hits / atBats)*1000.0)/1000.0; // H / AB
        }
        int c = 0;
        int tempTotal = 0;
        for (Player p : this.teamList) {
            if (p instanceof Pitcher) {
                tempTotal += p.getSBERA();
                c++;
            }
        }
        if (c != 0) {
            this.totalERA = Math.round(((double) tempTotal / c)*100.0)/100.0;
        }
        c = 0;
        tempTotal = 0;
        for (Player p : this.teamList) {
            if (p instanceof Pitcher) {
                tempTotal += p.getBAWHIP();
                c++;
            }
        }
        if (c != 0) {
            this.totalWHIP = Math.round(((double) tempTotal / c)*100.0)/100.0;
        }
    }
    
    public int emptyPosition(String position) {
        switch (position) {
            case "C": return cCount;
            case "1B": return oneBCount;
            case "CI": return ciCount;
            case "3B": return threeBCount;
            case "2B": return twoBCount;
            case "MI": return miCount;
            case "SS": return ssCount;
            case "U": return uCount;
            case "OF": return ofCount;
            case "P": return pCount;
            default: return -1;
        }
    }
    
    public void minusPosition(String position) {
        switch (position) {
            case "C": cCount--;
                break;
            case "1B": oneBCount--;
                break;
            case "CI": ciCount--;
                break;
            case "3B": threeBCount--;
                break;
            case "2B": twoBCount--;
                break;
            case "MI": miCount--;
                break;
            case "SS": ssCount--;
                break;
            case "U": uCount--;
                break;
            case "OF": ofCount--;
                break;
            case "P": pCount--;
                break;
            default:; // DO NOTHING
        }
    }
    
    public void restorePosition(String position) {
        switch (position) {
            case "C": cCount++;
            case "1B": oneBCount++;
            case "CI": ciCount++;
            case "3B": threeBCount++;
            case "2B": twoBCount++;
            case "MI": miCount++;
            case "SS": ssCount++;
            case "U": uCount++;
            case "OF": ofCount++;
            case "P": pCount++;
            default:; // DO NOTHING
        }
    }
    
    public int getpCount() {
        return pCount;
    }
    
}
