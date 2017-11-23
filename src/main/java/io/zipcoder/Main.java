package io.zipcoder;

import org.apache.commons.io.IOUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static ArrayList<Item> itemsList = new ArrayList<>();
    private static Integer numberOfItemParseExceptions = 0;

    private String readRawDataToString() throws Exception{
        ClassLoader classLoader = getClass().getClassLoader();
        String result = IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
        return result;
    }

    public static void main(String[] args) throws Exception{
        String output = (new Main()).readRawDataToString();
        //System.out.println(output);
        ItemParser itemParser = new ItemParser();
        ArrayList<String> items = itemParser.parseRawDataIntoStringArray(output);
        for (String itemString : items) {
            try {
                itemsList.add(itemParser.parseStringIntoItem(itemString));
            } catch (ItemParseException e) {
                numberOfItemParseExceptions++;
            }
        }
        HashMap<Double, Integer> priceFrequencyMilk = createPriceHashMap("[Mm][Ii][Ll][Kk]");
        HashMap<Double, Integer> priceFrequencyBread = createPriceHashMap("[Bb][Rr][Ee][Aa][Dd]");
        HashMap<Double, Integer> priceFrequencyCookies = createPriceHashMap("[Cc][Oo0]{2}[Kk][Ii][Ee][Ss]");
        HashMap<Double, Integer> priceFrequencyApples = createPriceHashMap("[Aa][Pp]{2}[Ll][Ee][Ss]");

        printOccurrencesOf("Milk", priceFrequencyMilk);
        printOccurrencesOf("Bread", priceFrequencyBread);
        printOccurrencesOf("Cookies", priceFrequencyCookies);
        printOccurrencesOf("Apples", priceFrequencyApples);

        printErrors();
    }

    private static void printOccurrencesOf(String itemName, HashMap<Double, Integer> priceMap) {
        int counter = 0;

        for (Map.Entry<Double, Integer> map : priceMap.entrySet()) {
            Integer value = map.getValue();
            counter += value;
        }

        System.out.println("name:\t" + itemName + "\t\t\tseen: " + counter + " times");
        printSeparator("=");
        printPriceTable(priceMap);
    }

    private static void printErrors() {
        System.out.println("Errors\t\t\t\t\tseen: " + numberOfItemParseExceptions + " times");
    }

    private static HashMap<Double,Integer> createPriceHashMap(String regex) {
        HashMap<Double, Integer> map = new HashMap<>();
        for (Item item : itemsList) {
            if (item.getName().matches(regex)) {
                if (map.containsKey(item.getPrice())) {
                    Integer newValue = map.get(item.getPrice());
                    newValue++;
                    map.put(item.getPrice(), newValue);
                }
                else {
                    map.put(item.getPrice(), 1);
                }
            }
        }
        return map;
    }

    private static void printPriceTable(HashMap<Double,Integer> priceMap) {
        for (Map.Entry<Double, Integer> map : priceMap.entrySet()) {
            Double key = map.getKey();
            Integer value = map.getValue();
            if (value == 1) {
                System.out.println("Price:\t" + key + "\t\t\tseen: " + value + " time");
            }
            else {
                System.out.println("Price:\t" + key + "\t\t\tseen: " + value + " times");
            }
            printSeparator("-");
        }
        System.out.println();
    }

    private static void printSeparator(String separator) {
        String addOneEachTimeInLoop = separator;
        for (int i = 0; i < 15; i++) {
            separator += addOneEachTimeInLoop;
        }
        System.out.println(separator + "\t\t" + separator);
    }
}
