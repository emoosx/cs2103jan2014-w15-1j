package test;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import logic.CommandFactory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommandFactoryTest {

	private CommandFactory cf = CommandFactory.INSTANCE;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashMapUpdate() {
		LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		map.put(0,0);
		map.put(1,1);
		map.put(2,2);
		map.put(3,3);
		map.put(4,4);
		map.put(5,5);
		map.put(6,6);
		map = updateHashMapAfterDelete2(map, 3);
		System.out.println("After:"+ map);
		assertEquals("{0=0, 1=1, 2=2, 3=4, 4=5, 5=6}", map.toString());
		map = updateHashMapAfterDelete2(map, 2);
		assertEquals("{0=0, 1=1, 2=4, 3=5, 4=6}", map.toString());
	}
	
	private LinkedHashMap<Integer, Integer> updateHashMapAfterDelete(LinkedHashMap<Integer, Integer> tasksMap, int fakeid) {
		LinkedHashMap<Integer, Integer> temp  = new LinkedHashMap<Integer, Integer>();

		Iterator<Entry<Integer, Integer>> it = tasksMap.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Integer, Integer> pair = (Entry<Integer, Integer>)it.next();
			if(pair.getKey() < fakeid) {
				temp.put(pair.getKey(), pair.getValue());
			} else {
				if(it.hasNext()) {
                    int key = pair.getKey();
                    Entry<Integer, Integer> next = (Entry<Integer, Integer>)it.next();
                    int value = next.getValue();
                    temp.put(key, value);
				}
			}
		}
	 return temp;
	}
	
	private LinkedHashMap<Integer, Integer> updateHashMapAfterDelete2(LinkedHashMap<Integer, Integer> tasksMap, int fakeid) {
		LinkedHashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
		for(int i = 0; i < tasksMap.size(); i++) {
			if(i < fakeid) {
				temp.put(i, tasksMap.get(i));
			} else {
				temp.put(i, tasksMap.get(i+1));
			}
		}
		temp.remove(tasksMap.size()-1);
		return temp;
	}
}
