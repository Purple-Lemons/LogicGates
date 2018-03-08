package logicgates;

/***************************************************************************/
/*This class creates the GUI and handles the user input for the menu screen*/
/***************************************************************************/
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MenuScreen extends JPanel implements Commons{

    Controller controller;
    Debug debug;

    int sButtonX, sButtonY;
    int eButtonX, eButtonY;
    int sButtonWidth, sButtonHeight;
    int panelWidth = 1000;
    int panelHeight = 600;

    public MenuScreen(Controller controllerImport, Debug debugIn){
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        addMouseListener(new MouseHandler());

        controller = controllerImport;
        debug = debugIn;

        repaint();
    }

    public void paint(Graphics g){
        sButtonWidth = 300;
        sButtonHeight = 50;

        sButtonX = panelWidth / 2 - sButtonWidth / 2;
        sButtonY = panelHeight / 2 - sButtonHeight;

        g.setColor(new Color(165, 165, 165));

        g.fill3DRect(sButtonX, sButtonY, sButtonWidth, sButtonHeight, true);

        eButtonX = sButtonX;
        eButtonY = sButtonY + sButtonHeight + 20;//Places the button 100 pixels below the start button

        g.fill3DRect(eButtonX, eButtonY, sButtonWidth, sButtonHeight, true);

        g.setFont(new Font("impact", Font.BOLD, 30));
        g.setColor(Color.black);
        g.drawString("Start", sButtonX + sButtonWidth / 2 - 30, sButtonY + sButtonHeight - 10);
        g.drawString("Exit", eButtonX + sButtonWidth / 2 - 30, eButtonY + sButtonHeight - 10);

        debug.stringOut("MenuScreen - Repainting");
    }

    class MouseHandler extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            int mouseX = e.getX();
            int mouseY = e.getY();

            if(mouseX > sButtonX && mouseX < sButtonX + sButtonWidth && mouseY > sButtonY && mouseY < sButtonY + sButtonHeight){
                debug.stringOut("MenuScreen - Starting");
                controller.start();
            }

            if(mouseX > eButtonX && mouseX < eButtonX + sButtonWidth && mouseY > eButtonY && mouseY < eButtonY + sButtonHeight){
                debug.stringOut("MenuScreen - Exiting");
                System.exit(0);
            }
        }
    }
}