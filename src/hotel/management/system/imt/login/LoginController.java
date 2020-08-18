/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.imt.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import hotel.management.system.imt.main.MainController;
import hotel.management.system.imt.settings.Preferences;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * FXML Controller class
 *
 * @author Connel
 */
public class LoginController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;

    private Preferences preference;
    final private String auth="collins";
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        preference = Preferences.getPreferences();
    }    

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        titleLabel.setText("Royal Garden Heotel Login");
        titleLabel.setStyle("-fx-background-color:black;-fx-text-fikll:white");
        
        String user=username.getText();
        String pass=DigestUtils.shaHex(password.getText());
        
        if (user.equals(preference.getUsername()) && pass.equals(preference.getPassword())) {
            closeStage();
            loadMain();
        }else{
           titleLabel.setText("Invalid Credentails");
            titleLabel.setStyle("-fx-background-color:#d32f2f;-fx-text-fill:white");
        }
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        System.exit(0);
    }
    
    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/hotel/management/system/imt/main/Main.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Hotel Management");
            stage.setScene(new Scene(parent));
            stage.sizeToScene();
            stage.show();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
