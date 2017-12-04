/*
* File Name: PassengerShip.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;

public class PassengerShip extends Ship {

    int numberOfOccupiedRooms, numberOfPassengers, numberOfRooms;

    public PassengerShip(Scanner sc) {
        super(sc);
        if (sc.hasNextInt()) {
            numberOfPassengers = sc.nextInt();
        }
        if (sc.hasNextInt()) {
            numberOfRooms = sc.nextInt();
        }
        if (sc.hasNextInt()) {
            numberOfOccupiedRooms = sc.nextInt();
        }
    }

    public String toString() {
        String st = "Passenger Ship: " + super.toString();
        if (jobs.size() == 0) {
            return st;
        }
        for (Job mj : jobs) {
            st += "\n - " + mj;
        }
        return st;
    }

}
