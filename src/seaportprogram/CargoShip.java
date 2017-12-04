/*
* File Name: CargoShip.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;

public class CargoShip extends Ship {

    double cargoValue, cargoVolume, cargoWeight;

    public CargoShip(Scanner sc) {
        super(sc);
        if (sc.hasNextDouble()) {
            cargoWeight = sc.nextDouble();
        }
        if (sc.hasNextDouble()) {
            cargoVolume = sc.nextDouble();
        }
        if (sc.hasNextDouble()) {
            cargoValue = sc.nextDouble();
        }
    }

    public String toString() {
        String st = "Cargo Ship: " + super.toString();
        if (jobs.size() == 0) {
            return st;
        }
        for (Job mj : jobs) {
            st += "\n - " + mj;
        }
        return st;
    }
}
