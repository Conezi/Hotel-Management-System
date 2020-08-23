/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.main;

import hotel.management.system.database.DatabaseHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Connel
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/hotel/management/system/login/Login.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/hotel/management/system/imt/main/Main.fxml"));
        Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        
        new Thread(() -> {
            DatabaseHandler.getInstance();
        }).start();
        
        //System.out.println("java version: "+System.getProperty("java.version"));
        //System.out.println("javafx.version: " + System.getProperty("javafx.version"));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
