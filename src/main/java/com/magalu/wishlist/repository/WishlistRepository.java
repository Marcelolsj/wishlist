package com.magalu.wishlist.repository;

import com.magalu.wishlist.model.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WishlistRepository extends MongoRepository<Wishlist, String> {

    Optional<Wishlist> findByCustomerEmail(String email);

}
