package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.time.LocalDate;
import java.util.*;

public class ReservationService {

    private static ReservationService instance;
    private final Map<String,IRoom> roomMap = new HashMap<String,IRoom>();
    private final Map<String, Set<Reservation>> strReservationsMap = new HashMap<String,Set<Reservation>>();

    private static final int RECOMMENDED_ROOM_ADDED_DAYS = 7;

    private ReservationService() {
    }

    public static ReservationService getInstance() {
        if (null == instance) {
            synchronized (ReservationService.class) {
                if (instance == null) {
                    instance = new ReservationService();
                }
            }
        }
        return instance;
    }

    public void addRoom(IRoom room) {
        roomMap.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return roomMap.get(roomId);
    }

    public Collection<IRoom> getAllRooms() {
        return roomMap.values();
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        if (!strReservationsMap.containsKey(customer.getEmail())) {
            strReservationsMap.put(customer.getEmail(), new HashSet<Reservation>());
        }
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        strReservationsMap.get(customer.getEmail()).add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate, boolean isFreeRooms) {
        Map<String,IRoom> availableRoomsMap = null;
        if(isFreeRooms) {
            availableRoomsMap = new HashMap<String, IRoom>();

            for (IRoom room : roomMap.values()) {
                if(room.isFree()) {
                    availableRoomsMap.put(room.getRoomNumber(), room);
                }
            }
        }
        else {
            availableRoomsMap = new HashMap<String, IRoom>(roomMap);
        }

//        if(isFlexible) {
//            checkInDate = getAddedDaystoDate(checkInDate,flexibleByDays);
//            checkOutDate = getAddedDaystoDate(checkOutDate,flexibleByDays);
//        }

        for(Set<Reservation> reservations : strReservationsMap.values()) {
            for (Reservation obj : reservations) {
                if((obj.getCheckInDate().before(checkOutDate)) && (obj.getCheckOutDate().after(checkInDate))) {
                    availableRoomsMap.remove(obj.getRoom().getRoomNumber());
                }
            }
        }
        return availableRoomsMap.values();
    }

    public Collection<Reservation> getCustomerReservation(Customer customer) {
        return strReservationsMap.get(customer.getEmail());
    }

    public void printAllReservation() {

        if(strReservationsMap.isEmpty())
            System.out.println("Reservations not present.");
        else {
            for (Set<Reservation> reservations : strReservationsMap.values()) {
                reservations.forEach(System.out::println);
            }
        }
    }

    public Date getAddedDaystoDate(Date date, int ... noOfDaysToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, (noOfDaysToAdd.length >= 1) ? noOfDaysToAdd[0] : RECOMMENDED_ROOM_ADDED_DAYS);
        return calendar.getTime();
    }
}
