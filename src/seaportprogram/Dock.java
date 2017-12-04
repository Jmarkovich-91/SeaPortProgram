/*
* File Name: Dock.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;

public class Dock extends Thing {

    Ship ship;
    ArrayList<Job> jobs = new ArrayList<Job> ();

    public Dock(Scanner sc) {
        super(sc);
    }

    public String toString() {
        return "\n\nDock: " + super.toString() + "\n Ship: " + ship;
    }

}
