package logic;

public class RegExp {
	
	private static final int TOTAL_DATE_FIELDS = 3;
	private static final int TOTAL_TIME_FIELDS = 2;
	// Date Regular Expressions
	// 1. DDMM[YY[YY]]
    public static String DATE1 = "\\b\\d{1,2}\\/\\d{1,2}(?:\\/\\d{2,4})?\\b";
    
    // 2. DD string_rep_of_month [YY[YY]]
    public static String DATE2 = "\\b\\d{1,2}\\s(?:january|febuary|march|april|may|june|july|august|september|october|november|december|jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec)(?:\\s\\d{2,4})?\\b";

    
    // Hash Tag Regular Expressions
    public static String HASHTAG = "#(.+?)\\b";
    

    // extracts information from a date string and return it as an array of 3 items.
    public static int[] dateFromDateString(String dateString) {
    	int [] date = new int[TOTAL_DATE_FIELDS];
    	
    	// case1
    	// DD/MM/[/YY[YY]}
    	
        // 2. DD string_rep_of_month [YY[YY]]

    	// stub
    	date[0] = 2;
    	date[1] = 3;
    	date[2] = 2014;
    	return date;
    	
    }
    
    public static int[]  timeFromTimeString(String timeString) {
    	int [] time = new int[TOTAL_TIME_FIELDS];
    	
    	
    	// case 1
    	
    	// case 2
 
    	// and so on

    	time[0] = 10;
    	time[1] = 15;
    	return time; 
    }
}
