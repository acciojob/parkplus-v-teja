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
        try {

            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
                throw new Exception("Cannot make reservation");
            }
            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            boolean spotsCheck = false;
            for (Spot spot : spotList) {
                if (!spot.getOccupied()) {
                    spotsCheck = true;
                    break;
                }
            }

            if (!spotsCheck) {
                throw new Exception("Cannot make reservation");
            }


            SpotType spotType;
            if (numberOfWheels <=2 ) {
                spotType = SpotType.TWO_WHEELER;
            } else if (numberOfWheels <=4) {
                spotType = SpotType.FOUR_WHEELER;
            } else{
                spotType = SpotType.OTHERS;
            }


            int totalPrice = Integer.MAX_VALUE;

            spotsCheck = false;

            Spot finalSpot = null;

            for (Spot spot : spotList) {
                if (spotType.equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS)) {
                    if (spot.getPricePerHour()*timeInHours < totalPrice && !spot.getOccupied()) {
                        totalPrice = spot.getPricePerHour() * timeInHours;
                        spotsCheck = true;
                        finalSpot = spot;
                    }
                } else if (spotType.equals(SpotType.FOUR_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < totalPrice && !spot.getOccupied()) {
                        totalPrice = spot.getPricePerHour() * timeInHours;
                        spotsCheck = true;
                        finalSpot = spot;
                    }
                } else if (spotType.equals(SpotType.TWO_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.TWO_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < totalPrice && !spot.getOccupied()) {
                        totalPrice = spot.getPricePerHour() * timeInHours;
                        spotsCheck = true;
                        finalSpot = spot;
                    }
                }
            }

            if (!spotsCheck || finalSpot==null) {
                throw new Exception("Cannot make reservation");
            }

            finalSpot.setOccupied(Boolean.TRUE);

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(finalSpot);
            reservation.setUser(user);

            //updating spot
            finalSpot.getReservationList().add(reservation);
            //updating user
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(finalSpot);

            return reservation;
        }
        catch (Exception e){
            return null;
        }
    }


}
