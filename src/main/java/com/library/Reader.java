package com.library;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;

public class Reader {
    private  String firstName;
    private String lastName;
    private final Gender gender;
    private final LocalDate birthday;
    private int id;
    private static int idCount=0;
    private double fine;

    public Reader(String firstName, String lastName, Gender gender, LocalDate birthday) {
        if(birthday.isAfter(LocalDate.now())){
            throw new IllegalArgumentException("Birthday must be earlier than today");
        }
        this.birthday = birthday;
        this.lastName = lastName;
        this.gender = gender;
        this.firstName = firstName;
        this.id = idCount;
        idCount++;
    }
    @JsonCreator
    public Reader(@JsonProperty("firstName") String firstName,
                  @JsonProperty("lastName") String lastName,
                  @JsonProperty("gender") Gender gender,
                  @JsonProperty("birthday") LocalDate birthday,
                  @JsonProperty("id") int id) {
        this.lastName = lastName;
        this.gender = gender;
        this.birthday = birthday;
        this.firstName = firstName;
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public String info(){
        return "id: "+id+"\n"+
                firstName+" "+lastName+"\n"
                +"Gender: "+gender+"\n"
                +"Birthday: "+birthday+"\n"
                +"Fine: "+fine;
    }
    public void setName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public void addFine(double fine) {
        this.fine += fine;
    }
    public String payFine(double amount) {
        if(amount >= fine){
            double rest = amount-fine;
            fine = 0;
            return "You successfully paid fine. You change "+rest;
        }
        this.fine -= amount;
        return "You successfully paid "+amount+". You fine: "+fine;
    }
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public double getFine() {
        return fine;
    }

    public Gender getGender() {
        return gender;
    }
    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return info();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reader reader = (Reader) o;
        return id == reader.id && Double.compare(fine, reader.fine) == 0 && Objects.equals(firstName, reader.firstName) && Objects.equals(lastName, reader.lastName) && gender == reader.gender && Objects.equals(birthday, reader.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, gender, birthday, id, fine);
    }
}
