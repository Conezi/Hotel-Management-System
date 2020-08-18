/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.imt.addcategory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import hotel.management.system.imt.main.MainController;
import hotel.management.system.imt.database.DatabaseHandler;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Connel
 */
public class AddCategoryController implements Initializable {

    ObservableList<Category> list = FXCollections.observableArrayList();
    
    @FXML
    private JFXButton saveCategory;
    @FXML
    private JFXButton updateCategory;
    @FXML
    private JFXTextField category;
    @FXML
    private JFXTextField price;
    @FXML
    private TableColumn<Category, String> category_col;
    @FXML
    private TableColumn<Category, String> price_col;
    @FXML
    private TableView<Category> table_view;
    
    private String UNIQUE_ID; 
    private Category selectedCategory;
    DatabaseHandler databaseHandler;
    private ResultSet rs;
    @FXML
    private JFXButton deleteCategory;
    @FXML
    private BorderPane border_pane;
    
    private int index;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        initCategoryCol();
        loadRoomCategoryData();
        
        table_view.setRowFactory((TableView<Category> tableView2) -> {
            final TableRow<Category> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    index = row.getIndex();
                    if (index >= 0 && index < table_view.getItems().size() && table_view.getSelectionModel().isSelected(index)  ) {
                        table_view.getSelectionModel().clearSelection();
                        category.setText(null);
                        price.setText(null);
                        updateCategory.setDisable(true);
                        deleteCategory.setDisable(true);
                        saveCategory.setDisable(false);
                        event.consume();
                    }
                }
            });
            return row;
        });  
        
    }    

    @FXML
    private void addRoomCategory(ActionEvent event) {
         if (category.getText().isEmpty() || price.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
        UNIQUE_ID= UUID.randomUUID().toString();
        String categori = category.getText();
        String pric = price.getText();
        
        addnewRoomCategory(categori, pric);
    }

    @FXML
    private void updateRoomCategory(ActionEvent event) {
         if (category.getText().isEmpty() || price.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return;
        }
         String categori = category.getText();
         String pric = price.getText();
         updateRoomCat(categori, pric, selectedCategory.getId());
    }

    @FXML
    private void deleteRoomCategory(ActionEvent event) {
        deleteRoomCat(selectedCategory.getId());
    }
    
    @FXML
    private void categoryTableClicked(MouseEvent event) {
        if (index >= 0 && index < table_view.getItems().size() && table_view.getSelectionModel().isSelected(index)  ) {
        onSelect();
        updateCategory.setDisable(false);
        deleteCategory.setDisable(false);
        saveCategory.setDisable(true);
        }
        
    }
    
   /* private void refreshButtonClicked(ActionEvent event) {
        table_view.getSelectionModel().clearSelection();
        category.setText(null);
        price.setText(null);
        updateCategory.setDisable(true);
        deleteCategory.setDisable(true);
        saveCategory.setDisable(false);
    }*/
    
    private void addnewRoomCategory(String category, String price){
        String qu = "INSERT INTO ROOM_CATEGORY VALUES ("
        + "'" + UNIQUE_ID + "',"
        + "'" + category + "',"
        + "'" + price + "'"
        + ")";
        
        if (databaseHandler.execAction(qu)) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText("Room category added Successfuly");
        alert.showAndWait();
        
        refreshCategory();
        
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
        category_col.setCellValueFactory(new PropertyValueFactory<>("category"));
        price_col.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    
    private void loadRoomCategoryData() {
        String selectQuery = "SELECT * FROM ROOM_CATEGORY";
        rs = databaseHandler.execQuery(selectQuery);
        try {
            while (rs.next()) {
                String room_id = rs.getString("id");
                String room_category = rs.getString("category");
                String room_price = rs.getString("price");
                
                list.add(new Category(room_id, room_category, room_price));
               
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        table_view.getItems().setAll(list);
        
    }  
    public void onSelect() {
    // check the table's selected item and get selected item
    if (table_view.getSelectionModel().getSelectedItem() != null) {
        selectedCategory = table_view.getSelectionModel().getSelectedItem();
        category.setText(selectedCategory.getCategory());
        price.setText(selectedCategory.getPrice());
    }
}
   public void deleteRoomCat(String categoryID) {
       String deleteQuery = "DELETE FROM ROOM_CATEGORY WHERE ID = " + "'"+categoryID+"'";
       if(databaseHandler.execAction(deleteQuery)){
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Room category deleted");
       alert.showAndWait();
       
        refreshCategory();
        
       }else{
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Room category delete failed");
       alert.showAndWait();
       }
    } 
   
   public void updateRoomCat(String category, String price, String id){
       String updateQuery =  "UPDATE ROOM_CATEGORY SET "
               + "CATEGORY="+"'"+category+"',"
               +" PRICE= "+"'"+price+"'"
               +" WHERE ID ="+"'"+id+"'";
        if(databaseHandler.execAction(updateQuery)){
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Room category updated successfuly");
       alert.showAndWait();
       
        refreshCategory();
        
       }else{
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Room category update failed");
       alert.showAndWait();
       }
   }
   
   private void refreshCategory(){
       category.setText("");
       price.setText("");
       list.removeAll(list);
       loadRoomCategoryData();
   }
}
