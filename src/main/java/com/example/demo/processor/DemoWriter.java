package com.example.demo.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class DemoWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) {
        items.forEach(this::write);
    }


    private void write(String item) {
        log.info("Writing item: '{}'", item);
    }
}
