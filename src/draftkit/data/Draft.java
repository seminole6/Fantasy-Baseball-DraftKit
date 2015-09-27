package draftkit.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import javafx.collections.ObservableList;

/**
 * The Draft class creates the Draft object for the
 * Draft Kit and holds the values of all the players
 * and all the teams involved in the draft.
 * 
 * @author Devon Maguire
 */
public class Draft {
    
    // BASIC VALUES FOR EACH DRAFT
    String draftName;
    ArrayList<Player> draftData;
    ArrayList<Team> fantasyTeams;
    ArrayList<Player> playerPool;
    ArrayList<Player> sortedPlayerPool;
    ArrayList<Player> hitters;
    ArrayList<Player> pitchers;
    
    public Draft() {
        draftName = "";
        draftData = new ArrayList<>();
        fantasyTeams = new ArrayList<>();
        playerPool = new ArrayList<>();
        sortedPlayerPool = new ArrayList<>();
        hitters = new ArrayList<>();
        pitchers = new ArrayList<>();
    }
    
    // create constructor that accepts hitters and pitchers and makes a giant pool of players
    public void setDraftName(String draftName) {
        this.draftName = draftName;
    }
    
    public String getDraftName() {
        return this.draftName;
    }
    
    public ArrayList<Player> getDraftData() {
        return draftData;
    }
    
    public void removeFromDraft(Player player) {
        this.draftData.remove(player);
    }
    
    public void setFantasyTeams(ArrayList<Team> teams) {
        this.fantasyTeams = teams;
    }
    
    public ArrayList<Team> getFantasyTeams() {
        return fantasyTeams;
    }
    
    public void addTeam(Team team) {
        this.fantasyTeams.add(team);
    }
    
    public void setPlayerPool(ArrayList<Player> playerPool) {
        this.playerPool = playerPool;
    }
    
    public ArrayList<Player> getPlayerPool() {
        return playerPool;
    }
    
    public void setSortedPlayerPool(ArrayList<Player> sortedPlayerPool) {
        this.sortedPlayerPool = sortedPlayerPool;
    }
    
    public ArrayList<Player> getSortedPlayerPool() {
        return sortedPlayerPool;
    }
    
    public void setHitters(ArrayList<Player> hitters) {
        this.hitters = hitters;
    }
    
    public ArrayList<Player> getHitters() {
        return hitters;
    }
    
    public void setPitchers(ArrayList<Player> pitchers) {
        this.pitchers = pitchers;
    }
    
    public ArrayList<Player> getPitchers() {
        return pitchers;
    }
    
    public ArrayList<Player> getPositions(String selectedRadio) {
        ArrayList<Player> sorted = new ArrayList<Player>();
        if (selectedRadio.equals("ALL")) {
            sorted = playerPool;
        }
        else {
            for (int n = 0; n < playerPool.size(); n++) {
                ArrayList<String> list = new ArrayList<String>();
                String [] pos =  playerPool.get(n).getPosition().split("_");
                for (String e : pos) {
                    list.add(e);
                }
                if (selectedRadio.equals("MI")) {
                    if (list.contains("2B") || list.contains("SS"))
                        sorted.add(playerPool.get(n));
                }
                else if (selectedRadio.equals("CI")) {
                    if (list.contains("1B") || list.contains("3B"))
                        sorted.add(playerPool.get(n));
                }
                else if (list.contains(selectedRadio)) {
                    sorted.add(playerPool.get(n));
                }
            }
        }
        return sorted;
    }
    
    // CALC METHOD FOR TEAMS
    public void calculateTotalPoints(ArrayList<Team> teams) {
        // make comparators for EVERYTHING MUAHAHAHAHAHAHAHAHA *cough* *cough* no seriously
        //      :P have fun!
        
        for (Team t : teams) {
            t.setTotalPoints(1);
        }
        
        this.sortR(teams);
        this.countPoints(teams);
        
        this.sortHR(teams);
        this.countPoints(teams);
        
        this.sortRBI(teams);
        this.countPoints(teams);
        
        this.sortSB(teams);
        this.countPoints(teams);
        
        this.sortBA(teams);
        this.countPoints(teams);
        
        this.sortW(teams);
        this.countPoints(teams);
        
        this.sortSV(teams);
        this.countPoints(teams);
        
        this.sortK(teams);
        this.countPoints(teams);
        
        this.sortERA(teams);
        this.countPoints(teams);
        
        this.sortWHIP(teams);
        this.countPoints(teams);
    }
    
    // COUNT METHOD FOR TEAMS
    public void countPoints(ArrayList<Team> teams) {
        int counter = 0;
        for (Team t : teams) {
            int n = t.getTotalPoints();
            t.setTotalPoints(n + counter);
            counter++;
        }
    }
    
