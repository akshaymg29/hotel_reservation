package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class HotelResource {

    private static HotelResource instance;
    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();

    private HotelResource() {
    }

    public static HotelResource getInstance() {
        if (null == instance) {
            synchronized (HotelResource.class) {
                if (instance == null) {
                    instance = new HotelResource();
                }
            }
        }
        return instance;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void createACustomer(String email, String firstName, String lastName) {
        customerService.addCustomer(email,firstName,lastName);
    }

    public IRoom getRoom(String roomNumber) {
        return reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = getCustomer(customerEmail);
        if(null == customer)
            return null;
        else {
            return reservationService.reserveARoom(customer,room,checkInDate,checkOutDate);
        }
    }

    public Collection<Reservation> getCustomerReservations(String customerEmail) {
        Customer customer = getCustomer(customerEmail);
        if(null == customer)
            return Collections.<Reservation>emptyList();

        return reservationService.getCustomerReservation(customer);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut, boolean isFreeRooms) {
        return reservationService.findRooms(checkIn, checkOut, isFreeRooms);
    }

    public Date getAddedDaystoDate(Date date, int ... noOfDaysToAdd) {
        return reservationService.getAddedDaystoDate(date, noOfDaysToAdd);
    }
}
