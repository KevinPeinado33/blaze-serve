package com.qbin.blaze.controllers;

import com.qbin.blaze.dto.OrderDto;
import com.qbin.blaze.dto.ProductDto;
import com.qbin.blaze.models.Order;
import com.qbin.blaze.models.OrderDetail;
import com.qbin.blaze.models.Product;
import com.qbin.blaze.service.IOrderService;
import com.qbin.blaze.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IProductService productService;

    private static final Integer INITIAL_CAPACITY = 1;

    @GetMapping("/get-all")
    public List<Order> findAll() {
        return orderService.getAllOrders();
    }

    @PostMapping("/create")
    public ResponseEntity<?> create( @RequestBody OrderDto order ) {

        // we create a list of details to store some data
        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>(INITIAL_CAPACITY);

        Map<String, Object> responses = new HashMap<>();

        Order orderCreate = new Order();

        Order newOrder = null;

        // we validate that you have products
        if ( order.getProducts().isEmpty() ) {

            responses.put("mesage", "Error your order has no products.");

            return new ResponseEntity<Map<String, Object>>( responses, HttpStatus.NOT_FOUND );

        }

        try {

            // calculate subtotal
            float subTotal = 0.0f;

            // we go through the product list
            for ( ProductDto product: order.getProducts() ) {

                OrderDetail orderDetail = new OrderDetail();

                // we search for products by id to fill in some missing data
                Product productFound = productService.findProductById(product.get_id());

                // we calculate the subtotal
                subTotal = subTotal + ( productFound.getPrice() * product.getQuantity() );

                // we fill in the necessary data for the detail object
                orderDetail.setQuantity(product.getQuantity());
                orderDetail.setProduct(productFound);
                orderDetail.setCost(productFound.getPrice() * product.getQuantity());

                // we create the order detail and set the object that is returned to us
                orderDetails.add(orderService.createOrderDetail(orderDetail));

            }

            // we fill the subtotal at this level since the value is modified below.
            orderCreate.setSubTotal(subTotal);

            // calculate all taxes
            float totalCityTax    = (float) ( subTotal * 0.10 );

            subTotal = subTotal + totalCityTax;

            float totalCountryTax = (float) ( subTotal * 0.05 );

            subTotal = subTotal + totalCountryTax;

            float totalStateTax   = (float) ( subTotal * 0.08 );

            subTotal = subTotal + totalStateTax;

            float totalFederalTax = (float) ( subTotal * 0.02 );

            // set data an order
            orderCreate.setConsumer(order.getConsumer());
            orderCreate.setNumOrder("ORD-"+(int) (Math.random() * 1000+1));
            orderCreate.setStatus("Pending");
            orderCreate.setDate(new Date());
            orderCreate.setTotalCityTax(totalCityTax);
            orderCreate.setTotalCountyTax(totalCountryTax);
            orderCreate.setTotalStateTax(totalStateTax);
            orderCreate.setTotalFederalTax(totalFederalTax);
            orderCreate.setOrderDetails(orderDetails);

            orderCreate.setTotalTaxes( totalCityTax + totalCountryTax + totalStateTax + totalFederalTax );

            orderCreate.setTotalAmount( orderCreate.getSubTotal() + orderCreate.getTotalTaxes() );

            // finally create order
            newOrder = orderService.createOrder(orderCreate);

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

        // we obtain all the details of this order
        Order order = orderService.getOrderById(id);

        try {

            // we remove all details of this order
            for ( OrderDetail orderDetail: order.getOrderDetails() ){

                // delete order detail by id
                orderService.deleteOrderDetail(orderDetail.get_id());

            }

            // we finally eliminated the order
            orderService.deleteOrder(id);

        } catch ( DataAccessException exception ) {

            responses.put("mesage", "Error deleting order from database.");
            responses.put("error", exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responses.put("mesage", "Order successfully removed from the database.");

        return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.OK);

    }

    /**
     * This method will have two important parts for the update, the first one when
     * it finds that the status has changed repeatedly it returns the updated object
     * and terminates the method; the other process is to update the whole order detail.
     *
     * @param order -> object that can have between the changed status or the detail of new or deleted products
     * @return -> new json with information
     */
    @PutMapping("/update-order")
    public ResponseEntity<?> update(@RequestBody OrderDto order) {

        List<OrderDetail> orderDetails = new ArrayList<OrderDetail>(INITIAL_CAPACITY);

        Map<String, Object> responses = new HashMap<>();

        // we obtain the old order to make the following status or detail validations
        Order orderFound = orderService.getOrderById(order.get_id());

        Order orderUpdated = null;

        // we validate that the order exists in the database
        if ( orderFound == null ){

            responses.put("mesage", "Cannot edit order with ID: " + order.get_id() + ", does not exist in database.");

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.NOT_FOUND);

        }

        try {

            // we validate that the new object has a different status from the one we have registered
            if ( order.getStatus() != null && !(order.getStatus().equals(orderFound.getStatus())) ) {

                orderFound.setStatus(order.getStatus());

                // update the order
                orderUpdated = orderService.updateOrder(orderFound);

                responses.put("mesage", "The order has been successfully updated.");
                responses.put("order", orderUpdated);

                // we send the object to be updated and return values
                return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.CREATED);

            }

            // we delete all the previous details to insert them again
            for ( OrderDetail orderDetail: orderFound.getOrderDetails() ){

                // delete order detail by id
                orderService.deleteOrderDetail(orderDetail.get_id());

            }

            // calculate subtotal
            float subTotal = 0.0f;

            // we go through the product list nuevos
            for ( ProductDto product: order.getProducts() ) {

                OrderDetail orderDetail = new OrderDetail();

                // we search for products by id to fill in some missing data
                Product productFound = productService.findProductById(product.get_id());

                // we calculate the subtotal
                subTotal = subTotal + ( productFound.getPrice() * product.getQuantity() );

                // we fill in the necessary data for the detail object
                orderDetail.setQuantity(product.getQuantity());
                orderDetail.setProduct(productFound);
                orderDetail.setCost(productFound.getPrice() * product.getQuantity());

                // we create the order detail and set the object that is returned to us
                orderDetails.add(orderService.createOrderDetail(orderDetail));

            }

            // we fill the subtotal at this level since the value is modified below.
            orderFound.setSubTotal(subTotal);

            // calculate all taxes
            float totalCityTax    = (float) ( subTotal * 0.10 );

            subTotal = subTotal + totalCityTax;

            float totalCountryTax = (float) ( subTotal * 0.05 );

            subTotal = subTotal + totalCountryTax;

            float totalStateTax   = (float) ( subTotal * 0.08 );

            subTotal = subTotal + totalStateTax;

            float totalFederalTax = (float) ( subTotal * 0.02 );

            // set data an order

            if (order.getConsumer() != null) {
                orderFound.setConsumer(order.getConsumer());
            }
            orderFound.setTotalCityTax(totalCityTax);
            orderFound.setTotalCountyTax(totalCountryTax);
            orderFound.setTotalStateTax(totalStateTax);
            orderFound.setTotalFederalTax(totalFederalTax);
            orderFound.setOrderDetails(orderDetails);

            orderFound.setTotalTaxes( totalCityTax + totalCountryTax + totalStateTax + totalFederalTax );

            orderFound.setTotalAmount( orderFound.getSubTotal() + orderFound.getTotalTaxes() );

            // finally update order
            orderUpdated = orderService.updateOrder(orderFound);

        } catch ( DataAccessException exception ) {

            responses.put("mesage", "Error while trying to update the order");
            responses.put("error", exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responses.put("mesage", "The order has been successfully updated.");
        responses.put("order", orderUpdated);


        return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.CREATED);

    }

}
