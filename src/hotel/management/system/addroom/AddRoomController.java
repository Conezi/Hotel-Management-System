/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.addroom;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import hotel.management.system.database.DatabaseHandler;
import hotel.management.system.main.MainController;
import hotel.management.system.main.Room;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Connel
 */
public class AddRoomController implements Initializable {

    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;
    @FXML
    private JFXComboBox room_category;
    @FXML
    private JFXTextField room_no;
    @FXML
    private JFXTextField bed_count;
    @FXML
    private JFXRadioButton tv_yes;
    @FXML
    private ToggleGroup tv;
    @FXML
    private JFXRadioButton tv_no;
    @FXML
    private JFXRadioButton wifi_yes;
    @FXML
    private ToggleGroup wifi;
    @FXML
    private JFXRadioButton wifi_no;
    @FXML
    private JFXRadioButton phone_yes;
    @FXML
    private ToggleGroup phone;
    @FXML
    private JFXRadioButton phone_no;
    @FXML
    private JFXTextField other_assets;
    @FXML
    private JFXChipView<String> asset_chip;
    @FXML
    private AnchorPane rootPane;
    
    private DatabaseHandler databaseHandler;
    private ResultSet rs;

    /**
     * Initializes the controller class.
     */
    //ObservableList<String> roomCategoryList=FXCollections.observableArrayList("deluxe", "standard", "suite");
    ObservableList<String> roomCategoryList=FXCollections.observableArrayList();
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        loadRoomCategoryData();
        room_category.setItems(roomCategoryList);
        
        other_assets.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode().equals(KeyCode.ENTER)) {
                
                asset_chip.getChips().add(other_assets.getText());
                other_assets.setText("");
                
            }
        });
    }    

    public void initData(Room room) {
        room_no.setText(room.getId());
        bed_count.setText(room.getBed_no());
        saveButton.setText("Update");
        room_no.setEditable(false);
        
        for(int i=0; i<roomCategoryList.size(); i++){
            if(roomCategoryList.get(i).toLowerCase().equals(room.getCategory().toLowerCase())){
                room_category.getSelectionModel().select(i);
            }
        }
        if(room.getTv().equals("Yes")){
            tv.selectToggle(tv_yes);
        }else{
           tv.selectToggle(tv_no); 
        }
        
        if(room.getWifi().equals("Yes")){
            wifi.selectToggle(wifi_yes);
        }else{
           wifi.selectToggle(wifi_no); 
        }
        
        if(room.getPhone().equals("Yes")){
            phone.selectToggle(phone_yes);
        }else{
           phone.selectToggle(phone_no); 
        }
        
        String[] others=room.getOthers().split(",");
        asset_chip.getChips().addAll(others);
    }
    
    @FXML
    private void addRoom(ActionEvent event) {
         if (room_no.getText().isEmpty() || bed_count.getText().isEmpty() || room_category.getSelectionModel().isEmpty()||(other_assets.getText().isEmpty()&&asset_chip.getChips().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter in all fields");
            alert.showAndWait();
            return;
        }
         String otherAssets="";
         otherAssets = asset_chip.getChips().stream().map((assets) -> assets+",").reduce(otherAssets, String::concat);
         if(other_assets.getText().isEmpty()){
             otherAssets=otherAssets.substring(0, otherAssets.length()-1);
         }else{
             otherAssets+=other_assets.getText();
         }
        String RoomNo = room_no.getText();
        String RoomCategory = room_category.getValue().toString();
        String BedCount = bed_count.getText();
        //String OtherAssets = other_assets.getText();
        int BCount=1;
        try{BCount=Integer.parseInt(BedCount);}catch(NumberFormatException ignore){}
        boolean TV = tv_yes.isSelected();
        boolean Wifi = wifi_yes.isSelected();
        boolean Phone = phone_yes.isSelected();
        
        if(saveButton.getText().equals("Update")){
          updateRoom(RoomNo, RoomCategory, BCount, TV, Wifi, Phone, otherAssets);
        }else{
            addnewRoom(RoomNo, RoomCategory, BCount, TV, Wifi, Phone, otherAssets);
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
         Stage stage = (Stage) rootPane.getScene().getWindow();
         stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }
    
    private void checkData() {
        String qu = "SELECT id FROM ROOM";
        rs = databaseHandler.execQuery(qu);
        try {
            while(rs.next()){
                String roomNo = rs.getString("id");
                System.out.println(roomNo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addnewRoom(String RoomNo, String RoomCategory, int BCount, boolean TV, boolean Wifi, boolean Phone, String otherAssets){
        String qu = "INSERT INTO ROOM VALUES ("
        + "'" + RoomNo + "',"
        + "'" + RoomCategory + "',"
        + BCount + ","
        + "'" + TV + "',"
        + "'" + Wifi + "',"
        + "'" + Phone + "',"
        + "'" + otherAssets + "',"
        + "" + "true" + ""
        + ")";
        System.out.println(qu);
        if (databaseHandler.execAction(qu)) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Success");
        alert.showAndWait();
        
        refreshWindow();
       cancelButton.setText("Done");
        
        } else //Error
        {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Failed");
        alert.showAndWait();
        }
    }
    
 private void loadRoomCategoryData() {
        String selectQuery = "SELECT * FROM ROOM_CATEGORY";
        rs = databaseHandler.execQuery(selectQuery);
        try {
            while (rs.next()) {
                roomCategoryList.add(rs.getString("category"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
 
 public void updateRoom(String RoomNo, String RoomCategory, int BCount, boolean TV, boolean Wifi, boolean Phone, String otherAssets){
       String updateQuery =  "UPDATE ROOM SET "
        + "CATEGORY="+"'" + RoomCategory + "',"
        + "BED_NO="+ BCount + ","
        + "TV="+ "'" + TV + "',"
        + "WIFI="+ "'" + Wifi + "',"
        + "PHONE="+ "'" + Phone + "',"
        + "OTHER_ASSETS="+ "'" + otherAssets + "'"
        +" WHERE ID ="+"'"+RoomNo+"'";
        if(databaseHandler.execAction(updateQuery)){
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Room updated successfuly");
       alert.showAndWait();
       
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
       
       }else{
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Room category update failed");
       alert.showAndWait();
       }
   }
    private void refreshWindow(){
        room_no.setText("");
        room_category.getSelectionModel().clearSelection();
        bed_count.setText("");
        tv_yes.setSelected(true);
        wifi_yes.setSelected(true);
        phone_yes.setSelected(true);
    }
}
