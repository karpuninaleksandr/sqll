package ru.ac.uniyar.databasescourse.utils;

import de.siegmar.fastcsv.reader.CsvReader;
import ru.ac.uniyar.databasescourse.Essences.CsvParserString;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class CsvDataLoader {
    public static void load(Path path) throws IOException {
        try (CsvReader csvReader = CsvReader.builder().build(path)) {
            // .skip(1) <=> skip the header
            csvReader.stream().skip(1).forEach(csvRow -> {System.out.println(csvRow.getField(1));});
        }
    }

    public static ArrayList<CsvParserString> parseCsvFile(String path) throws FileNotFoundException {
        ArrayList data = new ArrayList<CsvParserString>();
        try (CsvReader csvReader = new CsvReader(new FileReader(path.toString()))) {
            // .skip(1) <=> skip the header
            csvReader.stream().skip(1).forEach(csvRow -> {System.out.println(csvRow.getField(1));});
        }
    }
}
