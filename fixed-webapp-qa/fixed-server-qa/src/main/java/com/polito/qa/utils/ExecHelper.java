package com.polito.qa.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Scanner;

/**
 * ExecHelper Class
 * Fixed Version: only accept help command (is never used)
 * @author Simone Licitra
 */
public class ExecHelper implements Serializable {
    private static final long serialVersionUID = 1L;
	private Base64Helper[] command;
    private String output;

    public ExecHelper(Base64Helper[] command) throws IOException {
        this.command = command;
    }

    public void run() throws IOException {
        if (!(isHelpCommand())) {
            System.out.println("Only 'help' command is allowed.");
            return;
        }
        
        String[] command = new String[this.command.length];
        for (int i = 0; i < this.command.length; i++) {
            String str = this.command[i].decode();
            command[i] = str;
        }

        Scanner s = new Scanner(Runtime.getRuntime().exec(command).getInputStream()).useDelimiter("\\A");
        String result =  s.hasNext() ? s.next() : "";
        System.out.println("executing...");
        System.out.println(result);
        this.output = result;
    }
    
    private boolean isHelpCommand() {
        if (this.command.length != 1) {
            return false; 
        }
        return this.command[0].decode().equals("help"); 
    }

    @Override
    public String toString() {
        return "ExecHelper{" +
                "command=" + Arrays.toString(command) +
                ", output='" + output + '\'' +
                '}';
    }

    private final void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        run();
    }
}