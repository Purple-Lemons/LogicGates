package logicgates;

/*********************************************************/
/*This is the class that handles the circuit diagram GUI,*/
/*it also handler the user inputs for the circuit pane   */
/*********************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

public class CircuitPane extends JPanel implements Commons{

    //All possible paths to images that can be placed on the diagram
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
                                   "images/OutOFF.png"};

    protected BufferedImage[] gridList = new BufferedImage[numRows * numCols]; //stores images that are in the diagram

    protected BufferedImage[] imgList = new BufferedImage[pathList.length]; //Stores all possible images

    protected int[][] inputs = new int[gridList.length][5]; //input 1 position, input 2 position, state 1, state 2, ammount of inputs (data about inputs into each gate)
    protected boolean[] outputs = new boolean[gridList.length]; //outputs from each gate
    protected int[] gridRef = new int[gridList.length]; //stores which gate each input is linked to, used for traversing the circuit

    protected int maximumX = xOffset + cellWidth * numRows;//The edges of the cicuit diagram
    protected int maximumY = yOffset + cellHeight * numCols;

    //Information about the selected item
    int row1;
    int col1;
    int grid;
    int grid1;

    //The classed that need to be referenced in the CircuitPane class
    Logic logic;
    TruthTable truthTable;
    GateMenu gateMenu;
    Controller controller;
    Debug debug;

    public CircuitPane(Logic logicImport, TruthTable truthTableImport, BufferedImage[] iLImport, GateMenu gateMenuImport, Debug debugIn, Controller controllerImport){

        this.addMouseListener(new MouseHandler());

        setDoubleBuffered(true);
        setFocusable(true);

        //All classes referenced are imported from the controller class
        logic = logicImport;
        truthTable = truthTableImport;
        imgList = iLImport;
        gateMenu = gateMenuImport;
        controller = controllerImport;
        debug = debugIn;
    }

    //Handles most of the drawing in on the circuit diagram
    public void paintComponent(Graphics g){

        int x;
        int y;

        int column;
        int row;

        g.setColor(Color.white);
        g.fillRect(0, 0 , getWidth(), getHeight());

        for(int i = 0; i < gridList.length; i++){
            row = i / numRows;
            column = i % numCols;

            y = xOffset + (row * cellWidth);
            x = yOffset + (column * cellHeight);

            //draw grid and images on grid
            g.setColor(new Color(200, 200, 200));
            g.drawRect(x, y, cellWidth, cellHeight);

            if(gridRef[i] != 0){
                int targetRow = gridRef[i] / numRows;
                int targetCol = gridRef[i] % numCols;

                int targetY = xOffset + (targetRow * cellWidth);
                int targetX = yOffset + (targetCol * cellHeight);

                drawWire(g, x, y, targetX, targetY);
            }//vectors wires onto grid

            //draws images onto of wires
            g.drawImage(gridList[i], x, y, null);

            if(gridList[i] != null && gridList[i] != imgList[3] && gridList[i] != imgList[4]){
                String bString = new Boolean(outputs[(row * numCols) + column]).toString();
                g.setColor(Color.green);
                g.drawString(bString, x + 10, y + 32);
            }
        }

        g.setColor(Color.black);
        g.drawRect(xOffset, yOffset, cellWidth * numCols, cellHeight * numRows);

        debug.stringOut("CircuitPane - Repainting");
    }

    //The method for vectoring wires onto the grid between gates and inputs
    public void drawWire(Graphics g, int x1, int y1, int x2, int y2){
        g.setColor(Color.BLACK);

        x1 += cellWidth / 2;
        y1 += cellHeight / 2;

        x2 += cellWidth / 2;

        if(y2 > y1){
                y2 += cellHeight / 4;
        }//sets wire offset if target is below origin

        if(y2 < y1){
                y2 += (cellHeight / 4) * 3;
        }//sets wire offset if wire is above origin

        int xDistance = x2 - x1;
        int yDistance = y2 - y1;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(4));

        g2d.drawLine(x1, y1, x1 + xDistance / 2, y1);//adds half the horizontal wire

        g2d.drawLine(x1 + xDistance / 2, y2, x2, y2);//adds other half of horizontal wire

        g2d.drawLine(x1 + xDistance / 2, y1, x1 + xDistance / 2, y2);//adds vertical wire

        g2d.setStroke(new BasicStroke(1));
    }

    public int inputs(int pos){
        int input = 0;

        //decide whether or not an input is on or off
        if(gridList[pos] == imgList[3]){
            input = 1;
        }

        if(gridList[pos] == imgList[4]){
            input = 0;
        }

        //finds the output from a logic gate
        if(gridList[pos] == imgList[0] || gridList[pos] == imgList[1] || gridList[pos] == imgList[2] || gridList[pos] == imgList[5] || gridList[pos] == imgList[6] || gridList[pos] == imgList[7] || gridList[pos] == imgList[8] || gridList[pos] == imgList[9]){
            if(outputs[pos]){
                input = 1;
             }

            if(!outputs[pos]){
               input = 0;
            }
        }

        return input;
    }

    public void assignInputs(boolean isSwitchClick){//isSwitchClick stops columns being added to the truth table every time an inputs state is changed
        //changes input information
        if(inputs[grid][0] == grid1){
            inputs[grid][0] = grid1;
            inputs[grid][2] = inputs(grid1);
        }

        if(inputs[grid][1] == grid1 && gridList[grid] != imgList[2]){//assigns second input unless it is a not gate (one input)
            inputs[grid][1] = grid1;
            inputs[grid][3] = inputs(grid1);
        }

        //Sets the input information if there is no information to change (the first time a wire is linked to a gate)
        else if(inputs[grid][0] == 0){
            inputs[grid][0] = grid1;
            inputs[grid][2] = inputs(grid1);
        }

        else if(inputs[grid][1] == 0 && gridList[grid] != imgList[2]){//assigns second input unless it is a not gate (one input)
            inputs[grid][1] = grid1;
            inputs[grid][3] = inputs(grid1);
        }

        //passes input information to the logic class to find outputs
        if(inputs[grid][0] != 0 && (inputs[grid][1] != 0 || gridList[grid] == imgList[2])){//checks that a logic gate has all of it's inputs
            if(gridList[grid] == imgList[0]){
                outputs[grid] = logic.and(inputs[grid][2], inputs[grid][3]);//Sets the outputs from logic gates by passing inputs to the logic class

                debug.objectOut("CircuitPane - And - " + new Integer(grid) + " - " + outputs[grid]);

                updateInputs();//If there is a change in the circuit, any other gate that is linked to this gate may change state as well
                truthTable.setFunctionTitles(inputs[grid][0], inputs[grid][1], 0, isSwitchClick);
            }

            if(gridList[grid] == imgList[1]){
                outputs[grid] = logic.or(inputs[grid][2], inputs[grid][3]);

                debug.objectOut("CircuitPane - Or - " + new Integer(grid) + " - " + outputs[grid]);

                updateInputs();

                truthTable.setFunctionTitles(inputs[grid][0], inputs[grid][1], 1, isSwitchClick);
            }

            if(gridList[grid] == imgList[2]){
                outputs[grid] = logic.not(inputs[grid][2]);

                debug.objectOut("CircuitPane - Not - " + new Integer(grid) + " - " + outputs[grid]);

                updateInputs();

                truthTable.setFunctionTitles(inputs[grid][0], inputs[grid][1], 2, isSwitchClick);
            }

            if(gridList[grid] == imgList[5]){
                outputs[grid] = logic.xor(inputs[grid][2], inputs[grid][3]);

                debug.objectOut("CircuitPane - Xor - " + new Integer(grid) + " - " + outputs[grid]);

                updateInputs();

                truthTable.setFunctionTitles(inputs[grid][0], inputs[grid][1], 5, isSwitchClick);
            }

            if(gridList[grid] == imgList[6]){
                outputs[grid] = logic.xnor(inputs[grid][2], inputs[grid][3]);

                debug.objectOut("CircuitPane - Xnor - " + new Integer(grid) + " - " + outputs[grid]);

                updateInputs();

                truthTable.setFunctionTitles(inputs[grid][0], inputs[grid][1], 6, isSwitchClick);
            }

            if(gridList[grid] == imgList[7]){
                outputs[grid] = logic.nor(inputs[grid][2], inputs[grid][3]);

                debug.objectOut("CircuitPane - Nor - " + new Integer(grid) + " - " + outputs[grid]);

                updateInputs();

                truthTable.setFunctionTitles(inputs[grid][0], inputs[grid][1], 7, isSwitchClick);
            }

            if(gridList[grid] == imgList[8]){
                outputs[grid] = logic.nand(inputs[grid][2], inputs[grid][3]);

                debug.objectOut("CircuitPane - Nand - " + new Integer(grid) + " - " + outputs[grid]);

                updateInputs();

                truthTable.setFunctionTitles(inputs[grid][0], inputs[grid][1], 8, isSwitchClick);
            }

            if(gridList[grid] == imgList[9] || gridList[grid] == imgList[10]){
                outputs[grid] = logic.output(inputs[grid][2]);

                debug.objectOut("CircuitPane - Output - " + new Integer(grid) + " - " + outputs[grid]);

                if(outputs[grid]){
                    gridList[grid] = imgList[9];
                }
                else{
                    gridList[grid] = imgList[10];
                }

                updateInputs();
            }
        }
    }

    //Passes new outputs along the logic circuit so that when one input is changed,
    //all logic gates that are linked to the logic gate that has been changed are updated
    public void updateInputs(){

        if(gridRef[gridRef[grid1]] != 0){
            int inputToUpdate = 0;

            //decides which input is to be changed
            if(inputs[gridRef[gridRef[grid1]]][0] == grid){
                inputToUpdate = 2;
            }

            if(inputs[gridRef[gridRef[grid1]]][1] == grid){
                inputToUpdate = 3;
            }

            if(inputToUpdate != 0){
                //converts boolean into int and changes input
                if(outputs[inputs[gridRef[gridRef[grid1]]][inputToUpdate - 2]]){
                    inputs[gridRef[gridRef[grid1]]][inputToUpdate] = 1;
                }

                if(!outputs[inputs[gridRef[gridRef[grid1]]][inputToUpdate - 2]]){
                    inputs[gridRef[gridRef[grid1]]][inputToUpdate] = 0;
                }


                //assigns new outputs
                if(gridList[gridRef[gridRef[grid1]]] == imgList[0]){
                    outputs[gridRef[gridRef[grid1]]] = logic.and(inputs[gridRef[gridRef[grid1]]][2], inputs[gridRef[gridRef[grid1]]][3]);

                    grid = gridRef[gridRef[grid1]];
                    grid1 = gridRef[grid1];
                    updateInputs();
                }

                if(gridList[gridRef[gridRef[grid1]]] == imgList[1]){
                    outputs[gridRef[gridRef[grid1]]] = logic.or(inputs[gridRef[gridRef[grid1]]][2], inputs[gridRef[gridRef[grid1]]][3]);

                    grid = gridRef[gridRef[grid1]];
                    grid1 = gridRef[grid1];
                    updateInputs();
                }

                if(gridList[gridRef[gridRef[grid1]]] == imgList[2]){
                    outputs[gridRef[gridRef[grid1]]] = logic.not(inputs[gridRef[gridRef[grid1]]][2]);

                    grid = gridRef[gridRef[grid1]];
                    grid1 = gridRef[grid1];
                    updateInputs();
                }

                if(gridList[gridRef[gridRef[grid1]]] == imgList[5]){
                    outputs[gridRef[gridRef[grid1]]] = logic.xor(inputs[gridRef[gridRef[grid1]]][2], inputs[gridRef[gridRef[grid1]]][3]);

                    grid = gridRef[gridRef[grid1]];
                    grid1 = gridRef[grid1];
                    updateInputs();
                }

                if(gridList[gridRef[gridRef[grid1]]] == imgList[6]){
                    outputs[gridRef[gridRef[grid1]]] = logic.xnor(inputs[gridRef[gridRef[grid1]]][2], inputs[gridRef[gridRef[grid1]]][3]);

                    grid = gridRef[gridRef[grid1]];
                    grid1 = gridRef[grid1];
                    updateInputs();
                }

                if(gridList[gridRef[gridRef[grid1]]] == imgList[7]){
                    outputs[gridRef[gridRef[grid1]]] = logic.nor(inputs[gridRef[gridRef[grid1]]][2], inputs[gridRef[gridRef[grid1]]][3]);

                    grid = gridRef[gridRef[grid1]];
                    grid1 = gridRef[grid1];
                    updateInputs();
                }

                if(gridList[gridRef[gridRef[grid1]]] == imgList[8]){
                    outputs[gridRef[gridRef[grid1]]] = logic.nand(inputs[gridRef[gridRef[grid1]]][2], inputs[gridRef[gridRef[grid1]]][3]);

                    grid = gridRef[gridRef[grid1]];
                    grid1 = gridRef[grid1];
                    updateInputs();
                }

                if(gridList[gridRef[gridRef[grid1]]] == imgList[9] || gridList[gridRef[gridRef[grid1]]] == imgList[10]){
                    outputs[gridRef[gridRef[grid1]]] = logic.output(inputs[gridRef[gridRef[grid1]]][2]);

                    if(outputs[gridRef[gridRef[grid1]]]){
                        gridList[gridRef[gridRef[grid1]]] = imgList[9];
                    }
                    else{
                        gridList[gridRef[gridRef[grid1]]] = imgList[10];
                    }
                }
            }
        }

        repaint();
    }

    //Outputs

    public BufferedImage[] getGridList(){
    	return gridList;
    }

    public BufferedImage[] getImgList(){
    	return imgList;
    }

    public int[][] getInputs(){
    	return inputs;
    }

    public int[] getGridRef(){
    	return gridRef;
    }

    public boolean[] getOutputs(){
    	return outputs;
    }

    public void updateGridList(BufferedImage[] newGrid, int[] newGridRef, int[][] newInputs, boolean[] newOutputs){
    	gridList = newGrid;
    	gridRef = newGridRef;
    	inputs = newInputs;
    	outputs = newOutputs;
    	repaint();
    }

    public void resetCell(){
	    for(int i = 0; i < inputs[grid][0]; i++){
	    	inputs[grid][i] = 0;
	    }

	    outputs[grid] = false;

    	if(inputs[gridRef[grid]][0] == grid){
    		inputs[gridRef[grid]][4]--;
    		inputs[gridRef[grid]][0] = 0;
    	}
    	if(inputs[gridRef[grid]][1] == grid){
    		inputs[gridRef[grid]][4]--;
    		inputs[gridRef[grid]][1] = 0;
    	}
    }

    class MouseHandler extends MouseAdapter{

        public void mouseReleased(MouseEvent e){
            int mouseX = e.getX();
            int mouseY = e.getY();

            //assigns images to grid
            if(e.getButton() == MouseEvent.BUTTON1){
                if(!(e.getY() < yOffset || e.getY() > maximumY || e.getX() < xOffset || e.getX() > maximumX)){
                    int row = (mouseY - yOffset) / cellHeight;
                    int column = (mouseX - xOffset) / cellWidth;

                    int grid = (row * numCols) + column;

                    if(gridList[grid] != null){//Removes items from circuit when clickd on in the circuit diagram
                        if(gridList[grid] == imgList[3] || gridList[grid] == imgList[4]){
                            truthTable.updateGrid(gridList, gridRef);//Sends updated grid data to the truth table class, if it is changed in the diagram
                            truthTable.removeValue(grid);//If a value is removed from the cicuit, the corresponing item is removed from the truth table
                        }

						resetCell();
                        gridList[grid] = null;//Sets the selected diagram item to null (removes it from the circuit)
                        gridRef[grid] = 0;//Removes the relevant grid referance
                    }

                    else{
                        gridList[grid] = imgList[gateMenu.getSelectedImage()];

                        if(gridList[grid] == imgList[3] || gridList[grid] == imgList[4]){
                            truthTable.updateGrid(gridList, gridRef);//Sends updated grid data to the truth table class, if it is changed in the diagram
                            truthTable.addValue(grid);//If an input is added to the circuit, it is also added to the truth table
                        }
                    }
                    repaint();
                }
            }

            //assigns wires to grid and finds wire end
            if(e.getButton() == MouseEvent.BUTTON3){
                if(!(mouseY < yOffset || mouseY > maximumY || mouseX < xOffset || mouseX > maximumX)){
                    int row = (mouseY - yOffset) / cellHeight;
                    int column = (mouseX - xOffset) / cellWidth;

                    grid = (row * numCols) + column;

					debug.stringOut("CircuitPane - " + inputs[grid][1]);

                    if(grid1 != grid && gridList[grid] != null && (inputs[grid][1] == 0 || inputs[grid][0] == 0) && gridList[grid] != imgList[3] && gridList[grid] != imgList[4]){

                        //if the gate to link to is a not gate, this needs to stop the visual model from connecting a second wire,
                        //this also stops the system models from assigning a second input
                        if(gridList[grid] == imgList[2]){//if the gate to link to is a not gate, this needs to stop the visual model from connecting a second wire
                            if(inputs[grid][0] == 0){//if the first input has an input, no other input can be added in the visual model
                                gridRef[grid1] = grid;

								debug.stringOut("CircuitPane - " + grid1 + " connected to: " + gridRef[grid1]);

                                assignInputs(false);
                            }
                        }

                        else{
                            gridRef[grid1] = grid;

                            debug.stringOut("CircuitPane - " + grid1 + " connected to: " + gridRef[grid1]);

                        	assignInputs(false);
                        }
                   }

                    //switches inputs between on and off when right clicked
                    else if(grid == grid1){
                        if(gridList[grid] == imgList[3]){
                            gridList[grid] = imgList[4];
                            grid = gridRef[grid1];
                            assignInputs(true);
                        }

                        else if(gridList[grid] == imgList[4]){
                            gridList[grid] = imgList[3];
                            grid = gridRef[grid1];
                            assignInputs(true);
                        }
                    }
                }
                repaint();
            }
            truthTable.updateGrid(gridList, gridRef);
        }

        public void mousePressed(MouseEvent e){

            int mouseX = e.getX();
            int mouseY = e.getY();

            //This find the the start of a wire, since the start of a wire is where the mouse is clicked
            //and the end is where it is released
            if(e.getButton() == MouseEvent.BUTTON3){
                if(!(mouseY < yOffset || mouseY > maximumX || mouseX < xOffset || mouseX > maximumX)){
                    row1 = (mouseY - yOffset) / cellHeight;
                    col1 = (mouseX - xOffset) / cellWidth;

                    grid1 = (row1 * numCols) + col1;
                }
            }
        }
    }
}