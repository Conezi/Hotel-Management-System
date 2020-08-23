/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.main;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Connel
 */
public class Room {
        private final SimpleStringProperty id;
        private final SimpleStringProperty category;
        private final SimpleStringProperty bed_no;
        private final SimpleBooleanProperty tv;
        private final SimpleBooleanProperty wifi;
        private final SimpleBooleanProperty phone;
        private final SimpleStringProperty others;
        private final SimpleBooleanProperty status;

        public Room(String id, String category, String bed_no, Boolean tv, Boolean wifi, Boolean phone, String others, Boolean status) {
            this.id = new SimpleStringProperty(id);
            this.category = new SimpleStringProperty(category);
            this.bed_no = new SimpleStringProperty(bed_no);
            this.tv = new SimpleBooleanProperty(tv);
            this.wifi = new SimpleBooleanProperty(wifi);
            this.phone = new SimpleBooleanProperty(phone);
            this.others = new SimpleStringProperty(others);
            this.status = new SimpleBooleanProperty(status);
        }

    Room() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

        public String getId() {
            return id.get();
        }

        public String getCategory() {
            return category.get();
        }

        public String getBed_no() {
            return bed_no.get();
        }

        public String getTv() {
            if(tv.get()){
                return "Yes";
            }else{
               return "No" ;
            }
        }
        
        public String getWifi() {
            if(wifi.get())
                return "Yes";
            else
                return "No";
        }
        
        public String getPhone() {
            if(phone.get())
                return "Yes" ;
            else
                return "No";
        }
        
        public String getOthers() {
            return others.get();
        }
        
        public String getStatus() {
            if(status.get())
                return "Available";
            else
                return "Taken";
        }
            
}
