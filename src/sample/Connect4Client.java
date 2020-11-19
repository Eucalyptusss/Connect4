package sample;
import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.stage.Stage;//464 LOC
public class Connect4Client extends Application
    implements Connect4Constants{

    /**
     * myTurn=Indicate whether the player has the turn
     * isBot=Indicate if a bot is playing
     * p2=Indicate if player 2 is a bot
     */

    private boolean myTurn = false;
    public boolean isBot= false;
    boolean p2;

    /** Indicate the token for the player **/
    private char myToken = ' ';

    /** Indicate the token for the other player **/
    private char otherToken = ' ';

    /** Create and initialize cells **/
    private Cell[][] cell =  new Cell[6][7];
    Cell k;

    /** Create and initialize a title label **/
    private Label lblTitle = new Label();

    /** Create and initialize a status label **/
    private Label lblStatus = new Label();

    /** Indicate selected row and column by the current move **/
    private int rowSelected;
    private int columnSelected;

    /** Input and output streams from/to server **/
    private DataInputStream fromServer;
    private DataOutputStream toServer;

    /** Continue to play? */
    private boolean continueToPlay = true;

    /** Wait for the player to mark a cell */
    private boolean waiting = true;

    /** Host name or ip **/
    private String host = "localhost";
    Cell mr;

    @Override /** Override the start method in the Application class */
    public void start(Stage primaryStage) {
        // Pane to hold cell
        GridPane pane = new GridPane();
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++)
                pane.add(cell[i][j] = new Cell(i, j), j, i);


        BorderPane borderPane = new BorderPane();
        mr=cell[0][0];
        cell[5][0].setAvailable(true);
        cell[5][1].setAvailable(true);
        cell[5][2].setAvailable(true);
        cell[5][3].setAvailable(true);
        cell[5][4].setAvailable(true);
        cell[5][5].setAvailable(true);
        cell[5][6].setAvailable(true);
        cell[0][0].setTop(true);
        cell[0][1].setTop(true);
        cell[0][2].setTop(true);
        cell[0][3].setTop(true);
        cell[0][4].setTop(true);
        cell[0][5].setTop(true);
        cell[0][6].setTop(true);
        borderPane.setTop(lblTitle);
        borderPane.setCenter(pane);
        borderPane.setBottom(lblStatus);


        /** Create a scene and place it in the stage **/
        Scene scene = new Scene(borderPane, 320, 350);
        primaryStage.setTitle("Connect4Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        // Connect to the server
        connectToServer();
    }
    /** Connect to the server **/
    private void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket(host, 8004);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // Control the game on a separate thread
        new Thread(() -> {
            try {
                // Get notification from the server
                int player = fromServer.readInt();


                // Am I player 1 or 2?
                if (player == PLAYER1) {
                    p2=false;
                    myToken = 'X';
                    otherToken = 'O';
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 1 with token 'RED'");
                        lblStatus.setText("Waiting for player 2 to join");
                    });

                    // Receive startup notification from the server
                    fromServer.readInt(); // Whatever read is ignored


                    // The other player has joined

                    Platform.runLater(() ->
                            lblStatus.setText("Player 2 has joined. I start first"));


                    // It is my turn

                    myTurn = true;
                }
                else if(isBot== true && player==PLAYER2){
                    p2=true;
                    myToken = 'O';
                    otherToken = 'X';
                    Platform.runLater(() -> {
                        lblTitle.setText("Bot with token 'BLACK'");
                        lblStatus.setText("Waiting for player 1 to move");
                    });


                }
                else if (player == PLAYER2) {
                    myToken = 'O';
                    otherToken = 'X';
                    Platform.runLater(() -> {
                        lblTitle.setText("Player 2 with token 'BLACK'");
                        lblStatus.setText("Waiting for player 1 to move");
                    });
                }

                // Continue to play
                while (continueToPlay) {
                    if (player == PLAYER1) {
                        waitForPlayerAction(); // Wait for player 1 to move
                        sendMove(); // Send the move to the server
                        receiveInfoFromServer(); // Receive info from the server
                    }
                    else if(player==PLAYER2 && isBot==true){

                        k=botTurn();
                        receiveInfoFromServer(); // Receive info from the server
                        sendMove(); // Send player 2's move to the server


                    }
                    else if (player == PLAYER2) {
                        receiveInfoFromServer(); // Receive info from the server
                        waitForPlayerAction(); // Wait for player 2 to move
                        sendMove(); // Send player 2's move to the server
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /** Wait for the player to mark a cell */
    private void waitForPlayerAction() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }

        waiting = true;
    }
    public Cell botTurn(){

        //Block Vertical
        int row=mr.row;
        int col=mr.column;

        System.out.println("Row for mr is "+row+" "+col);

        int z=0;
        for(int i=row;i>=0;i--){
            if(cell[i][col].getToken()=='X'){
                z++;
                if (z>=2 && cell[i][col].isAvailable()){
                    return cell[i][col].findAvailable();
                }
            }
            else break;
        }
        z=0;
        //Block Horizontal right
        for(int i=col;i<=6;i++){
            if(cell[row][i].getToken()=='X')z++;
            if (z>=2 && cell[row][i].isAvailable()){
                return cell[i][col].findAvailable();
            }

            else break;
        }
        z=0;
        //Block Horizontal left
        for(int i=col;i>=0;i--){
            if(cell[row][i].getToken()=='X')z++;
            if (z>=2 && cell[row][i].isAvailable()){
                return cell[i][col].findAvailable();
            }

            else break;
        }
        z=0;
        //Block Diagnol UpRight
        try{
            if(cell[row][col].getToken()=='X' &&
                    cell[row-1][col+1].getToken()=='X'){
                z=2;
                if(cell[row-2][col+2].isAvailable()){
                    return cell[row-2][col+2].findAvailable();}
            }
        }
        catch (ArrayIndexOutOfBoundsException e){

        }
        z=0;
        //Block Diagnol UpLeft
        try{
            if(cell[row][col].getToken()=='X' &&
                    cell[row-1][col-1].getToken()=='X'){
                z=2;
                if(cell[row-2][col-2].isAvailable()){
                    return cell[row-2][col-2].findAvailable();}
            }
        }
        catch (ArrayIndexOutOfBoundsException e){

        }
        z=0;
        //Block Diagnol DownRight
        try{
            if(cell[row][col].getToken()=='X' &&
                    cell[row+1][col+1].getToken()=='X'){
                z=2;
                if(cell[row+2][col+2].isAvailable()){
                    return cell[row+2][col+2].findAvailable();}
            }
        }
        catch (ArrayIndexOutOfBoundsException e){

        }
        z=0;
        //Block Diagnol DownLeft
        try{
            if(cell[row][col].getToken()=='X' &&
                    cell[row+1][col-1].getToken()=='X'){
                z=2;
                if(cell[row+2][col-2].isAvailable()){
                    return cell[row+2][col-2].findAvailable();}
            }
        }
        catch (ArrayIndexOutOfBoundsException e){

        }
        z=0;

        for(int i=0;i<=5;i++){
            for(int j=0;j<=6;j++){
                if (cell[i][j].isAvailable()) return cell[i][j].findAvailable();
            }
        }
        System.out.println("Inaccesble");
        return cell[3][3];

    }

    /** Send this player's move to the server */
    private void sendMove() throws IOException {
        if(isBot==true && p2==true){

            rowSelected=k.row;
            columnSelected=k.column;




        }

        toServer.writeInt(rowSelected); // Send the selected row
        toServer.writeInt(columnSelected); // Send the selected column
    }

    /** Receive info from the server */
    private void receiveInfoFromServer() throws IOException {
        // Receive game status
        int status = fromServer.readInt();

        if (status == PLAYER1_WON) {
            // Player 1 won, stop playing
            continueToPlay = false;
            if (myToken == 'X') {
                Platform.runLater(() -> lblStatus.setText("I won! (RED)"));
            }
            else if (myToken == 'O') {
                Platform.runLater(() ->
                        lblStatus.setText("Player 1 (RED) has won!"));
                receiveMove();
            }
        }
        else if (status == PLAYER2_WON) {
            // Player 2 won, stop playing
            continueToPlay = false;
            if (myToken == 'O') {
                Platform.runLater(() -> lblStatus.setText("I won! (BLACK)"));
            }
            else if (myToken == 'X') {
                Platform.runLater(() ->
                        lblStatus.setText("Player 2 (BLACK) has won!"));
                receiveMove();
            }
        }
        else if (status == DRAW) {
            // No winner, game is over
            continueToPlay = false;
            Platform.runLater(() ->
                    lblStatus.setText("Game is over, no winner!"));

            if (myToken == 'O') {
                receiveMove();
            }
        }
        else {
            receiveMove();
            Platform.runLater(() -> lblStatus.setText("My turn"));
            myTurn = true; // It is my turn
        }
    }

    private void receiveMove() throws IOException {
        // Get the other player's move
        int row;
        int column;
        if(isBot==true && p2==true){
            row=k.row;
            column=k.column;
        }
        else{
            row = fromServer.readInt();
            column = fromServer.readInt();

        }



        Platform.runLater(() -> cell[row][column].setToken(otherToken,cell[row][column]));
    }

    // An inner class for a cell
    public class Cell extends Pane {
        // Indicate the row and column of this cell in the board
        private int row;
        private int column;
        boolean aval;
        boolean isTop=false;

        // Token used for this cell
        private char token = ' ';
        private Cell cello;

        public Cell(int row, int column) {

            this.aval=false;
            this.row = row;
            this.column = column;
            this.setPrefSize(2000, 2000); // What happens without this?
            setStyle("-fx-border-color: black"); // Set cell's border
            this.setOnMouseClicked(e -> handleMouseClick());
        }
        public void setTop(boolean b){
            this.isTop=b;
        }
        public void next(){
            if(this.isTop=true){
                return;
            }
            else{
                this.setAvailable(false);
                cell[this.row-1][this.column].setAvailable(true);
            }
        }

        /** Return token */
        public char getToken() {
            return token;
        }
        public void setAvailable(boolean c){
            this.aval=c;

        }


        /** Set a new token */
        public void setToken(char c,Cell ch) {
            token = c;
            cello=ch;

            repaint();
        }
        public boolean isAvailable(){
            return aval;
        }
        public Cell findAvailable(){
            if (this.isAvailable()==true){

                return this;}
            else{
                for(int j=0;j<=6;j++){
                    if(cell[j][this.column].isAvailable()) return cell[j][this.column];
                }
            }

            System.out.println("Unaccesbile");
            return this;
        }

        protected void repaint() {
            if (token == 'X') {
                Ellipse ellipse = new Ellipse(cello.getWidth() / 2,
                        cello.getHeight() / 2, this.getWidth() / 2 - 10,
                        cello.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        cello.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        cello.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        cello.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        cello.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.RED);
                cello.setAvailable(false);
                mr=cello;
                if( cell[cello.row][cello.column].isTop==false){
                    cell[cello.row-1][cello.column].setAvailable(true);}




                getChildren().add(ellipse); // Add the ellipse to the pane
            }
            else if (token == 'O') {
                Ellipse ellipse = new Ellipse(cello.getWidth() / 2,
                        cello.getHeight() / 2, cello.getWidth() / 2 - 10,
                        cello.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        cello.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        cello.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        cello.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        cello.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.BLACK);
                cello.setAvailable(false);
                if( cell[cello.row][cello.column].isTop==false){
                cell[cello.row-1][cello.column].setAvailable(true);}


                getChildren().add(ellipse); // Add the ellipse to the pane
            }
        }

        /* Handle a mouse click event */
        private void handleMouseClick() {
            // If cell is not occupied and the player has the turn
            if (token == ' ' && myTurn) {
                rowSelected=findAvailable().row;
                columnSelected=findAvailable().column;
                findAvailable().setToken(myToken,findAvailable());



                //setToken(myToken);  // Set the player's token in the cell
                myTurn = false;

                lblStatus.setText("Waiting for the other player to move");
                waiting = false; // Just completed a successful move

            }
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