    // COMPARE METHODS FOR TEAMS
    public ArrayList<Team> sortR(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalR() > t2.getTotalR())
                    return 1;
                else if (Objects.equals(t1.getTotalR(), t2.getTotalR()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortHR(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalHR() > t2.getTotalHR())
                    return 1;
                else if (Objects.equals(t1.getTotalHR(), t2.getTotalHR()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortRBI(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalRBI() > t2.getTotalRBI())
                    return 1;
                else if (Objects.equals(t1.getTotalRBI(), t2.getTotalRBI()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortSB(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalSB() > t2.getTotalSB())
                    return 1;
                else if (Objects.equals(t1.getTotalSB(), t2.getTotalSB()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortBA(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalBA() > t2.getTotalBA())
                    return 1;
                else if (Objects.equals(t1.getTotalBA(), t2.getTotalBA()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortW(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalW() > t2.getTotalW())
                    return 1;
                else if (Objects.equals(t1.getTotalW(), t2.getTotalW()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortSV(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalSV() > t2.getTotalSV())
                    return 1;
                else if (Objects.equals(t1.getTotalSV(), t2.getTotalSV()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortK(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalK() > t2.getTotalK())
                    return 1;
                else if (Objects.equals(t1.getTotalK(), t2.getTotalK()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortERA(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalERA() > t2.getTotalERA())
                    return 1;
                else if (Objects.equals(t1.getTotalERA(), t2.getTotalERA()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public ArrayList<Team> sortWHIP(ArrayList<Team> teams) {
        Comparator<Team> compare = new Comparator<Team>() {
            @Override
            public int compare(Team t1, Team t2) {
                if (t1.getTotalWHIP() > t2.getTotalWHIP())
                    return 1;
                else if (Objects.equals(t1.getTotalWHIP(), t2.getTotalWHIP()))
                    return 0;
                else
                    return -1;
            }
        };
        
        teams.sort(compare);
        
        return teams;
    }
    
    public void removePlayer(Player playerToRemove) {
        if (hitters.contains(playerToRemove))
            hitters.remove(playerToRemove);
        if (pitchers.contains(playerToRemove))
            pitchers.remove(playerToRemove);
        playerPool.remove(playerToRemove);
        sortedPlayerPool.remove(playerToRemove);
    }
    
    public void removePlayerFromTeam (Team teamToEdit, Player playerToRemove) {
        if (teamToEdit.getTeamList().contains(playerToRemove))
            teamToEdit.getTeamList().remove(playerToRemove);
    }
    
    public void removeTeam(Team teamToRemove) {
        if (fantasyTeams.contains(teamToRemove))
            fantasyTeams.remove(teamToRemove);
    }
    
    public Team findTeam(String teamToFind) {
        for (Team t : fantasyTeams)
            if (t.getTeamName().equals(teamToFind))
                return t;
        return null;
    }
    
    public boolean teamsFull() {
        int full = 0;
        for (int n = 0; n < fantasyTeams.size(); n++) {
            if (fantasyTeams.get(n).getPlayersNeeded() > 8) {
                full++;
            }
        }
        if (full > 0) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean teamsComplete() {
        int full = 0;
        for (Team t : fantasyTeams) {
            if (t.getPlayersNeeded() == 0) {
                full++;
            }
        }
        if (full > 0) {
            return true;
        } else {
            return false;
        }
    }
    
    public int totalMoneyLeft() {
        int totalMoney = 0;
        for (Team t : fantasyTeams) {
            if (t.getPlayersNeeded() > 0) {
                totalMoney += t.getMoneyLeft();
            }
        }
        return totalMoney;
    }
    
    // CALCULATE ESTIMATED VALUE OF EVERY PLAYER LEFT IN THE PLAYERPOOL
    public void calcEValue() {
        // FOR HITTERS
        sortPR(playerPool);
        countPlayerPoints(playerPool);
        sortPHR(playerPool);
        countPlayerPoints(playerPool);
        sortPRBI(playerPool);
        countPlayerPoints(playerPool);
        sortPSB(playerPool);
        countPlayerPoints(playerPool);
        sortPBA(playerPool);
        countPlayerPoints(playerPool);
        
        // FOR PITCHERS
        sortPW(playerPool);
        countPlayerPoints(playerPool);
        sortPSV(playerPool);
        countPlayerPoints(playerPool);
        sortPK(playerPool);
        countPlayerPoints(playerPool);
        sortPERA(playerPool);
        countPlayerPoints(playerPool);
        sortPWHIP(playerPool);
        countPlayerPoints(playerPool);
        
        int totalM = totalMoneyLeft();
        
        int thn = totalHitters();
        int tpn = totalPitchers();
        
        int medianSalaryH = 0;
         int medianSalaryP = 0;
        
        if (thn != 0 && tpn !=0) {
            medianSalaryH = totalM/ (2*thn);
            medianSalaryP = totalM/ (2*tpn);
        }
        
        for (Player p : playerPool) {
            if (p.getRank() != 0) {
                if (p instanceof Hitter) {
                    p.setSalary((int)(medianSalaryH * ((thn * 2)/p.getRank())));
                } else if (p instanceof Pitcher) {
                    p.setSalary((int)(medianSalaryP * ((tpn * 2)/p.getRank())));
                }
            }
        }
        
    }
    
    public void countPlayerPoints(ArrayList<Player> players) {
        int counter = 0;
        for (Player p : players) {
            if (p instanceof Hitter) {
                int n = p.getRank();
                p.setRank(n + counter);
                counter++;
            } else if (p instanceof Pitcher) {
                int n = p.getRank();
                p.setRank(n + counter/2);
                counter++;
            }
        }
    }
    
    public int totalHitters() {
        int r = 0;
        for (Team t : fantasyTeams) {
            r += (t.getPlayersNeeded() - t.getpCount());
        }
        return r;
    }
    
    public int totalPitchers() {
        int r = 0;
        for (Team t : fantasyTeams) {
            r += t.getpCount();
        }
        return r;
    }
    
    // COMPARE METHODS FOR HITTING PLAYERS
    public ArrayList<Player> sortPR(ArrayList<Player> hitters) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getRW() > p2.getRW())
                    return 1;
                else if (Objects.equals(p1.getRW(), p2.getRW()))
                    return 0;
                else
                    return -1;
            }
        };
        
        hitters.sort(compare);
        
        return hitters;
    }
    
    public ArrayList<Player> sortPHR(ArrayList<Player> hitters) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getHRSV() > p2.getHRSV())
                    return 1;
                else if (Objects.equals(p1.getHRSV(), p2.getHRSV()))
                    return 0;
                else
                    return -1;
            }
        };
        
        hitters.sort(compare);
        
        return hitters;
    }
    
    public ArrayList<Player> sortPRBI(ArrayList<Player> hitters) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getRBIK() > p2.getRBIK())
                    return 1;
                else if (Objects.equals(p1.getRBIK(), p2.getRBIK()))
                    return 0;
                else
                    return -1;
            }
        };
        
        hitters.sort(compare);
        
        return hitters;
    }
    
    public ArrayList<Player> sortPSB(ArrayList<Player> hitters) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getSBERA() > p2.getSBERA())
                    return 1;
                else if (Objects.equals(p1.getSBERA(), p2.getSBERA()))
                    return 0;
                else
                    return -1;
            }
        };
        
        hitters.sort(compare);
        
        return hitters;
    }
    
    public ArrayList<Player> sortPBA(ArrayList<Player> hitters) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getBAWHIP() > p2.getBAWHIP())
                    return 1;
                else if (Objects.equals(p1.getBAWHIP(), p2.getBAWHIP()))
                    return 0;
                else
                    return -1;
            }
        };
        
