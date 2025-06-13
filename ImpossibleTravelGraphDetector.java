package strategies;

import graph.TransactionEdge;
import graph.UserNode;
import models.FraudDataModel;
import utils.LocationUtils;

import java.util.*;

public class ImpossibleTravelGraphDetector implements GraphFraudDetectionStrategy {

    @Override
    public List<FraudDataModel.Transaction> detectFraud(Map<String, UserNode> userNodes) {
        List<FraudDataModel.Transaction> frauds = new ArrayList<>();

        for (UserNode node : userNodes.values()) {
            for (TransactionEdge edge : node.getEdges()) {
                FraudDataModel.Transaction t = edge.getTransaction();
                UserNode toNode = userNodes.get(t.getToAccountId());

                if (toNode == null) continue;

                long timeDiff = getTimeDifference(t.getTimestamp());
                double distance = LocationUtils.haversine(
                        node.getLatitude(), node.getLongitude(),
                        toNode.getLatitude(), toNode.getLongitude());

                double speed = (distance / (timeDiff / 3600000.0)); // km/h

                if (speed > 1000) { // Impossible travel speed threshold
                    frauds.add(t);
                }
            }
        }
        return frauds;
    }

    private long getTimeDifference(String timestamp) {
        // For simplicity, just return 1 hour in ms
        // TODO: Implement real timestamp difference calculation if needed
        return 3600000;
    }
}
