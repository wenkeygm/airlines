package com.everest.airline.controllers;

import com.everest.airline.config.DbConfig;
import com.everest.airline.database.DataReader;
import com.everest.airline.model.Flight;
import com.everest.airline.model.SortingList;
import com.everest.airline.resultextractors.GetFlight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class FlightsApiController {

    @Autowired
    public DataReader dataReader;

    private final DbConfig dbConfig = new DbConfig();

    private final NamedParameterJdbcTemplate npJdbcTemplate = dbConfig.namedParameterJdbcTemplate();

    @GetMapping({"/flights/{number}", "/flights"})
    public List<Flight> getAllFlights(@PathVariable Optional<String> number) {
        if(number.isPresent()){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("number", number.get());
             return npJdbcTemplate.query("select * from flights where flight_number = :number", map, new GetFlight());
        }else
        return npJdbcTemplate.query("select * from flights", new GetFlight());
    }

    //     CUD
    @PostMapping("/flights")
    public ResponseEntity<String> create(String source, String destination, String departureDate, String departureTime, String arrivalDate, String arrivalTime, String availableSeats, String economicSeatsAvailable, String secondClassSeatsAvailable, String firstClassSeatsAvailable, String economicClassCapacity, String secondClassCapacity, String firstClassCapacity, String baseFare) {
        List<Flight> flightsList = new DataReader().getFlightsList().stream().sorted(new SortingList()).collect(Collectors.toList());
        long lastId = flightsList.get(flightsList.size() - 1).getNumber();
        SqlParameterSource parmSource = new MapSqlParameterSource()
                .addValue("flight_number",  lastId+1)
                .addValue("source", source)
                .addValue("destination", destination)
                .addValue("departure_date", departureDate)
                .addValue("departure_time", departureTime)
                .addValue("arrival_date", arrivalDate)
                .addValue("arrival_time", arrivalTime)
                .addValue("available_seats", availableSeats)
                .addValue("economic_seats_avaliable", economicSeatsAvailable)
                .addValue("secondclass_seats_avaliable", secondClassSeatsAvailable)
                .addValue("firstclass_seats_avaliable", firstClassSeatsAvailable)
                .addValue("economic_capacity", economicClassCapacity)
                .addValue("secondclass_capacity", secondClassCapacity)
                .addValue("firstclass_capacity", firstClassCapacity)
                .addValue("basefare", baseFare);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertSql = "INSERT INTO flights VALUES (:flight_number,:source,:destination,:departure_date, :departure_time," +
                ":arrival_date,:arrival_time,:available_seats,:economic_seats_avaliable,:secondclass_seats_avaliable,:firstclass_seats_avaliable," +
                ":economic_capacity,:secondclass_capacity,:firstclass_capacity,:basefare)";
            npJdbcTemplate.update(insertSql,parmSource,keyHolder);
            return new ResponseEntity<>("Inserted Successfully", HttpStatus.OK);
                }
    // Update
    @PutMapping("/flights/{number}")
    public ResponseEntity<String> update(@PathVariable("number") long number, String source, String destination, String departureDate, String departureTime, String arrivalDate, String arrivalTime, String availableSeats, String economicSeatsAvailable, String secondClassSeatsAvailable, String firstClassSeatsAvailable, String economicClassCapacity, String secondClassCapacity, String firstClassCapacity, String baseFare){
        SqlParameterSource parmSource = new MapSqlParameterSource()
                .addValue("number", number)
                .addValue("source", source)
                .addValue("destination", destination)
                .addValue("departure_date", departureDate)
                .addValue("departure_time", departureTime)
                .addValue("arrival_date", arrivalDate)
                .addValue("arrival_time", arrivalTime)
                .addValue("available_seats", availableSeats)
                .addValue("economic_seats_avaliable", economicSeatsAvailable)
                .addValue("secondclass_seats_avaliable", secondClassSeatsAvailable)
                .addValue("firstclass_seats_avaliable", firstClassSeatsAvailable)
                .addValue("economic_capacity", economicClassCapacity)
                .addValue("secondclass_capacity", secondClassCapacity)
                .addValue("firstclass_capacity", firstClassCapacity)
                .addValue("basefare", baseFare);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertSql = "update flights set source=:source,destination=:destination,departure_date=:departure_date, departure_time = :departure_time," +
                                                "arrival_date=:arrival_date,arrival_time=:arrival_time, available_seats=:available_seats,economic_seats_avaliable=:economic_seats_avaliable,secondclass_seats_avaliable=:secondclass_seats_avaliable,firstclass_seats_avaliable=:firstclass_seats_avaliable," +
                                                "economic_capacity=:economic_capacity,secondclass_capacity=:secondclass_capacity,firstclass_capacity=:firstclass_capacity,basefare=:basefare where  flight_number = :number";
         npJdbcTemplate.update(insertSql,parmSource,keyHolder);
         return new ResponseEntity<>("Updated successfully", HttpStatus.OK);

    }

    // Update
    @DeleteMapping("/flights/{number}")
    public ResponseEntity<String> delete(@PathVariable long number) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("number", number);
        String deleteQuery = "delete from flights where flight_number = :number";
        npJdbcTemplate.update(deleteQuery,parameterSource);
        return new ResponseEntity<>("record Deleted", HttpStatus.OK);
    }
}
