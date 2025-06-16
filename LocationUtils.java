package utils;

// Provides geographical distance calculations
public class LocationUtils {
    // Calculates distance between two points using Haversine formula
    // Returns distance in kilometers
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in KM
        // Convert latitude and longitude differences to radians
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        // Haversine formula components
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        // Calculate angular distance
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // Return actual distance
        return R * c;
    }
}

