package edu.sabanciuniv.hotelbookingapp.controller;

import edu.sabanciuniv.hotelbookingapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping(value = "/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/delete/{id}")
    public String deleteCart(@PathVariable Long id) {
        cartService.deleteCartById(id);
        return "redirect:/customer/carts";
    }
}
