package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    private static AdminResource instance;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private AdminResource() {
    }

    public static AdminResource getInstance() {
        if (null == instance) {
            synchronized (AdminResource.class) {
                if (instance == null) {
                    instance = new AdminResource();
                }
            }
        }
        return instance;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(List<IRoom> rooms) {
        rooms.forEach(reservationService::addRoom);
    }

    public Collection<IRoom> getAllRooms() {
        return reservationService.getAllRooms();
    }

    public Collection<Customer> getAllCustomers() {
        return customerService.getAllCustomer();
    }

    public void displayAllReservations() {
        reservationService.printAllReservation();
    }
}
