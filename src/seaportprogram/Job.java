/*
* File Name: Job.java
* Date: March 14, 2017
* Author: Joshua Markovich
* Purpose: To simulate the aspects of a Sea Port, and to create a GUI.
 */
package seaportprogram;

import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Job extends Thing implements Runnable {

    double duration;
    ArrayList<String> requirements = new ArrayList<String>();
    ArrayList<Person> resources = new ArrayList<Person>();
    boolean suspendFlag = true;
    boolean cancelFlag = true;
    JPanel panel;
    JButton suspendButton = new JButton("Suspend");
    JButton cancelButton = new JButton("Cancel");
    JProgressBar progressBar = new JProgressBar();
    Status status = Status.SUSPENDED;
    HashMap<Integer, Ship> ships;
    HashMap<Integer, Dock> docks;
    HashMap<Integer, SeaPort> ports;
    Ship jobParent;
    Dock dockParent;
    SeaPort portParent;
    Thread thread;

    enum Status {
        RUNNING, SUSPENDED, WAITING, DONE
    };

    public Job(Scanner sc, HashMap<Integer, Ship> hms, HashMap<Integer, Dock> hmd,
            HashMap<Integer, SeaPort> hmp, JPanel jp) {
        super(sc);
        if (sc.hasNextDouble()) {
            duration = sc.nextDouble();
        }
        while (sc.hasNext()) {
            requirements.add(sc.next());
        }

        panel = jp;
        panel.add(progressBar);
        progressBar.setStringPainted(true);

        ships = hms;
        docks = hmd;
        ports = hmp;
        jobParent = ships.get(this.parent);

        if (ports.get(jobParent.parent) == null) {
            dockParent = docks.get(jobParent.parent);
            portParent = ports.get(dockParent.parent);
        } else {
            portParent = ports.get(jobParent.parent);
        }

        panel.add(new JLabel(jobParent.name, SwingConstants.CENTER));
        panel.add(new JLabel(this.name, SwingConstants.CENTER));
        panel.add(suspendButton);
        panel.add(cancelButton);

        suspendButton.addActionListener(e -> toggleSuspendFlag());
        cancelButton.addActionListener(e -> toggleCancelFlag());

        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        long time = System.currentTimeMillis();
        long startTime = time;
        long stopTime = time + 1000 * (long) duration;
        double jobTime = stopTime - time;

        synchronized (portParent) {
            while (jobParent.busyFlag || !checkResources() || dockParent == null) {
                portParent.checkDock();
                showStatus(Status.WAITING);
                try {
                    portParent.wait();
                } catch (InterruptedException i) {
                }
                jobParent.busyFlag = true;
                try {
                    for (int i = 0; i < requirements.size(); i++) {
                        resources.add(portParent.getItem(requirements.get(i)));
                    }
                    if (dockParent == null) {
                        dockParent = portParent.getDock();
                    }
                } catch (InterruptedException i) {
                }
            }
        }

        while (time < stopTime && cancelFlag) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException i) {
            }
            if (suspendFlag) {
                showStatus(Status.RUNNING);
                time += 100;
                progressBar.setValue((int) (((time - startTime) / jobTime) * 100));
            } else {
                showStatus(Status.SUSPENDED);
            }
        }

        progressBar.setValue(100);
        showStatus(Status.DONE);
        synchronized (portParent) {
            jobParent.busyFlag = false;
            for (int i = 0; i < resources.size(); i++) {
                portParent.putItem(resources.get(i));
            }
            portParent.putDock(dockParent);
            portParent.notifyAll();
        }
    }

    public void toggleSuspendFlag() {
        suspendFlag = !suspendFlag;
    }

    public void toggleCancelFlag() {
        cancelFlag = false;
        cancelButton.setBackground(Color.RED);
        suspendButton.setText("Cancelled");
    }

    public void showStatus(Status s) {
        status = s;
        switch (status) {
            case RUNNING:
                suspendButton.setBackground(Color.GREEN);
                suspendButton.setText("Running");
                break;
            case SUSPENDED:
                suspendButton.setBackground(Color.YELLOW);
                suspendButton.setText("Suspended");
                break;
            case WAITING:
                suspendButton.setBackground(Color.ORANGE);
                suspendButton.setText("Waiting");
                break;
            case DONE:
                suspendButton.setBackground(Color.RED);
                suspendButton.setText("Done");
                break;
        }
    }

    public boolean checkResources() {
        for (int i = 0; i < requirements.size(); i++) {
            int l = 0;
            if (!portParent.checkStatus(requirements.get(i))) {
                for (int j = 0; j < portParent.persons.size(); j++) {
                    if (portParent.persons.get(j).skill.equals(requirements.get(i))) {
                        l++;
                    }
                }
                if (l == 0) {
                    toggleCancelFlag();
                    cancelButton.setText("NO RESOURCES!");
                    return false;
                }
                return false;
            }
        }
        return true;
    }

    public String toString() {
        String st = "Job: " + super.toString();
        return st;
    }
}
