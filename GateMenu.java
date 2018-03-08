package logicgates;

/***********************************************************************************/
/*This class creates the panel with the options for items that can be placed on the*/
/*grid, it also handles input from the user to select which gate is being used     */
/***********************************************************************************/

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

import javax.swing.*;
import javax.imageio.*;

import java.io.*;

public class GateMenu extends JPanel implements Commons{

    BufferedImage[] imgList;
    int numItems;

    int margin = 10;

    int chooseImage = 0;

    String embossPath = "images/Emboss.png";
    BufferedImage emboss;

    Debug debug;

    public GateMenu(BufferedImage[] imgListImport, Debug debugIn){
        this.addMouseListener(new MouseHandler());
        this.setDoubleBuffered(true);
        this.setBackground(Color.WHITE);

        imgList = imgListImport;
        numItems = imgList.length;

        File embossLoc = new File(embossPath);

        debug = debugIn;

        try{
            emboss = ImageIO.read(embossLoc);
        }
        catch(IOException e){
            debug.objectOut("GateMenu - Exception - " + e);
        }

        repaint();
    }

    public void paint(Graphics g){

        g.setColor(Color.white);
        g.fillRect(0, 0 , getWidth(), getHeight());

        for(int i = 0; i < imgList.length; i++){
            int x = margin;
            int y = i * cellHeight + borderWidth / 2;

            if(i >= 0 && i < 4){
                g.drawImage(imgList[i], x, y, null);

                if(i == chooseImage){
                    g.drawImage(emboss, x, y, null);
                }
            }

            int newY = (i - 1) * cellWidth + borderWidth / 2;
            if(i > 4 && i < 10){
                g.drawImage(imgList[i], x, newY, null);

                if(i == chooseImage){
                    g.drawImage(emboss, x, newY, null);
                }
            }

            g.setColor(Color.black);
            switch(i){
                case 0:
                    g.drawString("AND", x + cellWidth + 5, y + cellHeight / 2);
                    break;
                case 1:
                    g.drawString("OR", x + cellWidth + 5, y + cellHeight / 2);
                    break;
                case 2:
                    g.drawString("NOT", x + cellWidth + 5, y + cellHeight / 2);
                    break;
                case 3:
                    g.drawString("Switch", x + cellWidth + 5, y + cellHeight / 2);
                    break;
                case 5:
                    g.drawString("XOR", x + cellWidth + 5, newY + cellHeight / 2);
                    break;
                case 6:
                    g.drawString("XNOR", x + cellWidth + 5, newY + cellHeight / 2);
                    break;
                case 7:
                    g.drawString("NOR", x  + cellWidth + 5, newY + cellHeight / 2);
                    break;
                case 8:
                    g.drawString("NAND", x + cellWidth + 5, newY + cellHeight / 2);
                    break;
            }
        }

        debug.stringOut("GateMenu - Repainting");
    }

    public int getSelectedImage(){
        return chooseImage;
    }

    class MouseHandler extends MouseAdapter{
        public void mousePressed(MouseEvent e){
            int mouseX = e.getX();
            int mouseY = e.getY();

            if(mouseY >= borderWidth / 2 && mouseY <= cellHeight + borderWidth / 2){
                chooseImage = 0;
                debug.stringOut("GateMenu - Selected Gate: AND");
            }
            if(mouseY >= cellHeight + borderWidth / 2 && mouseY <= cellHeight * 2 + borderWidth / 2){
                chooseImage = 1;
                debug.stringOut("GateMenu - Selected Gate: OR");
            }
            if(mouseY >= cellHeight * 2 + borderWidth / 2 && mouseY <= cellHeight * 3 + borderWidth / 2){
                chooseImage = 2;
                debug.stringOut("GateMenu - Selected Gate: NOT");
            }
            if(mouseY >= cellHeight * 3 + borderWidth / 2 && mouseY <= cellHeight * 4 + borderWidth / 2){
                chooseImage = 3;
                debug.stringOut("GateMenu - Selected Gate: Switch");
            }
            if(mouseY >= cellHeight * 4 + borderWidth / 2 && mouseY <= cellHeight* 5 + borderWidth / 2){
                chooseImage = 5;
                debug.stringOut("GateMenu - Selected Gate: XOR");
            }
            if(mouseY >= cellHeight * 5 + borderWidth / 2 && mouseY <= cellHeight * 6 + borderWidth / 2){
                chooseImage = 6;
                debug.stringOut("GateMenu - Selected Gate: XNOR");
            }
            if(mouseY >= cellHeight * 6 + borderWidth / 2 && mouseY <= cellHeight * 7 + borderWidth / 2){
                chooseImage = 7;
                debug.stringOut("GateMenu - Selected Gate: NOR");
            }
            if(mouseY >= cellHeight * 7 + borderWidth / 2 && mouseY <= cellHeight * 8 + borderWidth / 2){
                chooseImage = 8;
                debug.stringOut("GateMenu - Selected Gate: NAND");
            }
            if(mouseY >= cellHeight * 8 + borderWidth / 2 && mouseY <= cellHeight * 9 + borderWidth / 2){
                chooseImage = 10;
                debug.stringOut("GateMenu - Selected Gate: Output");
            }
            repaint();
        }
    }
}