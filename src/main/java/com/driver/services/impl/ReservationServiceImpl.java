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
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            if(parkingLot==null){
                throw new Exception("Cannot make reservation");
            }
            User user  = userRepository3.findById(userId).get();
            if(user==null){
                throw new Exception("Cannot make reservation");
            }
            List<Spot> spotList = parkingLot.getSpotList();
            Spot finalSpot = null;
            int totalPrice = Integer.MAX_VALUE;
            boolean success = false;
            for(Spot spot: spotList){
                if(spot.getOccupied()==false){
                    int price = timeInHours * spot.getPricePerHour();
                    if(finalSpot==null || totalPrice > price){
                        totalPrice = price;
                        finalSpot = spot;
                    }
                }
            }

            if(finalSpot==null){
                throw new Exception("Cannot make reservation");
            }
            reservation = new Reservation();
            if( (finalSpot.getSpotType()==SpotType.TWO_WHEELER && numberOfWheels<=2) || (finalSpot.getSpotType()==SpotType.FOUR_WHEELER && numberOfWheels<=4)
                    || (finalSpot.getSpotType()==SpotType.OTHERS && numberOfWheels>4)){
                finalSpot.setOccupied(Boolean.TRUE);
                reservation.setNumberOfHours(timeInHours);
                reservation.setSpot(finalSpot);
                reservation.setUser(user);


                //updating list of spot
                List<Reservation> reservationList = finalSpot.getReservationList();
                if(reservationList!=null){
                    reservationList.add(reservation);
                }else{
                    reservationList = new ArrayList<>();
                    reservationList.add(reservation);
                }

                //updating list of user
                List<Reservation> reservationList1 = user.getReservationList();
                if(reservationList1!=null){
                    reservationList1.add(reservation);
                }else{
                    reservationList1=new ArrayList<>();
                    reservationList1.add(reservation);
                }

                userRepository3.save(user);
                spotRepository3.save(finalSpot);

                success=true;
            }

            if(success==false){
                throw new Exception("Cannot make reservation");
            }

        }catch(Exception e){
            System.out.println(e);
        }

        return reservation;
    }


}
