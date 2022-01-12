/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kesmarkijavafeladat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *
 * @author imaginifer
 */
public class DatabaseHandler {
    
    private Connection conn;

    //constructor connects to db with given password
    public DatabaseHandler(String dbPassword) {
        try{
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/address_admin_test?useSSL=false", "root", dbPassword);
        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    //list all rows of a table
    private HashMap<Integer,String> listTableRows(String sql, int foreignKey, String keyName, String valueName){
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            if(foreignKey != 0){
                stmt.setInt(1, foreignKey);
            }
            ResultSet rs = stmt.executeQuery();
            return readResultSetColumns(keyName, valueName, rs);
        }catch(SQLException | NullPointerException ex){
            System.out.println(ex.getMessage());
        }
        HashMap<Integer,String> ersatz = new HashMap<>();
        return ersatz;
    }
    
    //translate query results into key-value pairs
    private HashMap<Integer,String> readResultSetColumns(String keyName, String valueName, ResultSet results) throws SQLException{
        HashMap<Integer,String> returnee = new HashMap<>();
        while(results.next()){
            returnee.put(results.getInt(keyName), results.getString(valueName));
        }
        return returnee;
    }    
    
    //insert a row into a table
    private boolean insertTableRow(String sql, String value, int foreignKey){
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, value);
            if(foreignKey != 0){
                stmt.setInt(2, foreignKey);
            }
            stmt.execute();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    //edit a row in a table
    private boolean editTableRow(String sql, String value, int ident){
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, value);
            stmt.setInt(2, ident);
            stmt.execute();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    //delete a row from a table
    private boolean deleteTableRow(String sql, int ident){
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, ident);
            stmt.execute();
        }catch(SQLException ex){
            System.out.println(ex.getMessage());
            return false;
        }
        return true;
    }
    
    /*The actual service methods*/
    
    public boolean newPerson(String name){
        String sql = "insert into persons(name) values (?);";
        return insertTableRow(sql, name, 0);
    }
    
    
    public HashMap<Integer,String> listPersons(){
        String sql = "select person_id, name from persons order by person_id asc;";        
        return listTableRows(sql, 0, "person_id", "name"); 
    }
    
    
    public boolean editPerson(int ident, String name){
        String sql = "update persons set name = ? where person_id = ?;";
        return editTableRow(sql, name, ident);
    }
    
    public boolean deletePerson(int ident){
        String sql = "delete from persons where person_id = ?;";
        return deleteTableRow(sql, ident);
    }
    
    
    public HashMap<Integer,String> listAddresses(int personId){
        String sql = "select address_id, address from addresses where person_id = ? order by address_id asc;";
        return listTableRows(sql, personId, "address_id", "address"); 
    } 
    
    public boolean newAddress(int personId, String addr){
        String sql = "insert into addresses(address, person_id) values (?,?);";
        return insertTableRow(sql, addr, personId);
    }
    
    
    public boolean editAddress(int ident, String value){
        String sql = "update addresses set address = ? where address_id = ?;";
        return editTableRow(sql, value, ident);
    }
    
    public boolean deleteAddress(int ident){
        String sql = "delete from addresses where address_id = ?;";
        return deleteTableRow(sql, ident);
    }
    
    public HashMap<Integer,String> listContacts(int addressId){
        String sql = "select contact_id, contact from contacts where address_id = ? order by contact_id asc;";
        return listTableRows(sql, addressId, "contact_id", "contact");
    } 
    
    public boolean newContact(int addressId, String addr){
        String sql = "insert into contacts(contact, address_id) values (?,?);";
        return insertTableRow(sql, addr, addressId);
    }
    
    
    public boolean editContact(int ident, String value){
        String sql = "update contacts set contact = ? where contact_id = ?;";
        return editTableRow(sql, value, ident);
    }
    
    public boolean deleteContact(int ident){
        String sql = "delete from contacts where contact_id = ?;";
        return deleteTableRow(sql, ident);
    }
    
    
    
    
    
    
    
    
}
