package com.deliveroo.assignment;

public enum CronField {
	MINUTE(0),
	HOUR(1),
	DAY(2),
	MONTH(3),
	WEEKDAY(4),
	COMMAND(5);
	
	private int position;
	
	CronField(int position) {
		this.position = position;
	}
	
	public int index() {
		return position;
	}
	
}
