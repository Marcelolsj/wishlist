package com.magalu.wishlist.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Customer {

    private String email;

    private String name;

}
