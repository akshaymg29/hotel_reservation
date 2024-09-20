package ui;

import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static final HotelResource HOTEL_RESOURCE = HotelResource.getInstance();
    private final AdminMenu adminMenu;

    public MainMenu(AdminMenu adminMenu) {
        this.adminMenu = adminMenu;
    }

    public void displayMainMenu(Scanner sc) {

         boolean isExit = false;

         while (!isExit) {
             print();
             String userInput = sc.nextLine();
             
             switch (userInput.charAt(0)) {
                 case '1':
                     findAndReserveRoom(sc);
                     break;
                 case '2':
                     displayMyReservations(sc);
                     break;
                 case '3':
                     createAccount(sc);
                     break;
                 case '4':
                     adminMenu.displayAdminMenu(sc);
                     break;
                 case '5':
                     isExit = true;
                     break;
                 default:
                     System.out.println("Enter valid response from 1 to 5");
             }
         }
    }

    public void findAndReserveRoom(Scanner sc) {

        Date checkInDate = null;
        Date checkOutDate = null;
        Date currentDate = new Date();
        do {
            while (null == checkInDate) {
                System.out.println("Enter checkIn Date mm/dd/yyyy example 02/01/2020");
                checkInDate = getInputDate(sc);
                if(null != checkInDate) {
                    if(!checkInDate.after(currentDate)) {
                        checkInDate = null;
                        System.out.println("Enter valid date after present day.");
                    }
                }
            }

            while (null == checkOutDate) {
                System.out.println("Enter checkOut Date mm/dd/yyyy example 02/21/2020");
                checkOutDate = getInputDate(sc);
            }

            if(!checkInDate.before(checkOutDate)) {
                System.out.println("Check-out date entered is before check-in date.");
                checkInDate = null;
                checkOutDate = null;
            }
            else
                break;

        }while (true);

        boolean isFreeRooms = false;
        while(true) {
            System.out.println("Would you like to book a free rooms? y/n");
            String userInput = sc.nextLine();

            if(!userInput.equalsIgnoreCase("y") && !(userInput.equalsIgnoreCase("n")))
                System.out.println("Please enter Y (Yes) or N (No)");
            else {
                if(userInput.equalsIgnoreCase("y"))
                    isFreeRooms = true;
                break;
            }
        }


        Collection<IRoom> availableRooms = HOTEL_RESOURCE.findARoom(checkInDate,checkOutDate, isFreeRooms);
        if(availableRooms.isEmpty()) {
            System.out.println("No rooms present.");
            Integer flexibleByDays = null;
            while(null == flexibleByDays) {
                System.out.println("Enter number of days for recommended rooms to show");
                String roomPrice = sc.nextLine();
                try {
                    flexibleByDays = Integer.parseInt(roomPrice);
                } catch (Exception e) {
                    System.out.println("Enter valid Integer number." + e.getLocalizedMessage());
                }
            }

            checkInDate = HOTEL_RESOURCE.getAddedDaystoDate(checkInDate, flexibleByDays);
            checkOutDate = HOTEL_RESOURCE.getAddedDaystoDate(checkOutDate, flexibleByDays);
            availableRooms = HOTEL_RESOURCE.findARoom(checkInDate,checkOutDate, false);
            if(availableRooms.isEmpty()) {
                System.out.println("No recommended rooms present.");
                return;
            }
            else {
                System.out.println("Recommended rooms for check-in date " + checkInDate + " and check-out date " + checkOutDate);
                availableRooms.forEach(System.out::println);
            }
        }
        else
            availableRooms.forEach(System.out::println);

        boolean isBooking = false;
        while(true) {
            System.out.println("Would you like to book a room? y/n");
            String userInput = sc.nextLine();

            if(!userInput.equalsIgnoreCase("y") && !(userInput.equalsIgnoreCase("n")))
                System.out.println("Please enter Y (Yes) or N (No)");
            else {
                if(userInput.equalsIgnoreCase("y"))
                    isBooking = true;
                break;
            }
        }

        if (isBooking) {
            while(true) {
                System.out.println("Do you have an account with us? y/n");
                String userInput = sc.nextLine();

                if(!userInput.equalsIgnoreCase("y") && !(userInput.equalsIgnoreCase("n")))
                    System.out.println("Please enter Y (Yes) or N (No)");
                else {
                    if(userInput.equalsIgnoreCase("n")) {
                        System.out.println("Please Create Customer account.");
                        return;
                    }
                    break;
                }
            }
        }
        else
            return;

        String emailAddress = getEmailAddress(sc);
        Customer customer = HOTEL_RESOURCE.getCustomer(emailAddress);
        if(null == customer) {
            System.out.println("Customer not found. Please create customer account");
        }
        else {
            System.out.println("What room number would you like to reserve?");
            String roomNumber = sc.nextLine();
            IRoom room = null;
            if(availableRooms.stream().anyMatch(obj-> obj.getRoomNumber().equalsIgnoreCase(roomNumber))) {
                room = HOTEL_RESOURCE.getRoom(roomNumber);
                Reservation reservation = HOTEL_RESOURCE.bookARoom(emailAddress,room, checkInDate,checkOutDate);
                if(null != reservation) {
                    System.out.println("Room reserved successfully");
                    System.out.println(reservation);
                }
                else {
                    System.out.println("Room Number not present. Start fresh reservation once again.");
                }
            }
        }
    }

    private Date getInputDate(Scanner sc) {
        String dateString = sc.nextLine();
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
        } catch (ParseException e) {
            System.out.println("Entered date is not in a valid format.");
        }
        return null;
    }

    public void displayMyReservations(Scanner sc) {
        String emailAddress = getEmailAddress(sc);
        Collection<Reservation> reservations = HOTEL_RESOURCE.getCustomerReservations(emailAddress);

        if(null == reservations || reservations.isEmpty())
            System.out.println("No reservations present for "+ emailAddress);
        else
            reservations.forEach(System.out::println);

        System.out.println();
    }

    public void createAccount(Scanner sc) {

        String emailAddress = getEmailAddress(sc);
        Customer customer = HOTEL_RESOURCE.getCustomer(emailAddress);
        if(null != customer) {
            System.out.println("Customer already exists.\n");
            return;
        }

        System.out.println("Enter First Name");
        String firstName = sc.nextLine();

        System.out.println("Enter Last Name");
        String lastName = sc.nextLine();

        try{
            HOTEL_RESOURCE.createACustomer(emailAddress,firstName,lastName);
            System.out.println("Account created successfully.");
        }catch (IllegalArgumentException e) {
            System.out.println("Account creation failed. " + e.getLocalizedMessage());
        }
        System.out.println();
    }

    public void print() {

        System.out.println("""
                Welcome to the Hotel Reservation Application\s
                ------------------------------------------------------------------\s
                1. Find and reserve a room\s
                2. See my reservations\s
                3. Create an account\s
                4. Admin\s
                5. Exit\s
                ------------------------------------------------------------------\s
                Please select a number for the menu option"""
        );
    }

    private String getEmailAddress(Scanner sc) {
        System.out.println("Enter your Email id (format: name@domain.com)");
        return sc.nextLine();
    }
}
