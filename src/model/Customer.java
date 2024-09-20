package model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {

    private String firstName;
    private String lastName;
    private String email;
    private static final Pattern VALID_EMAIL_REGEX = Pattern.compile("^(.+)@(.+).(.+)$");

    public Customer(String firstName, String lastName, String email) {

        isEmailValid(email);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    private void isEmailValid(String email) {
        if(!VALID_EMAIL_REGEX.matcher(email).matches())
            throw new IllegalArgumentException("Insert Valid Email address.");
    }

    public final String getFirstName() {
        return firstName;
    }

    public final String getLastName() {
        return lastName;
    }

    public final String getEmail() {
        return email;
    }

    public final void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public final void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public final void setEmail(String email) {
        isEmailValid(email);
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;

        return this.email.equals(customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Customer{" +
                " First Name:'" + firstName + '\'' +
                ", Last Name:'" + lastName + '\'' +
                ", Email:'" + email + '\'' +
                '}';
    }
}
