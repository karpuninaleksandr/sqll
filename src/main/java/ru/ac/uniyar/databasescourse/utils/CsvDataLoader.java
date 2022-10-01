package ru.ac.uniyar.databasescourse.utils;


import de.siegmar.fastcsv.reader.CsvReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class CsvDataLoader {
    public static ArrayList<CsvParserString> parseCsvFile(Path path) {
        ArrayList<CsvParserString> data = new ArrayList<>();
        try (CsvReader csvReader = CsvReader.builder().build(path)) {
            csvReader.stream().skip(1).forEach(csvRow -> {
                data.add(new CsvParserString(
                        Integer.parseInt(csvRow.getField(0)),
                        csvRow.getField(1),
                        csvRow.getField(2),
                        Integer.parseInt(csvRow.getField(3)),
                        Integer.parseInt(csvRow.getField(4)),
                        csvRow.getField(5),
                        csvRow.getField(6),
                        Double.parseDouble(csvRow.getField(7)),
                        csvRow.getField(8)));
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
