package UI;

import javax.swing.*;

public class LoginDriver {

    public static void run() {

        init();

        JFrame frame = new JFrame("LoginMenu");

        frame.setContentPane(new LoginMenu().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);


    }

    private static void login() {
        // TODO
        System.out.println("something printed here");
    }

    public static void init(){
        // TODO
        System.out.println("Initialized");
    }
}