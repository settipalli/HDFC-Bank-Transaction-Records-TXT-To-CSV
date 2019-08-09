package com.settipalli;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static String parse(String line) {
        StringBuilder sb = new StringBuilder(line.length() + 10);
        String date = "";
        String details = "";
        String refNumber = "";
        String valueDate = "";
        String debit = "";
        String credit = "";
        String balance = "";

        try {
            date = line.substring(0, 8 + 1).trim();
        } catch (StringIndexOutOfBoundsException ex) {
            System.err.println("\t Date missing.");
        }
        try {
            details = line.substring(11, 50 + 1).trim();
        } catch (StringIndexOutOfBoundsException ex) {
            System.err.println("\t Details missing.");
        }
        try {
            refNumber = line.substring(53, 70 + 1).trim();
        } catch (StringIndexOutOfBoundsException ex) {
            System.err.println("\t Reference Number missing.");
        }
        try {
            valueDate = line.substring(72, 85 + 1).trim();
        } catch (StringIndexOutOfBoundsException ex) {
            System.err.println("\t Value Date missing.");
        }
        try {
            debit = line.substring(85, 105 + 1).trim().replace(",", "");
        } catch (StringIndexOutOfBoundsException ex) {
            System.err.println("\t Debit missing.");
        }
        try {
            credit = line.substring(104, 120 + 1).trim().replace(",", "");
        } catch (StringIndexOutOfBoundsException ex) {
            System.err.println("\t Credit missing.");
        }
        try {
            balance = line.substring(119).trim().replace(",", "");
        } catch (StringIndexOutOfBoundsException ex) {
            System.err.println("\t Balance missing.");
        }

        sb.append(date)
                .append(",")
                .append(details)
                .append(",")
                .append(refNumber)
                .append(",")
                .append(valueDate)
                .append(",")
                .append(debit)
                .append(",")
                .append(credit)
                .append(",")
                .append(balance);

        return sb.toString();
    }

    static String append(String line, String newData) {
        String[] tempResult = line.split(",");
        String[] tempNewData = newData.split(",");
        tempResult[1] += " " + tempNewData[1];
        return String.join(",", tempResult);
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 1) {
            System.err.println("Arguments have to just one.");
            System.exit(1);
        }
        String newline = System.getProperty("line.separator");
        File f = new File(args[0]);
        if (!f.exists()) {
            System.err.println(args[0] + " must exist.");
            System.exit(2);
        }
        System.out.println(args[0] + " found. Reading ...");
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line;
        int lineCount = 0;
        List<String> output = new ArrayList<>(1500); // 144 chars * 1420 lines
        while ((line = br.readLine()) != null) {
            lineCount++;
            System.out.println("Processed: " + lineCount + " line" + (lineCount == 1 ? "" : "s"));
            String result = parse(line);
            if (result.charAt(0) == ',') {
                String data = output.remove(output.size() - 1);
                result = append(data, result);
            }
            output.add(result);
        }
        br.close();

        String outputFilename = "Processed-" + f.getName().substring(0, f.getName().indexOf(".")) + ".csv";
        FileWriter outputFile = new FileWriter(outputFilename);
        outputFile.write(String.join(newline, output).toString());
        outputFile.close();
        System.out.println("Done.");
    }
}
