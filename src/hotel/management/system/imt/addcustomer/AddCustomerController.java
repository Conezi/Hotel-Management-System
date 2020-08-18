/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.imt.addcustomer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import hotel.management.system.imt.database.DatabaseHandler;
import hotel.management.system.imt.main.MainController;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Connel
 */
public class AddCustomerController implements Initializable {
    
    ObservableList<Customer> list = FXCollections.observableArrayList();
    
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField address;
    @FXML
    private JFXTextField mobile_no;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXComboBox category;
    @FXML
    private TableView<Customer> table_view;
    @FXML
    private TableColumn<Customer, String> category_col;
    @FXML
    private TableColumn<Customer, String> name_col;
    @FXML
    private TableColumn<Customer, String> address_col;
    @FXML
    private TableColumn<Customer, String> mobile_col;
    @FXML
    private TableColumn<Customer, String> email_col;
    
    ObservableList<String> costomerCategoryList=FXCollections.observableArrayList("regular", "premuim", "new");
    private String UNIQUE_ID; 
    DatabaseHandler databaseHandler;
    private ResultSet rs;
    private Customer selectedCustomer;
    
    private int index;
    @FXML
    private JFXButton saveCustomer;
    @FXML
    private JFXButton updateCustomer;
    @FXML
    private JFXButton deleteCustomer;
    @FXML
    private BorderPane border_pane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        category.setItems(costomerCategoryList);
        initCategoryCol();
        loadCustomerData();
        
        table_view.setRowFactory((TableView<Customer> tableView2) -> {
            final TableRow<Customer> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    index = row.getIndex();
                    if (index >= 0 && index < table_view.getItems().size() && table_view.getSelectionModel().isSelected(index)  ) {
                        table_view.getSelectionModel().clearSelection();
                        name.setText(null);
                        address.setText(null);
                        mobile_no.setText(null);
                        email.setText(null);
                        category.getSelectionModel().clearSelection();
                        updateCustomer.setDisable(true);
                        deleteCustomer.setDisable(true);
                        saveCustomer.setDisable(false);
                        event.consume();
                    }
                }
            });
            return row;
        });  
    }   

    @FXML
    private void handleAddCustomerAction(ActionEvent event) {
        if (name.getText().isEmpty() || address.getText().isEmpty()||mobile_no.getText().isEmpty() || email.getText().isEmpty()|| category.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
        UNIQUE_ID= UUID.randomUUID().toString();
        String namee=name.getText(),
        addres=address.getText(),
        mobile=mobile_no.getText(),
        mail=email.getText(),
        categor=category.getValue().toString();
        
        addnewCustomer(UNIQUE_ID, namee, addres, mobile, mail, categor);
        
    }

    @FXML
    private void handleUpdateCustomerAction(ActionEvent event) {
        if (name.getText().isEmpty() || address.getText().isEmpty()||mobile_no.getText().isEmpty() || email.getText().isEmpty()|| category.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
        String namee=name.getText(),
        addres=address.getText(),
        mobile=mobile_no.getText(),
        mail=email.getText(),
        categor=category.getValue().toString();
        
        updateCustomerInfo(selectedCustomer.getId(), namee, addres, mobile, mail, categor);
    }

    @FXML
    private void handleDeleteCustomerAction(ActionEvent event) {
        deleteCustomer(selectedCustomer.getId());
    }

    @FXML
    private void customerTableClicked(MouseEvent event) {
        if (index >= 0 && index < table_view.getItems().size() && table_view.getSelectionModel().isSelected(index)  ) {
        onSelect();
        deleteCustomer.setDisable(false);
        updateCustomer.setDisable(false);
        saveCustomer.setDisable(true);
        }
    }
    
    private void addnewCustomer(String id, String name, String address, String mobile, String email, String category){
        String qu = "INSERT INTO CUSTOMER VALUES ("
        + "'" + id + "',"
        + "'" + name + "',"
        + "'" + address + "',"
        + "'" + mobile + "',"
        + "'" + email + "',"
        + "'" + category + "'"
        + ")";
        
        if (databaseHandler.execAction(qu)) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Customer added Successfuly");
        alert.showAndWait();
        
        refreshCustomers();
        
        } else //Error
        {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText("Failed");
        alert.showAndWait();
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
    
    private void initCategoryCol() {
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        address_col.setCellValueFactory(new PropertyValueFactory<>("address"));
        mobile_col.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        email_col.setCellValueFactory(new PropertyValueFactory<>("email"));
        category_col.setCellValueFactory(new PropertyValueFactory<>("category"));
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
                
                list.add(new Customer(customer_id, namee, addres, mobile, mail, categori));
               
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        table_view.getItems().setAll(list); 
    } 
    
    public void onSelect() {
    // check the table's selected item and get selected item
    if (table_view.getSelectionModel().getSelectedItem() != null) {
        selectedCustomer = table_view.getSelectionModel().getSelectedItem();
        name.setText(selectedCustomer.getName());
        address.setText(selectedCustomer.getAddress());
        mobile_no.setText(selectedCustomer.getMobile());
        email.setText(selectedCustomer.getEmail());
        category.getSelectionModel().select(0);
        }
    }
    
    public void deleteCustomer(String categoryID) {
       String deleteQuery = "DELETE FROM CUSTOMER WHERE ID = " + "'"+categoryID+"'";
       if(databaseHandler.execAction(deleteQuery)){
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Customer deleted");
       alert.showAndWait();
       
        refreshCustomers();
        
       }else{
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Customer delete failed");
       alert.showAndWait();
       }
    } 
    
    public void updateCustomerInfo(String id, String name, String address, String mobile, String email, String category){
       String updateQuery =  "UPDATE CUSTOMER SET "
               + "NAME="+"'"+name+"',"
               +" ADDRESS= "+"'"+address+"',"
               +" MOBILE= "+"'"+mobile+"',"
               +" EMAIL= "+"'"+email+"',"
               +" CATEGORY= "+"'"+category+"'"
               +" WHERE ID ="+"'"+id+"'";
        if(databaseHandler.execAction(updateQuery)){
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Customer info updated successfuly");
       alert.showAndWait();
       
        refreshCustomers();
        
       }else{
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Customer info update failed");
       alert.showAndWait();
       }
   }
    
    private void refreshCustomers(){
        name.setText("");
        address.setText("");
        mobile_no.setText("");
        email.setText("");
        category.getSelectionModel().clearSelection();
        list.removeAll(list);
        loadCustomerData();
    }
    
}
