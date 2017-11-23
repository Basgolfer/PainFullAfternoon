package io.zipcoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {

    public ArrayList<String> parseRawDataIntoStringArray(String rawData){
        String stringPattern = "##";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawData);
        return response;
    }

    @SuppressWarnings("unchecked")
    private <T> T parseStringWithRegexToGetEachField(String rawItem, String regex) throws ItemParseException {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(rawItem);
        T valueToReturn = null;
        if (matcher.find()) {
            int startingIndex = matcher.start();
            int endingIndex = matcher.end();
            if ("name:\\w+".equals(regex) || "type:\\w+".equals(regex)) {
                valueToReturn = (T) rawItem.substring(startingIndex + 5, endingIndex);
            }
            else if ("\\d+[.]\\d+".equals(regex)) {
                Double aDouble = Double.parseDouble(rawItem.substring(startingIndex, endingIndex));
                valueToReturn = (T) aDouble;
            }
            else if ("\\d+[/]\\d+[/]\\d+".equals(regex)) {
                valueToReturn = (T) rawItem.substring(startingIndex, endingIndex);
            }
            else {
                System.out.println("Invalid regex expression. Please modify this method.");
            }
        }
        else {
            throw new ItemParseException();
        }
        return valueToReturn;
    }

    public Item parseStringIntoItem(String rawItem) throws ItemParseException{
        String name = parseStringWithRegexToGetEachField(rawItem, "name:\\w+");
        String type = parseStringWithRegexToGetEachField(rawItem, "type:\\w+");
        Double price = parseStringWithRegexToGetEachField(rawItem, "\\d+[.]\\d+");
        String expiration = parseStringWithRegexToGetEachField(rawItem,"\\d+[/]\\d+[/]\\d+");
        return new Item(name, price, type, expiration);
    }

    public ArrayList<String> findKeyValuePairsInRawItemData(String rawItem){
        String stringPattern = "[;^!@%]";
        ArrayList<String> response = splitStringWithRegexPattern(stringPattern , rawItem);
        return response;
    }

    private ArrayList<String> splitStringWithRegexPattern(String stringPattern, String inputString){
        return new ArrayList<>(Arrays.asList(inputString.split(stringPattern)));
    }
}
