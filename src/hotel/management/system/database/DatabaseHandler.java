package hotel.management.system.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public final class DatabaseHandler {

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;

    private DatabaseHandler() {
        createConnection();
        setupRoomTable();
        setupCustomerTable();
        setupBookTable();
        setupRoomCategoryTable();
        setupSubAdminTable();
        setupGoodTable();
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | SQLException e) {
            System.out.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    void setupRoomTable() {
        String TABLE_NAME = "ROOM";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "	id varchar(200) primary key,\n"
                        + "	category varchar(200),\n"
                        + "	bed_no integer default 0,\n"
                        + "	tv boolean default false,\n"
                        + "	wifi boolean default false,\n"
                        + "	phone boolean default false,\n"
                        + "	other_assets long varchar default null,\n"
                        + "	status boolean default true"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        } finally {
        }
    }

    void setupCustomerTable() {
        String TABLE_NAME = "CUSTOMER";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "	id varchar(200) primary key,\n"
                        + "	name varchar(200),\n"
                        + "	address varchar(200),\n"
                        + "	mobile varchar(20),\n"
                        + "	email varchar(100),\n"
                        + "	category varchar(100)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        } finally {
        }
    }

    void setupBookTable() {
        String TABLE_NAME = "BOOK";
        try {

            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "	unique_id varchar(200) primary key,\n"
                        + "     room_id varchar(200),\n"
                        + "	customer_id varchar(200),\n"
                        + "	customer_name varchar(200),\n"
                        + "	guests integer default 0,\n"
                        + "	date_booked timestamp default CURRENT_TIMESTAMP,\n"
                        + "	check_in varchar(200),\n"
                        + "	check_out varchar(200),\n"
                        + "	type varchar(200),\n"
                        + "	oders long varchar default null,\n"
                        + "	checked_out boolean default false,\n"
                        + "	FOREIGN KEY (room_id) REFERENCES ROOM(id),\n"
                        + "	FOREIGN KEY (customer_id) REFERENCES CUSTOMER(id)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        } finally {
        }
    }
    void setupRoomCategoryTable(){
        String TABLE_NAME = "ROOM_CATEGORY";
        try {

            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();

            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     id varchar(200) primary key,\n"
                        + "	category varchar(200),\n"
                        + "	price varchar(200)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        } finally {
        }
    }
    
    void setupSubAdminTable() {
        String TABLE_NAME = "SUB_ADMIN";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "	username_id varchar(200) primary key,\n"
                        + "	password varchar(200),\n"
                        + "	add_user boolean default false,\n"
                        + "	add_room boolean default false,\n"
                        + "	add_customer boolean default false,\n"
                        + "	add_room_cat boolean default false,\n"
                        + "	add_goods boolean default false,\n"
                        + "	update_discount boolean default false"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        } finally {
        }
    }
    
    void setupGoodTable() {
        String TABLE_NAME = "GOODS";
        try {
            stmt = conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exists. Ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "	id varchar(200) primary key,\n"
                        + "	product_name varchar(200),\n"
                        + "	product_type varchar(200),\n"
                        + "	product_price varchar(200),\n"
                        + "	stock integer default 0"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " --- setupDatabase");
        } finally {
        }
    }
    
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        } finally {
        }
        return result;
    }

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        } finally {
        }
    }
}
