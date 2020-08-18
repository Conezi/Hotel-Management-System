/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.imt.main;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Connel
 */
public class BookedRoom {
    private final SimpleStringProperty roomId;
    private final SimpleStringProperty customerId;
    private final SimpleStringProperty customerName;
    private final SimpleStringProperty guests;
    private final SimpleStringProperty dateBooked;
    private final SimpleStringProperty checkIn;
    private final SimpleStringProperty checkOut;
    private final SimpleStringProperty type;
    
    public BookedRoom(String roomId, String customerId, String customerName, String guests, String dateBooked, String checkIn, String checkOut, String type){
        this.roomId = new SimpleStringProperty(roomId);
        this.customerId = new SimpleStringProperty(customerId);
        this.customerName = new SimpleStringProperty(customerName);
        this.guests = new SimpleStringProperty(guests);
        this.dateBooked = new SimpleStringProperty(dateBooked);
        this.checkIn = new SimpleStringProperty(checkIn);
        this.checkOut = new SimpleStringProperty(checkOut);
        this.type = new SimpleStringProperty(type);
    }
    
    public String getRoomId() {
            return roomId.get();
        }
    public String getCustomerId() {
            return customerId.get();
        }
    public String getCustomerName() {
            return customerName.get();
        }
    public String getGuests() {
            return guests.get();
        }
    public String getDateBooked() {
            return dateBooked.get();
        }
    public String getCheckIn() {
            return checkIn.get();
        }
    public String getCheckOut() {
            return checkOut.get();
        }
    public String getType() {
            return type.get();
        }
}
