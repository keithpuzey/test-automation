package com.quantum.tests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVUtils {

    public static Iterator<Object[]> readCSV(String filePath) throws Exception {
        List<Object[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean header = true;
            while ((line = br.readLine()) != null) {
                if (header) { header = false; continue; } // skip header
                data.add(new Object[]{line});
            }
        }
        return data.iterator();
    }
}