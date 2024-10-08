package ua.edu.ucu.apps.tempseries;

import java.util.InputMismatchException;
import java.util.function.Predicate;

public class TemperatureSeriesAnalysis {
    private static final double ABSOLUTE_ZERO = -273.0; // Константа для абсолютного нуля
    private static final double FLOAT_COMPARISON_THRESHOLD = 1e-9; // Константа для порівняння з плаваючою точкою
    private double[] temperatureSeries;
    private int seriesLength;

    public TemperatureSeriesAnalysis() {
    }

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
        return average / seriesLength;
    }

    public double deviation() {
        emptyError();
        double deviation = 0.0;
        double averageTemp = average();
        for (int i = 0; i < seriesLength; i++) {
            deviation += (temperatureSeries[i] - averageTemp) *
                    (temperatureSeries[i] - averageTemp);
        }
        return Math.sqrt(deviation / seriesLength);
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

        for (int i = 0; i < seriesLength; i++) { // виправлено умову циклу
            double currentTemp = temperatureSeries[i];
            double div = Math.abs(tempValue - currentTemp);

            if (div < minDiv ||
                    (Math.abs(minDiv - div) < FLOAT_COMPARISON_THRESHOLD && currentTemp > 0)) {
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
        this.seriesLength = 0;
    }

    public double[] sortTemps() {
        double[] temp = new double[temperatureSeries.length];
        System.arraycopy(temperatureSeries, 0, temp, 0, temperatureSeries.length);

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

    public int addTemps(double... temps) {
        int currentSize = temperatureSeries.length;

        for (double temp : temps) {
            if (temp <= ABSOLUTE_ZERO) { // Використовуємо константу
                throw new InputMismatchException("Temperature cannot be less than " + ABSOLUTE_ZERO + "°C");
            }
        }

        if (seriesLength < currentSize + temps.length) {
            double[] tempArray = new double[Math.max(seriesLength * 2, currentSize + temps.length)];
            System.arraycopy(temperatureSeries, 0, tempArray, 0, currentSize);

            for (int i = 0; i < temps.length; i++) {
                tempArray[currentSize + i] = temps[i];
            }

            temperatureSeries = tempArray;
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

    public double[] createArrayByCondition(Predicate<Double> condition) {
        int count = 0;
        for (int i = 0; i < seriesLength; i++) {
            if (condition.test(temperatureSeries[i])) {
                count++;
            }
        }

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
