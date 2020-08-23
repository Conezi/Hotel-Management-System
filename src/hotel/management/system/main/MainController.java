/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.main;

import com.jfoenix.controls.JFXButton;
import hotel.management.system.addroom.AddRoomController;
import hotel.management.system.bookRoom.BookRoomController;
import hotel.management.system.checkOut.CheckOutController;
import hotel.management.system.database.DatabaseHandler;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author Connel
 */
public class MainController implements Initializable {

    ObservableList<Room> roomList = FXCollections.observableArrayList();
    ObservableList<BookedRoom> bookedRoomList = FXCollections.observableArrayList();
    
    @FXML
    private JFXButton book_in;
    @FXML
    private JFXButton chech_out;
    @FXML
    private JFXButton place_oder;
    @FXML
    private JFXButton customer_info;
    @FXML
    private JFXButton settings;
    @FXML
    private MenuItem add_room;
    @FXML
    private MenuItem add_customer;
    @FXML
    private TableColumn<Room, String> id;
    @FXML
    private TableColumn<Room, String> category;
    @FXML
    private TableColumn<Room, String> bed_count;
    @FXML
    private TableColumn<Room, Boolean> tv;
    @FXML
    private TableColumn<Room, Boolean> wifi;
    @FXML
    private TableColumn<Room, Boolean> phone;
    @FXML
    private TableColumn<Room, String> others;
    @FXML
    private TableColumn<Room, Boolean> status;
    @FXML
    private TableView<Room> rooms_table;
    @FXML
    private TableView<BookedRoom> booked_rooms_table;
    @FXML
    private TableColumn<BookedRoom, String> room_no;
    @FXML
    private TableColumn<BookedRoom, String> customer_name;
    @FXML
    private TableColumn<BookedRoom, Integer> guests;
    @FXML
    private TableColumn<BookedRoom, String> date_booked;
    @FXML
    private TableColumn<BookedRoom, String> check_in;
    @FXML
    private TableColumn<BookedRoom, String> check_out;
    @FXML
    private TableColumn<BookedRoom, String> type;
    @FXML
    private MenuItem exit;
    @FXML
    private MenuItem full_screen;
    @FXML
    private BorderPane border_pane;
    @FXML
    private MenuItem view_room_category;
    @FXML
    private MenuItem add_room_category;
    @FXML
    private MenuItem delete_menu;
    @FXML
    private MenuItem update_menu;
    @FXML
    private MenuItem check_out_menu;
    @FXML
    private MenuItem book_room;
    
    private int index;
    private Room selectedRoom;
    private BookedRoom selectedBookedRoom;
    private DatabaseHandler handler;
    
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private boolean multiSelection=false;
    private String multiIds="";
    private final ArrayList<BookedRoom> invoice_data = new ArrayList<>();
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRoomCol();
        initBookedRoomCol();
        loadRoomData();
        loadBookedRoomData();
        
        /*rooms_table.setRowFactory((TableView<Room> tableView2) -> {
            final TableRow<Room> row = new TableRow<>();
            row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                index = row.getIndex();
                if (index >= 0 && index < rooms_table.getItems().size() && rooms_table.getSelectionModel().isSelected(index)  ) {
                    rooms_table.getSelectionModel().clearSelection();
                    unSelect();
                    delete_menu.setDisable(true);
                    update_menu.setDisable(true);
                    book_in.setDisable(true);
                    book_room.setDisable(true);
                    event.consume();
                }
            });
            return row;
        }); */
        
