package islander;

import java.awt.*;
import javax.swing.*;

public class Islander {

    public static void main(String[] args) {
        final int WIDTH = 80;
        final int HEIGHT = 60;
        final int SCALE = 11;
        final double RATIO = 0.46;
        JFrame frame = new JFrame("Map");
        Control control = new Control(WIDTH, HEIGHT, RATIO, SCALE);
        frame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE + 22 + 39));
        frame.add(control);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
