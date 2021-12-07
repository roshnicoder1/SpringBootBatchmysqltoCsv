package com.bsli.config;

import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.bsli.model.Product;
import com.bsli.processor.ProductItemProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource datasource;
	
	@Bean
	public JdbcCursorItemReader<Product> reader() {
		JdbcCursorItemReader<Product> cursorItemReader=new JdbcCursorItemReader<>();
		cursorItemReader.setDataSource(datasource);
		cursorItemReader.setSql("select prod_id,prod_name,prod_desc,unit,price from products;");
		cursorItemReader.setRowMapper(new ProductRowMapper());
		
		return cursorItemReader;
	}
	
	@Bean
	public ProductItemProcessor processor(){
		return new ProductItemProcessor();
	}
	
	@Bean
	public FlatFileItemWriter<Product> writer(){
		FlatFileItemWriter<Product> writer = new FlatFileItemWriter<Product>();
		writer.setResource(new ClassPathResource("product.csv"));
		
		DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<Product>();
		lineAggregator.setDelimiter(",");
		
		BeanWrapperFieldExtractor<Product>  fieldExtractor = new BeanWrapperFieldExtractor<Product>();
		fieldExtractor.setNames(new String[]{"productID","prodName","price","unit","ProductDesc"});
		lineAggregator.setFieldExtractor(fieldExtractor);
	

		writer.setLineAggregator(lineAggregator);
		return writer;
	}
	
	@Bean
	public Step step1(){
		return stepBuilderFactory.get("step1").<Product,Product>chunk(100).reader(reader()).processor(processor()).writer(writer()).build();
	}

	@Bean
	public Job exportProductJob(){
		return jobBuilderFactory.get("exportProductJob").incrementer(new RunIdIncrementer()).flow(step1()).end().build();
	}

}
