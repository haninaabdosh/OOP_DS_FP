package utils;

import models.FraudDataModel;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvExporter {

    public static void export(List<FraudDataModel.Transaction> frauds, String fraudType, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("FraudType,TransactionID,FromAccountID,ToAccountID,Amount,Timestamp\n");
            for (FraudDataModel.Transaction t : frauds) {
                writer.write(fraudType + "," + t.getTransactionId() + "," + t.getFromAccountId() + "," +
                        t.getToAccountId() + "," + t.getAmount() + "," + t.getTimestamp() + "\n");
            }
            System.out.println("Exported fraud results to: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

