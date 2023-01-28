package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.PaymentRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot(name,address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        Spot newSpot = new Spot();
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        newSpot.setParkingLot(parkingLot);
        newSpot.setOccupied(Boolean.FALSE);
        newSpot.setPricePerHour(pricePerHour);
        if(numberOfWheels<=2){
            newSpot.setSpotType(SpotType.TWO_WHEELER);
        }else if(numberOfWheels<=4){
            newSpot.setSpotType(SpotType.FOUR_WHEELER);
        }else{
            newSpot.setSpotType(SpotType.OTHERS);
        }

        if(parkingLot.getSpotList()==null){
            List<Spot> spotList = new ArrayList<>();
            spotList.add(newSpot);
            parkingLot.setSpotList(spotList);
        }else{
            parkingLot.getSpotList().add(newSpot);
        }

        parkingLotRepository1.save(parkingLot);//saving parent should save spot
       // spotRepository1.save(newSpot);

        return newSpot;

    }

    @Override
    public void deleteSpot(int spotId) {
        Spot spot = spotRepository1.findById(spotId).get();
        spotRepository1.delete(spot);
    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = spotRepository1.findById(spotId).get();
        spot.setPricePerHour(pricePerHour);
        spotRepository1.save(spot);

        return spot;
    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        parkingLotRepository1.delete(parkingLot);
    }
}
