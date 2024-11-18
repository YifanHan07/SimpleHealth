package App;

import view.BrowsePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Recipe Browser");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BrowsePanel browsePanel = new BrowsePanel();
        frame.add(browsePanel);

        frame.setSize(800, 600);

        frame.setVisible(true);
    }
}
