/*
* File Name: Ship.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;

public class Ship extends Thing {

    double draft, length, weight, width;
    PortTime arrivalTime, dockTime;
    ArrayList<Job> jobs = new ArrayList<Job>();
    boolean busyFlag = false;

    public Ship(Scanner sc) {
        super(sc);
        if (sc.hasNextDouble()) {
            weight = sc.nextDouble();
        }
        if (sc.hasNextDouble()) {
            length = sc.nextDouble();
        }
        if (sc.hasNextDouble()) {
            width = sc.nextDouble();
        }
        if (sc.hasNextDouble()) {
            draft = sc.nextDouble();
        }

    }

    public String toString() {
        return super.toString();
    }
}
