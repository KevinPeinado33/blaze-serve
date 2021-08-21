package com.qbin.blaze.controllers;

import com.qbin.blaze.models.Product;
import com.qbin.blaze.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin({"*"})
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/get-all")
    public List<Product> findAll() {
        return productService.getAllProducts();
    }

    @PostMapping("/create")
    public ResponseEntity<?> create( @RequestBody Product product ) {

        Map<String, Object> responses = new HashMap<>();

        Product newProduct = null;

        try {

            newProduct = productService.createProduct(product);

        } catch ( DataAccessException exception ) {

            responses.put("mesage", "Error when trying to insert a product.");
            responses.put("error", exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responses.put("mesage", "The product has been correctly registered.");
        responses.put("product", newProduct);

        return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.CREATED);

    }

    @GetMapping("/find/{id}")
    public ResponseEntity<?> findById(@PathVariable String id) {

        Map<String, Object> responses = new HashMap<>();

        Product product = null;

        try {

            product = productService.findProductById(id);

        } catch ( DataAccessException exception ) {

            responses.put("mesage", "Query error");
            responses.put("error", exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        if ( product == null ) {

            responses.put("mesage", "The product with ID: " + id + " does not exist in the database.");
            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<Product>(product, HttpStatus.OK);

    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Product product) {

        Map<String, Object> responses = new HashMap<>();

        Product productCurrent = productService.findProductById(product.get_id());

        Product productUpdated = null;

        if ( productCurrent == null ) {

            responses.put("mesage", "Cannot edit product with ID: " + product.get_id() + ", does not exist in database.");

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.NOT_FOUND);

        }

        try {

            productCurrent.setName(product.getName());
            productCurrent.setCategory(product.getCategory());
            productCurrent.setPrice(product.getPrice());
            productCurrent.setState(product.getState());

            productUpdated = productService.updateProduct( productCurrent );

        } catch ( DataAccessException exception ) {

            responses.put("mesage", "Error while trying to update the product");
            responses.put("error", exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responses.put("mesage", "The product has been successfully updated.");
        responses.put("product", productUpdated);

        return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){

        Map<String, Object> responses = new HashMap<>();

        try {

            productService.deleteProduct(id);

        } catch ( DataAccessException exception ) {

            responses.put("mesage", "Error deleting product from database.");
            responses.put("error", exception.getMessage() + ": " + exception.getMostSpecificCause().getMessage());

            return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        responses.put("mesage", "Product successfully removed from the database.");

        return new ResponseEntity<Map<String, Object>>(responses, HttpStatus.OK);

    }

    @GetMapping("/find-by-state/{state}")
    public List<Product> findAllByState(@PathVariable String state) {
        return productService.getAllProductsByState(state);
    }

}
