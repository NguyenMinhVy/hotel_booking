package edu.sabanciuniv.hotelbookingapp.repository;

import edu.sabanciuniv.hotelbookingapp.model.HotelManager;
import edu.sabanciuniv.hotelbookingapp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelManagerRepository extends JpaRepository<HotelManager, Long> {

    Optional<HotelManager> findByUsers(Users users);
}
