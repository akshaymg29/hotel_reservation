package ui;

import api.AdminResource;
import model.*;

import java.util.*;

public class AdminMenu {

    private static final AdminResource ADMIN_RESOURCE = AdminResource.getInstance();

    public void displayAdminMenu(Scanner sc) {
        boolean isBackToMenu = false;

        while (!isBackToMenu) {
            print();
            String userString = sc.nextLine();
            switch (userString.charAt(0)) {
                case '1':
                    displayAllCustomers();
                    break;
                case '2':
                    displayAllRooms();
                    break;
                case '3':
                    displayAllReservations();
                    break;
                case '4':
                    addRoom(sc);
                    break;
                case '5':
                    isBackToMenu = true;
                    break;
                default:
                    System.out.println("Enter valid response from 1 to 5");
            }
        }
    }

    public void displayAllCustomers() {
        Collection<Customer> customers = ADMIN_RESOURCE.getAllCustomers();
        if(customers.isEmpty())
            System.out.println("No customers present");
        else
            customers.forEach(System.out::println);

        System.out.println();
    }

    public void displayAllRooms() {
        Collection<IRoom> rooms = ADMIN_RESOURCE.getAllRooms();
        if(rooms.isEmpty())
            System.out.println("No rooms present");
        else
            rooms.forEach(System.out::println);

        System.out.println();
    }

    public void displayAllReservations() {
        ADMIN_RESOURCE.displayAllReservations();
    }

    public void addRoom(Scanner sc) {
        Collection<IRoom> roomCollection = ADMIN_RESOURCE.getAllRooms();
        List<IRoom> roomList = new ArrayList<>();
        boolean isAddingRoom = true;

        while(isAddingRoom) {
            System.out.println("Enter room number");
            String roomNumber = sc.nextLine();

            if (roomCollection.contains(roomNumber)) {
                System.out.println("This room number already exists.");
                continue;
            }

            Double price = null;
            while(null == price) {
            System.out.println("Enter price per night");
            String roomPrice = sc.nextLine();
                try {
                    price = Double.parseDouble(roomPrice);
                } catch (Exception e) {
                    System.out.println("Enter valid fraction number." + e.getLocalizedMessage());
                }
            }

            System.out.println("Enter room type: 1. for single bed, 2. for double bed");
            String roomTypeStr = sc.nextLine();
            while(!roomTypeStr.equalsIgnoreCase("1") && !roomTypeStr.equalsIgnoreCase("2")) {
                System.out.println("Enter valid number from 1 or 2.");
                roomTypeStr = sc.nextLine();
            }

            RoomType roomType = null;

            if(roomTypeStr.equalsIgnoreCase("1"))
                roomType = RoomType.SINGLE;
            else
                roomType = RoomType.DOUBLE;

            IRoom room = null;
            if(0.0 == price)
                room = new FreeRoom(roomNumber, roomType);
            else
                room = new Room(roomNumber, price, roomType);

            roomList.add(room);

            System.out.println("Would you like to add another room? y/n");
            String userInput = sc.nextLine();
            while(!userInput.equalsIgnoreCase("Y") && !userInput.equalsIgnoreCase("N")) {
                System.out.println("Please enter Y (Yes) or N (No)");
                userInput = sc.nextLine();
            }

            if(userInput.equalsIgnoreCase("N")) {
                isAddingRoom = false;
            }
        }
        ADMIN_RESOURCE.addRoom(roomList);
    }

    public void print() {
        System.out.println("Admin Menu \n"+
                "------------------------------------------------------------------ \n" +
                "1. See all Customers \n" +
                "2. See all Rooms \n" +
                "3. See all Reservations \n" +
                "4. Add a Room \n" +
                "5. Back to Main Menu  \n" +
                "------------------------------------------------------------------ \n" +
                "Please select a number for the menu option"
        );
    }
}
