/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hotel.management.system.addcategory;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Connel
 */
public class Category {
        private final SimpleStringProperty id;
        private final SimpleStringProperty category;
        private final SimpleStringProperty price;
        
        public Category(String id, String category, String price){
            this.id= new SimpleStringProperty(id);
            this.category=new SimpleStringProperty(category);
            this.price=new SimpleStringProperty(price);
        }
        
        public String getId() {
            return id.get();
        }
        
        public String getCategory() {
            return category.get();
        }
        
        public String getPrice() {
            return price.get();
        }
}
