package com.my.app.config;

import com.epam.app.exception.ObjectNotFoundException;
import com.epam.app.model.Category;
import com.my.app.model.entity.Product;
import com.my.app.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.math.BigDecimal;
import java.time.Instant;

@Slf4j
@AllArgsConstructor
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private ProductRepository productRepository;
    private PropertiesConfig propertiesConfig;

    @Bean
    public Job loadProductsJob(JobBuilderFactory jobBuilderFactory,
                               Step setupStep,
                               Step loadProductsStep) {
        return jobBuilderFactory.get("load-products-job")
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        log.info("Starting to load data");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        final Iterable<Product> products = productRepository.findAll();

                        log.info("Data loading is finished");
                        log.info("There were load {} products", productRepository.count());

                        products.forEach(product -> log.info(product.toString()));
                    }
                })
                .start(setupStep)
                .next(loadProductsStep)
                .build();
    }

    @Bean
    public Step setupStep(Tasklet dropIndexTasklet,
                          StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("drop-products-index")
                .tasklet(dropIndexTasklet)
                .build();
    }

    @Bean
    public Tasklet dropIndexTasklet() {
        return (stepContribution, chunkContext) -> {
            log.info("Drop products index in the ElasticSearch");
            productRepository.deleteAll();
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step loadProductsStep(StepBuilderFactory stepBuilderFactory,
                                 FlatFileItemReader<ProductDetail> reader,
                                 ItemProcessor<ProductDetail, Product> processor,
                                 ItemWriter<Product> elasticSearchWriter) {
        return stepBuilderFactory.get("load-csv-file")
                .<ProductDetail, Product>chunk(10)
                .reader(reader)
                .processor(processor)
                .faultTolerant()
                .skip(ObjectNotFoundException.class)
                .skipLimit(3)
                .writer(elasticSearchWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<ProductDetail> csvReader() {
        return new FlatFileItemReaderBuilder<ProductDetail>().name("csv-reader")
                .resource(new ClassPathResource(propertiesConfig.getProductsDataPath()))
                .targetType(ProductDetail.class)
                .delimited()
                .names("id", "name", "description", "cost", "category", "count", "storage", "ram", "processor", "hasBluetooth")
                .build();
    }

    @Bean
    public ItemProcessor<ProductDetail, Product> processor() {
        return item -> {
            final Category category = Category.of(item.getCategory());
            if (category == null)
                throw new ObjectNotFoundException(String.format("Not found Category with value %s", item.getCategory()));

            return Product.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .cost(item.getCost())
                    .category(category)
                    .count(item.getCount())
                    .storage(item.getStorage())
                    .ram(item.getRam())
                    .processor(item.getProcessor())
                    .hasBluetooth(item.getHasBluetooth())
                    .created(Instant.now())
                    .updated(Instant.now())
                    .build();
        };
    }

    @Bean
    public ItemWriter<Product> elasticSearchWriter() {
        return items -> {
            productRepository.saveAll(items);
        };
    }

    @Data
    public static class ProductDetail {
        private String id;
        private String name;
        private String description;
        private BigDecimal cost;
        private String category;
        private Integer count;

        private String storage;
        private String ram;
        private String processor;
        private String hasBluetooth;
    }
}
