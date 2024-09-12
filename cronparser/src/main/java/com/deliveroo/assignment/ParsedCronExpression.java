package com.deliveroo.assignment;

import java.util.SortedSet;

public class ParsedCronExpression {
	private SortedSet<Integer> minutes;
    private SortedSet<Integer> hours;
    private SortedSet<Integer> days;
    private SortedSet<Integer> months;
    private SortedSet<Integer> weekDays;
	private String command;
	
	public ParsedCronExpression(SortedSet<Integer> minutes, SortedSet<Integer> hours, SortedSet<Integer> days,
			SortedSet<Integer> months, SortedSet<Integer> weekDays, String command) {
		super();
		this.minutes = minutes;
		this.hours = hours;
		this.days = days;
		this.months = months;
		this.weekDays = weekDays;
		this.command = command;
	}
	public SortedSet<Integer> getMinutes() {
		return minutes;
	}
	
	public SortedSet<Integer> getHours() {
		return hours;
	}
	
	public SortedSet<Integer> getDays() {
		return days;
	}
	
	public SortedSet<Integer> getMonths() {
		return months;
	}
	
	public SortedSet<Integer> getWeekDays() {
		return weekDays;
	}
	
	public String getCommand() {
		return command;
	}

}
