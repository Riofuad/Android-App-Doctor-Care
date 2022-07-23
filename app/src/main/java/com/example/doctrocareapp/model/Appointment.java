package com.example.doctrocareapp.model;

import java.util.Date;

public class Appointment {
    private int noAppointment;
    private Date dateAppointment;
    private Date dateAppointmentTaken;
    private String nameDoctor;

    public Appointment(int noAppointment, Date dateAppointment, Date dateAppointmentTaken, String nameDoctor) {
        this.noAppointment = noAppointment;
        this.dateAppointment = dateAppointment;
        this.dateAppointmentTaken = dateAppointmentTaken;
        this.nameDoctor = nameDoctor;
    }

    public int getNoAppointment() {
        return noAppointment;
    }

    public void setNoAppointment(int noAppointment) {
        this.noAppointment = noAppointment;
    }

    public Date getDateAppointment() {
        return dateAppointment;
    }

    public void setDateAppointment(Date dateAppointment) {
        this.dateAppointment = dateAppointment;
    }

    public Date getDateAppointmentTaken() {
        return dateAppointmentTaken;
    }

    public void setDateAppointmentTaken(Date dateAppointmentTaken) {
        this.dateAppointmentTaken = dateAppointmentTaken;
    }

    public String getNameDoctor() {
        return nameDoctor;
    }

    public void setNameDoctor(String nameDoctor) {
        this.nameDoctor = nameDoctor;
    }

    public void appointmentStatus(){
        System.out.println("The appointment "+ getNoAppointment()+" is in progress");
    }
}
