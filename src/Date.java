/*
 *  Date class that contains the day, month and year.
 *  The date object will be used by task objects as one of its attributes.
 */

public class Date {
	
	private int day;
	private int month;
	private int year;
	
	// Constructor method for date class
	public Date(int day, int month, int year) {
		setDay(day);
		setMonth(month);
		setYear(year);
	}
	
	public int getDay() {
		return day;
	}
	
	public int getMonth() {
		return month;
	}
	
	public int getYear() {
		return year;
	}
	
	public void setDay(int day) {
		this.day = day;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
}
