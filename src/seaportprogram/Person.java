/*
* File Name: Person.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;

public class Person extends Thing {

    String skill;

    public Person(Scanner sc) {
        super(sc);
        if (sc.hasNext()) {
            skill = sc.next();
        }
    }

    public String toString() {
        return "Person: " + super.toString() + " " + skill;
    }
}
