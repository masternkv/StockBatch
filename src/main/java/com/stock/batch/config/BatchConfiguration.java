package com.stock.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.stock.batch.listener.JobCompletionNotificationListener;
import com.stock.batch.model.NiftyStock;
import com.stock.batch.model.Person;
import com.stock.batch.processor.PersonItemProcessor;
import com.stock.batch.processor.StockCountProcessor;
import com.stock.batch.tasklet.StockExtractTasklet;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;
    
    @Autowired
    public ApplicationContext appContext;

    @Bean
    public FlatFileItemReader<Person> reader() {
        FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
        reader.setResource(new PathResource("C:\\Users\\NV5053984\\Desktop\\Stock List\\persons.csv"));
        reader.setLineMapper(new DefaultLineMapper<Person>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "firstName", "lastName","email","age" });
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }});
        }});
        return reader;
    }

    @Bean
    public FlatFileItemReader<NiftyStock> stockReader() {
        FlatFileItemReader<NiftyStock> reader = new FlatFileItemReader<NiftyStock>();
        reader.setLinesToSkip(1);
        reader.setResource(new PathResource("C:\\Users\\NV5053984\\Desktop\\Stock List\\inputstock.csv"));
        reader.setLineMapper(new DefaultLineMapper<NiftyStock>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] { "stockName", "stockPrice","stockChangeDate"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<NiftyStock>() {{
                setTargetType(NiftyStock.class);
            }});
        }});
        return reader;
    }
   /* @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }*/
    
     @Bean
    public StockCountProcessor processor() {
        return new StockCountProcessor();
    }
/*
    @Bean
    public JdbcBatchItemWriter<Person> writer() {
        JdbcBatchItemWriter<Person> writer = new JdbcBatchItemWriter<Person>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Person>());
        writer.setSql("INSERT INTO person (first_name, last_name,email,age) VALUES (:firstName, :lastName,:email,:age)");
        writer.setDataSource(dataSource);
        return writer;
    }*/
     
    @Bean
    public JdbcBatchItemWriter<NiftyStock> stockWriter() {
        JdbcBatchItemWriter<NiftyStock> writer = new JdbcBatchItemWriter<NiftyStock>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<NiftyStock>());
        writer.setSql("INSERT INTO daily_nifty (stock_name, stock_price,stock_chg_date) VALUES (:stockName, :stockPrice,:stockChangeDate)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importStockJob(JobCompletionNotificationListener listener) {
        return jobBuilderFactory.get("importStockJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(stockStep1()).next(stockExtractorStep())
                .end()
                .build();
    }
 
    @Bean
    public Step stockStep1() {
        return stepBuilderFactory.get("step1")
                .<NiftyStock, NiftyStock> chunk(10)
                .reader(stockReader())
                .processor(processor())
                .writer(stockWriter())
                .build();
    }
   
/*    @Bean
    public Job importStockJob1() {
        return jobBuilderFactory.get("importStockJob")
                .incrementer(new RunIdIncrementer())
                .flow(stockExtractorStep())
                .end()
                .build();
    }
    */
    @Bean
    public Step stockExtractorStep() {
        return stepBuilderFactory.get("stockExtractorStep")
        		  .allowStartIfComplete(true)
                 .tasklet(new StockExtractTasklet())
                .build();
    }
    
}