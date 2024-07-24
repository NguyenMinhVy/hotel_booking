package edu.sabanciuniv.hotelbookingapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Users users;

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", user=" + users +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return Objects.equals(id, admin.id) && Objects.equals(users, admin.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, users);
    }
}
