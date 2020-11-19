package sample;
//Case Study: Developing a Tic-Tac-Toe Game

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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import java.util.Scanner;

/**
 * @version 3
 * @author John Welsh
 * @date 11-7-2020
 */
//LOC 123

public class Connect4TextConsole extends Application {



    /** This is a reference to the the Connect 4 class. */
    Connect4 game=new Connect4();
    Scanner scan=new Scanner(System.in);
    String ans;
    Controller c=new Controller();


    /** The main methdod calls the run method */
    public static void main(String[] args) {
        Connect4TextConsole go = new Connect4TextConsole();



        System.out.println("Would you like the GUI or console version? Enter GUI or Console.");
        go.ans = go.scan.nextLine();
        if (go.ans.equals("GUI")) {
            System.out.println("Please open the board on your java runner");
            launch(args);

        } else {
            go.run();
        }
    }
    /**This method sets up the game by starting, ending,
     * and looping throught the turns.
     */
    @Override
    public void start(Stage stage){
        Connect4GUI gui=new Connect4GUI();
        gui.start(stage);

    }
    private void run() {

        String x = "PlayerX";
        String o = "PlayerO";
        String s = "Stalemate.";

        boolean bc = false;





        System.out.println("Would like to play another player or the bot? bot/player are the applicable answers.");
        ans = scan.nextLine();
        if (!ans.equals("bot") && !ans.equals("player")) {
            System.out.println("Invalid answer.");
            System.out.println("Please enter bot or player as an answer.");
            ans = scan.nextLine();
        }

        if (ans.equals("bot")) {
            int d = 0;
            Print();
            System.out.println("Begin Game! You are player X");
            while (bc == false) {
                if (d % 2 == 0) {
                    game.playTurn();
                    d++;

                } else {
                    game.botPlayTurn();
                    d++;
                }

                Print();

                bc = game.baseCase();
                if (game.stalemate == true) {
                    System.out.println(s);
                } else if (game.px == true) {
                    System.out.println(x + " Won the game");

                } else if (game.po == true) {
                    System.out.println(o + " Won the game");

                }


            }


        } else {


            Print();
            System.out.println("Begin Game");
            while (bc == false) {
                game.playTurn();
                Print();
                bc = game.baseCase();
            }
            if (game.stalemate == true) {
                System.out.println(s);
            } else if (game.px == true) {
                System.out.println(x + " Won the game");

            } else if (game.po == true) {
                System.out.println(o + " Won the game");

            }
        }

    }
    /**
     * This method prints the current board as it is.
     */
    public void Print(){
        for (int i = 0; i < game.board.length; i++) {
            System.out.print("|" + game.board[i] + "|");
            if (i % 7 == 6) System.out.print("\n");

        }
    }


}
