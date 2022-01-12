/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kesmarkijavafeladat;

import java.io.Console;
import java.util.HashMap;
import java.util.Scanner;

/**
 *
 * @author imaginifer
 */
public class TextUI {
    private DatabaseHandler dh;

    public TextUI() {
        System.out.println("Welcome!");
        this.dh = new DatabaseHandler(requestPassword());
    }
    
    //Names of the people in registry, first screen
    public void personsList(){
        
        boolean proceed = true;
        do{
            
           HashMap<Integer, String> persons = dh.listPersons();
                       
           int q = printItemsMap(persons, "__Persons in registry");
           switch(q){
               case 0:
                   String name = requestInput("the name of the new person:");
                   dh.newPerson(name);
                   break;
               case -1:
                   proceed=!requestConfirmation("quit the program");
                   break;
               default:
                   personOptionsMenu(persons.get(q), q);
           }
                   
        }while(proceed);
        System.out.println("Goodbye!");
    }
    
    //Options for a selected person
    private void personOptionsMenu(String title, int personId){
        String[] menu = {"List addresses", "Edit name", "Delete entry"};
        int q = choiceMenu(menu, "Options menu: "+title);
        switch(q){
            case 3:
                if(requestConfirmation("delete "+title)){
                    dh.deletePerson(personId);
                }
                break;
            case 2:
                String newName = requestInput("the new name:");
                dh.editPerson(personId, newName);
                break;
            case 1:
                addressList(personId);
                break;
            default:
                break;
        }
    }
    
    //List of addresses of a selected person
    private void addressList(int personId){
        HashMap<Integer, String> addresses = dh.listAddresses(personId);
        int q = printItemsMap(addresses, "__Addresses for selected person");
        switch(q){
            case 0:
                String newAddress = requestInput("the new address:");
                dh.newAddress(personId, newAddress);
                break;
            case -1:
                break;
            default:
                addressOptionsMenu(addresses.get(q), q);
                break;
        }
    }
    
    //Options for a selected address
    private void addressOptionsMenu(String title, int addressId){
        String[] menu = {"List contacts", "Edit address", "Delete entry"};
        int q = choiceMenu(menu, "Options menu: "+title);
        switch(q){
            case 3:
                if(requestConfirmation("delete "+title)){
                    dh.deleteAddress(addressId);
                }
                break;
            case 2:
                String newAddress = requestInput("the new address:");
                dh.editAddress(addressId, newAddress);
                break;
            case 1:
                contactsList(addressId);
                break;
            default:
                break;
        }
    }
    
    //Listing of all contacts for a given address
    private void contactsList(int addressId){
        HashMap<Integer, String> contacts = dh.listContacts(addressId);
        int q = printItemsMap(contacts, "__Contacts for selected address");
        switch(q){
            case 0:
                String newContact = requestInput("the new contact:");
                dh.newContact(addressId, newContact);
                break;
            case -1:
                break;
            default:
                contactOptionsMenu(contacts.get(q), q);
                break;
        }
    }
    
    //Options for a selected contact
    private void contactOptionsMenu(String title, int contactId){
        String[] menu = {"Edit contact", "Delete entry"};
        int q = choiceMenu(menu, "Options menu: "+title);
        switch(q){
            case 2:
                if(requestConfirmation("delete "+title)){
                    dh.deleteContact(contactId);
                }
                break;
            case 1:
                String newContact = requestInput("the new contact:");
                dh.editContact(contactId, newContact);
                break;            
            default:
                break;
        }
    }
    
    //menu pathway selector, 0 cancellation response
    private int choiceMenu(String[] choices, String title){
        System.out.println(title);
        for (int i = 0; i < choices.length; i++) {
            System.out.printf("   %-7d%s%n", (i + 1), choices[i]);
        }
        System.out.printf("   %-7s%s%n", "Other", "Quit");
        
        String response = requestInput("the number of desired action:");
        int returnNr = 0;
        
        try{
            returnNr = Integer.parseInt(response);
        } catch (NumberFormatException e){
            return 0;
        }
        return returnNr > 0 && returnNr <= choices.length ? returnNr : 0;
    }
    
    //Request and process text input
    private String requestInput(String inputInQuestion){
        String response = "";
        do{
            System.out.println("Please type in "+ inputInQuestion);
            Scanner sc = new Scanner(System.in);
            response = sc.nextLine();
            if(response.isEmpty()){
                System.out.println("Empty input is invalid!");
            }
        } while (response.isEmpty());
        
        return response;
    }
    
    //Request secure password if possible
    private String requestPassword(){
        Console cns = System.console();
        if(cns == null){
            System.out.println("Please type in your database password below:");
            Scanner sc = new Scanner(System.in);
            return sc.nextLine();
        } else {
            char[] pwd = cns.readPassword("Please type in your database password:");
            return String.copyValueOf(pwd);
        }
    }
    
    //Confirmation feedback question
    private boolean requestConfirmation(String actionToConfirm){
        System.out.println("Are you sure you wish to "+actionToConfirm+"? y/any");
        Scanner sc = new Scanner(System.in);
        String response = sc.nextLine();
        return response.toLowerCase().equals("y");
    }
    
    //output list selection screen, -1 cancellation response
    private int printItemsMap(HashMap<Integer, String> items, String title){
        System.out.println(title);
        if(!items.isEmpty()){
            items.keySet().forEach(i -> {
                System.out.printf("   %-7d%s%n", i, items.get(i));
            });
        }
        System.out.printf("   %-7d%s%n", 0, "Add new");
        System.out.printf("   %-7s%s%n", "Other", "Quit");
        
        String response = requestInput("the number of the chosen item:");
        int returnNr = -1;
        
        try{
            returnNr = Integer.parseInt(response);
        } catch (NumberFormatException e){
            return -1;
        }
        return returnNr == 0 || items.containsKey(returnNr) ? returnNr : -1;
        
    }
}
