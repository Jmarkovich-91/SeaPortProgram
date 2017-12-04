/*
* File Name: SeaPort.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;
import java.util.concurrent.*;
import javax.swing.*;

public class SeaPort extends Thing {

    ArrayList<Dock> docks = new ArrayList<Dock>();
    ArrayList<Ship> que = new ArrayList<Ship>();
    ArrayList<Ship> ships = new ArrayList<Ship>();
    ArrayList<Person> persons = new ArrayList<Person>();
    Semaphore available;
    Semaphore dock;
    ArrayList<Boolean> used = new ArrayList<Boolean>();
    ArrayList<Boolean> usedDocks = new ArrayList<Boolean>();
    JPanel panel;

    public SeaPort(Scanner sc, JPanel jr) {
        super(sc);
        panel = jr;
    }
    
    public Person getItem (String s) throws InterruptedException {
        available.acquire();
        return getNextAvailableItem(s);
    }
    
    public Dock getDock () throws InterruptedException {
        dock.acquire();
        return getNextAvailableDock();
    }
    
    public void putItem (Object o) {
        if (markAsUnused(o)) {
            available.release();
        }
    }
    
    public void putDock (Object o) {
        if (markDockAsUnused(o)) {
            dock.release();
        }
    }
    
    protected synchronized Person getNextAvailableItem (String s) {
        for (int i = 0; i < persons.size(); i++) {
            if (!used.get(i) && persons.get(i).skill.equals(s)) {
                used.set(i, true);
                return persons.get(i);
            }
        }
        return null;
    }
    
    protected synchronized Dock getNextAvailableDock () {
        for (int i = 0; i < docks.size(); i++) {
            if (!usedDocks.get(i)) {
                usedDocks.set(i, true);
                return docks.get(i);
            }
        }
        return null;
    }
    
    protected synchronized boolean checkStatus (String s) {
        if (used.size() == 0) {
            for (int i = 0; i < persons.size(); i++) {
                used.add(i, false);
            }
            available = new Semaphore (persons.size(), true);
        }
        for (int i = 0; i < persons.size(); i++) {
            if (!used.get(i) && persons.get(i).skill.equals(s)) {
                return true;
            }
        }
        return false;
    }
    
    protected synchronized boolean checkDock () {
        if (usedDocks.size() == 0) {
            for (int i = 0; i < docks.size(); i++) {
                usedDocks.add(i, true);
            }
            dock = new Semaphore (docks.size(), true);
        }
        for (int i = 0; i < docks.size(); i++) {
            if (!usedDocks.get(i)) {
                return true;
            }
        }
        return false;
    }
    
    protected synchronized boolean markAsUnused (Object item) {
        for (int i = 0; i < persons.size(); i++) {
            if (item == persons.get(i)) {
                if (used.get(i)) {
                    used.set(i, false);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    
    protected synchronized boolean markDockAsUnused (Object item) {
        for (int i = 0; i < docks.size(); i++) {
            if (item == docks.get(i)) {
                if (usedDocks.get(i)) {
                    usedDocks.set(i, false);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
    
    public void displayResources () {
            for (int i = 0; i < persons.size(); i++) {
                    panel.add(new JLabel(persons.get(i).name));
                    panel.add(new JLabel(persons.get(i).skill));
                    panel.add(new JLabel(this.name));
            }
    }

    public String toString() {
        String st = "\n\nSeaPort: " + super.toString();
        for (Dock md : docks) {
            st += "\n" + md;
        }
        st += "\n\n --- List of all ships in que:";
        for (Ship ms : que) {
            st += "\n >" + ms;
        }
        st += "\n\n --- List of all ships:";
        for (Ship ms : ships) {
            st += "\n > " + ms;
        }
        st += "\n\n --- List of all persons:";
        for (Person mp : persons) {
            st += "\n > " + mp;
        }
        return st;
    }
}
