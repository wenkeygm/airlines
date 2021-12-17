package com.everest.airline.services;
import com.everest.airline.database.DataParser;
import com.everest.airline.model.FilterClass;
import com.everest.airline.model.Flight;

import com.everest.airline.validation.ValidateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SearchService {
    private List<Flight> flightList;

    @Autowired
    public FilterClass filterClass;
    @Autowired
    public ValidateData validateData;


    public List<Flight> searchByFlight(String from, String to, String departureDate, String flightClass, String noOfPass) throws Exception {
        flightList = new ArrayList<>();
        if (DataParser.multiFileReader() != null)
            searchLogic(from, to, departureDate, flightClass, noOfPass);

        return flightList;
    }

    public void searchLogic(String from, String to, String departureDate, String flightClass, String noOfPass) throws Exception {
        boolean check;
        for (int i = 1; i < DataParser.multiFileReader().length; i++) {
            if (DataParser.multiFileReader()[i].isFile()) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(DataParser.multiFileReader()[i]));
                String[] strings = bufferedReader.readLine().split(",", -1);
                if ((strings[1].equalsIgnoreCase(from)) && (strings[2].equalsIgnoreCase(to)) && (strings[3].equalsIgnoreCase(departureDate))) {
                     check= validateData.validSeats(flightClass,Integer.parseInt(noOfPass),Integer.parseInt(strings[8]),Integer.parseInt(strings[9]),Integer.parseInt(strings[10]));
                    if(check) {
                        flightList.add(new Flight(Long.parseLong(strings[0]), strings[1], strings[2],
                                LocalDate.parse(strings[3]), LocalTime.parse(strings[4]),
                                LocalDate.parse(strings[5]), LocalTime.parse(strings[6]),
                                Integer.parseInt(strings[7]), Integer.parseInt(strings[8]),
                                Integer.parseInt(strings[9]), Integer.parseInt(strings[10]),
                                Double.parseDouble(strings[11])));
                    }
                    }
                }
            }
        }

}
