package sample;
import java.util.Scanner;

/**
 * @version 3
 * @author John Welsh
 * @date 11-7-2020
 */
//477 LOC
public class Connect4{

    /**Nested class containing the connect4 bot functions
     * */
    private class Connect4Bot {




        /**An int that tracks the turn count.
         *
         */
        public int turnCount;
        /**Constructor. Sets turnCount to 0
         */
        Connect4Bot(){
            turnCount=0;
        }
        /**Returns the index number of bot's turn
         * @return the desired index number
         */
        public int findSpot(){
            if (turnCount<4){
                turnCount++;
                return ((int)Math.floor(Math.random()));
            }
            boolean bH=blockHorizontal();
            boolean bV=blockVertical();
            boolean bD=blockDiagnol();
            if(bH==true){
                if (checkSpot(mostRecent-1)==true){
                    turnCount++;
                    return mostRecent-1;
                }
                if(checkSpot(mostRecent+1)==true){
                    return mostRecent+1;
                }
            }
            if(bV==true){
                if (checkSpot(mostRecent-7)==true){
                    turnCount++;
                    return mostRecent-7;
                }
            }
            if(bD==true){
                if (checkSpot(mostRecent+8)==true){
                    turnCount++;
                    return mostRecent+8;
                }
                if(checkSpot(mostRecent+6)==true){
                    turnCount++;
                    return mostRecent+6;
                }
            }
            String t=" ";
            for(int i=0;i<42;i++){
                if(board[i].equals(t)){
                    turnCount++;
                    return i;
                }
            }
            System.out.println("Unaccesible");
            turnCount++;
            return 1;






        }
        /**Returns true if the given variable is an available index
         * @param The desired index to determine availabilty
         * @return true if the desired index is available, otherwise false
         */
        public boolean checkSpot(int idx){

            if(idx <=6){
                return false;
            }
            else if(board[idx].equals(" ")){
                return true;
            }
            return false;
        }
        /**Checks to see if a horizontal block is needed
         * @return true if ye, false if nay
         * */
        public boolean blockHorizontal(){
            if(mostRecent+1<42){
                if(board[mostRecent+1].equals("X"))
                {
                    return true;
                }
            }
            if(mostRecent-1>0){
                if(board[mostRecent-1].equals("X")){
                    return true;
                }
            }
            return false;
        }
        /**Checks to see if a vertical block is needed
         * @return true if ye, false if nay
         * */
        public boolean blockVertical(){
            if(mostRecent+7<42){
                if(board[mostRecent+7].equals("X")){
                    return true;
                }
            }
            if(mostRecent-7>0){
                if(board[mostRecent-7].equals("X")){
                    return true;
                }
            }
            return false;



        }
        /**Checks to see if a diagnol block is needed
         * @return true if ye, false if nay
         * */
        public boolean blockDiagnol(){
            if(mostRecent+8<42){
                if(board[mostRecent+8]=="X"){
                    return true;
                }
                if(mostRecent+6<42){
                    if(board[mostRecent+6]=="X"){
                        return true;
                    }

                }
                if(mostRecent-8>0){
                    if(board[mostRecent-8]=="X"){
                        return true;
                    }
                }
                if(mostRecent-6>0){
                    if(board[mostRecent-6]=="X"){
                        return true;
                    }
                }
            }
            return false;


        }
    }

    public class Players{
        /**These strings represent the players
         * and the current players turn  */
        String PlayerX="PlayerX";
        String PlayerO="PlayerO";
        String current;
        /**Assigns values to players */
        Players(){
            PlayerX=this.PlayerX;
            PlayerO=this.PlayerO;
            current=PlayerX;
        }
        /**@return Returns the current player
         */
        String currentPlayer(){
            return current;
        }
        //Changes the current player value
        void nextPlayer(){
            if(current.equals(PlayerX)){
                current=PlayerO;
                return;}
            else if(current.equals(PlayerO)){
                current=PlayerX;
                return;
            }
        }
    }
    /**Reference to bot class*/
    Connect4Bot alexa=new Connect4Bot();

