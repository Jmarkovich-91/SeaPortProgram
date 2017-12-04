/*
* File Name: Thing.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;

public class Thing<E> implements Comparable<Thing> {

    int index;
    String name;
    int parent;

    public Thing(Scanner sc) {
        if (sc.hasNext()) {
            name = sc.next();
        }
        if (sc.hasNextInt()) {
            index = sc.nextInt();
        }
        if (sc.hasNextInt()) {
            parent = sc.nextInt();
        }
    }
    
    //Compare to method to compare indexes
    public int compareTo(Thing t) {
        return Integer.compare(this.index, t.index);
    }

    public String toString() {
        return name + " " + index;
    }
}
