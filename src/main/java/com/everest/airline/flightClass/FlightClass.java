package com.everest.airline.flightClass;

import com.everest.airline.model.Flight;

public abstract class FlightClass {

    private int passengers;
    private String flightClass;
    private Flight flight;

    public FlightClass(int passengers, String flightClass, Flight flight) {
        this.passengers = passengers;
        this.flightClass = flightClass;
        this.flight = flight;
        setNumberOfPassengers();
        setFlightClass();
    }

    public void setNumberOfPassengers(){
        flight.setNoOfPass(passengers);
    }

    public void setFlightClass(){
        flight.setFlightClass(flightClass);
    }



}
