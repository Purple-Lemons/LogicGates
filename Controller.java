package logicgates;

/********************************************************************************************/
/*This is the class that initializes all of the panels and adds or removes them dynamically.*/
/********************************************************************************************/

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;
import javax.imageio.*;

import java.io.*;

public class Controller extends JPanel implements Commons{

    protected String[] pathList = {"images/AND.png",
                                   "images/OR.png",
                                   "images/NOT.png",
                                   "images/ON.png",
                                   "images/OFF.png",
                                   "images/XOR.png",
                                   "images/XNOR.png",
                                   "images/NOR.png",
                                   "images/NAND.png",
                                   "images/OutON.png",
                                   "images/OutOFF.png"}; //stores all image paths

    protected BufferedImage[] imgList = new BufferedImage[pathList.length]; //Stores all possible images

    LogicGates logicGates;
    Logic logic;
    MenuScreen menuScreen;
    CircuitPane circuitScreen;
    TruthTable truthTable;
    GateMenu gateMenu;
    SaveCircuit saveCircuit;
    Debug debug;
    OptionsPane optionsPane;

    //all posible panels
    JPanel menuPane;
    JPanel circuitPane;
    JPanel tablePane;
    JPanel gateMenuPane;

    JScrollPane cScrollPane;
    JScrollPane tScrollPane;

    boolean onMenu = true;
    boolean panelsCreated = false;

    public Controller(LogicGates lGImport){
        this.setDoubleBuffered(true);
        this.setBackground(new Color(218, 218, 218));

        try{
            int i;
            for(i = 0; i < pathList.length; i++){
                imgList[i] = ImageIO.read(new File(pathList[i]));
            }//fills image array with images, this is done here so that it can be passed to the truth table, gate menu and cicuit pane class and
        }
        catch(IOException e){
            debug.objectOut("Controller - Exception - " + e);
        }

        //Initialises all of the classes
        logicGates = lGImport;
        debug = new Debug();
        logic = new Logic();
        optionsPane = new OptionsPane(this, debug, logicGates);
        menuScreen = new MenuScreen(this, debug);
        truthTable = new TruthTable(imgList, debug);
        gateMenu = new GateMenu(imgList, debug);
        circuitScreen = new CircuitPane(logic, truthTable, imgList, gateMenu, debug, this);
        saveCircuit = new SaveCircuit(circuitScreen, truthTable, debug, logicGates);

        createPanels();
    }

    //This method controls which panels are visible on screen
    public void createPanels(){
        if(onMenu){//Sets panels that are on the manu screen
            if(panelsCreated){//Switches from circuit screen to menu screen
                circuitPane.setVisible(false);
                gateMenuPane.setVisible(false);
                tablePane.setVisible(false);
                tScrollPane.setVisible(false);
                cScrollPane.setVisible(false);

                menuPane.setVisible(true);
            }

            else{//Creates panels for the menu screen
                menuPane = new JPanel();
                menuPane = menuScreen;
                this.add(menuPane);
            }
        }//Adds the menu screen to the window

        if(!onMenu){
            if(panelsCreated){//Switches from menu screen to circuit screen
                circuitPane.setVisible(true);
                gateMenuPane.setVisible(true);
                tablePane.setVisible(true);
                tScrollPane.setVisible(true);
                cScrollPane.setVisible(true);

                menuPane.setVisible(false);
            }

            else{//Creates and arranges panels for the circuit screen

                menuPane.setVisible(false);//Removes the menu screen from the window

                //Adds the panel with the circuit diagram to the window
                circuitPane = new JPanel();
                circuitPane = circuitScreen;
                circuitPane.setPreferredSize(new Dimension(numRows * cellWidth + xOffset * 2, numRows * cellWidth + xOffset * 2));

                cScrollPane = new JScrollPane(circuitPane);//Creates a scrollable JPanel for the cicuit diagram
                circuitPane.setAutoscrolls(true);
                cScrollPane.setPreferredSize(new Dimension(screenWidth - borderWidth - 200, cellHeight * (imgList.length - 2) + borderWidth));
                cScrollPane.getVerticalScrollBar().setUnitIncrement(16);

                this.add(cScrollPane, BorderLayout.LINE_START);

                //Adds the panel with menu of possible logic gates
                gateMenuPane = new JPanel();
                gateMenuPane = gateMenu;
                gateMenuPane.setPreferredSize(new Dimension(cellWidth + borderWidth, cellHeight * (imgList.length - 2) + borderWidth));

                this.add(gateMenuPane, BorderLayout.CENTER);

                //Adds the panel with truth table to the window
                tablePane = new JPanel(new BorderLayout());
                tablePane = truthTable;
                tablePane.setPreferredSize(new Dimension(1000, 300));

                tScrollPane = new JScrollPane(tablePane);//Creates a scrollable JPanel for the truth table
                tablePane.setAutoscrolls(true);
                tScrollPane.setPreferredSize(new Dimension(screenWidth + cellWidth - 196, 300));
                tScrollPane.getVerticalScrollBar().setUnitIncrement(16);

                this.add(tScrollPane, BorderLayout.LINE_END);//Places the truth table after the ciruit digram, either to the left or below (dependent on screen size)

                panelsCreated = true;
            }
        }
    }

    //Sets the menu to off, so that the panels created are the cicuit screen panels
    public void start(){
        onMenu = false;
        createPanels();
    }

    //Sets the menu to on, so that the circuit screen panels are made invisible and the menu is made visible
    public void exitToMenu(){
        onMenu = true;
        createPanels();
    }

    public void save(){
    	if(!onMenu){
    		debug.stringOut("Saving - Controller");
        	saveCircuit.saveToFile(circuitScreen.getGridList(), circuitScreen.getImgList(), circuitScreen.getGridRef(), circuitScreen.getOutputs(), circuitScreen.getInputs());
    	}
    }

    public void load(){
    	if(!onMenu){
	        debug.stringOut("Loading - Controller");
	        circuitScreen.updateGridList(saveCircuit.loadFile(circuitScreen.getImgList()), saveCircuit.getGridRef(), saveCircuit.getInputs(), saveCircuit.getOutputs());
    	}
    }


}