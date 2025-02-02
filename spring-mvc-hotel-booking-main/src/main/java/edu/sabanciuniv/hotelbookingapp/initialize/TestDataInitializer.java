package edu.sabanciuniv.hotelbookingapp.initialize;

import edu.sabanciuniv.hotelbookingapp.model.*;
import edu.sabanciuniv.hotelbookingapp.model.enums.RoleType;
import edu.sabanciuniv.hotelbookingapp.model.enums.RoomType;
import edu.sabanciuniv.hotelbookingapp.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final HotelManagerRepository hotelManagerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final HotelRepository hotelRepository;
    private final AvailabilityRepository availabilityRepository;

    @Override
    @Transactional
    public void run(String... args) {

        try {
            log.warn("Checking if test data persistence is required...");

            if (roleRepository.count() == 0 && userRepository.count() == 0) {
                log.info("Initiating test data persistence");

                Role adminRole = new Role(RoleType.ADMIN);
                Role customerRole = new Role(RoleType.CUSTOMER);
                Role hotelManagerRole = new Role(RoleType.HOTEL_MANAGER);

                roleRepository.save(adminRole);
                roleRepository.save(customerRole);
                roleRepository.save(hotelManagerRole);
                log.info("Role data persisted");

                Users users1 = Users.builder().username("admin@hotel.com").password(passwordEncoder.encode("123456")).name("Admin").lastName("Admin").role(adminRole).build();
                Users users2 = Users.builder().username("customer1@hotel.com").password(passwordEncoder.encode("123456")).name("customer").lastName("customer").role(customerRole).build();
                Users users3 = Users.builder().username("manager1@hotel.com").password(passwordEncoder.encode("123456")).name("hotel manager1").lastName("hotel manager1").role(hotelManagerRole).build();
                Users users4 = Users.builder().username("manager2@hotel.com").password(passwordEncoder.encode("123456")).name("hotel manager2").lastName("hotel manager2").role(hotelManagerRole).build();

                userRepository.save(users1);
                userRepository.save(users2);
                userRepository.save(users3);
                userRepository.save(users4);

                Admin admin1 = Admin.builder().users(users1).build();
                Customer c1 = Customer.builder().users(users2).build();
                HotelManager hm1 = HotelManager.builder().users(users3).build();
                HotelManager hm2 = HotelManager.builder().users(users4).build();

                adminRepository.save(admin1);
                customerRepository.save(c1);
                hotelManagerRepository.save(hm1);
                hotelManagerRepository.save(hm2);
                log.info("User data persisted");

                Address addressIst1 = Address.builder().addressLine("le duc tho").city("Ho Chi Minh")
                        .country("Viet Nam").build();
                Address addressIst2 = Address.builder().addressLine("le duan").city("Vung Tau")
                        .country("Viet Nam").build();
                Address addressIst3 = Address.builder().addressLine("nam ky khoi nghia").city("Da Nang")
                        .country("Viet Nam").build();

                Address addressBerlin1 = Address.builder().addressLine("nam ky khoi nghia").city("Quang Nam")
                        .country("Viet Nam").build();
                Address addressBerlin2 = Address.builder().addressLine("le duan").city("Quang Ngai")
                        .country("Viet Nam").build();
                Address addressBerlin3 = Address.builder().addressLine("ton duc thang").city("Da Lat")
                        .country("Viet Nam").build();

                addressRepository.save(addressIst1);
                addressRepository.save(addressIst2);
                addressRepository.save(addressIst3);
                addressRepository.save(addressBerlin1);
                addressRepository.save(addressBerlin2);
                addressRepository.save(addressBerlin3);

                Hotel hotelIst1 = Hotel.builder().name("Swissotel The Bosphorus Istanbul")
                        .address(addressIst1).hotelManager(hm1).build();
                Hotel hotelIst2 = Hotel.builder().name("Four Seasons Hotel Istanbul")
                        .address(addressIst2).hotelManager(hm1).build();
                Hotel hotelIst3 = Hotel.builder().name("Ciragan Palace Kempinski Istanbul")
                        .address(addressIst3).hotelManager(hm1).build();

                Hotel hotelBerlin1 = Hotel.builder().name("Hotel Adlon Kempinski Berlin")
                        .address(addressBerlin1).hotelManager(hm2).build();
                Hotel hotelBerlin2 = Hotel.builder().name("The Ritz-Carlton Berlin")
                        .address(addressBerlin2).hotelManager(hm2).build();
                Hotel hotelBerlin3 = Hotel.builder().name("InterContinental Berlin")
                        .address(addressBerlin3).hotelManager(hm2).build();

                Room singleRoomIst1 = Room.builder().roomType(RoomType.SINGLE)
                        .pricePerNight(370).roomCount(35).hotel(hotelIst1).build();
                Room doubleRoomIst1 = Room.builder().roomType(RoomType.DOUBLE)
                        .pricePerNight(459).roomCount(45).hotel(hotelIst1).build();

                Room singleRoomIst2 = Room.builder().roomType(RoomType.SINGLE)
                        .pricePerNight(700).roomCount(25).hotel(hotelIst2).build();
                Room doubleRoomIst2 = Room.builder().roomType(RoomType.DOUBLE)
                        .pricePerNight(890).roomCount(30).hotel(hotelIst2).build();

                Room singleRoomIst3 = Room.builder().roomType(RoomType.SINGLE)
                        .pricePerNight(691).roomCount(30).hotel(hotelIst3).build();
                Room doubleRoomIst3 = Room.builder().roomType(RoomType.DOUBLE)
                        .pricePerNight(800).roomCount(75).hotel(hotelIst3).build();

                Room singleRoomBerlin1 = Room.builder().roomType(RoomType.SINGLE)
                        .pricePerNight(120.0).roomCount(25).hotel(hotelBerlin1).build();
                Room doubleRoomBerlin1 = Room.builder().roomType(RoomType.DOUBLE)
                        .pricePerNight(250.0).roomCount(15).hotel(hotelBerlin1).build();

                Room singleRoomBerlin2 = Room.builder().roomType(RoomType.SINGLE)
                        .pricePerNight(300).roomCount(50).hotel(hotelBerlin2).build();
                Room doubleRoomBerlin2 = Room.builder().roomType(RoomType.DOUBLE)
                        .pricePerNight(400).roomCount(50).hotel(hotelBerlin2).build();

                Room singleRoomBerlin3 = Room.builder().roomType(RoomType.SINGLE)
                        .pricePerNight(179).roomCount(45).hotel(hotelBerlin3).build();
                Room doubleRoomBerlin3 = Room.builder().roomType(RoomType.DOUBLE)
                        .pricePerNight(256).roomCount(25).hotel(hotelBerlin3).build();

                hotelIst1.getRooms().addAll(Arrays.asList(singleRoomIst1,doubleRoomIst1));
                hotelIst2.getRooms().addAll(Arrays.asList(singleRoomIst2,doubleRoomIst2));
                hotelIst3.getRooms().addAll(Arrays.asList(singleRoomIst3,doubleRoomIst3));
                hotelBerlin1.getRooms().addAll(Arrays.asList(singleRoomBerlin1,doubleRoomBerlin1));
                hotelBerlin2.getRooms().addAll(Arrays.asList(singleRoomBerlin2,doubleRoomBerlin2));
                hotelBerlin3.getRooms().addAll(Arrays.asList(singleRoomBerlin3,doubleRoomBerlin3));

                hotelRepository.save(hotelIst1);
                hotelRepository.save(hotelIst2);
                hotelRepository.save(hotelIst3);
                hotelRepository.save(hotelBerlin1);
                hotelRepository.save(hotelBerlin2);
                hotelRepository.save(hotelBerlin3);
                log.info("Hotel data persisted");

                Availability av1Berlin1 = Availability.builder().hotel(hotelBerlin1)
                        .date(LocalDate.of(2023,9,1)).room(singleRoomBerlin1).availableRooms(5).build();
                Availability av2Berlin1 = Availability.builder().hotel(hotelBerlin1)
                        .date(LocalDate.of(2023,9,2)).room(doubleRoomBerlin1).availableRooms(7).build();

                availabilityRepository.save(av1Berlin1);
                availabilityRepository.save(av2Berlin1);
                log.info("Availability data persisted");

            } else {
                log.info("Test data persistence is not required");
            }
            log.warn("App ready");
        } catch (DataAccessException e) {
            log.error("Exception occurred during data persistence: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected exception occurred: " + e.getMessage());
        }
    }
}
