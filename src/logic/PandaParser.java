package logic;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

public class PandaParser {

	// mutable
	private static MutableDateTime startDateTime = null;
	private static MutableDateTime endDateTime = null;
	
	public static void main(String[] args) {
//		PrettyTimeParser  ptt = new PrettyTimeParser();
//		List<Date> dates = ptt.parse("attend a meeting next week from 2pm to 4pm");
//		System.out.println(dates);
			
//		List<DateGroup> parseResult = ptt.parseSyntax("camp from 12/2/2014 2pm to 13/2/2014 4pm");
		
//		System.out.println(parseResult.size());
//		System.out.println(parseResult.get(0).getDates());
		//test("camp from 1/1/2014 2:15pm to 2/1/14 3pm");
		//test("camp from tomorrow 14:00 to next week 24:15");
		//String input = "camp on 22/8/2014 at 5:56pm";
		//test(RegExp.changeDateFormat("camp on 13/2/14 at 5:15pm"));
		//test("camp from 1/1/2014 2:15pm to 2/1/14 3pm");
		while(true) {
			Scanner sc = new Scanner(System.in);
			test(RegExp.changeDateFormat(sc.nextLine()));
		}
	}
	
	// Tester method
	public static void test(String input) {
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(input);
		for(DateGroup group: groups) {
			List<Date> dates = group.getDates();
			System.out.println("Matching value: " + group.getText());
			DateTimeFormatter fmt = DateTimeFormat.forPattern("d MMM, yyyy HH:mm");
			
			if(dates.size() == 1) {
				startDateTime = new MutableDateTime(dates.get(0));			
				System.out.println(fmt.print(startDateTime));
			} else {
				endDateTime = new MutableDateTime(dates.get(1));
				System.out.println(fmt.print(endDateTime));
			}
		}
	}
}