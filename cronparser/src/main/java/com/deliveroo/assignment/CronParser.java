package com.deliveroo.assignment;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import com.deliveroo.assignment.exception.IllegalCronFormatException;

public class CronParser {
	
	private static final EnumMap<CronField, String> fieldsRegexMap = new EnumMap<>(CronField.class);
	private static final Map<String, Integer> monthMap = new HashMap<>();
	private static final Map<String, Integer> weekMap = new HashMap<>();
	private static final int SUN_START = 0;
	private static final int SUN_END = 7;
	
	private static void buildMonthMap() {
		monthMap.put("JAN", 1);
		monthMap.put("FEB", 2);
		monthMap.put("MAR", 3);
		monthMap.put("APR", 4);
		monthMap.put("MAY", 5);
		monthMap.put("JUN", 6);
		monthMap.put("JUL", 7);
		monthMap.put("AUG", 8);
		monthMap.put("SEP", 9);
		monthMap.put("OCT", 10);
		monthMap.put("NOV", 11);
		monthMap.put("DEC", 12);	
	}
	private static void buildWeekMap() {
		weekMap.put("SUN", 0);
		weekMap.put("MON", 1);
		weekMap.put("TUE", 2);
		weekMap.put("WED", 3);
		weekMap.put("THU", 4);
		weekMap.put("FRI", 5);
		weekMap.put("SAT", 6);
	}
	
	private static String[] splitAndValidateCronExpression(String expression) throws IllegalCronFormatException{
		if(expression == null) {
			throw new IllegalArgumentException("null input");
		}
		String[] fields = expression.split("\\s+");
		if(fields.length > CronField.values().length) {
			throw new IllegalCronFormatException("Invalid cron format! Too many fields !");	
		} else if (fields.length < CronField.values().length) {
			throw new IllegalCronFormatException("Invalid cron format! Not enough fields!");
		}
		for(CronField field : CronField.values()) {
			// add validation for each field
			String fieldRegex = fieldsRegexMap.get(field);
			Pattern fieldPattern = Pattern.compile(fieldRegex);
			if(!fieldPattern.matcher(fields[field.index()]).matches()) {
	            throw new IllegalArgumentException("Invalid cron expression. Illegal field : "+ field.toString());
	        }
		}
		return fields;		
	}
	
	private static void buildFieldsValidationRegexMap() {
		String monthNames = "JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC";
		String daysOfWeekNames = "MON|TUE|WED|THU|FRI|SAT|SUN";
		String monthNamesRangePattern = "(?:" + monthNames + ")(?:(?:-)(?:" + monthNames + "))?";
		String daysOfWeekNamesRangePattern = "(?:" + daysOfWeekNames + ")(?:(?:-)(?:" + daysOfWeekNames + "))?";
		
		// build map with validation regex pattern for each field
		fieldsRegexMap.put(CronField.MINUTE, "[0-5]?\\d");
		fieldsRegexMap.put(CronField.HOUR, "[01]?\\d|2[0-3]");
		fieldsRegexMap.put(CronField.DAY, "0?[1-9]|[12]\\d|3[01]");
		fieldsRegexMap.put(CronField.MONTH, "[1-9]|1[012]");
		fieldsRegexMap.put(CronField.WEEKDAY, "[0-7]");
		fieldsRegexMap.put(CronField.COMMAND, "\\S+");

		//add more patterns
        for (CronField field : CronField.values()) {
            if(field != CronField.COMMAND) {
            	String numberPattern = fieldsRegexMap.get(field) ;
                String rangePattern =
                        "(?:" + numberPattern + ")" +
                        "(?:" +
                            "(?:-)" +
                            "(?:" + numberPattern + ")" +
                        ")?";
                if(field == CronField.MONTH) {
                	rangePattern += "|" + monthNamesRangePattern;
                }
                if(field == CronField.WEEKDAY) {
                	rangePattern += "|" + daysOfWeekNamesRangePattern;
                }
                String stepPattern =
                		"(?:\\*|" + rangePattern + ")" +
                         "(?:" +
                               "(?:/)" +
                               "(?:" + numberPattern + ")" +
                         ")?";
                String questionMarkPattern = (field == CronField.DAY || field == CronField.WEEKDAY ? "\\?|" : "");
                String finalPattern = questionMarkPattern + stepPattern + "(?:," + stepPattern + ")*";//removed \\*
                fieldsRegexMap.put(field, finalPattern);
            }
        }
	}


	private CronParser() {
	}

	public static ParsedCronExpression parse(String expression) throws IllegalCronFormatException {
		buildFieldsValidationRegexMap();
		buildMonthMap();
		buildWeekMap();
		String[] fields = splitAndValidateCronExpression(expression);
		
		SortedSet<Integer> minutes = parseMinutes(fields[CronField.MINUTE.index()]);
		SortedSet<Integer> hours = parseHours(fields[CronField.HOUR.index()]);
		SortedSet<Integer> days = parseDays(fields[CronField.DAY.index()]);
		SortedSet<Integer> months = parseMonths(fields[CronField.MONTH.index()]);
		SortedSet<Integer> weekdays = parseWeekdays(fields[CronField.WEEKDAY.index()]);
		String command = fields[CronField.COMMAND.index()];
		
		return new ParsedCronExpression(minutes, hours, days, months, weekdays, command);
	}
	
	private static SortedSet<Integer> parseMinutes(String expression) {
		return parseTime(expression, 0, 59);
	}
	
	private static SortedSet<Integer> parseHours(String expression) {
		return parseTime(expression, 0, 23);
	}
	
	private static SortedSet<Integer> parseDays(String expression) {
		return parseTime(expression, 1, 31);
	}
	
	private static SortedSet<Integer> parseMonths(String expression) {
		
		return parseTime(expression, 1, 12);
	}
	
	private static SortedSet<Integer> parseWeekdays(String expression) {
		
		SortedSet<Integer> result = parseTime(expression, 0, 7);
		if(result.contains(0)) {
			result.remove(SUN_START);
			result.add(SUN_END);
		}
		return result;
	}
	
	private static SortedSet<Integer> parseTime(String expression, int min, int max) {
		SortedSet<Integer> resultSet = new TreeSet<>();	
		
		String[] parts = expression.split(",");
		
		for(String part : parts) {
			int stepSize = 1;
			int start;
			int end = -1;
			String[] stepParts = part.split("/");
			if(stepParts.length > 1) {
				stepSize = Integer.parseInt(stepParts[1]);
				end = max;
			}
			
			String[] rangeParts = stepParts[0].split("-");
			if(rangeParts.length > 1) {
				if(monthMap.keySet().contains(rangeParts[1])) {
					end = monthMap.get(rangeParts[1]);
				} else if (weekMap.keySet().contains(rangeParts[1])) {
					end = weekMap.get(rangeParts[1]) == SUN_START ? SUN_END : weekMap.get(rangeParts[1]);
				}else {
					end = Integer.parseInt(rangeParts[1]);
				}
			}
			if(rangeParts[0].equals("*") || rangeParts[0].equals("?")) {
				start = min;
				end = max;
			} else {
				if(monthMap.keySet().contains(rangeParts[0])) {
					start = monthMap.get(rangeParts[0]);
				} else if (weekMap.keySet().contains(rangeParts[0])) {
					start = weekMap.get(rangeParts[0]);
				} else {
					start = Integer.parseInt(rangeParts[0]);
				}
			}
			
			int result = start;
			do {
				resultSet.add(result);
				result += stepSize;
			} while(result <= end);
		}
		
		return resultSet;
	}

}
