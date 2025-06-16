package detectors;

import models.FraudDataModel;
import java.util.List;
//interface defines the contract for fraud detectors (strategy pattern)
public interface FraudDetector {
    //method to detect fraudulent transactions
    List<FraudDataModel.Transaction> detectFraud();
}
