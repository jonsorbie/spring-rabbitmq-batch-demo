package com.example.demo.queue;

import com.example.demo.config.QueueConfig;
import com.example.demo.processor.DemoProcessor;
import com.example.demo.processor.DemoReader;
import com.example.demo.processor.DemoWriter;
import com.example.demo.processor.JobCompletionNotificationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class SpringBatchAmqpDemoListener {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    private AtomicInteger stepCounter = new AtomicInteger();


    @RabbitListener(queues= QueueConfig.QUEUE_NAME)
    public void receiveMessage(String payload) throws JobExecutionException {
        log.info("Received message: '{}'", payload);
        launchJob(payload);
    }


    private void launchJob(String message) throws JobExecutionException {
        Step step = dummyStep(message);
        Job job = dummyJob(step);
        JobParameters parameters = new JobParameters();
        jobLauncher.run(job, parameters);
    }


    private Step dummyStep(String message) {
        // Each step apparently needs a different name if you want to run
        // multiple jobs. An easy way to accomplish that is with a suffixed
        // number that's incremented each time.
        return stepBuilderFactory
            .get("DummyStep " + stepCounter.incrementAndGet())
            .<String, String>chunk(1)
            .reader(new DemoReader(message))
            .processor(new DemoProcessor())
            .writer(new DemoWriter())
            .build();
    }


    private Job dummyJob(Step dummyStep) {
        return jobBuilderFactory
            .get("DummyJob")
            .incrementer(new RunIdIncrementer())
            .listener(new JobCompletionNotificationListener())
            .flow(dummyStep)
            .end()
            .build();
    }
}
