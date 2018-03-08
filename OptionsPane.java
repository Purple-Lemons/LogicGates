package logicgates;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class OptionsPane extends JPanel{
    Controller controller;
    Debug debug;
    LogicGates logicGates;

    int sButtonX, sButtonY;
    int eButtonX, eButtonY;
    int sButtonWidth, sButtonHeight;
    int panelWidth = 1000;
    int panelHeight = 600;

    int screenSizeStepper = 0;

    public OptionsPane(Controller controllerImport, Debug debugIn, LogicGates lGImport){
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        addMouseListener(new MouseHandler());

        controller = controllerImport;
        debug = debugIn;
        logicGates = lGImport;

        repaint();
    }

    public void paint(Graphics g){
        sButtonWidth = 400;
        sButtonHeight = 100;

        sButtonX = panelWidth / 2 - sButtonWidth / 2;
        sButtonY = 100;

        g.setColor(new Color(180, 210, 230));

        g.fill3DRect(sButtonX, sButtonY, sButtonWidth, sButtonHeight, true);

        eButtonX = sButtonX;
        eButtonY = sButtonY + sButtonHeight + 100;//Places the button 100 pixels below the start button

        g.fill3DRect(eButtonX, eButtonY, sButtonWidth, sButtonHeight, true);

        g.setFont(new Font("impact", Font.BOLD, 30));
        g.setColor(Color.black);
        g.drawString("ChangeSize", sButtonX + sButtonWidth / 2 - 30, sButtonY + sButtonHeight / 2);
        g.drawString("Exit", eButtonX + sButtonWidth / 2 - 30, eButtonY + sButtonHeight / 2);

        debug.stringOut("MenuScreen - Repainting");
    }

    class MouseHandler extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            int mouseX = e.getX();
            int mouseY = e.getY();

            if(mouseX > sButtonX && mouseX < sButtonX + sButtonWidth && mouseY > sButtonY && mouseY < sButtonY + sButtonHeight){
                debug.stringOut("OptionsPane - Change Screen Size");

            }

            if(mouseX > eButtonX && mouseX < eButtonX + sButtonWidth && mouseY > eButtonY && mouseY < eButtonY + sButtonHeight){
                debug.stringOut("MenuScreen - Exiting");
                System.exit(0);
            }
        }
    }
}
