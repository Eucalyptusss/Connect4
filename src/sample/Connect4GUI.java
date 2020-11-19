/**
 * @author John Welsh
 * @version 4.0
 * @11-13-2020
 */
package sample;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;


import java.util.Arrays;
//533 LOC
public class Connect4GUI extends Application
{
    /**
     * whoseTurn represents the current player's turn
     * bot represents wheter or not the player wants to play a bot
     * cell is the game board
     * most recent is the most recently played cell after any given turn
     */
    // Indicate which player has a turn, initially it is the X player
    private char whoseTurn = 'X';
    //X=red
    //O=black
    private boolean bot=false;


    // Create and initialize cell
    private Cell[][] cell = new Cell[6][7];
    public Cell mostRecent=cell[0][5];

    // Create and initialize a status label
    private Label lblStatus = new Label("X's turn to play");

    /**
     * Starting point
     * @param args
     */
    public static void main(String[]args){
    launch(args);

}


    /**
     * Starting point
     * @param primaryStage
     */
    @Override // Override the start method in the Application class
    public void start(Stage primaryStage)
    {
        Button robot=new Button("Bot");
        Button player=new Button("Player");
        Text text=new Text();
        text.setText("Would you like to play a player or a bot?");
        text.setLayoutX(250);
        text.setLayoutY(100);
        text.setFont(Font.font(30));

        robot.setTranslateX(300);
        robot.setTranslateY(500);
        robot.setFont(Font.font(25));

        player.setLayoutX(600);
        player.setLayoutY(500);
        player.setFont(Font.font(25));

        Pane r= new Pane();
        r.getChildren().add(robot);
        r.getChildren().add(player);
        r.getChildren().add(text);

        Scene sc=new Scene(r,1000,1000);
        primaryStage.setScene(sc);
        primaryStage.show();
        /**
         * This action prepares the board for a pvb match
         */
        robot.setOnAction((ActionEvent event)->{
            bot=true;
            GridPane pane = new GridPane();

            for (int i = 0; i < 6; i++){
                for (int j = 0; j < 7; j++){
                    pane.add(cell[i][j] = new Cell(), j, i);
                    cell[i][j].setColumn(j);
                    cell[i][j].setRow(i);
                    cell[i][j].setAvailable(false);}}


            BorderPane borderPane = new BorderPane();
            cell[5][0].setAvailable(true);
            cell[5][1].setAvailable(true);
            cell[5][2].setAvailable(true);
            cell[5][3].setAvailable(true);
            cell[5][4].setAvailable(true);
            cell[5][5].setAvailable(true);
            cell[5][6].setAvailable(true);

            cell[0][0].setTop();
            cell[0][1].setTop();
            cell[0][2].setTop();
            cell[0][3].setTop();
            cell[0][4].setTop();
            cell[0][5].setTop();
            cell[0][6].setTop();


            borderPane.setCenter(pane);
            borderPane.setBottom(lblStatus);


            // Create a scene and place it in the stage
            Scene scene = new Scene(borderPane, 450, 170);
            primaryStage.setTitle("Connect4"); // Set the stage title
            primaryStage.setScene(scene); // Place the scene in the stage
            primaryStage.show(); // Display the stage
        });
        /**
         * This action sets up the board preparing for a pvp game.
         */
        player.setOnAction((ActionEvent event)->{
            GridPane pane = new GridPane();

            for (int i = 0; i < 6; i++){
                for (int j = 0; j < 7; j++){
                    pane.add(cell[i][j] = new Cell(), j, i);
                    cell[i][j].setColumn(j);
                    cell[i][j].setRow(i);
                    cell[i][j].setAvailable(false);}}


            BorderPane borderPane = new BorderPane();
            cell[5][0].setAvailable(true);
            cell[5][1].setAvailable(true);
            cell[5][2].setAvailable(true);
            cell[5][3].setAvailable(true);
            cell[5][4].setAvailable(true);
            cell[5][5].setAvailable(true);
            cell[5][6].setAvailable(true);

            cell[0][0].setTop();
            cell[0][1].setTop();
            cell[0][2].setTop();
            cell[0][3].setTop();
            cell[0][4].setTop();
            cell[0][5].setTop();
            cell[0][6].setTop();


            borderPane.setCenter(pane);
            borderPane.setBottom(lblStatus);


            // Create a scene and place it in the stage
            Scene scene = new Scene(borderPane, 450, 170);
            primaryStage.setTitle("Connect4"); // Set the stage title
            primaryStage.setScene(scene); // Place the scene in the stage
            primaryStage.show(); // Display the stage

        });



        // Pane to hold cell

    }

