/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.bookRoom;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hotel.management.system.database.DatabaseHandler;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * FXML Controller class
 *
 * @author Connel
 */
public class BookRoomController implements Initializable {

    @FXML
    private BorderPane border_pane;
    @FXML
    private JFXButton ok;
    @FXML
    private JFXButton cancle;
    @FXML
    private JFXTextField room_number;
    @FXML
    private JFXTextField customer_name;
    @FXML
    private JFXTextField customer_address;
    @FXML
    private JFXTextField customer_mobile;
    @FXML
    private JFXTextField customer_email;
    @FXML
    private JFXComboBox customer_category;
    @FXML
    private JFXTextField guests;
    @FXML
    private JFXDatePicker check_in_date;
    @FXML
    private JFXDatePicker check_out_date;
    @FXML
    private JFXComboBox bookin_type;

    private DatabaseHandler databaseHandler;
    private ResultSet rs;
    private String UNIQUE_ID; 
    private String customerId; 
    private Boolean registerdCustomer=false;
    private final ArrayList<String> customers = new ArrayList<>();
    private final ArrayList<String> customers_info = new ArrayList<>();
    
    ObservableList<String> costomerCategoryList=FXCollections.observableArrayList("regular", "premuim", "new");
    ObservableList<String> bookTypeList=FXCollections.observableArrayList("on spot", "paid reservation", "reservation");
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        customer_category.setItems(costomerCategoryList);
        bookin_type.setItems(bookTypeList);
        loadCustomerData();
       
        check_in_date.setValue(LocalDate.now());
   
 JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();
    autoCompletePopup.getSuggestions().addAll(customers);

    autoCompletePopup.setSelectionHandler(event -> {
        customer_name.setText(event.getObject());
        
        registerdCustomer=true;
        String[] otherInfo=customers_info.get(customers.indexOf(event.getObject())).split(",");
        customerId=otherInfo[0];
        customer_address.setText(otherInfo[1]);
        customer_mobile.setText(otherInfo[2]);
        customer_email.setText(otherInfo[3]);
        for(int i=0; i<costomerCategoryList.size(); i++){
            if(costomerCategoryList.get(i).toLowerCase().equals(otherInfo[4])){
                customer_category.getSelectionModel().select(i);
            }
        }
        
        // you can do other actions here when text completed
    });

    // filtering options
    customer_name.textProperty().addListener(observable -> {
        autoCompletePopup.filter(string -> string.toLowerCase().contains(customer_name.getText().toLowerCase()));
        if (autoCompletePopup.getFilteredSuggestions().isEmpty() || customer_name.getText().isEmpty()) {
            autoCompletePopup.hide();
            
            registerdCustomer=false;
            // if you remove textField.getText.isEmpty() when text field is empty it suggests all options
            // so you can choose
        } else {
            autoCompletePopup.show(customer_name);
        }
    });
 
    }    

    public void initData(String roomId){
        System.out.println(roomId);
        if(roomId.endsWith(","))
        room_number.setText(roomId.substring(0, roomId.length()-1));
        else
        room_number.setText(roomId);
    }
    @FXML
    private void handleOkAction(ActionEvent event) {
         if (room_number.getText().isEmpty() || customer_name.getText().isEmpty() || guests.getText().isEmpty() || check_in_date.getValue().toString().isEmpty() || check_out_date.getValue().toString().isEmpty() || bookin_type.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
         String[] roomNo=room_number.getText().split(",");
         UNIQUE_ID= UUID.randomUUID().toString();
         String customerName=customer_name.getText();
         String customerAddress=customer_address.getText();
         String customerMobile=customer_mobile.getText();
         String customerEmail=customer_email.getText();
         String customerCategory=customer_category.getValue().toString();
         String guest=guests.getText();
         String checkIn=check_in_date.getValue().toString();
         String checkOut=check_out_date.getValue().toString();
         String bookType=bookin_type.getValue().toString();
         
         if(registerdCustomer){
           for(String roomId:roomNo){
               String uniqueId=UUID.randomUUID().toString();
             bookRoom(uniqueId, roomId, customerId, customerName, Integer.parseInt(guest), checkIn, checkOut, bookType);
         }  
         }else{
         addnewCustomer(UNIQUE_ID, customerName, customerAddress, customerMobile, customerEmail, customerCategory, roomNo, Integer.parseInt(guest), checkIn, checkOut, bookType);
         }
    }

    @FXML
    private void handleCancleAction(ActionEvent event) {
        Stage stage = (Stage) border_pane.getScene().getWindow();
         stage.close();
    }
    
    private void bookRoom(String uniqueId, String RoomNo, String customerId, String customerName, int guests, String checkIn, String checkOut, String type){
        String qu = "INSERT INTO BOOK(unique_id, room_id, customer_id, customer_name, guests, check_in, check_out, type) VALUES ("
        + "'" + uniqueId + "',"
        + "'" + RoomNo + "',"
        + "'" + customerId + "',"
        + "'" + customerName + "',"
        + guests + ","
        //+ "CURRENT_TIMESTAMP"+","
        + "'" + checkIn + "',"
        + "'" + checkOut + "',"
        + "'" + type + "'"
        + ")";
        System.out.println(qu);
        if (databaseHandler.execAction(qu)) {
            updateRoom(RoomNo);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Success");
        alert.showAndWait();
        
        Stage stage = (Stage) border_pane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
        
        } else //Error
        {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Failed");
        alert.showAndWait();
        }
    }
    
    public void updateRoom(String RoomNo){
       String updateQuery =  "UPDATE ROOM SET "
        + "STATUS="+ "'" + false + "'"
        +" WHERE ID ="+"'"+RoomNo+"'";
        if(databaseHandler.execAction(updateQuery)){
       System.out.println("Room updated successfuly");
       
       }else{
            System.out.println("Room category update failed");
       }
   }
    
    private void addnewCustomer(String id, String name, String address, String mobile, String email, String category, String[] RoomNo, int guests, String checkIn, String checkOut, String type){
        String qu = "INSERT INTO CUSTOMER VALUES ("
        + "'" + id + "',"
        + "'" + name + "',"
        + "'" + address + "',"
        + "'" + mobile + "',"
        + "'" + email + "',"
        + "'" + category + "'"
        + ")";
        
        if (databaseHandler.execAction(qu)) {
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Customer added Successfuly");
        alert.showAndWait();*/
        for(String roomId:RoomNo){
            String uniqueId=UUID.randomUUID().toString();
             bookRoom(uniqueId, roomId, id, name, guests, checkIn, checkOut, type);
         }
        
        } else //Error
        {
            System.out.println("Failed");
        /*Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Failed");
        alert.showAndWait();*/
        }
    }
    
    private void loadCustomerData() {
        String selectQuery = "SELECT * FROM CUSTOMER";
        rs = databaseHandler.execQuery(selectQuery);
        try {
            while (rs.next()) {
                String customer_id = rs.getString("id"),
                namee=rs.getString("name"),
                addres=rs.getString("address"),
                mobile=rs.getString("mobile"),
                mail=rs.getString("email"),
                categori = rs.getString("category");
               
                boolean duplicateName=false;
                for(String cos: customers){
                    if(cos.equals(namee))
                     duplicateName=true; 
                }
                
                while(duplicateName){
                for(String cos: customers){
                    if(cos.equals(namee)){
                     duplicateName=true; 
                     namee+=" ";
                    }else{
                     duplicateName=false; 
                    }
                }
                }
                customers.add(namee);
                customers_info.add(customer_id+","+addres+","+mobile+","+mail+","+categori);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BookRoomController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
}
