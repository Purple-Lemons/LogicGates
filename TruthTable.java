package logicgates;

/*****************************************************/
/*This class creates the truth table and handles data*/
/*flow in and out of it from the circuit diagram     */
/*****************************************************/
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

public class TruthTable extends JPanel implements Commons{

    BufferedImage[] gridList = new BufferedImage[numRows * numCols];
    BufferedImage[] imgList;
    int[] gridRef = new int[numRows * numCols];

    ArrayList<Integer> inputs = new ArrayList<Integer>();
    ArrayList<Integer> functions = new ArrayList<Integer>();
    ArrayList<String> functionTitles = new ArrayList<String>();
    int[][] functionData;
    ArrayList<Integer> fInputs1 = new ArrayList<Integer>();
    ArrayList<Integer> fInputs2 = new ArrayList<Integer>();
    ArrayList<Integer> gateTypes = new ArrayList<Integer>();

    JTable table;
    JTableHeader header;

    JTable functionTable;
    JTableHeader functionHeader;//used to display the functions performed by logic gates, seperatly from the inputs table to stop overlap

    char ch = 'A';//used to cycle through inputs for inout table header
    int newNumRows = 0;//stores the new number of rows
    int currentNumRows = 0;//stores the current number of rows
    int numColumns = 0;//stores number of columns in inputs table

    int[][] bits;

    Logic logic;
    Debug debug;

    DefaultTableModel tableModel;
    DefaultTableModel functionTableModel;

    public TruthTable(BufferedImage[] imgListImport, Debug debugIn){
        setDoubleBuffered(true);
        setFocusable(true);

        addTables();

        imgList = imgListImport;
        logic = new Logic();
        debug = debugIn;
    }

