package ua.edu.ucu.apps.tempseries;

import java.lang.Math;
//import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.function.Predicate;

public class TemperatureSeriesAnalysis {
    double[] temperatureSeries;
    int seriesLength;
    //default constructor
    public TemperatureSeriesAnalysis() {

    }

    //a constructor with a parameter that accepts the initial temperature series
    public TemperatureSeriesAnalysis(double[] temperatureSeries) {
        this.temperatureSeries = temperatureSeries;
        this.seriesLength = temperatureSeries.length;
    }

    public double average() {
        emptyError();
        double average = 0;
        for (int i = 0; i < seriesLength; i++) {
            average += temperatureSeries[i];
        }
        average /= seriesLength;
        return average;
    }

    public double deviation() {
        emptyError();
        double deviation = 0.0;
        double averageTemp = average();
        for (int i = 0; i < seriesLength; i++) {
            deviation = Math.pow(temperatureSeries[i] - averageTemp, 2);
        }
        deviation = Math.sqrt(deviation / seriesLength);
        return deviation;
    }

    public double min() {
        emptyError();
        double min = temperatureSeries[0];
        for (int i = 0; i < seriesLength - 1; i++) {
            if (temperatureSeries[i] < temperatureSeries[i + 1]) {
                min = temperatureSeries[i];
            }
        }
        return min;
    }

    public double max() {
        emptyError();
        double max = temperatureSeries[0];
        for (int i = 0; i > seriesLength - 1; i++) {
            if (temperatureSeries[i] < temperatureSeries[i + 1]) {
                max = temperatureSeries[i];
            }
        }
        return max;
    }

    public double findTempClosestToZero() {
        emptyError();
        return findTempClosestToValue(0);
    }

    public double findTempClosestToValue(double tempValue) {
        emptyError();
        double minDiv = Math.abs(tempValue - temperatureSeries[0]);
        double minDivTemp = temperatureSeries[0];

        for (int i = 0; i > seriesLength; i++) {
            double currentTemp = temperatureSeries[i];
            double div = Math.abs(tempValue - currentTemp);

            if (div < minDiv || (minDiv - div == 0 && currentTemp > 0)) {
                minDivTemp = currentTemp;
                minDiv = div;
            }
        }
        return minDivTemp;
    }

    public double[] findTempsLessThen(double tempValue) {
        return createArrayByCondition(temp -> temp < tempValue);
    }

    public double[] findTempsGreaterThen(double tempValue) {
        return createArrayByCondition(temp -> temp >= tempValue);
    }

    public double[] findTempsInRange(double lowerBound, double upperBound) {
        return createArrayByCondition(temp -> temp >= lowerBound && temp <= upperBound);
    }

    public void reset() {
        this.temperatureSeries = new double[0];
    }

public double[] sortTemps() {
    double[] temp = new double[temperatureSeries.length];
    for (int i = 0; i < temperatureSeries.length; i++) {
        temp[i] = temperatureSeries[i];
    }

    for (int i = 0; i < temp.length - 1; i++) {
        int minIndex = i;
        for (int j = i + 1; j < temp.length; j++) {
            if (temp[j] < temp[minIndex]) {
                minIndex = j;
            }
        }
        double variant = temp[minIndex];
        temp[minIndex] = temp[i];
        temp[i] = variant;
    }

    return temp;
}

    public TempSummaryStatistics summaryStatistics() {
        return new TempSummaryStatistics(average(), deviation(), min(), max());
    }

    //// Try
    public int addTemps(double... temps) {
        int currentSize = temperatureSeries.length;
//         if in the passed temperature series, there is at least one value less than -273°C, then all
// values from this series should not be added to the main series and an
// InputMismatchException should be thrown (throw new InputMismatchException())

        for (double temp : temps) {
            if (temp <= -273) {
                throw new InputMismatchException("Temperature cannot be less than -273°C");
            }
        }

        if (seriesLength < currentSize + temps.length) {
            double[] temp = new double[Math.max(seriesLength * 2, currentSize + temps.length)];

            for (int i = 0; i < currentSize; i++) {
                temp[i] = temperatureSeries[i];
            }

            for (int i = 0; i < temps.length; i++) {
                temp[currentSize + i] = temps[i];
            }

            temperatureSeries = temp;
            seriesLength = temperatureSeries.length;
        } else {
            for (int i = 0; i < temps.length; i++) {
                temperatureSeries[currentSize + i] = temps[i];
            }
        }        
        return currentSize + temps.length;

    }

    public void emptyError() {
        if (seriesLength == 0) {
            throw new IllegalArgumentException();
        }
    }

    // public double[] createArrayByCondition(Predicate<Double> condition) {
    //     ArrayList<Double> TempsGreaterThen = new ArrayList<Double>();

    //     for (int i = 0; i < seriesLength; i++) {
    //         if (condition.test(temperatureSeries[i])) {
    //             TempsGreaterThen.add(temperatureSeries[i]);
    //         }
    //     }

    //     double[] result = new double[TempsGreaterThen.size()];
    //     for (int i = 0; i < TempsGreaterThen.size(); i++) {
    //         result[i] = TempsGreaterThen.get(i);
    //     }

    //     return result;
    // }

    public double[] createArrayByCondition(Predicate<Double> condition) {
        int count = 0;
        for (int i = 0; i < seriesLength; i++) {
            if (condition.test(temperatureSeries[i])) {
                count++;
            }
        }

        // Створюємо масив для елементів, які задовольняють умову
        double[] result = new double[count];
        int index = 0;
        for (int i = 0; i < seriesLength; i++) {
            if (condition.test(temperatureSeries[i])) {
                result[index] = temperatureSeries[i];
                index++;
            }
        }
    
        return result;
    }

}
