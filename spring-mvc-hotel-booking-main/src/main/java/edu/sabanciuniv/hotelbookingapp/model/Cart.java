package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(unique = true, nullable = false)
//    private String confirmationNumber;

    @CreationTimestamp
    private LocalDateTime addToCartDate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Hotel hotel;

    @Column(nullable = false)
    private LocalDate checkinDate;

    @Column(nullable = false)
    private LocalDate checkoutDate;

    @OneToOne(mappedBy = "cart")
    private Booking booking;

    @Column
    private boolean delFlag;

//    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private List<BookedRoom> bookedRooms = new ArrayList<>();

//    @OneToOne(mappedBy = "booking")
//    private Payment payment;

//    @PrePersist
//    protected void onCreate() {
//        this.confirmationNumber = UUID.randomUUID().toString().substring(0, 8);
//    }

//    @Override
//    public String toString() {
//        return "Booking{" +
//                "id=" + id +
//                ", confirmationNumber='" + confirmationNumber + '\'' +
//                ", bookingDate=" + bookingDate +
//                ", customer=" + customer +
//                ", hotel=" + hotel +
//                ", checkinDate=" + checkinDate +
//                ", checkoutDate=" + checkoutDate +
//                ", bookedRooms=" + bookedRooms +
//                ", payment=" + payment +
//                '}';
//    }


    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", addToCartDate=" + addToCartDate +
                ", customer=" + customer +
                ", hotel=" + hotel +
                ", checkinDate=" + checkinDate +
                ", checkoutDate=" + checkoutDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(id, cart.id) && Objects.equals(addToCartDate, cart.addToCartDate) && Objects.equals(customer, cart.customer) && Objects.equals(hotel, cart.hotel) && Objects.equals(checkinDate, cart.checkinDate) && Objects.equals(checkoutDate, cart.checkoutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, addToCartDate, customer, hotel, checkinDate, checkoutDate);
    }
}
