package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId,
                                   Integer timeInHours, Integer numberOfWheels) throws Exception {

        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.
        Reservation reservation = null;
        try{
           if(!parkingLotRepository3.findById(parkingLotId).isPresent() || !userRepository3.findById(userId).isPresent()){
               throw new Exception("Cannot make reservation");
           }

            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            Spot finalSpot = null;

            int totalPrice = Integer.MAX_VALUE;
            boolean success = false;

            for(Spot spot: spotList){
                if(spot.getOccupied()==false){
                    int price = timeInHours * spot.getPricePerHour();
                    if(finalSpot==null || price<totalPrice){
                        totalPrice = price;

                        if( (spot.getSpotType()==SpotType.TWO_WHEELER && numberOfWheels<=2) || (spot.getSpotType()==SpotType.FOUR_WHEELER && numberOfWheels<=4)
                                || (spot.getSpotType()==SpotType.OTHERS && numberOfWheels>4)){

                            finalSpot = spot;
                            success=true;
                        }
                    }
                }
            }

            if(finalSpot==null){
                throw new Exception("Cannot make reservation");
            }

            if(success==false){
                throw new Exception("Cannot make reservation");
            }

            reservation = new Reservation();
            finalSpot.setOccupied(Boolean.TRUE);
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(finalSpot);
            reservation.setUser(user);


            //updating user
            user.getReservationList().add(reservation);
            //updating spot
            finalSpot.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(finalSpot);

        }catch(Exception e){
            System.out.println(e);
        }

        return reservation;
    }


}
