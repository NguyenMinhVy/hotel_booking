package edu.sabanciuniv.hotelbookingapp.service;

import edu.sabanciuniv.hotelbookingapp.model.Booking;
import edu.sabanciuniv.hotelbookingapp.model.Cart;
import edu.sabanciuniv.hotelbookingapp.model.dto.BookingDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.CartDTO;

import java.util.List;

public interface CartService {
    List<CartDTO> findCartsByCustomerId(Long customerId);

    CartDTO mapCartModelToCartDto(Cart cart);

    void deleteCartById(Long id);
}
