/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.settings;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Connel
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXTextField discount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initDefaultValues();
    }    

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        float myDiscount = Float.parseFloat(discount.getText());
        //float fine = Float.parseFloat(finePerDay.getText());
        String uname = username.getText();
        String pass = password.getText();
        
        Preferences preferences = Preferences.getPreferences();
        preferences.setDiscount(myDiscount);
        //preferences.setFinePerDay(fine);
        preferences.setUsername(uname);
        preferences.setPassword(pass);
        
        Preferences.writePreferenceToFile(preferences);
    }

    @FXML
    private void handleCancelButtonAction(ActionEvent event) {
        ((Stage)discount.getScene().getWindow()).close();
    }
    
    private void initDefaultValues() {
        Preferences preferences = Preferences.getPreferences();
        discount.setText(String.valueOf(preferences.getDiscount()));
        //finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
        username.setText(String.valueOf(preferences.getUsername()));
        password.setText(String.valueOf(preferences.getPassword()));
    }
    
}