    /**Scanner to get player input*/
    private Scanner input=new Scanner(System.in);
    /**Initializes reference to players class*/
    Players p=new Players();
    /**The first array represents the board
     * The second array keeps track of
     * available columns to determine
     * if there is a tie.
     */
    public String[] board=new String[42];
    public boolean[] colAvail=new boolean[7];
    /**This integer determines the most
     * recently played index in the board
     * array.
     */
    public int mostRecent;
    /**These boolean values are set in the case there is
     * an end game event. They are then read to determine
     * the cause of the end game event.
     */
    public boolean stalemate=false;
    public boolean px=false;
    public boolean po=false;

    /**Initializes reference to players class*/
    public Connect4(){
        String blank=" ";
        for (int i=0;i<colAvail.length-1;i++){
            colAvail[i]=true;
        }
        for (int i=0;i<board.length;i++){
            board[i]=blank;
        }
    }
    /**This class starts by displaying a message
     * to the current player. Then collects
     * the column desired by the current
     * player. Then calls the addtoColumn method
     */
    public void playTurn(){
        if(p.currentPlayer().equals(p.PlayerO)){
            System.out.print("PlayerO-your turn");
        }
        else if(p.currentPlayer().equals(p.PlayerX)){
            System.out.print("PlayerX-your turn");
        }
        System.out.println("Choose a column number from 1-7");
        int col=input.nextInt()-1;
        boolean es=false;
        while(es == false){
            if(col<0 || col>6){
                System.out.println("Please choose a number from 1-7 this time.");
                col=input.nextInt();
            }
            else{
                es=true;
            }
        }
        addtoColumn(col);
    }
    /**This class does the same as playTurn but for the bot.
     *
     */
    public void botPlayTurn(){
        System.out.println("Bot's turn.");
        int botCol=alexa.findSpot();
        addtoColumn(botCol%7);


    }


    /**This method first checks the current player
     * to determine the character to be placed on
     * the borad.
     * @param The column that the player determined in playTurn()
     */
    public void addtoColumn(int column){
        boolean done=false;
        String t=" ";
        String cp="";

        if(p.currentPlayer().equals(p.PlayerX)){
            cp="X";
        }
        else if(p.currentPlayer().equals(p.PlayerO)){
            cp="O";
        }
        for(int i=column+35;done != true; i=i-7){
            if (i==column && !board[i].equals(t)){
                System.out.println("Please choose a different column");

                playTurn();
                done=true;


            }
            else if(board[i].equals(t)){
                mostRecent=i;


                board[i]=cp;
                done=true;
                p.nextPlayer();
                alexa.turnCount++;

                if(i < 7){
                    colAvail[i]=false;
                }
            }
        }
    }
    /** This method check for the following end game events:
     * 1.Stalemate
     * 2.Horizontal 4 in a row
     * 3.Vetical 4 in a row
     * 4.Check diagnol
     * @return Returns false if the end game event
     * never occurs. Returns True and updates
     * the determiner boolean values depending on
     * the type of end of game event.
     */
    public boolean baseCase(){
        boolean bC=false;
        int n=0;

        for(int i=0;i<colAvail.length-1;i++){
            if(colAvail[i]==false)n++;
        }
        if(n==8){
            stalemate=true;
            bC=true;
            return bC;

        }


        if(checkHorizontal()==true){
            if(board[mostRecent]=="X"){
                px=true;
            }
            else if(board[mostRecent]=="O"){
                po=true;
            }
            bC=true;
            return bC;


        }

        else if(checkVertical()==true){
            if(board[mostRecent]=="X"){
                px=true;
            }
            else if(board[mostRecent]=="O"){
                po=true;
            }
            bC=true;
            return bC;

        }

        else if(checkDiagnol()==true){
            if(board[mostRecent]=="X"){
                px=true;
            }
            else if(board[mostRecent]=="O"){
                po=true;
            }
            bC=true;
            return bC;

        }


        return bC;



    }
    /**This method does:
     * 1.Determine the column of the most recently
     * played postion.
     * 2.Checks left and right where neccesary in a loop
     * adding to a value keeping track of how many pieces
     * are in order
     * @return Returns false if there is not 4-in-a-row
     * true if there is
     */
    public boolean checkHorizontal(){
        boolean cH=false;
        int f=1;



        if(mostRecent % 7 ==0){
            for(int idx=mostRecent+1;idx % 7 != 4;idx++){

                if(board[mostRecent]==board[idx]){
                    f++;
                }
                else{
                    break;
                }
            }


        }
        else if(mostRecent % 7 ==1){
            if(board[mostRecent-1]==board[mostRecent]) f++;



            for(int idx=mostRecent+1;idx % 7 != 5;idx++){
                if(board[mostRecent]==board[idx]){
                    f++;
                }
                else{
                    break;
                }
            }
        }

        else if(mostRecent % 7 ==2){
            if(board[mostRecent-1]==board[mostRecent]) f++;
            if(board[mostRecent-2]==board[mostRecent]) f++;



            for(int idx=mostRecent+1;idx % 7 != 4;idx++){
                if(board[mostRecent]==board[idx]){
                    f++;
                }
                else{
                    break;
                }
            }
        }
        else if(mostRecent % 7 ==3){
            for(int idx=mostRecent+1;idx != mostRecent+4;idx++){
                if(board[mostRecent]==board[idx])f++;
                else {break;}

            }
            for(int idx=mostRecent-1;idx != mostRecent-4;idx--){
                if(board[mostRecent]==board[idx])f++;
                else {break;}

            }
        }

        else if(mostRecent % 7 ==4){
            if(board[mostRecent+1]==board[mostRecent]) f++;
            if(board[mostRecent+2]==board[mostRecent]) f++;
            for(int idx=mostRecent-1;idx % 7 != 0;idx=idx-1){

                if(board[mostRecent]==board[idx]){
                    f++;
                }
                else{
                    break;
                }
            }
        }
        else if(mostRecent % 7 ==5){
            if(board[mostRecent+1]==board[mostRecent]) f++;
            for(int idx=mostRecent-1;idx % 7 != 1;idx--){
                if(board[mostRecent]==board[idx]){
                    f++;
                }
                else{
                    break;
                }
            }
        }
        else if(mostRecent % 7 ==6){
            for(int idx=mostRecent-1;idx % 7 != 2;idx--){
                if(board[mostRecent]==board[idx]){
                    f++;
                }
                else{
                    break;
                }
            }




        }
        if(f >=4) cH=true;
        return cH;
    }