    public void addTables(){//This class creates two tables: One to represent inputs and one to represent Logic Gate operations
        tableModel = new MyDefaultTableModel();
        table = new JTable(tableModel);
        table.setFillsViewportHeight(false);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 1000));
        header = table.getTableHeader();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        functionTableModel = new MyDefaultTableModel();
        functionTable = new JTable(functionTableModel);
        table.setFillsViewportHeight(false);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 1000));
        functionHeader = functionTable.getTableHeader();
        functionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		//This JPanel uses a Grid Bag Layout so that the table can be arranged freely into a grid
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

		//Adds tables headers to the top row of the grid bag layout
        c.gridy = 0;
        this.add(header, c);

        c.gridy = 0;
        this.add(functionHeader, c);

		//Adds table content to the second row of the grir bag layout
        c.gridy= 1;
        this.add(table, c);

        c.gridy = 1;
        this.add(functionTable, c);
    }

    public void fillTable(){

        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.getDataVector().removeAllElements();//removes table content, so that it can be re-added with no empty cells

        newNumRows = (int)Math.pow(2, inputs.size());

        int rowHeight = table.getRowHeight();

        this.setPreferredSize(new Dimension(table.getWidth() + functionTable.getWidth(), (newNumRows + 1) * rowHeight));

        fillInputs();

        for(int i = 0; i < newNumRows; i++){//adds rows

            String[] bitString = new String[bits[i].length];

            for(int j = 0; j < bits[i].length; j++){
            	bitString[j] = Integer.toString(bits[i][j]);
            }

            ((DefaultTableModel) table.getModel()).addRow(bitString);
        }//adds new table content

        currentNumRows = newNumRows;
    }

    public void fillFunctionsTable(boolean update){
        if(functionTitles.size() > 0){
            DefaultTableModel model = (DefaultTableModel)functionTable.getModel();
            model.getDataVector().removeAllElements();

            if(!update){//checks if this function i being used to update the table rows or add data to the whole table
                for(int i = functionTitles.size() - 1; i < functionTitles.size(); i++){
                	((DefaultTableModel) functionTable.getModel()).addColumn(functionTitles.get(i));
                }
            }

            for(int i = 0; i < newNumRows; i++){

                String[] bitString = new String[bits[i].length];

                for(int j = 0; j < functionData[i].length; j++){
                	bitString[j] = Integer.toString(functionData[i][j]);
                }
                ((DefaultTableModel) functionTable.getModel()).addRow(bitString);
            }
        }
    }

    public void setFunctionTitles(int grid, int grid1, int gateType, boolean isSwitchClick){
    	if(!isSwitchClick){
            functions.add(gridRef[grid]);

            String function = "";
            String firstInput = "";
            String secondInput = "";

            String title = "";

            for(int i = 0; i < inputs.size(); i++){//finds which column each input corresponds to
                if(grid == inputs.get(i)){
                        firstInput = table.getColumnName(i);
                        fInputs1.add(i);
                }

                if(grid1 == inputs.get(i)){
                        secondInput = table.getColumnName(i);
                        fInputs2.add(i);
                }
            }

            for(int i = 0; i < functions.size(); i++){
                    if(grid == functions.get(i)){
                            firstInput = functionTable.getColumnName(i);
                            fInputs1.add(i);
                    }

                            if(grid1 == functions.get(i)){
                                    secondInput = functionTable.getColumnName(i);
                                    fInputs2.add(i);
                            }
            }

            switch(gateType){//decides which gate is being used
                case 0:
                    title = firstInput + " AND " + secondInput;
                    break;
                case 1:
                    title = firstInput + " OR " + secondInput;
                    break;
                case 2:
                    title = "NOT " + firstInput;
                    break;
                case 5:
                    title = firstInput + " XOR " + secondInput;
                    break;
                case 6:
                    title = firstInput + " XNOR " + secondInput;
                    break;
                case 7:
                    title = firstInput + " NOR " + secondInput;
                    break;
                case 8:
                    title = firstInput + " NAND " + secondInput;
                    break;
            }
            gateTypes.add(gateType);

            functionTitles.add(title);

            setFunction(false);
    	}
    }

    public void setFunction(boolean update){

        functionData = new int[newNumRows][fInputs1.size()];

        for(int i = 0; i < fInputs1.size(); i++){
            switch(gateTypes.get(i)){
                case 0:
                    for(int j = 0; j < newNumRows; j++){
                        if(logic.and(bits[j][fInputs1.get(i)], bits[j][fInputs2.get(i)])){
                            functionData[j][i] = 1;
                        }
                        else{
                            functionData[j][i] = 0;
                        }
                    }
                    break;
                case 1:
                    for(int j = 0; j < newNumRows; j++){
                        if(logic.or(bits[j][fInputs1.get(i)], bits[j][fInputs2.get(i)])){
                            functionData[j][i] = 1;
                        }
                        else{
                            functionData[j][i] = 0;
                        }
                    }
                    break;
                case 2:
                    for(int j = 0; j < newNumRows; j++){
                        if(logic.not(bits[j][fInputs1.get(i)])){
                            functionData[j][i] = 1;
                        }
                        else{
                            functionData[j][i] = 0;
                        }
                    }
                    break;
                case 5:
                    for(int j = 0; j < newNumRows; j++){
                        if(logic.xor(bits[j][fInputs1.get(i)], bits[j][fInputs2.get(i)])){
                            functionData[j][i] = 1;
                        }
                        else{
                            functionData[j][i] = 0;
                        }
                    }
                    break;
                case 6:
                    for(int j = 0; j < newNumRows; j++){
                        if(logic.xnor(bits[j][fInputs1.get(i)], bits[j][fInputs2.get(i)])){
                            functionData[j][i] = 1;
                        }
                        else{
                            functionData[j][i] = 0;
                        }
                    }
                    break;
                case 7:
                    for(int j = 0; j < newNumRows; j++){
                        if(logic.nor(bits[j][fInputs1.get(i)], bits[j][fInputs2.get(i)])){
                            functionData[j][i] = 1;
                        }
                        else{
                            functionData[j][i] = 0;
                        }
                    }
                    break;
                case 8:
                    for(int j = 0; j < newNumRows; j++){
                        if(logic.nand(bits[j][fInputs1.get(i)], bits[j][fInputs2.get(i)])){
                            functionData[j][i] = 1;
                        }
                        else{
                            functionData[j][i] = 0;
                        }
                    }
                    break;
            }
        }

        fillFunctionsTable(update);
    }

    public void addValue(int position){

        inputs.add(position);

        tableModel.addColumn(ch);
        //table.addColumn(new TableColumn(ch));
        ch++;

        numColumns++;
        fillTable();

        if(functionTitles.size() > 0){
            setFunction(true);
        }
    }

    public void removeValue(int positionToRemove){

    	numColumns--;

        char titleToRemove = 'A';
        int columnToRemove = 0;

        for(int i = 0; i < inputs.size(); i++){
            if(inputs.get(i) == positionToRemove){
                i = inputs.size();
            }
            else{
                columnToRemove++;
                ch++;//Steps through characters until correct header is found
            }
        }

        inputs.remove(new Integer(positionToRemove));

        MyDefaultTableModel model = (MyDefaultTableModel)table.getModel();
        MyDefaultTableModel myModel = (MyDefaultTableModel)table.getModel();
        TableColumn column = table.getColumnModel().getColumn(columnToRemove);
        int columnModelIndex = column.getModelIndex();
        Vector data = tableModel.getDataVector();
        Vector columnIds = model.getColumnIndentifiers();

        table.removeColumn(column);

        columnIds.removeElementAt(columnModelIndex);

        for(int i = 0; i < data.size(); i++){
            Vector row = (Vector)data.get(i);
            row.removeElementAt(columnModelIndex);
        }
        tableModel.setDataVector(data, columnIds);

        Enumeration Enum = table.getColumnModel().getColumns();

        for(; Enum.hasMoreElements(); ){
            TableColumn c = (TableColumn)Enum.nextElement();
            if(c.getModelIndex() >= columnModelIndex){
                    c.setModelIndex(c.getModelIndex()-1);
            }
        }
        tableModel.fireTableStructureChanged();

        removeFunction(titleToRemove);

        fillTable();
    }

    public void removeFunction(char titleToRemove){//removes columns from the functions table if they are relevant to the inputs table columns that are being removed
        int indexToRemove = 0;
        boolean remove = false;

        for(int i = 0; i < functionTable.getColumnCount(); i++){
            if(functionTitles.get(i).charAt(0) == titleToRemove || functionTitles.get(i).charAt(functionTitles.get(i).length()) == titleToRemove){
                remove = true;//there is an item to remove
                indexToRemove = i;
                i = functionTable.getColumnCount();//kills the loop when index is found
            }
        }

        //removes a column and all of it's corresponding data and information
        if(remove){//If the column to be removed is valid
            MyDefaultTableModel model = (MyDefaultTableModel)functionTable.getModel();
            MyDefaultTableModel myModel = (MyDefaultTableModel)functionTable.getModel();
            TableColumn column = functionTable.getColumnModel().getColumn(indexToRemove);
            int columnModelIndex = column.getModelIndex();
            Vector data = functionTableModel.getDataVector();
            Vector columnIds = model.getColumnIndentifiers();

            //removes the column from the table
            functionTable.removeColumn(column);

            //removes the column from the table model
            columnIds.removeElementAt(columnModelIndex);

            //removes all data in the rows that column is linked to
            for(int i = 0; i < data.size(); i++){
                Vector row = (Vector)data.get(i);
                row.removeElementAt(columnModelIndex);
            }
            functionTableModel.setDataVector(data, columnIds);

            Enumeration Enum = functionTable.getColumnModel().getColumns();

            for(; Enum.hasMoreElements(); ){
                TableColumn c = (TableColumn)Enum.nextElement();
                if(c.getModelIndex() >= columnModelIndex){
                        c.setModelIndex(c.getModelIndex()-1);
                }
            }
            functionTableModel.fireTableStructureChanged();
        }
    }

    public void updateGrid(BufferedImage[] gridListImport, int[] gridRefImport){
        gridList = gridListImport;
        gridRef = gridRefImport;
    }//Updates where everything is on the grid when it is changed in the circuit pane

    public void fillInputs(){
        bits = new int[newNumRows][inputs.size() + 3];//to store bits for inputs part of table

        for(int i = 0; i < newNumRows; i++){//turns denary into binary
            int a = i;
            int pos = 0;
            while(a > 0){
                int bit = a % 2;
                a = a / 2;
                bits[i][pos] = bit;
                pos++;
            }//adds relevent input values to bit array

            for(pos = pos; pos < bits[i].length; pos++){
                    bits[i][pos] = 0;
            }//adds extra zeros as to eliminate empty rows in table
        }
    }

    class MyDefaultTableModel extends DefaultTableModel {
        public Vector getColumnIndentifiers(){//used to get the relevant column identifiers from tables
            return columnIdentifiers;
        }
    }
}