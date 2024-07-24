package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Role role;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private Admin admin;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private Customer customer;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private HotelManager hotelManager;

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", createdDate=" + createdDate +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(id, users.id) && Objects.equals(username, users.username) && Objects.equals(createdDate, users.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, createdDate);
    }
}
