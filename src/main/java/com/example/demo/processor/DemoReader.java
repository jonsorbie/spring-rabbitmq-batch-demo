package com.example.demo.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.Collections;
import java.util.Iterator;

@Slf4j
public class DemoReader implements ItemReader<String> {

    private Iterator<String> items;


    public DemoReader(String message) {
        // If one wanted to divide the message up into a number of pieces, it could be done here.
        // For the purposes of an example, this implementation simply considers a message as one item.
        items = Collections.singletonList(message).iterator();
    }


    @Override
    public String read() {
        if (!items.hasNext()) return null;
        String item = items.next();
        log.info("Reading item: '{}'", item);
        return item;
    }
}
