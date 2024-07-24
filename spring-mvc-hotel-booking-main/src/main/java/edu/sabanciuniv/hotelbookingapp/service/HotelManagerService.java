package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.HotelManager;
import edu.sabanciuniv.hotelbookingapp.model.Users;

public interface HotelManagerService {

    HotelManager findByUser(Users users);

}
