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
		
		
//		List<DateGroup> parseResult = ptt.parseSyntax("attend a meeting next week from 2pm to 4pm");
		
//		System.out.println(parseResult.size());
//		System.out.println(parseResult.get(0).getDates());
		
		
		// Natty
		Parser parser = new Parser();
		List<DateGroup> groups = parser.parse("attend a meeting next week from 2pm to 4pm");
		for(DateGroup group: groups) {
			List<Date> dates = group.getDates();
			System.out.println(dates);
			int line = group.getLine();
			System.out.println("line" + line);
		}

	}
}
