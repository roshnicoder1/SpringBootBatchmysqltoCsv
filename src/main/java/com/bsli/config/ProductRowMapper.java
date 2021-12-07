package com.bsli.config;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.bsli.model.Product;

public class ProductRowMapper implements RowMapper<Product> {

	@Override
	public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Product product= new Product();
		product.setProductID(rs.getInt("prod_id"));
		product.setProdName(rs.getString("prod_name"));
		product.setProductDesc(rs.getString("prod_desc"));
		product.setPrice(rs.getBigDecimal("unit"));
		product.setUnit(rs.getInt("price"));
		//select prod_id,prod_name,prod_desc,unit,price from products;
		return product;
	}

}
