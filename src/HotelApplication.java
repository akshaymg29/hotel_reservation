import ui.AdminMenu;
import ui.MainMenu;

import java.util.Scanner;

public class HotelApplication {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
//        Customer customer = new Customer("first","second","j@domain.com");
//        System.out.println(customer);
//
//        Customer customer1 = new Customer("first","second","email");
//        System.out.println(customer1);
        AdminMenu adminMenu = new AdminMenu();
        MainMenu mainMenu = new MainMenu(adminMenu);
        mainMenu.displayMainMenu(sc);
        sc.close();
    }
}