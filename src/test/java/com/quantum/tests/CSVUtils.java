package com.quantum.tests;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVUtils {

    public static Iterator<Object[]> readCSV(String resourcePath) throws Exception {
        List<Object[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                CSVUtils.class.getClassLoader().getResourceAsStream(resourcePath)))) {

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