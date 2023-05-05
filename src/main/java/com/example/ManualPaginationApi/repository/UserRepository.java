package com.example.ManualPaginationApi.repository;

import com.example.ManualPaginationApi.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Users, Integer> {
    public Users findByEmail(String email);

    @Query("SELECT u FROM Users u WHERE (:email is null or u.email = :email) and (:phone is null or u.phone = :phone) and (:fullname is null or u.fullname = :fullname)")
    public List<Users> findByEmailAndPhoneAndFullname(@Param("email") String email, @Param("phone") String phone, @Param("fullname") String fullname);

}
