package com.micropos.batch.config;

import com.micropos.batch.model.Product;
import com.micropos.batch.service.JsonFileReader;
import com.micropos.batch.service.ProductProcessor;
import com.micropos.batch.service.ProductWriter;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;


//@Configuration
//@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    public ItemReader<JsonNode> itemReader() throws FileNotFoundException {
        var file = ResourceUtils.getFile("classpath:data/meta_Magazine_Subscriptions_100.json");
        return new JsonFileReader(file.getPath());
    }

    @Bean
    public ItemProcessor<JsonNode, Product> itemProcessor() {
        return new ProductProcessor();
    }

    @Bean
    public ItemWriter<Product> itemWriter() {
        return new ProductWriter();
    }

    @Bean
    protected Step processProducts(ItemReader<JsonNode> reader, ItemProcessor<JsonNode, Product> processor, ItemWriter<Product> writer) {
        return stepBuilderFactory
            .get("processProducts")
            .<JsonNode, Product>chunk(20)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .taskExecutor(taskExecutor())
            .build();
    }

    @Bean
    public Job chunksJob() throws FileNotFoundException {
        return jobBuilderFactory
            .get("chunksJob")
            .start(processProducts(itemReader(), itemProcessor(), itemWriter()))
            .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        var executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(20);
        return executor;
    }
}
