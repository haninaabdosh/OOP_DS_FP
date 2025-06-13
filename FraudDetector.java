package detectors;

import models.FraudDataModel;
import java.util.List;

public interface FraudDetector {
    List<FraudDataModel.Transaction> detectFraud();
}
