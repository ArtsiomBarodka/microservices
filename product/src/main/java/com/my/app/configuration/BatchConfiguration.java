package com.my.app.configuration;

import com.epam.app.model.Category;
import com.my.app.model.Product;
import com.my.app.model.ProductDetail;
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
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@Slf4j
@RefreshScope
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${app.products.data.path}")
    private String productsDataPath;

    @Value("${app.products.data.collection}")
    private String productsCollection;


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
                        final List<Product> products = mongoTemplate.findAll(Product.class, productsCollection);
                        log.info("Data loading is finished");
                        log.info("There were load {} products", products.size());
                        products.forEach(product -> log.info(product.toString()));
                    }
                })
                .start(setupStep)
                .next(loadProductsStep)
                .build();
    }

    @Bean
    public Step setupStep(Tasklet dropTableTasklet,
                          StepBuilderFactory stepBuilderFactory) {
        return stepBuilderFactory.get("drop-product-table")
                .tasklet(dropTableTasklet)
                .build();
    }

    @Bean
    public Tasklet dropTableTasklet() {
        return (stepContribution, chunkContext) -> {
            log.info("Drop product table in MongoDB");
            mongoTemplate.dropCollection(productsCollection);
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step loadProductsStep(StepBuilderFactory stepBuilderFactory,
                                 FlatFileItemReader<ProductDetail> reader,
                                 ItemProcessor<ProductDetail, Product> processor,
                                 MongoItemWriter<Product> writer) {
        return stepBuilderFactory.get("load-csv-file")
                .<ProductDetail, Product>chunk(10)
                .reader(reader)
                .processor(processor)
                .faultTolerant()
                .skip(IllegalArgumentException.class)
                .skipLimit(3)
                .writer(writer)
                .build();
    }

    @Bean
    public FlatFileItemReader<ProductDetail> csvReader() {
        return new FlatFileItemReaderBuilder<ProductDetail>().name("csv-reader")
                .resource(new ClassPathResource(productsDataPath))
                .targetType(ProductDetail.class)
                .delimited()
                .names("name", "description", "cost", "category", "storage", "ram", "processor", "hasBluetooth")
                .build();
    }

    @Bean
    public ItemProcessor<ProductDetail, Product> processor() {
        return item -> {
            final Category category = Category.of(item.getCategory());
            if (category == null)
                throw new IllegalArgumentException(String.format("Wrong category value %s", item.getCategory()));

            return Product.builder()
                    .name(item.getName())
                    .description(item.getDescription())
                    .cost(item.getCost())
                    .category(category)
                    .storage(item.getStorage())
                    .ram(item.getRam())
                    .processor(item.getProcessor())
                    .hasBluetooth(item.getHasBluetooth())
                    .build();
        };
    }

    @Bean
    public MongoItemWriter<Product> writer() {
        return new MongoItemWriterBuilder<Product>()
                .template(mongoTemplate).collection(productsCollection)
                .build();
    }
}
