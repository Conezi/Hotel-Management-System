/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.imt.checkOut;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import hotel.management.system.imt.database.DatabaseHandler;
import hotel.management.system.imt.main.MainController;
import hotel.management.system.imt.settings.Preferences;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Connel
 */
public class CheckOutController implements Initializable {

    @FXML
    private Label titleLabel;
    @FXML
    private JFXButton check_out;
    @FXML
    private JFXButton print;
    @FXML
    private JFXButton cancle;
    @FXML
    private BorderPane border_pane;
    @FXML
    private JFXTextArea invoice_area;
    
    private String invoice="";
    private String checkOutDate;
    private double overAllPrice=0.0;
    private double toPayPrice=0.0;
    //private final int discount=10;
    private final ArrayList<String> room_id = new ArrayList<>();
    //private final Label jobStatus = new Label();
   
    
    private DatabaseHandler handler;
    private Date today;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
        checkOutDate = dateFormat.format(today);
        
    }    

    public void initData(ArrayList<String> invoiceData){
        Preferences preferences = Preferences.getPreferences();
        for(String invoiceDetails:invoiceData){
        String[] data=invoiceDetails.split(",");
        double total=Double.parseDouble(loadRoomData(data[0]));
        if(getTimeDiff(data[4])!=0){
            total=Double.parseDouble(loadRoomData(data[0]))*getTimeDiff(data[4]);
        }
        try{
        invoice+="ROOM NO: "+data[0]+"\n\n"+"CUSTOMER NAME: "+data[2]+"\n\n"+"GUESTS: "+data[3]+"\n\n"+"DATE BOOKED: "+data[4]+"\n\n"+
                "CHECK IN DATE: "+data[5]+"\n\n"+"ESTIMATED CHECK OUT DATE: "+data[6]+"\n\n"+"CHECK OUT DATE: "
                +checkOutDate+"\n\n"+"BOOKING TYPE: "+data[7]+"\n\n"+"PRICE PER DAY: "+"₦"+formatPrice(Double.parseDouble(loadRoomData(data[0])))+"\n\n"+"TOTAL PRICE FOR ROOM: "+"₦"+formatPrice(total)+"\n\n\n";
            }catch (NumberFormatException err){
                invoice+="ROOM NO: "+data[0]+"\n\n"+"CUSTOMER NAME: "+data[2]+"\n\n"+"GUESTS: "+data[3]+"\n\n"+"DATE BOOKED: "+data[4]+"\n\n"+
                "CHECK IN DATE: "+data[5]+"\n\n"+"ESTIMATED CHECK OUT DATE: "+data[6]+"\n\n"+"CHECK OUT DATE: "
                +checkOutDate+"\n\n"+"BOOKING TYPE: "+data[7]+"\n\n"+"PRICE PER DAY: "+"₦"+loadRoomData(data[0])+"\n\n"+"TOTAL PRICE FOR ROOM: "+"₦"+total+"\n\n\n";
            }
        room_id.add(data[0]);
        overAllPrice+=total;
        toPayPrice=overAllPrice-((overAllPrice*preferences.getDiscount())/100);
        
        }
        invoice+="TOTAL PRICE: "+"₦"+formatPrice(overAllPrice)+"\n DISCOUNT: "+preferences.getDiscount()+"%"+"\n ESTIMATED AMOUNT TO PAY: "+"₦"+formatPrice(toPayPrice);
         invoice_area.setText(invoice);
    }
    @FXML
    private void handleCheckOutAction(ActionEvent event) {
        room_id.forEach((id) -> {
            checkOut(id, checkOutDate);
        });
    }

    @FXML
    private void handlePrintAction(ActionEvent event) {
        //print(invoice_area);
        print(border_pane);   
    }

    @FXML
    private void handleCancleAction(ActionEvent event) {
        Stage stage = (Stage) border_pane.getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        stage.close();
    }
    
    private String loadRoomData(String id) {
        handler = DatabaseHandler.getInstance();
        String qu = "SELECT * FROM ROOM WHERE ID = "+ "'"+id+"'";
        ResultSet rs = handler.execQuery(qu);
        String room_category="";
        try {
            while (rs.next()) {
                room_category = rs.getString("category"); 
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return loadRoomCategoryData(room_category);
    }
    
    private String loadRoomCategoryData(String category) {
        String selectQuery = "SELECT * FROM ROOM_CATEGORY WHERE CATEGORY="+ "'"+category+"'";
        ResultSet rs = handler.execQuery(selectQuery);
        String price="";
        try {
            while (rs.next()) {
                price=rs.getString("price");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return price;
    } 
    
    private String formatPrice(double price){
        NumberFormat numberFormatter=NumberFormat.getCurrencyInstance();
        String result = numberFormatter.format(price);
        return result.substring(1);
    }
    
    private long getTimeDiff(String data){
        try{
        SimpleDateFormat oldDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
          Date oldDate=oldDateFormat.parse(data);
          long diff = oldDate.getTime() - today.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            
            return diffDays;
            } catch (ParseException e) {
            return 0;
            }
    }
    
    private void checkOut(String RoomNo, String date){
       String updateQuery =  "UPDATE BOOK SET "
        + "CHECK_OUT="+ "'" + date + "',"
        + "CHECKED_OUT="+ "'" + true + "'"
        +" WHERE ROOM_ID ="+"'"+RoomNo+"'";
        if(handler.execAction(updateQuery)){
       //System.out.println("Room updated successfuly");
       updateRoom(RoomNo);
       
       }else{
            System.out.println("Room category update failed");
       }
   }
    
    private void updateRoom(String RoomNo){
       String updateQuery =  "UPDATE ROOM SET "
        + "STATUS="+ "'" + true + "'"
        +" WHERE ID ="+"'"+RoomNo+"'";
        if(handler.execAction(updateQuery)){
          
       check_out.setDisable(true);
       print.setDisable(false);
       cancle.setText("DONE");
       System.out.println("Room updated successfuly");
       
       }else{
            System.out.println("Room category update failed");
       }
   }
    
    private void print(Node node) 
    {
        // Define the Job Status Message
        //jobStatus.textProperty().unbind();
        //jobStatus.setText("Creating a printer job...");
         
        // Create a printer job for the default printer
        PrinterJob job = PrinterJob.createPrinterJob();
         
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) 
        {
            // Show the printer job status
           // jobStatus.textProperty().bind(job.jobStatusProperty().asString());
             
            // Print the node
            boolean printed = job.printPage(node);
 
            if (printed) 
            {
                // End the printer job
                job.endJob();
            } 
            else
            {
                // Write Error Message
               // jobStatus.textProperty().unbind();
                //jobStatus.setText("Printing failed.");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Printer error");
                alert.setHeaderText(null);
                alert.setContentText("Printing failed.");
                alert.showAndWait();
            }
        } 
        else
        {
            // Write Error Message
            //jobStatus.setText("Could not create a printer job.");
            System.out.println("Could not create a printer job.");
        }
    }
    
}
