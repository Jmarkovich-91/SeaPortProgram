/*
* File Name: World.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;
import javax.swing.tree.*;
import javax.swing.*;

public class World extends Thing {

    ArrayList<SeaPort> ports;
    ArrayList<Thing> nameResults;
    ArrayList<Thing> skillResults;
    ArrayList<Thing> lengthResults;
    PortTime time;

    //Constructor, breaks file into single lines to process
    public World(Scanner sc, HashMap<Integer, SeaPort> hmp, HashMap<Integer, Dock> hmd,
            HashMap<Integer, Ship> hms, JPanel jp, JPanel jr) {
        super(sc);
        ports = new ArrayList<SeaPort>();
        String currentLine;
        while (sc.hasNext()) {
            currentLine = sc.nextLine().trim();
            if (currentLine.length() == 0 || currentLine.charAt(0) == '/') {
                continue;
            }
            process(currentLine, hmp, hmd, hms, jp, jr);
        }
    }

    //Method to process file line by line, and create objects based on type
    private void process(String s, HashMap<Integer, SeaPort> hmp, HashMap<Integer, Dock> hmd,
            HashMap<Integer, Ship> hms, JPanel jp, JPanel jr) {
        Scanner sc = new Scanner(s).useDelimiter("\\s+");
        while (sc.hasNext()) {
            switch (sc.next()) {
                case "port":
                    addPort(sc, hmp, jr);
                    break;
                case "dock":
                    addDock(sc, hmd, hmp);
                    break;
                case "ship":
                    addShip(sc, hmd, hmp, hms);
                    break;
                case "cship":
                    addCShip(sc, hmd, hmp, hms);
                    break;
                case "pship":
                    addPShip(sc, hmd, hmp, hms);
                    break;
                case "person":
                    addPerson(sc, hmp);
                    break;
                case "job":
                    addJob(sc, hms, hmd, hmp, jp);
                    break;
            }
        }
    }

    //Creates new port, adds to arraylist
    private void addPort(Scanner sc, HashMap<Integer, SeaPort> hmp, JPanel jr) {
        SeaPort sp = new SeaPort(sc, jr);
        ports.add(sp);
        hmp.put(sp.index, sp);
    }

    //Creates new dock, assigns to parent port
    private void addDock(Scanner sc, HashMap<Integer, Dock> hmd, HashMap<Integer, SeaPort> hmp) {
        Dock d = new Dock(sc);
        hmd.put(d.index, d);
        hmp.get(d.parent).docks.add(d);
    }

    //Creates new ship, assigns to parent port
    private void addShip(Scanner sc, HashMap<Integer, Dock> hmd, HashMap<Integer, SeaPort> hmp,
            HashMap<Integer, Ship> hms) {
        Ship s = new Ship(sc);
        hms.put(s.index, s);
        Dock md = hmd.get(s.parent);
        if (md == null) {
            hmp.get(s.parent).ships.add(s);
            hmp.get(s.parent).que.add(s);
            return;
        }
        md.ship = s;
        hmp.get(md.parent).ships.add(s);
    }

    //Creates new cargo ship, assigns to parent port
    private void addCShip(Scanner sc, HashMap<Integer, Dock> hmd, HashMap<Integer, SeaPort> hmp,
            HashMap<Integer, Ship> hms) {
        CargoShip cs = new CargoShip(sc);
        hms.put(cs.index, cs);
        Dock md = hmd.get(cs.parent);
        if (md == null) {
            hmp.get(cs.parent).ships.add(cs);
            hmp.get(cs.parent).que.add(cs);
            return;
        }
        md.ship = cs;
        hmp.get(md.parent).ships.add(cs);
    }

    //Creates new passenger ship, assigns to parent port
    private void addPShip(Scanner sc, HashMap<Integer, Dock> hmd, HashMap<Integer, SeaPort> hmp,
            HashMap<Integer, Ship> hms) {
        PassengerShip ps = new PassengerShip(sc);
        hms.put(ps.index, ps);
        Dock md = hmd.get(ps.parent);
        if (md == null) {
            hmp.get(ps.parent).ships.add(ps);
            hmp.get(ps.parent).que.add(ps);
            return;
        }
        md.ship = ps;
        hmp.get(md.parent).ships.add(ps);
    }

    //Creates new person, assigns to parent port
    private void addPerson(Scanner sc, HashMap<Integer, SeaPort> hmp) {
        Person p = new Person(sc);
        hmp.get(p.parent).persons.add(p);
    }

    //Creates new job
    private void addJob(Scanner sc, HashMap<Integer, Ship> hms, HashMap<Integer, Dock> hmd, HashMap<Integer, SeaPort> hmp, 
            JPanel jp) {
        Job j = new Job(sc, hms, hmd, hmp, jp);
        if (hms.get(j.parent) == null) {
            hmd.get(j.parent).jobs.add(j);
        } else {
            hms.get(j.parent).jobs.add(j);
        }
    }

    //Searches for ship by index
    private Ship getShipByIndex(int x) {
        for (SeaPort msp : ports) {
            for (Ship ms : msp.ships) {
                if (ms.index == x) {
                    return ms;
                }
            }
        }
        return null;
    }

    //Searches for ship by name, adds result to array list
    private void getShipByName(String s) {
        for (SeaPort msp : ports) {
            for (Ship ms : msp.ships) {
                if (ms.name.equalsIgnoreCase(s)) {
                    nameResults.add(ms);
                }
            }
        }
    }

    //Searches for ship by length, adds to array list
    private void getShipByLength(String s) {
        for (SeaPort msp : ports) {
            for (Ship ms : msp.ships) {
                if (ms.length > Double.parseDouble(s)) {
                    lengthResults.add(ms);
                }
            }
        }
    }

    //Searches for dock by index
    private Dock getDockByIndex(int x) {
        for (SeaPort msp : ports) {
            for (Dock md : msp.docks) {
                if (md.index == x) {
                    return md;
                }
            }
        }
        return null;
    }

    //Searches for dock by name, adds to array list
    private void getDockByName(String s) {
        for (SeaPort msp : ports) {
            for (Dock md : msp.docks) {
                if (md.name.equalsIgnoreCase(s)) {
                    nameResults.add(md);
                }
            }
        }
    }

    //Searches for sea port by index
    private SeaPort getSeaPortByIndex(int x) {
        for (SeaPort msp : ports) {
            if (msp.index == x) {
                return msp;
            }
        }
        return null;
    }

    //Searches for sea port by name, adds to array list
    private void getSeaPortByName(String s) {
        for (SeaPort msp : ports) {
            if (msp.name.equalsIgnoreCase(s)) {
                nameResults.add(msp);
            }
        }
    }

    //Searches for person by index
    private Person getPersonByIndex(int x) {
        for (SeaPort msp : ports) {
            for (Person mp : msp.persons) {
                if (mp.index == x) {
                    return mp;
                }
            }
        }
        return null;
    }

    //Searches for person by name, adds to array list
    private void getPersonByName(String s) {
        for (SeaPort msp : ports) {
            for (Person mp : msp.persons) {
                if (mp.name.equalsIgnoreCase(s)) {
                    nameResults.add(mp);
                }
            }
        }
    }

    //Searches for person by skill, adds to array list
    private void getPersonBySkill(String s) {
        for (SeaPort msp : ports) {
            for (Person mp : msp.persons) {
                if (mp.skill.equalsIgnoreCase(s)) {
                    skillResults.add(mp);
                }
            }
        }
    }

    //Method to search based on input from GUI, either returns result/s or no results found message
    public String search(String type, String target) {
        nameResults = new ArrayList<Thing>();
        skillResults = new ArrayList<Thing>();
        lengthResults = new ArrayList<Thing>();
        String st = "";
        switch (type) {
            case "Index":
                try {
                    if (getSeaPortByIndex(Integer.parseInt(target)) != null) {
                        st += "\n" + getSeaPortByIndex(Integer.parseInt(target));
                    }
                    if (getDockByIndex(Integer.parseInt(target)) != null) {
                        st += "\n" + getDockByIndex(Integer.parseInt(target));
                    }
                    if (getShipByIndex(Integer.parseInt(target)) != null) {
                        st += "\n" + getShipByIndex(Integer.parseInt(target));
                    }
                    if (getPersonByIndex(Integer.parseInt(target)) != null) {
                        st += "\n" + getPersonByIndex(Integer.parseInt(target));
                    }
                    if (st.equals("")) {
                        return "No results";
                    } else {
                        return st;
                    }
                } catch (NumberFormatException n) {
                    st = "Please enter a number";
                    return st;
                }
            case "Name":
                getSeaPortByName(target);
                getDockByName(target);
                getShipByName(target);
                getPersonByName(target);
                for (Thing nr : nameResults) {
                    st += "\n" + nr;
                }
                if (nameResults.isEmpty()) {
                    return "No results";
                } else {
                    return st;
                }
            case "Skill":
                getPersonBySkill(target);
                for (Thing sr : skillResults) {
                    st += "\n" + sr;
                }
                if (skillResults.isEmpty()) {
                    return "No results";
                } else {
                    return st;
                }
            case "Ship Length Greater Than":
                try {
                    getShipByLength(target);
                    for (Thing lr : lengthResults) {
                        st += "\n" + lr;
                    }
                    if (lengthResults.isEmpty()) {
                        return "No results";
                    } else {
                        return st;
                    }
                } catch (NumberFormatException n) {
                    st = "Please enter a number";
                    return st;
                }
            default:
                return "No results";
        }

    }
    
    //Method to sort implementing comparators
    public String sort (String type) {
        switch (type) {
            case "Weight" : 
                for (SeaPort msp : ports) {
                    Collections.sort (msp.ships, (a, b) -> a.weight < b.weight ? -1 : a.weight == b.weight ? 0 : 1);
                }
                return "*****Sorted by Weight";
            case "Length" :
                for (SeaPort msp : ports) {
                    Collections.sort (msp.ships, (a, b) -> a.length < b.length ? -1 : a.length == b.length ? 0 : 1);
                }
                return "*****Sorted by Length";
            case "Width" :
                for (SeaPort msp : ports) {
                    Collections.sort (msp.ships, (a, b) -> a.width < b.width ? -1 : a.width == b.width ? 0 : 1);
                }
                return "*****Sorted by Width";
            case "Draft" :
                for (SeaPort msp : ports) {
                    Collections.sort (msp.ships, (a, b) -> a.draft < b.draft ? -1 : a.draft == b.draft ? 0 : 1);
                }
                return "*****Sorted by Draft";
            case "Name" :
                Collections.sort (ports, (a, b) -> a.name.compareTo(b.name));
                for (SeaPort msp: ports) {
                    Collections.sort (msp.docks, (a, b) -> a.name.compareTo(b.name));
                    Collections.sort (msp.que, (a, b) -> a.name.compareTo(b.name));
                    Collections.sort (msp.ships, (a, b) -> a.name.compareTo(b.name));
                    Collections.sort (msp.persons, (a, b) -> a.name.compareTo(b.name));
                }
                return "*****Sorted by Name";
        }
        return "";
    }
    
    //Method to create a JTree
    public DefaultMutableTreeNode createNode (String title) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode (title);
        DefaultMutableTreeNode port, ship, dock, person, job;
        for (SeaPort sp : ports) {
            port = new DefaultMutableTreeNode(sp.name);
            top.add(port);
            for (Dock d : sp.docks) {
                dock = new DefaultMutableTreeNode(d.name);
                port.add(dock);
                for (Job j : d.jobs) {
                    job = new DefaultMutableTreeNode(j.name);
                    dock.add(job);
                }
            }
            for (Ship s : sp.ships) {
                ship = new DefaultMutableTreeNode(s.name);
                port.add(ship);
                for (Job j : s.jobs) {
                    job = new DefaultMutableTreeNode(j.name);
                    ship.add(job);
                }
            }
            for (Person p : sp.persons) {
                person = new DefaultMutableTreeNode(p.name);
                port.add(person);
            }
        }
        return top;
    }

    //To string method
    public String toString() {
        String st = ">>>>> The World: ";
        for (SeaPort mp : ports) {
            st += "\n" + mp;
        }
        return st;
    }
}
