package com.qbin.blaze.controllers;

import com.qbin.blaze.models.Order;
import com.qbin.blaze.models.Product;
import com.qbin.blaze.repository.IProductDao;
import com.qbin.blaze.service.IOrderService;
import com.qbin.blaze.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    @GetMapping("/get-all")
    public List<Order> findAll() {
        return orderService.getAllOrders();
    }

    @PostMapping("/create")
    public ResponseEntity<?> create( @RequestBody Order order ) {

        Map<String, Object> responses = new HashMap<>();

        Order newOrder = null;

        // we validate that you have products
        if ( order.getProducts().isEmpty() ) {

            responses.put("mesage", "Error your order has no products.");

            return new ResponseEntity<Map<String, Object>>( responses, HttpStatus.NOT_FOUND );

        }

        // calculate subtotal
        float subTotal = 0.0f;

        for ( Product product: order.getProducts() ) {

            Product productFound = productService.findProductById(product.get_id());

            subTotal = subTotal + productFound.getPrice();

        }

        order.setSubTotal(subTotal);

        // calculate all taxes
        float totalCityTax    = (float) ( subTotal * 0.10 );

        subTotal = subTotal + totalCityTax;

        float totalCountryTax = (float) ( subTotal * 0.05 );

        subTotal = subTotal + totalCountryTax;

        float totalStateTax   = (float) ( subTotal * 0.08 );

        subTotal = subTotal + totalStateTax;

        float totalFederalTax = (float) ( subTotal * 0.02 );

        // set data an order
        order.setNumOrder("ORD-"+(int) (Math.random()*10+1));

        order.setStatus("Pending");

        order.setDate(new Date());

        order.setTotalCityTax(totalCityTax);
        order.setTotalCountyTax(totalCountryTax);
        order.setTotalStateTax(totalStateTax);
        order.setTotalFederalTax(totalFederalTax);

        order.setTotalTaxes( totalCityTax + totalCountryTax + totalStateTax + totalFederalTax );

        order.setTotalAmount( order.getSubTotal() + order.getTotalTaxes() );

        try {

            newOrder = orderService.createOrder(order);

        } catch ( DataAccessException exception ) {

            responses.put("mesage", "Error when trying to insert a order.");
            responses.put("error", exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responses.put("mesage", "The order has been correctly registered.");
        responses.put("order", newOrder);


        return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {

        Map<String, Object> responses = new HashMap<>();

        try {

            orderService.deleteOrder(id);

        } catch ( DataAccessException exception ) {

            responses.put("mesage", "Error deleting order from database.");
            responses.put("error", exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responses.put("mesage", "Order successfully removed from the database.");

        return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.OK);

    }

}
