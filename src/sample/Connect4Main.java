package sample;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;
//64 LOC
public class Connect4Main extends Application {
    /**
     * References to the variable classes
     */
    static Connect4TextConsole nonServer= new Connect4TextConsole();
    static Connect4Server server=new Connect4Server();
    static Connect4Client playerOne=new Connect4Client();
    static Connect4Client playerTwo=new Connect4Client();
    static String[] z;
    static boolean b=false;
    /**
     * Starting Point
     */
    public static void main(String[] args){
        set(args);

        Scanner scano=new Scanner(System.in);
        System.out.println("Would you like to play online (PVP)? Answer y for yes or n for no. By answering no" +
                "you will be automaticall placed into offline mode.");
        String ans=scano.nextLine();
        if(ans.equals("y")){


            launch(args);
            System.out.println("Open the java runner on your computer to play. The bot version is available for gui or");

        }
        else if(ans.equals("n")){
           not(args);



        }


    }
    @Override
    /**
     * Starting point
     */
    public void start(Stage primaryStage){
        Stage one=new Stage();
        Stage two=new Stage();

        if(b==true){
            playerTwo.isBot=true;

        }
        server.start(primaryStage);

        playerOne.start(primaryStage);
        playerTwo.start(one);




    }
    /**
     * Sets the args into z
     */
    static void set(String[] a){
        z=a;
    }
    /**
     * returns z. Probably an unnessary class.
     */
    static String[] get(){
        return z;
    }
    /**
     * Starts offline mode (PVB)(PVP shared screen)
     */

    static void not(String[] args){
        nonServer.main(args);


    }

}
