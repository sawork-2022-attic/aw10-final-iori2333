package com.micropos.batch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonFileReader implements StepExecutionListener, ItemReader<JsonNode> {

    private BufferedReader reader;

    private ObjectMapper objectMapper;

    private final String fileName;

    public JsonFileReader(String file) {
        if (file.matches("^file:(.*)"))
            file = file.substring(file.indexOf(":") + 1);
        this.fileName = file;
    }

    private void initReader() throws FileNotFoundException {
        File file = new File(fileName);
        reader = new BufferedReader(new FileReader(file));
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public JsonNode read() throws Exception {
        if (objectMapper == null)
            objectMapper = new ObjectMapper();

        if (reader == null) {
            initReader();
        }

        String line = reader.readLine();
        if (line != null) {
            var node = (ObjectNode) objectMapper.readTree(line);
            var priceNode = node.get("price");
            var priceString = priceNode.asText()
                .replace("$", "") // $xxx.xx
                .replace(",", ""); // x,xxx.xx
            try {
                if (priceString.isEmpty()) {
                    // price == ''
                    node.put("price", 0);
                } else if (priceString.contains("-")) {
                    // price == 'xxx - xxx'
                    var priceRange = priceString.split("-");
                    node.put("price", Double.parseDouble(priceRange[0].replace("$", "")));
                } else {
                    // price == 'xxx'
                    node.put("price", Double.parseDouble(priceString));
                }
            } catch (NumberFormatException e) {
                // some price is not available (recorded as css variable)
                System.out.println("Error parsing price: " + priceString);
                node.put("price", 0);
            }
            return node;
        } else
            return null;
    }
}
