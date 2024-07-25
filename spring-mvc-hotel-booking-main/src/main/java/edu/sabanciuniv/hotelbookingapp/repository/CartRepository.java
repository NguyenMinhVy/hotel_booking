package edu.sabanciuniv.hotelbookingapp.repository;

import edu.sabanciuniv.hotelbookingapp.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c from Cart c where c.customer.id = :customerId and c.delFlag = false")
    List<Cart> findCartsByCustomerId(Long customerId);

    void deleteCartByBookingId(Long booking);

    @Modifying
    @Transactional
    @Query("UPDATE Cart c SET c.delFlag = true WHERE c.id = :cartId")
    void disableCart(Long cartId);
}