    /**
     * This method represents the bot's calculation on where to play a turn
     * @return  The cell chosen by the bot
     */
    public Cell botTurn(){
        //Block Vertical
        int row=mostRecent.getRow();
        int col=mostRecent.getColumn();

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

    /** Determine if the cell are all occupied */
    public boolean isFull()
    {
        for (int i = 0; i < 6; i++)
            for (int j = 0; j < 7; j++)
                if (cell[i][j].getToken() == ' ')
                    return false;

        return true;
    }

    /** Determine if the player with the specified token wins
     * @return True if the player has won, false otherwise
     * */
    public boolean isWon(Cell c,char token)
    {
        int f=0;
        int row=c.getRow();
        int col=c.getColumn();
        //Check Vertical

        for(int i=row;i>=0;i--){
            if(cell[i][col].getToken()==token)f++;
            else break;
        }
        if (f>=4){
            return true;
        }
        f=0;
        for(int i=row;i<=5;i++){
            if(cell[i][col].getToken()==token)f++;
            else break;
        }
        if (f>=4){
            return true;
        }
        f=0;

        //Check Horizontal Win

        for(int i=col;i>=0;i--){
            if(cell[row][i].getToken()==token)f++;
            else break;
        }
        if (f>=4){
            return true;
        }
        f=0;


        for(int i=col;i<=6;i++){
            if(cell[row][i].getToken()==token)f++;
            else break;
        }
        if (f>=4){
            return true;
        }
        f=0;
        //Check diagnol
        //Up right

        try{
            if(cell[row][col].getToken()==token &&
                    cell[row-1][col+1].getToken()==token &&
                    cell[row-2][col+2].getToken()==token &&
                    cell[row-3][col+3].getToken()==token){
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
            if(cell[row][col].getToken()==token &&
                    cell[row-1][col-1].getToken()==token &&
                    cell[row-2][col-2].getToken()==token &&
                    cell[row-3][col-3].getToken()==token){
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
            if(cell[row][col].getToken()==token &&
                cell[row+1][col+1].getToken()==token &&
                        cell[row+2][col+2].getToken()==token &&
                        cell[row+3][col+3].getToken()==token){
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
            if(cell[row][col].getToken()==token &&
                    cell[row+1][col-1].getToken()==token &&
                    cell[row+2][col-2].getToken()==token &&
                    cell[row+3][col-3].getToken()==token){
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

    /**
     * This is an inner class of the board positions
     */
    public class Cell extends Pane
    {
        /**
         * token is the current player's token
         * ava represents if the location is available to play on
         * column is the column location of this board piece
         * row is the row location of this board piece
         * top determines if this piece is a top piece
         */
        private char token = ' ';
        private boolean ava;
        private int column;
        private boolean top=false;
        private int row;

        /**
         * Constructor
         */
        public Cell()
        {
            setStyle("-fx-border-color: black");
            this.setPrefSize(2000, 2000);
            this.setOnMouseClicked(e -> handleMouseClick());
        }
        /**
         * Sets the row
         */
        public void setRow(int n){
            row=n;

        }
        /**
         * Sets the board piece above the current board piece to available
         */
        public void setNext(){
            if(this.isTop()==true){
                return;
            }
            else{
            this.ava=false;
            cell[this.row-1][this.column].setAvailable(true);}
    }
        /**
         * @return  row
         */
        public int getRow(){
            return row;
        }
        /**
         * Set column to @param
         */
        public void setColumn(int n){
            column=n;

        }
        /**
         * @return  Column
         */
        public int getColumn(){
            return column;
        }
        /**
         * set ava to @param
         */
        public void setAvailable(boolean c){
            ava=c;

        }
        /**
         * @return ava
         */
        public boolean isAvailable(){
            return ava;
        }

        /** Return token */
        public char getToken() {
            return token;
        }
        /**
         * sets this area to top
         */
        public void setTop(){
            top=true;

        }
        /**
         * @return  top
         */
        public boolean isTop(){
            return top;
        }
        /**
         * @return  available cell
         */
        public Cell findAvailable(){
            if (this.isAvailable()==true){

                return this;}
            else{
                for(int j=0;j<6;j++){
                    if(cell[j][this.column].isAvailable()) return cell[j][this.column];
                }
            }

            System.out.println("Unaccesbile");
            return this;




        }


        /** Set a new token */
        public void setToken(char c, Cell cell)
        {
            token = c;



            if (token == 'X')
            {




                Ellipse ellipse = new Ellipse(cell.getWidth() / 2,
                        cell.getHeight() / 2, cell.getWidth() / 2 - 10,
                        cell.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(cell.widthProperty().divide(2));
                ellipse.centerYProperty().bind(cell.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(cell.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(cell.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.RED);
                cell.setNext();
                mostRecent=cell;
                getChildren().add(ellipse); // Add the ellipse to the pane
            }
            else if (token == 'O') {
                Ellipse ellipse = new Ellipse(cell.getWidth() / 2,
                        cell.getHeight() / 2, cell.getWidth() / 2 - 10,
                        cell.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(cell.widthProperty().divide(2));
                ellipse.centerYProperty().bind(cell.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(cell.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(cell.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.BLACK);
                cell.setNext();
                mostRecent=cell;

                getChildren().add(ellipse); // Add the ellipse to the pane
            }
        }



        /** Handle a mouse click event */
        private void handleMouseClick()
        {
            // If cell is empty and game is not over







            if (token == ' ' && whoseTurn != ' ')
            {

                findAvailable().setToken(whoseTurn,findAvailable());

                // Check game status
                if (isWon(mostRecent,whoseTurn)) {
                    lblStatus.setText(whoseTurn + " won! The game is over");
                    whoseTurn = ' '; // Game is over
                }
                else if (isFull()) {
                    lblStatus.setText("Draw! The game is over");
                    whoseTurn = ' '; // Game is over
                }
                else {
                    // Change the turn
                    whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                    // Display whose turn
                    lblStatus.setText(whoseTurn + "'s turn");
                    if(bot==true && whoseTurn=='O'){
                        findAvailable().setToken(whoseTurn,botTurn());

                        // Change the turn
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        // Display whose turn
                        lblStatus.setText(whoseTurn + "'s turn");

                    }
                }
            }
        }
    }

}