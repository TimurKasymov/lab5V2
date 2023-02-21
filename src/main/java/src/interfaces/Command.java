package src.interfaces;

import java.util.Scanner;

public interface Command {
    /** executes the command */
    boolean execute(String[] args);
    /** prints the command description */
    String getInfo();
}
