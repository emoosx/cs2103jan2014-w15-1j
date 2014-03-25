package test;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommandFactoryTest {

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
		System.out.println("Before:"+ map);
		map = updateHashMapAfterDelete(map, 3);
		System.out.println("After:"+ map);
		fail("to be");
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

}
