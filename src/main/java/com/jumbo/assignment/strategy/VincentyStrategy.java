package com.jumbo.assignment.strategy;

import com.jumbo.assignment.domain.entity.Store;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class VincentyStrategy implements DistanceStrategy{

	@Override
	public double calculateDistance(Store store, double startLatitude, double startLongitude) {

		double a = 6378137;
		double b = 6356752.314245;
		double f = 1 / 298.257223563;
		double l = Math.toRadians(store.getLongitude() - startLongitude);
		double u1 = Math.atan((1 - f) * Math.tan(Math.toRadians(startLatitude)));
		double u2 = Math.atan((1 - f) * Math.tan(Math.toRadians(store.getLatitude())));
		double sinU1 = Math.sin(u1);
		double cosU1 = Math.cos(u1);
		double sinU2 = Math.sin(u2);
		double cosU2 = Math.cos(u2);
		double cosSqAlpha;
		double sinSigma;
		double cos2SigmaM;
		double cosSigma;
		double sigma;
		double lambda = l;
		double lambdaP;
		double iterLimit = 100;

		do {
			double sinLambda = Math.sin(lambda);
			double cosLambda = Math.cos(lambda);

			sinSigma = Math.sqrt( (cosU2 * sinLambda)
					* (cosU2 * sinLambda)
					+ (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)
					* (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda)

			);

			if (sinSigma == 0) return 0;

			cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
			sigma = Math.atan2(sinSigma, cosSigma);
			double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
			cosSqAlpha = 1 - sinAlpha * sinAlpha;
			cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;

			double c = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));

			lambdaP = lambda;

			lambda =  l + (1 - c) * f * sinAlpha
					* (sigma + c * sinSigma
					* (cos2SigmaM + c * cosSigma
					*(-1 + 2 * cos2SigmaM * cos2SigmaM)
			)
			);

		} while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

		if (iterLimit == 0) return 0;

		double uSq = cosSqAlpha * (a * a - b * b) / (b * b);

		double A = 1 + uSq / 16384
				* (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));

		double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

		double deltaSigma =
				B * sinSigma
						* (cos2SigmaM + B / 4
						* (cosSigma
						* (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM
						* (-3 + 4 * sinSigma * sinSigma)
						* (-3 + 4 * cos2SigmaM * cos2SigmaM)));

		double s = b * A * (sigma - deltaSigma);
		BigDecimal distanceBd = BigDecimal.valueOf(s);
		BigDecimal distanceInKm = distanceBd.divide(new BigDecimal(1000),4);
		distanceInKm = distanceInKm.setScale(4,BigDecimal.ROUND_HALF_UP);
		store.setDistance(distanceInKm.doubleValue());
		return distanceInKm.doubleValue();
	}

}
