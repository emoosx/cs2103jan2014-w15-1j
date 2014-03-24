package logic;

import java.util.Date;
import java.util.List;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;


public class PandaParser {

	public static void main(String[] args) {
//		PrettyTimeParser  ptt = new PrettyTimeParser();
//		List<Date> dates = ptt.parse("attend a meeting next week from 2pm to 4pm");
//		System.out.println(dates);
		
		
//		List<DateGroup> parseResult = ptt.parseSyntax("camp from 12/2/2014 2pm to 13/2/2014 4pm");
		
//		System.out.println(parseResult.size());
//		System.out.println(parseResult.get(0).getDates());
		//test("camp from 1/1/2014 2:15pm to 2/1/14 3pm");
		//test("camp from tomorrow 14:00 to next week 24:15");
		test("camp on 22/8/2014 at 5:56pm");
		//test("camp from 1/1/2014 2:15pm to 2/1/14 3pm");
	}
	
	// Tester method
	public static void test(String input) {
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse(input);
		for(DateGroup group: groups) {
			List<Date> dates = group.getDates();
			System.out.println("Matching value: " + group.getText());
			System.out.println(dates);
		}
	}
}
