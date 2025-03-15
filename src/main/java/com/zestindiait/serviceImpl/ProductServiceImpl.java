package com.zestindiait.serviceImpl;

import com.zestindiait.customeExceptions.InvalidProductException;
import com.zestindiait.customeExceptions.ProductNotFoundException;
import com.zestindiait.entity.Product;
import com.zestindiait.repository.ProductRepository;
import com.zestindiait.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        if (product == null) {
            throw new InvalidProductException("Product cannot be null");
        }
        return productRepository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        if (product == null) {
            throw new InvalidProductException("Product cannot be null");
        }
        Product existingProduct = getProductById(id);
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }
}
