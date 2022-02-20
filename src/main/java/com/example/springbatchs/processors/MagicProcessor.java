package com.example.springbatchs.processors;

import com.example.springbatchs.model.Product;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MagicProcessor implements ItemProcessor<Product, Product>{

    @Override
        public Product process(Product item) throws Exception {
        Product product = new Product();
        product.setProductId(item.getProductId());
        product.setProductName(item.getProductName().toUpperCase());
        product.setProductDesc(item.getProductDesc().toUpperCase());
        product.setPrice(item.getPrice());
        product.setUnit(item.getUnit());
        return product;
    }
    
}
