package sample;

import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
//363 LOC
public class Connect4Server extends Application
    implements Connect4Constants{


    private int sessionNo = 1; // Number a session
    public Sock[][] cell=new Sock[6][7];
    public Sock mr;
    Connect4Client pOne=new Connect4Client();
    Connect4 pTwo=new Connect4();



    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        TextArea taLog = new TextArea();



        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
        primaryStage.setTitle("Connect4Server"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8004);
                Platform.runLater(() -> taLog.appendText(new Date() +
                        ": Server started at socket 8004\n"));

                // Ready to create a session for every two players
                while (true) {
                    Platform.runLater(() -> taLog.appendText(new Date() +
                            ": Wait for players to join session " + sessionNo + '\n'));

                    // Connect to player 1
                    Socket player1 = serverSocket.accept();

                    Platform.runLater(() -> {
                        taLog.appendText(new Date() + ": Player 1 joined session "
                                + sessionNo + '\n');
                        taLog.appendText("Player 1's IP address" +
                                player1.getInetAddress().getHostAddress() + '\n');
                    });

                    // Notify that the player is Player 1
                    new DataOutputStream(
                            player1.getOutputStream()).writeInt(PLAYER1);

                    // Connect to player 2
                    Socket player2 = serverSocket.accept();

                    Platform.runLater(() -> {
                        taLog.appendText(new Date() +
                                ": Player 2 joined session " + sessionNo + '\n');
                        taLog.appendText("Player 2's IP address" +
                                player2.getInetAddress().getHostAddress() + '\n');
                    });

                    // Notify that the player is Player 2
                    new DataOutputStream(
                            player2.getOutputStream()).writeInt(PLAYER2);

                    // Display this session and increment session number
                    Platform.runLater(() ->
                            taLog.appendText(new Date() +
                                    ": Start a thread for session " + sessionNo++ + '\n'));

                    // Launch a new thread for this session of two players
                    new Thread(new HandleASession(player1, player2)).start();
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
    class Sock{
        private char token;
        private boolean ava;
        int row;
        int col;
        boolean isTop=false;

        Sock(int r, int c){
            this.ava=false;
            this.token=' ';
            this.row=r;
            this.col=c;

        }
        public Sock findAvailable() {
            if (this.isAva() == true) {

                return this;
            } else {
                for (int j = 0; j <6; j++) {
                    if (cell[j][this.col].isAva()) return cell[j][this.col];
                }
            }


            System.out.println("Unaccesbile");
            return this;}
        public void next(){
            if(this.isTop==true){
                this.setAva(false);
                return;
            }
            else{

                this.setAva(false);
                cell[this.row-1][this.col].setAva(true);
            }
        }
        void setTop(boolean b){
            this.isTop=b;
        }




            void setTok ( char c){
                this.token = c;
            }
            public void setAva ( boolean b){
                this.ava = b;
            }
            char getTok () {
                return this.token;

            }
            boolean isAva () {
                return this.ava;
            }
        }






    // Define the thread class for handling a new session for two players
    class HandleASession implements Runnable, Connect4Constants {
        private Socket player1;
        private Socket player2;




        // Create and initialize cells


        private DataInputStream fromPlayer1;
        private DataOutputStream toPlayer1;
        private DataInputStream fromPlayer2;
        private DataOutputStream toPlayer2;

        // Continue to play
        private boolean continueToPlay = true;


        /** Construct a thread */
        public HandleASession(Socket player1, Socket player2) {
            this.player1 = player1;
            this.player2 = player2;
            char t=' ';


            // Initialize cells
            for (int i = 0; i < 6; i++){
                for (int j = 0; j < 7; j++){

                    cell[i][j]=new Sock(i,j);}}
            cell[5][0].setAva(true);
            cell[5][1].setAva(true);
            cell[5][2].setAva(true);
            cell[5][3].setAva(true);
            cell[5][4].setAva(true);
            cell[5][5].setAva(true);
            cell[5][6].setAva(true);

            cell[0][0].setTop(true);
            cell[0][1].setTop(true);
            cell[0][2].setTop(true);
            cell[0][3].setTop(true);
            cell[0][4].setTop(true);
            cell[0][5].setTop(true);
            cell[0][6].setTop(true);
        }



        /** Implement the run() method for the thread */
        public void run() {
            try {
                // Create data input and output streams
                DataInputStream fromPlayer1 = new DataInputStream(
                        player1.getInputStream());
                DataOutputStream toPlayer1 = new DataOutputStream(
                        player1.getOutputStream());
                DataInputStream fromPlayer2 = new DataInputStream(
                        player2.getInputStream());
                DataOutputStream toPlayer2 = new DataOutputStream(
                        player2.getOutputStream());

                // Write anything to notify player 1 to start
                // This is just to let player 1 know to start
                toPlayer1.writeInt(1);

                // Continuously serve the players and determine and report
                // the game status to the players
                while (true) {
                    // Receive a move from player 1
                    int row = fromPlayer1.readInt();
                    int column = fromPlayer1.readInt();
                    //INSERT CORRECTION HERE

                    int newRow=cell[row][column].findAvailable().row;

                    int newCol=cell[row][column].findAvailable().col;



                    cell[newRow][newCol].setTok('X');
                    mr=cell[newRow][newCol];
                    cell[newRow][newCol].setAva(false);
                    cell[newRow][newCol].next();


                    // Check if Player 1 wins
                    if (isWon('X',mr)) {
                        toPlayer1.writeInt(PLAYER1_WON);
                        toPlayer2.writeInt(PLAYER1_WON);
                        sendMove(toPlayer2, newRow, newCol);
                        break; // Break the loop
                    }
                    else if (isFull()) { // Check if all cells are filled
                        toPlayer1.writeInt(DRAW);
                        toPlayer2.writeInt(DRAW);
                        sendMove(toPlayer2, newRow, newCol);
                        break;
                    }
                    else {
                        // Notify player 2 to take the turn
                        toPlayer2.writeInt(CONTINUE);

                        // Send player 1's selected row and column to player 2
                        sendMove(toPlayer2, newRow, newCol);
                    }

                    // Receive a move from Player 2
                    row = fromPlayer2.readInt();
                    column = fromPlayer2.readInt();

                    newRow=cell[row][column].findAvailable().row;
                    newCol=cell[row][column].findAvailable().col;

                    cell[newRow][newCol].setTok('O');
                    mr=cell[newRow][newCol];
                    cell[newRow][newCol].next();



                    // Check if Player 2 wins
                    if (isWon('O',mr)) {
                        toPlayer1.writeInt(PLAYER2_WON);
                        toPlayer2.writeInt(PLAYER2_WON);
                        sendMove(toPlayer1, newRow, newCol);
                        break;
                    }
                    else {
                        // Notify player 1 to take the turn
                        toPlayer1.writeInt(CONTINUE);

                        // Send player 2's selected row and column to player 1
                        sendMove(toPlayer1, newRow, newCol);
                    }
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }

        /** Send the move to other player */
        private void sendMove(DataOutputStream out, int row, int column)
                throws IOException {





            out.writeInt(row); // Send row indexd
            out.writeInt(column); // Send column index
        }

        /** Determine if the cells are all occupied */
        private boolean isFull() {
            for (int i = 0; i < 6; i++)
                for (int j = 0; j < 7; j++)
                    if (cell[i][j].getTok() == ' ')
                        return false; // At least one cell is not filled

            // All cells are filled
            return true;
        }


        /** Determine if the player with the specified token wins */
        private boolean isWon(char token, Sock c) {
            int f=0;
            int row=c.row;
            int col=c.col;
            //Check Vertical

            for(int i=row;i>=0;i--){
                if(cell[i][col].getTok()==token)f++;
                else break;
            }
            if (f>=4){
                return true;
            }
            f=0;
            for(int i=row;i<=5;i++){
                if(cell[i][col].getTok()==token)f++;
                else break;
            }
            if (f>=4){
                return true;
            }
            f=0;

            //Check Horizontal Win

            for(int i=col;i>=0;i--){
                if(cell[row][i].getTok()==token)f++;
                else break;
            }
            if (f>=4){
                return true;
            }
            f=0;


            for(int i=col;i<=6;i++){
                if(cell[row][i].getTok()==token)f++;
                else break;
            }
            if (f>=4){
                return true;
            }
            f=0;
            //Check diagnol
            //Up right

            try{
                if(cell[row][col].getTok()==token &&
                        cell[row-1][col+1].getTok()==token &&
                        cell[row-2][col+2].getTok()==token &&
                        cell[row-3][col+3].getTok()==token){
                    f=4;

                }
            }
            catch (ArrayIndexOutOfBoundsException e){

            }

            if (f>=4){
                return true;
            }
            f=0;
            //Up Left

            try{
                if(cell[row][col].getTok()==token &&
                        cell[row-1][col-1].getTok()==token &&
                        cell[row-2][col-2].getTok()==token &&
                        cell[row-3][col-3].getTok()==token){
                    f=4;

                }
            }
            catch (ArrayIndexOutOfBoundsException e){

            }

            if (f>=4){
                return true;
            }
            f=0;
            //Down Right

            try{
                if(cell[row][col].getTok()==token &&
                        cell[row+1][col+1].getTok()==token &&
                        cell[row+2][col+2].getTok()==token &&
                        cell[row+3][col+3].getTok()==token){
                    f=4;

                }
            }
            catch (ArrayIndexOutOfBoundsException e){

            }

            if (f>=4){
                return true;
            }
            f=0;
            //Down Left

            try{
                if(cell[row][col].getTok()==token &&
                        cell[row+1][col-1].getTok()==token &&
                        cell[row+2][col-2].getTok()==token &&
                        cell[row+3][col-3].getTok()==token){
                    f=4;

                }
            }
            catch (ArrayIndexOutOfBoundsException e){

            }

            if (f>=4){
                return true;
            }
            f=0;



            return false;
        }
    }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

