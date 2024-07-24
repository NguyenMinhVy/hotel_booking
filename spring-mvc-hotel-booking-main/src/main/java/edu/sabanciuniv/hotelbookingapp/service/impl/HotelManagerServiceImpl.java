package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.HotelManager;
import edu.sabanciuniv.hotelbookingapp.model.Users;
import edu.sabanciuniv.hotelbookingapp.repository.HotelManagerRepository;
import edu.sabanciuniv.hotelbookingapp.service.HotelManagerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelManagerServiceImpl implements HotelManagerService {

    private final HotelManagerRepository hotelManagerRepository;

    @Override
    public HotelManager findByUser(Users users) {
        log.info("Attempting to find HotelManager for user ID: {}", users.getId());
        return hotelManagerRepository.findByUsers(users)
                .orElseThrow(() -> new EntityNotFoundException("HotelManager not found for user " + users.getUsername()));
    }
}
