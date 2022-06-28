package com.micropos.batch.config;

import com.micropos.batch.model.Product;
import com.micropos.batch.repository.ProductRepository;
import com.micropos.batch.service.JsonFileReader;
import com.micropos.batch.service.ProductProcessor;
import com.micropos.batch.service.ProductWriter;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
@EnableBatchProcessing
public class PartitionConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public ProductRepository productRepository;

    @Bean
    public Job partitioningJob() throws Exception {
        return jobBuilderFactory
            .get("partitioningJob")
            .incrementer(new RunIdIncrementer())
            .flow(masterStep())
            .end()
            .build();
    }

    @Bean
    public Step masterStep() throws Exception {
        return stepBuilderFactory
            .get("masterStep")
            .partitioner(slaveStep())
            .partitioner("partition", partitioner())
            .gridSize(10)
            .taskExecutor(new SimpleAsyncTaskExecutor())
            .build();
    }

    @Bean
    public Partitioner partitioner() throws Exception {
        var partitioner = new MultiResourcePartitioner();
        var resolver = new PathMatchingResourcePatternResolver();
        partitioner.setResources(resolver.getResources("classpath:data/meta_*.json"));
        return partitioner;
    }

    @Bean
    public Step slaveStep() {
        return stepBuilderFactory
            .get("slaveStep")
            .<JsonNode, Product>chunk(1)
            .reader(itemReader(null))
            .processor(itemProcessor())
            .writer(itemWriter())
            .build();
    }

    @Bean
    @StepScope
    public ItemReader<JsonNode> itemReader(@Value("#{stepExecutionContext['fileName']}") String file) {
        return new JsonFileReader(file);
    }

    @Bean
    public ItemProcessor<JsonNode, Product> itemProcessor() {
        return new ProductProcessor();
    }

    @Bean
    public ItemWriter<Product> itemWriter() {
        var writer = new ProductWriter();
        writer.setProductRepository(productRepository);
        return writer;
    }
}
