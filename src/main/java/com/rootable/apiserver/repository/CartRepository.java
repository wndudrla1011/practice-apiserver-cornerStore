package com.rootable.apiserver.repository;

import com.rootable.apiserver.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.owner.email = :email")
    public Optional<Cart> getCartOfMember(@Param("email") String email);

}
