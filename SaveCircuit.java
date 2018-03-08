package logicgates;

import java.io.*;
import java.awt.image.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.*;

import java.util.*;

public class SaveCircuit implements Commons{

	CircuitPane circuitPane;
	TruthTable truthTable;
    Debug debug;
    LogicGates logicGates;

	Object[] gridData;
	Object[] tempGridList;

	int[] outputData;
	int[] gridRefData;
	int[][] inputData;

	BufferedImage[] newGridList = new BufferedImage[numRows * numCols];

	public SaveCircuit(CircuitPane cPImport, TruthTable tableImport, Debug debugIn, LogicGates lGImport){
		circuitPane = cPImport;
		truthTable = tableImport;
        debug = debugIn;
        logicGates = lGImport;

        tempGridList = new Object[numCols * numRows];
        gridRefData = new int[numCols * numRows];
        outputData = new int[numCols * numRows];
        inputData = new int[numCols * numRows][5];
	}

	public void saveToFile(BufferedImage[] gridList, BufferedImage[] imgList, int[] gridRef, boolean[] outputs, int[][] inputs){

		convertImages(gridList, imgList);
		convertBoolean(outputs);

		try{

                    FileDialog fd = new FileDialog(logicGates, "Choose a file", FileDialog.SAVE);
                    fd.setDirectory("C:\\");
                    fd.setFile("*.txt");
                    fd.setVisible(true);
                    String fileName = fd.getDirectory() + fd.getFile();

                    File file = new File(fileName);
                    file.createNewFile();

                    FileWriter writer = new FileWriter(file);
                    BufferedWriter bWriter = new BufferedWriter(writer);

                    for(int i = 0; i < numRows * numCols; i ++){
                        bWriter.write(gridData[i].toString());
                        bWriter.newLine();
                    }

                    for(int i = 0; i < gridRef.length; i++){
                    	bWriter.write(Integer.toString(gridRef[i]));
                    	bWriter.newLine();
                    }

                    for(int i = 0; i < outputData.length; i++){
                    	bWriter.write(Integer.toString(outputData[i]));
                    	bWriter.newLine();
                    }

					for(int i = 0; i < inputs.length; i++){
						for(int j = 0; j < inputs[i].length; j++){
							bWriter.write(Integer.toString(inputs[i][j]));
							bWriter.newLine();
						}
					}

					bWriter.close();
                    writer.flush();
                    writer.close();
		}
		catch(IOException e){
			debug.objectOut("SaveCiruit - Exception - e");
		}
	}

	public BufferedImage[] loadFile(BufferedImage[] imgList){
		String[] fileContents = new String[newGridList.length];

		try{
                    FileDialog fd = new FileDialog(logicGates, "Choose a file", FileDialog.LOAD);
                    fd.setDirectory("C:\\");
                    fd.setFile("*.txt");
                    fd.setVisible(true);
                    String fileName = fd.getDirectory() + fd.getFile();

                    FileReader reader = new FileReader(fileName);
                    BufferedReader bReader = new BufferedReader(reader);

                    for(int i = 0; i < fileContents.length; i++){
                    	fileContents[i] = bReader.readLine();
                    }

                    for(int i = 0; i < gridRefData.length; i++){
                    	gridRefData[i] = Integer.parseInt(bReader.readLine());
                    }

                    for(int i = 0; i < outputData.length; i++){
                    	outputData[i] = Integer.parseInt(bReader.readLine());
                    }

                    for(int i = 0; i < inputData.length; i++){
                    	for(int j = 0; i < inputData[i].length; i++){
                    		inputData[i][j] = Integer.parseInt(bReader.readLine());
                    	}
                    }

		}
		catch(IOException e){
			debug.objectOut("SaveCiruit - Exception - e");
		}

		int position = 0;

		for(int i = 0; i < fileContents.length; i++){
			switch(Integer.parseInt(fileContents[i])){
				case 0:
					newGridList[i] = imgList[0];
					break;
				case 1:
					newGridList[i] = imgList[1];
					break;
				case 2:
					newGridList[i] = imgList[2];
					break;
				case 3:
					newGridList[i] = imgList[3];
					break;
				case 4:
					newGridList[i] = imgList[4];
					break;
				case 5:
					newGridList[i] = imgList[5];
					break;
				case 6:
					newGridList[i] = imgList[6];
					break;
				case 7:
					newGridList[i] = imgList[7];
					break;
				case 8:
					newGridList[i] = imgList[8];
					break;
                case 9:
                    newGridList[i] = imgList[9];
                    break;
                case 10:
                    newGridList[i] = imgList[10];
                    break;
				default:
					newGridList[i] = null;
					break;
			}
		}
		return newGridList;
	}

	public boolean[] getOutputs(){
		boolean[] bOutputs = new boolean[outputData.length];

		for(int i = 0; i < bOutputs.length; i++){
			bOutputs[i] = outputData[i] == 1;
		}

		return bOutputs;
	}

	public int[][] getInputs(){
		return inputData;
	}

	public int[] getGridRef(){
		return gridRefData;
	}

	public void convertImages(BufferedImage[] gridList, BufferedImage[] imgList){

		gridData = new Object[gridList.length];

		for(int i = 0; i < gridList.length; i++){

			if(gridList[i] == null){
				gridData[i] = new Integer(11);//11 represents a space in the save file
			}

			if(gridList[i] == imgList[0]){
				gridData[i] = new Integer(0);
			}

			if(gridList[i] == imgList[1]){
				gridData[i] = new Integer(1);
			}

			if(gridList[i] == imgList[2]){
				gridData[i] = new Integer(2);
			}

			if(gridList[i] == imgList[3]){
				gridData[i] = new Integer(3);
			}

			if(gridList[i] == imgList[4]){
				gridData[i] = new Integer(4);
			}

			if(gridList[i] == imgList[5]){
				gridData[i] = new Integer(5);
			}

			if(gridList[i] == imgList[6]){
				gridData[i] = new Integer(6);
			}

			if(gridList[i] == imgList[7]){
				gridData[i] = new Integer(7);
			}

			if(gridList[i] == imgList[8]){
				gridData[i] = new Integer(8);
			}

			if(gridList[i] == imgList[9]){
				gridData[i] = new Integer(9);
			}

			if(gridList[i] == imgList[10]){
				gridData[i] = new Integer(10);
			}
		}
	}

	public void convertBoolean(boolean[] list){
		for(int i = 0; i < list.length; i++){
			if(list[i] == true){
				outputData[i] = 1;
			}
			else{
				outputData[i] = 0;
			}
		}
	}

	public void fileDialog(){
		JFrame fileFrame = new JFrame("Load Files");
		fileFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		FileDialog fileDialog = new FileDialog(fileFrame, "Load Files");

		fileDialog.setVisible(true);
	}
}