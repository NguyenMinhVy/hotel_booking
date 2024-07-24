package edu.sabanciuniv.hotelbookingapp.service.impl;

import edu.sabanciuniv.hotelbookingapp.model.Booking;
import edu.sabanciuniv.hotelbookingapp.model.Cart;
import edu.sabanciuniv.hotelbookingapp.model.Users;
import edu.sabanciuniv.hotelbookingapp.model.dto.AddressDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.BookingDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.CartDTO;
import edu.sabanciuniv.hotelbookingapp.model.dto.RoomSelectionDTO;
import edu.sabanciuniv.hotelbookingapp.repository.CartRepository;
import edu.sabanciuniv.hotelbookingapp.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;


    @Override
    public List<CartDTO> findCartsByCustomerId(Long customerId) {
        List<Cart> carts = cartRepository.findCartsByCustomerId(customerId);
        return carts.stream()
                .map(this::mapCartModelToCartDto)
                .sorted(Comparator.comparing(CartDTO::getCheckinDate))
                .collect(Collectors.toList());
    }

    @Override
    public CartDTO mapCartModelToCartDto(Cart cart) {
        AddressDTO addressDto = AddressDTO.builder()
                .addressLine(cart.getHotel().getAddress().getAddressLine())
                .city(cart.getHotel().getAddress().getCity())
                .country(cart.getHotel().getAddress().getCountry())
                .build();

//        List<RoomSelectionDTO> roomSelections = cart.getBookedRooms().stream()
//                .map(room -> RoomSelectionDTO.builder()
//                        .roomType(room.getRoomType())
//                        .count(room.getCount())
//                        .build())
//                .collect(Collectors.toList());

        Users customerUsers = cart.getCustomer().getUsers();

        return CartDTO.builder()
                .id(cart.getId())
//                .confirmationNumber(cart.getConfirmationNumber())
                .addToCartDate(cart.getAddToCartDate())
                .customerId(cart.getCustomer().getId())
                .hotelId(cart.getHotel().getId())
                .checkinDate(cart.getCheckinDate())
                .checkoutDate(cart.getCheckoutDate())
//                .roomSelections(roomSelections)
//                .totalPrice(cart.getPayment().getTotalPrice())
                .hotelName(cart.getHotel().getName())
                .hotelAddress(addressDto)
                .customerName(customerUsers.getName() + " " + customerUsers.getLastName())
                .customerEmail(customerUsers.getUsername())
//                .paymentStatus(cart.getPayment().getPaymentStatus())
//                .paymentMethod(cart.getPayment().getPaymentMethod())
                .build();
    }

    @Override
    public void deleteCartById(Long id) {
        cartRepository.deleteById(id);
    }
}