        //border_pane.setOnKeyPressed(e -> pressedKeys.add(e.getCode()));
        //border_pane.setOnKeyReleased(e -> pressedKeys.remove(e.getCode()));
        border_pane.setOnKeyPressed((KeyEvent e) -> {
            if(e.isControlDown()){
                multiSelection=true;
                //rooms_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            }
        });
        border_pane.setOnKeyReleased((KeyEvent e) -> {
                if(!e.isControlDown()){
                    multiSelection=false;
                    //rooms_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                }
            });
        
        rooms_table.setOnMousePressed((MouseEvent event) -> {
            ObservableList<Room> selectedItems = rooms_table.getSelectionModel().getSelectedItems();
            ArrayList<String> selectedIDs = new ArrayList<>();
            if(multiSelection){
            selectedItems.forEach((row) -> {
                selectedIDs.add(row.getId());
                multiIds="";
            });
            
            ListIterator<String> iterator = selectedIDs.listIterator();
            while (iterator.hasNext()) {
                multiIds+=iterator.next()+",";
            }
            System.out.println(multiIds);
            }else{
                selectedIDs.clear();
                multiIds="";
            }
        });
        
            booked_rooms_table.setOnMousePressed((MouseEvent event) -> {
            ObservableList<BookedRoom> selectedItems = booked_rooms_table.getSelectionModel().getSelectedItems();
            ArrayList<BookedRoom> selectedRoomm = new ArrayList<>();
            if(multiSelection){
            selectedItems.forEach((row) -> {
                selectedRoomm.add(row);
                invoice_data.clear();
            });
            
            ListIterator<BookedRoom> iterator = selectedRoomm.listIterator();
            while (iterator.hasNext()) {
                invoice_data.add(iterator.next());
                //multiIds+=iterator.next()+",";
            }
            //System.out.println(invoice_data.get(0));
            }else{
                selectedRoomm.clear();
                invoice_data.clear();
            }
        });
                
        booked_rooms_table.setRowFactory((TableView<BookedRoom> tableView) -> {
            final TableRow<BookedRoom> row = new TableRow<>();
             row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                index = row.getIndex();
                if (index >= 0 && index < booked_rooms_table.getItems().size() && booked_rooms_table.getSelectionModel().isSelected(index)  ) {
                    booked_rooms_table.getSelectionModel().clearSelection();
                    unCheckOutSelect();
                    event.consume();
                }
            });
             
             
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem bookItem = new MenuItem("Check Out");
            bookItem.setOnAction((ActionEvent event) -> {
                if(invoice_data.isEmpty()){
               invoice_data.add(selectedBookedRoom);  
             }
                loadCheckOut("/hotel/management/system/checkOut/CheckOut.fxml", "Check Out", invoice_data);
            });
            
            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction((ActionEvent event) -> {
                booked_rooms_table.getItems().remove(row.getItem());
            });
            rowMenu.getItems().addAll(bookItem, removeItem);

            // only display context menu for non-empty rows:
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then(rowMenu)
                            .otherwise((ContextMenu)rowMenu));
            return row;
        });
        
        
                rooms_table.setRowFactory((TableView<Room> tableView) -> {
            final TableRow<Room> row = new TableRow<>();
             row.addEventFilter(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
                index = row.getIndex();
                if (index >= 0 && index < rooms_table.getItems().size() && rooms_table.getSelectionModel().isSelected(index)  ) {
                    rooms_table.getSelectionModel().clearSelection();
                    unSelect();
                    event.consume();
                }
            });
             
             
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem bookItem = new MenuItem("Book");
            bookItem.setOnAction((ActionEvent event) -> {
                if(multiIds.equals(""))
                    loadBookRoom("/hotel/management/system/bookRoom/BookRoom.fxml", "Book Room(s)", row.getItem().getId());
                else
                    loadBookRoom("/hotel/management/system/bookRoom/BookRoom.fxml", "Book Room(s)", multiIds);
            });
            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction((ActionEvent event) -> {
                if(rooms_table.getSelectionModel().getSelectedItem()!=null){
                    loadUpdateRoom("/hotel/management/system/addroom/addRoom.fxml", "Add New Room", rooms_table.getSelectionModel().getSelectedItem());
                    }
            });
            MenuItem removeItem = new MenuItem("Delete");
            removeItem.setOnAction((ActionEvent event) -> {
                deleteRoom(row.getItem().getId());
                //rooms_table.getItems().remove(row.getItem());
            });
            rowMenu.getItems().addAll(bookItem, editItem, removeItem);

            // only display context menu for non-empty rows:
            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then(rowMenu)
                            .otherwise((ContextMenu)rowMenu));
            return row;
        });
    }    

    public void resumeMain(){
            rooms_table.refresh();
        }
    
    @FXML
    private void handleBookInButtonAction(ActionEvent event) {
        if(multiIds.equals(""))
            loadBookRoom("/hotel/management/system/bookRoom/BookRoom.fxml", "Book Room(s)", selectedRoom.getId());
        else
        loadBookRoom("/hotel/management/system/bookRoom/BookRoom.fxml", "Book Room(s)", multiIds);
        //loadWindow("/hotel/management/system/bookRoom/BookRoom.fxml", "Book Room(s)");
    }

    @FXML
    private void handleCheckOutButtonAction(ActionEvent event) {
        if(selectedBookedRoom!=null){
            if(invoice_data.isEmpty()){
               invoice_data.add(selectedBookedRoom);  
             }
            loadCheckOut("/hotel/management/system/checkOut/CheckOut.fxml", "Check Out", invoice_data);
        }
    }

    @FXML
    private void handleCheckOutMenuAction(ActionEvent event) {
         if(selectedBookedRoom!=null){
             if(invoice_data.isEmpty()){
               invoice_data.add(selectedBookedRoom);  
             }
            loadCheckOut("/hotel/management/system/checkOut/CheckOut.fxml", "Check Out", invoice_data);
        }
    }
    
    @FXML
    private void handlePlaceOderButtonAction(ActionEvent event) {
    }

    @FXML
    private void handleCustomerInfoButtonAction(ActionEvent event) {
        loadWindow("/hotel/management/system/addcustomer/addCustomer.fxml", "Customers Info");
    }

    @FXML
    private void handleSettingsButtonAction(ActionEvent event) {
        loadWindow("/hotel/management/system/settings/Settings.fxml", "Admin Settings");
    }

    @FXML
    private void handleAddRoomMenuAction(ActionEvent event) {
         loadWindow("/hotel/management/system/addroom/addRoom.fxml", "Add New Room");
    }

    @FXML
    private void handleAddCustomerMenuAction(ActionEvent event) {
        loadWindow("/hotel/management/system/addcustomer/addCustomer.fxml", "Customers Info");
    }

    @FXML
    private void handleAddRoomCategoryMenuAction(ActionEvent event) {
        loadWindow("/hotel/management/system/addcategory/addCategory.fxml", "Room Category");
    }
    
    @FXML
    private void handleViewRoomCategoryMenuAction(ActionEvent event) {
        loadWindow("/hotel/management/system/addcategory/addCategory.fxml", "Room Category");
    }
    
    @FXML
    private void handleBookRoomMenuAction(ActionEvent event) {
         if(multiIds.equals(""))
            loadBookRoom("/hotel/management/system/bookRoom/BookRoom.fxml", "Book Room(s)", selectedRoom.getId());
        else
        loadBookRoom("/hotel/management/system/bookRoom/BookRoom.fxml", "Book Room(s)", multiIds);
        //loadWindow("/hotel/management/system/bookRoom/BookRoom.fxml", "Book Room(s)");
    }
    
    @FXML
    private void handleExitMenuAction(ActionEvent event) {
         Stage stage = (Stage) border_pane.getScene().getWindow();
         stage.close();
    }

    @FXML
    private void handleFullScreenMenuAction(ActionEvent event) {
        Stage stage = ((Stage) border_pane.getScene().getWindow());
        stage.setFullScreen(!stage.isFullScreen());
    }
    
    private void initRoomCol() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        bed_count.setCellValueFactory(new PropertyValueFactory<>("bed_no"));
        tv.setCellValueFactory(new PropertyValueFactory<>("tv"));
        wifi.setCellValueFactory(new PropertyValueFactory<>("wifi"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        others.setCellValueFactory(new PropertyValueFactory<>("others"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    
    private void initBookedRoomCol() {
        room_no.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        customer_name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        guests.setCellValueFactory(new PropertyValueFactory<>("guests"));
        date_booked.setCellValueFactory(new PropertyValueFactory<>("dateBooked"));
        check_in.setCellValueFactory(new PropertyValueFactory<>("checkIn"));
        check_out.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    private void loadRoomData() {
        handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM ROOM";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
                String room_id = rs.getString("id");
                String room_category = rs.getString("category");
                int room_bed_no = rs.getInt("bed_no");
                Boolean room_tv = rs.getBoolean("tv");
                Boolean room_wifi = rs.getBoolean("wifi");
                Boolean room_phone = rs.getBoolean("phone");
                String other_asset = rs.getString("other_assets");
                Boolean room_status = rs.getBoolean("status");
                
                roomList.add(new Room(room_id, room_category, Integer.toString(room_bed_no), room_tv, room_wifi, room_phone, other_asset, room_status));
               
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        rooms_table.getItems().setAll(roomList);
    }
    
    private void loadBookedRoomData() {
        handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM BOOK WHERE CHECKED_OUT=FALSE";
        ResultSet rs = handler.execQuery(qu);
        try {
            while (rs.next()) {
         String uniqueId=rs.getString("unique_id");
         String roomNo=rs.getString("room_id");
         String customerId=rs.getString("customer_id");
         String customerName=rs.getString("customer_name");
         int guest=rs.getInt("guests");
         String booked_date=rs.getString("date_booked");
         String checkIn=rs.getString("check_in");
         String checkOut=rs.getString("check_out");
         String bookType=rs.getString("type");
                
                bookedRoomList.add(new BookedRoom(uniqueId, roomNo, customerId, customerName, Integer.toString(guest), booked_date, checkIn, checkOut, bookType));
               
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        booked_rooms_table.getItems().setAll(bookedRoomList);
        
    }

    @FXML
    private void handleUpdateMenuAction(ActionEvent event) {
        if(selectedRoom!=null){
            loadUpdateRoom("/hotel/management/system/addroom/addRoom.fxml", "Update Room", selectedRoom);
        }
    }

    @FXML
    private void handleDeleteMenuAction(ActionEvent event) {
        deleteRoom(selectedRoom.getId());
    }

    @FXML
    private void handleRoomRowClicked(MouseEvent event) {
        if (index >= 0 && index < rooms_table.getItems().size() && rooms_table.getSelectionModel().isSelected(index)  ) {
        onSelect();
        }
        if(multiSelection){
            rooms_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }else{
           rooms_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); 
        }
    }

    private void onSelect() {    
        if(rooms_table.getSelectionModel().getSelectedItem().getStatus().equals("Taken")){
             Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The selected room is booked \n Discharge customer(s) before applying any changes");
            alert.showAndWait();
            rooms_table.getSelectionModel().clearSelection(index);
            delete_menu.setDisable(true);
            update_menu.setDisable(true);
            book_in.setDisable(true);
            book_room.setDisable(true);
            return;
        }
        if (rooms_table.getSelectionModel().getSelectedItem() != null) {
            selectedRoom = rooms_table.getSelectionModel().getSelectedItem();
            delete_menu.setDisable(false);
            update_menu.setDisable(false);
            book_in.setDisable(false);
            book_room.setDisable(false);
             }
    }
    
    private void unSelect() {
        selectedRoom = null;
        delete_menu.setDisable(true);
        update_menu.setDisable(true);
        book_in.setDisable(true);
        book_room.setDisable(true);
    }
    
    
    public void deleteRoom(String categoryID) {
       Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
       deleteAlert.setTitle("Confirm Delete");
       deleteAlert.setHeaderText(null);
       deleteAlert.setContentText("Delete Room?");
       Optional<ButtonType> result = deleteAlert.showAndWait();
        if (result.get() == ButtonType.OK){
    
            String deleteQuery = "DELETE FROM ROOM WHERE ID = " + "'"+categoryID+"'";
       if(handler.execAction(deleteQuery)){
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Room deleted");
       alert.showAndWait();   
        
        roomList.removeAll(roomList);
        loadRoomData();
        
       }else{
       Alert alert = new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText("Room delete failed");
       alert.showAndWait();
       }
       
            } else {
             deleteAlert.close();
        }
    } 

    void loadWindow(String loc, String title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            
            stage.setOnCloseRequest((WindowEvent we) -> {
            roomList.removeAll(roomList);
            loadRoomData();
            });
            
            stage.sizeToScene();
            stage.show();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
   private Stage loadUpdateRoom(String loc, String title, Room room) {
       try{
  FXMLLoader loader = new FXMLLoader(getClass().getResource( loc));
  Stage stage = new Stage(StageStyle.DECORATED);
  stage.setScene(new Scene((Pane) loader.load()));
  stage.show();

  AddRoomController controller = loader.<AddRoomController>getController();
  controller.initData(room);
  
  stage.setOnCloseRequest((WindowEvent we) -> {
      roomList.removeAll(roomList);
      loadRoomData();
  });
  
  return stage;
       }catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }
   
   private Stage loadBookRoom(String loc, String title, String roomId) {
       try{
  FXMLLoader loader = new FXMLLoader(getClass().getResource( loc));
  Stage stage = new Stage(StageStyle.DECORATED);
  stage.setScene(new Scene((Pane) loader.load()));
  stage.show();

  BookRoomController controller = loader.<BookRoomController>getController();
  controller.initData(roomId);
    stage.setOnCloseRequest((WindowEvent we) -> {
      roomList.removeAll(roomList);
      bookedRoomList.removeAll(bookedRoomList);
      loadRoomData();
      loadBookedRoomData();
  });
    
  return stage;
       }catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }
   
   private Stage loadCheckOut(String loc, String title, ArrayList<BookedRoom> invoice_data) {
       try{
  FXMLLoader loader = new FXMLLoader(getClass().getResource( loc));
  Stage stage = new Stage(StageStyle.DECORATED);
  stage.setScene(new Scene((Pane) loader.load()));
  stage.show();

  CheckOutController controller = loader.<CheckOutController>getController();
  controller.initData(invoice_data);
    stage.setOnCloseRequest((WindowEvent we) -> {
      roomList.removeAll(roomList);
      bookedRoomList.removeAll(bookedRoomList);
      loadRoomData();
      loadBookedRoomData();
  });
    
  return stage;
       }catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
       return null;
    }

    @FXML
    private void handleCheckedRoomRowClicked(MouseEvent event) {
        if (index >= 0 && index < booked_rooms_table.getItems().size() && booked_rooms_table.getSelectionModel().isSelected(index)  ) {
        onCheckOutSelect();
        }
        if(multiSelection){
            booked_rooms_table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }else{
           booked_rooms_table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); 
        }
    }
    
    private void onCheckOutSelect() {    
        if (booked_rooms_table.getSelectionModel().getSelectedItem() != null) {
            selectedBookedRoom = booked_rooms_table.getSelectionModel().getSelectedItem();
            check_out_menu.setDisable(false);
            chech_out.setDisable(false);
             }
    }
    
    private void unCheckOutSelect() {
        selectedRoom = null;
        check_out_menu.setDisable(true);
        chech_out.setDisable(true);
    }

}