    /**Determines if there is 4 in a row up and down
     * @return false if there is not 4 in a row else
     * true
     */
    public boolean checkVertical(){

        boolean cV=false;
        int f=1;
        int thisCol;
        int max;

        thisCol=mostRecent%7;
        max=thisCol+35;

        for(int i=mostRecent+7;i<=max;i=i+7){
            if(board[mostRecent]==board[i])f++;
            else{break;}
        }

        for(int i=mostRecent-7;i>=thisCol;i=i-7){
            if(board[mostRecent]==board[i])f++;
            else{break;}
        }
        if(f>=4) cV=true;
        return cV;
    }

    /**This method checks the diagnol spaces in the order
     * up left, up right, down left, down right.
     * @return false if there is not 4 in a row else
     * true
     */
    public boolean checkDiagnol(){

        boolean cD=false;
        int f=1;
        int thisCol;
        int max;
        thisCol=mostRecent%7;
        max=thisCol+35;

        //Checks up left spaces

        for(int i=mostRecent-8;i>0 || i<41;i=i-8){

            if(i<0 || i>41) break;
            System.out.println(f);
            if(board[mostRecent]==board[i])f++;
            else break;
        }

        //Checks down right spaces
        for(int i=mostRecent+8;i>0 || i<41;i=i+8){
            if(i<0 || i>41) break;
            if(board[mostRecent]==board[i])f++;
            else break;
        }

        //Checks up right spaces
        for(int i=mostRecent-6;i>0 || i<41;i=i-6){
            if(i<0 || i>41) break;
            if(board[mostRecent]==board[i])f++;
            else break;
        }
        //Checks down left spaces
        for(int i=mostRecent+6;i>0 || i<41;i=i+6){
            if(i<0 || i>41) break;
            if(board[mostRecent]==board[i])f++;
            else break;
        }

        if(f >= 4)cD=true;
        return cD;
    }


}