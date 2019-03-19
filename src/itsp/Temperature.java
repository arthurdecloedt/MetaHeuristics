package itsp;

public class Temperature {

    public static final int MINTEMP = 20;
    public static final int MAXTEMP = 80;
    private static final int SLOPE = 6;

    /**
     * Calculate waiting time for given starting temp and processing time.
     *
     * @param currTemp
     * @param pt
     * @return
     */
    public static int getWaitingTime(int currTemp, int pt) {
        return Math.max(pt-(MAXTEMP-currTemp)/SLOPE, 0);
    }

    /**
     * Get the temp after working for the specified time.
     *
     * @param startTemp
     * @param time
     * @return
     */
    public static int getWorkingTempAfter(int startTemp, int time) {
        return Math.min(startTemp+SLOPE*time, MAXTEMP);
    }

    /**
     * Get the temp after cooling down for the specified time.
     *
     * @param startTemp
     * @param time
     * @return
     */
    public static int getCoolingTempAfter(int startTemp, int time) {
        double t = Math.log((startTemp-20)/60.0)/-0.5 + time;
        if (t > 10)
            return 20;
        else
            return (int) Math.round(60*Math.exp(-0.5*t)+20);
    }

}
