package model;

import java.util.Date;
import java.util.Objects;

public class Reservation {

    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRoom(IRoom room) {
        this.room = room;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation reservation)) return false;

        return Objects.equals(customer, reservation.customer) && Objects.equals(room, reservation.room) &&
                Objects.equals(checkInDate, reservation.checkInDate) && Objects.equals(checkOutDate, reservation.checkOutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer,room, checkInDate,checkOutDate);
    }

    @Override
    public String toString() {
        return "Name: " + customer.getFirstName() + " " + customer.getLastName() + "\n" +
                "Room: " + room.getRoomNumber() + " " + room.getRoomType() + " Bedroom\n" +
                "Price: " + room.getRoomPrice() + " per night\n" +
                "Check-In: " + checkInDate + "\n"+
                "Check-Out: " + checkOutDate + "\n";
    }
}
