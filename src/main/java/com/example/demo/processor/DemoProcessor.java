package com.example.demo.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class DemoProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) {
        log.info("Processing item: '{}'", item);
        return item;
    }
}
