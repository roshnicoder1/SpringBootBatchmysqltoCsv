package com.bsli.processor;

import org.springframework.batch.item.ItemProcessor;

import com.bsli.model.Product;

public class ProductItemProcessor implements ItemProcessor<Product,Product>{

	@Override
	public Product process(Product product) throws Exception {
		// TODO Auto-generated method stub
		return product;
	}

	

}
