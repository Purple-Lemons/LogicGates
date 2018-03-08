package logicgates;

/**************************************************/
/*This class handles the logic for the logic gates*/
/**************************************************/

public class Logic {

    public Logic() {

    }

    public boolean and(int input1, int input2){
    	return ((input1 != 0) && (input2 != 0));
    }

    public boolean or(int input1, int input2){
    	return ((input1 != 0) || (input2 != 0));
    }

    public boolean xor(int input1, int input2){
       	return ((input1 != 0) != (input2 != 0));
    }

    public boolean xnor(int input1, int input2){
        return((input1 != 0) == (input2 != 0));
    }

    public boolean nand(int input1, int input2){
        return!((input1 != 0) && (input2 != 0));
    }

    public boolean nor(int input1, int input2){
        return!((input1 != 0) || (input2 != 0));
    }

    public boolean not(int input){
    	return input == 0;
    }

    public boolean output(int input){
        return input == 1;
    }
}