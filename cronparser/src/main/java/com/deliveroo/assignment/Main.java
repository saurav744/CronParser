package com.deliveroo.assignment;

public class Main {
	
	public static void main(String[] args) {
		String expression = args[0];
	//	String expression = "1  1  15  JAN-DEC  MON-FRI  /usr/bin/find";
		try {
			ParsedCronExpression result = CronParser.parse(expression);
			printParsedCronExpression(result);
		} catch (Exception e) {
			System.out.println("Error occured : "+e.getMessage());
		}
	}
	
	private static void printParsedCronExpression(ParsedCronExpression exp) {
		System.out.println("minute        "+exp.getMinutes());
		System.out.println("hour          "+exp.getHours());
		System.out.println("day of month  "+exp.getDays());
		System.out.println("month         "+exp.getMonths());
		System.out.println("day of week   "+exp.getWeekDays());
		System.out.println("command       "+exp.getCommand());
	}

}