        hitters.sort(compare);
        
        return hitters;
    }
    
    // COMPARE METHODS FOR PITCHING PLAYERS
    public ArrayList<Player> sortPW(ArrayList<Player> pitchers) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getRW() > p2.getRW())
                    return 1;
                else if (Objects.equals(p1.getRW(), p2.getRW()))
                    return 0;
                else
                    return -1;
            }
        };
        
        pitchers.sort(compare);
        
        return pitchers;
    }
    
    public ArrayList<Player> sortPSV(ArrayList<Player> pitchers) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getHRSV() > p2.getHRSV())
                    return 1;
                else if (Objects.equals(p1.getHRSV(), p2.getHRSV()))
                    return 0;
                else
                    return -1;
            }
        };
        
        pitchers.sort(compare);
        
        return pitchers;
    }
    
    public ArrayList<Player> sortPK(ArrayList<Player> pitchers) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getRBIK() > p2.getRBIK())
                    return 1;
                else if (Objects.equals(p1.getRBIK(), p2.getRBIK()))
                    return 0;
                else
                    return -1;
            }
        };
        
        pitchers.sort(compare);
        
        return pitchers;
    }
    
    public ArrayList<Player> sortPERA(ArrayList<Player> pitchers) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getSBERA() > p2.getSBERA())
                    return 1;
                else if (Objects.equals(p1.getSBERA(), p2.getSBERA()))
                    return 0;
                else
                    return -1;
            }
        };
        
        pitchers.sort(compare);
        
        return pitchers;
    }
    
    public ArrayList<Player> sortPWHIP(ArrayList<Player> pitchers) {
        Comparator<Player> compare = new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                if (p1.getBAWHIP() > p2.getBAWHIP())
                    return 1;
                else if (Objects.equals(p1.getBAWHIP(), p2.getBAWHIP()))
                    return 0;
                else
                    return -1;
            }
        };
        
        pitchers.sort(compare);
        
        return pitchers;
    }
    
}
