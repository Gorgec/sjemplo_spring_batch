package com.example.springbatchs.config;

import com.example.springbatchs.model.Product;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    @Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;
	
	@Autowired
	private ItemProcessor<Product,Product> magicProcessor;

	@Bean
	public Job helloWoldJob(){
		return jobs.get("helloWold")
                .start(step1())
                .build();
	}

	@Bean
	public Step step1() {
		return steps.get("step1")
				.<Product,Product>chunk(1)
				.reader(flatFileItemReader(null))
				.processor(magicProcessor)
				.writer(flatFileItemWriter())
				.faultTolerant()
				.skipPolicy(new AlwaysSkipItemSkipPolicy())
				.build();
	}

	@StepScope
	@Bean
    public FlatFileItemReader<Product> flatFileItemReader(@Value("#{jobParameters['fileInput']}") FileSystemResource inputFile){

		return new FlatFileItemReaderBuilder<Product>()
					.name("reader")
					.resource(inputFile)
					.delimited()
					.names(new String[]{"productId","productName","productDesc","price","unit"})
					.fieldSetMapper(new BeanWrapperFieldSetMapper<>(){{
						setTargetType(Product.class);
					}
					})
					.linesToSkip(1)
					.build();
	}

	@Bean
	public FlatFileItemWriter flatFileItemWriter(){
		return new FlatFileItemWriterBuilder<Product>()
		.name("writer")
		.resource(new FileSystemResource("outputs/products.cvs"))
		.delimited()
		.names(new String[] {"productId","productName","productDesc","price","unit"})
		.build();
	}
}
