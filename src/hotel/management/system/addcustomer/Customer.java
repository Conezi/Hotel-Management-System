/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.addcustomer;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Connel
 */
public class Customer {
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty address;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;
        private final SimpleStringProperty category;
        
        public Customer(String id, String name, String address, String mobile, String email, String category){
            this.id= new SimpleStringProperty(id);
            this.name=new SimpleStringProperty(name);
            this.address=new SimpleStringProperty(address);
            this.mobile=new SimpleStringProperty(mobile);
            this.email=new SimpleStringProperty(email);
            this.category=new SimpleStringProperty(category);
        }
        public String getId() {
            return id.get();
        }
        public String getName() {
            return name.get();
        }
        public String getAddress() {
            return address.get();
        }
        public String getMobile() {
            return mobile.get();
        }
        public String getEmail() {
            return email.get();
        }
        public String getCategory() {
            return category.get();
        }
}
