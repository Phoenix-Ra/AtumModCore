package me.phoenixra.atumodcore.api.utils;

public class TimeUtils {
    public static String SYMBOL_SECOND = "s";
    public static String SYMBOL_MINUTE = "m";
    public static String SYMBOL_HOUR = "h";
    public static String SYMBOL_DAY = "d";
    public static String SYMBOL_MONTH = "M";
    public static String SYMBOL_YEAR = "y";

    /**
     * Parse time to string
     * <p>Example: 1h 30m 20s</p>
     * <p>You can change the symbols used: </p>
     * <p>{@link #SYMBOL_SECOND} {@link #SYMBOL_MINUTE} {@link #SYMBOL_HOUR}</p>
     * @param time the time
     * @return the result
     */
    public static String parseTimeToString(long time) {
        long z=time/1000L;
        if(z<=0L) {
            return "0"+SYMBOL_SECOND;
        }
        if(z<60L) {
            return z+SYMBOL_SECOND;
        }
        else if(z<3600) {
            long min=z/60L;
            long seconds=z-(min*60L);
            if(seconds==0) {
                return min+SYMBOL_MINUTE;
            }
            return min+SYMBOL_MINUTE+" "+seconds+SYMBOL_SECOND;
        }
        else if(z<86400) {
            long hour = z/3600L;
            long min=(z-(hour*3600L))/60L;
            //ignore seconds
            if(min==0) {
                return hour+SYMBOL_HOUR;
            }
            return hour+SYMBOL_HOUR+" "+ min+SYMBOL_MINUTE;
        }else if (z<2592000){
            long day = z/86400L;
            long hour = (z-(day*86400L))/3600L;
            long min=(z-(hour*3600L+day*86400L))/60L;
            //ignore seconds
            if(min==0) {
                if(hour==0) {
                    return day+SYMBOL_DAY;
                }
                return day+SYMBOL_DAY+" "+hour+SYMBOL_HOUR;
            }
            if(hour==0) {
                return day+SYMBOL_DAY+" "+min+SYMBOL_MINUTE;
            }
            return day+SYMBOL_DAY+" "+hour+SYMBOL_HOUR+" "+ min+SYMBOL_MINUTE;
        }else if (z<31536000){
            //ignore minutes and hours
            long month = z/2592000L;
            long day = (z-(month*2592000L))/86400L;
            //ignore seconds
            if(day==0) {
                return month+SYMBOL_MONTH;
            }
            return month+SYMBOL_MONTH+" "+day+SYMBOL_DAY;
        }else {
            //ignore minutes and hours
            long year = z/31536000L;
            long month = (z-(year*31536000L))/2592000L;
            long day = (z-(month*2592000L+year*31536000L))/86400L;
            //ignore seconds
            if(day==0) {
                if(month==0) {
                    return year+SYMBOL_YEAR;
                }
                return year+SYMBOL_YEAR+" "+month+SYMBOL_MONTH;
            }
            if(month==0) {
                return year+SYMBOL_YEAR+" "+day+SYMBOL_DAY;
            }
            return year+SYMBOL_YEAR+" "+month+SYMBOL_MONTH+" "+day+SYMBOL_DAY;
        }
    }

    /**
     * Get time left parsed to string
     * <p>see also {@link #parseTimeToString(long)}</p>
     * @param startTime the start time
     * @param endTime the end time
     * @return the result
     */
    public static String getTimeLeftString(long startTime, long endTime) {
        return parseTimeToString(endTime-startTime);
    }

    /**
     * Parse string time to long
     * <p>see also {@link #parseTimeToString(long)}</p>
     * @param text the text to parse
     * @return time
     */
    public static long parseStringToTime(String text) {
        String s = text.toLowerCase();
        long time = 0L;
        try {
            if(s.contains(SYMBOL_SECOND)) {
                time+=Long.parseLong(s.split(SYMBOL_SECOND)[0])*1000L;
            }
            if(s.contains(SYMBOL_MINUTE)) {
                time+=Long.parseLong(s.split(SYMBOL_MINUTE)[0])*60000L;
            }
            if(s.contains(SYMBOL_HOUR)) {
                time+=Long.parseLong(s.split(SYMBOL_HOUR)[0])*3600000L;
            }
            if(s.contains(SYMBOL_DAY)) {
                time+=Long.parseLong(s.split(SYMBOL_DAY)[0])*86400000L;
            }
            if(s.contains(SYMBOL_MONTH)) {
                time+=Long.parseLong(s.split(SYMBOL_MONTH)[0])*2592000000L;
            }
            if(s.contains(SYMBOL_YEAR)) {
                time+=Long.parseLong(s.split(SYMBOL_YEAR)[0])*31536000000L;
            }
        }catch(Exception e) {
            return 0;
        }
        return time;
    }
}
