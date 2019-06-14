package model.warehouse;

import java.text.SimpleDateFormat;
import java.util.*;

public class OrderBins {

    public enum TIME_INTERVAL {DAY, WEEK, MONTH, YEAR}

    private static TIME_INTERVAL timeInterval;
    private static Calendar calendar = Calendar.getInstance();

    public static ArrayList<TimeBin> binItemStocks(Warehouse warehouse, TIME_INTERVAL t) {
        timeInterval = t;
        ArrayList<TimeBin> bins = setUpTimeBins(warehouse);
        for (Item item : warehouse.getItem2order().keySet()) {
            ArrayList<Order> orders = warehouse.getItem2order().get(item);
            int binIndex = 0;
            for (int i = 0; i < orders.size(); i++) {

                Order o = orders.get(i);
                Date date = o.getDateOfDelivery();

                Date currentDate = new Date(System.currentTimeMillis());
                if (date.compareTo(currentDate) < 0)
                    continue;

                String timeStamp = getTimeStamp(date);
                while (!bins.get(binIndex).getTimeStamp().equals(timeStamp)) {
                    bins.get(binIndex).addStock(item, 0);
                    binIndex++;
                }
                bins.get(binIndex).addStock(item, o.getNumber());

            }

            while (binIndex < bins.size())
                bins.get(binIndex++).addStock(item, 0);

        }

        return bins;
    }

    private static ArrayList<TimeBin> setUpTimeBins(Warehouse warehouse) {
        ArrayList<TimeBin> bins = new ArrayList<>();
        String latestTimeStamp = getTimeStamp(getLatestOrder(warehouse));
        Date date = new Date(System.currentTimeMillis());
        while (!getTimeStamp(date).equals(latestTimeStamp)) {
            bins.add(new TimeBin(getTimeStamp(date)));
            date = addTimeInterval(date);
        }
        bins.add(new TimeBin(latestTimeStamp));
        return bins;
    }

    private static Date getLatestOrder(Warehouse warehouse) {
        Date d1 = null;
        for (Item item : warehouse.getItem2order().keySet()) {
            ArrayList<Order> order = warehouse.getItem2order().get(item);
            Date d2 = order.get(order.size() - 1).getDateOfDelivery();
            d1 = d1 == null ? d2 : d1.compareTo(d2) == -1 ? d2 : d1;
        }
        return d1;
    }

    private static Date addTimeInterval(Date date) {
        calendar.setTime(date);
        switch (timeInterval) {
            case DAY:
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case WEEK:
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, 1);
                break;
            default:
                calendar.add(Calendar.YEAR, 1);
        }
        return calendar.getTime();
    }

    private static String getTimeStamp(Date date) {
        calendar.setTime(date);
        switch (timeInterval) {
            case DAY:
                return getYear(date) + "-" + formatDate(date, "MM") + "-" + formatDate(date, "DD");
            case WEEK:
                return calendar.getWeekYear() + "-KW" + getWeekOfYear(date);
            case MONTH:
                return getYear(date) + "-" + formatDate(date, "MM");
            default:
                return getYear(date);
        }
    }

    private static String getWeekOfYear(Date date) {
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
    }

    private static String getYear(Date date) {
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    private static String formatDate(Date date, String pattern) {
        calendar.setTime(date);
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }

    public static class TimeBin {

        private String timeStamp;
        private HashMap<String, Integer> item2stock = new HashMap<>();

        public TimeBin(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public int getStock(String itemIdentifier) {
            if (item2stock.containsKey(itemIdentifier))
                return item2stock.get(itemIdentifier);
            return 0;
        }

        public void addStock(Item item, int n) {
            item2stock.putIfAbsent(item.getIdentifier(), 0);
            item2stock.put(item.getIdentifier(), item2stock.get(item.getIdentifier()) + n);
        }

        public String toString() {
            StringBuilder build = new StringBuilder();
            build.append(timeStamp + " ");
            for (String itemIdentifier : item2stock.keySet())
                build.append(itemIdentifier + ":" + item2stock.get(itemIdentifier) + " ");
            return build.toString();
        }

        public ArrayList<String> getItemIdentifier() {
            return new ArrayList<>(item2stock.keySet());
        }

    }

}
