package test;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.List;

import logic.CommandFactory;
import logic.Command;
import core.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommandFactoryTest {

	private CommandFactory cf = CommandFactory.INSTANCE;
	private static final DateTimeFormatter dateTimeDisplay = DateTimeFormat
			.forPattern("dd/MM/YY HH:mm");

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
	public void test() {
		LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		map.put(0, 0);
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);
		map.put(5, 5);
		map.put(6, 6);
	}

	@Test
	public void testHashMapUpdate() {
		LinkedHashMap<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		map.put(0, 0);
		map.put(1, 1);
		map.put(2, 2);
		map.put(3, 3);
		map.put(4, 4);
		map.put(5, 5);
		map.put(6, 6);
		map = updateHashMapAfterDelete2(map, 3);
		System.out.println("After:" + map);
		assertEquals("{0=0, 1=1, 2=2, 3=4, 4=5, 5=6}", map.toString());
		map = updateHashMapAfterDelete2(map, 2);
		assertEquals("{0=0, 1=1, 2=4, 3=5, 4=6}", map.toString());
	}

	@Test
	/*Method to test the adding of tasks including undo/redo operation of add*/
	public void test1() {
		Command testCommand = new Command(
				"add testing task on 14/06/14 from 2pm to 3pm");
		cf.testAdd(testCommand);
		List<Task> testList = cf.getTasks();
		assertEquals("testing task", testList.get(testList.size() - 1)
				.getTaskDescription());
		assertEquals("14/06/14 14:00", dateTimeDisplay.print(testList.get(
				testList.size() - 1).getTaskStartTime()));
		assertEquals("14/06/14 15:00", dateTimeDisplay.print(testList.get(
				testList.size() - 1).getTaskEndTime()));
	}

	@Test
	/*Method to test the editing of tasks including undo/redo operation of edit*/
	public void test2() {
		int lastIndex = cf.getLastIndex();
		int displayID = cf.testGetDisplayId(lastIndex);
		String edit = ("edit " + displayID + " edited task on 12/06/14 from 1pm to 5pm");
		Command testEditCommand = new Command(edit);
		cf.testEdit(testEditCommand);
		List<Task> testList = cf.getTasks();
		assertEquals("edited task", testList.get(testList.size() - 1)
				.getTaskDescription());
		assertEquals("12/06/14 13:00", dateTimeDisplay.print(testList.get(
				testList.size() - 1).getTaskStartTime()));
		assertEquals("12/06/14 17:00", dateTimeDisplay.print(testList.get(
				testList.size() - 1).getTaskEndTime()));
		//editing task from timed to deadline
		String editTimeToDeadline = ("edit " + displayID + " deadline task on 12/06/14 by 9pm");
		Command testEditTimedToDeadlineCommand = new Command(editTimeToDeadline);
		cf.testEdit(testEditTimedToDeadlineCommand);
		List<Task> testETD = cf.getTasks();
		assertEquals("deadline task", testETD.get(testETD.size() - 1)
				.getTaskDescription());
		assertEquals(dateTimeDisplay.print(DateTime.now()), dateTimeDisplay.print(testETD.get(
				testETD.size() - 1).getTaskStartTime()));
		assertEquals("12/06/14 21:00", dateTimeDisplay.print(testETD.get(
				testETD.size() - 1).getTaskEndTime()));
		//editing task from deadline to floating
		String editDeadlineToFloating = ("edit " + displayID + " Non-timed");
		Command testDeadlineToFloatingCommand = new Command(editDeadlineToFloating);
		cf.testEdit(testDeadlineToFloatingCommand);
		List<Task> testDTF = cf.getTasks();
		assertEquals("Non-timed", testDTF.get(testDTF.size() - 1)
				.getTaskDescription());
		assertEquals(dateTimeDisplay.print(DateTime.now()), dateTimeDisplay.print(testDTF.get(
				testDTF.size() - 1).getTaskStartTime()));
		assertEquals(dateTimeDisplay.print(DateTime.now()), dateTimeDisplay.print(testDTF.get(
				testDTF.size() - 1).getTaskEndTime()));
		//editing task from floating to timed
		String editFloatingToTimed = ("edit " + displayID + " timed task on 12/06/14 from 2pm to 5pm");
		Command testFloatingToTimedCommand = new Command(editFloatingToTimed);
		cf.testEdit(testFloatingToTimedCommand);
		List<Task> testFTT = cf.getTasks();
		assertEquals("timed task", testDTF.get(testFTT.size() - 1)
				.getTaskDescription());
		assertEquals("12/06/14 14:00", dateTimeDisplay.print(testFTT.get(
				testFTT.size() - 1).getTaskStartTime()));
		assertEquals("12/06/14 17:00", dateTimeDisplay.print(testFTT.get(
				testFTT.size() - 1).getTaskEndTime()));
		// Testing undo edit
		cf.testUndo();
		List<Task> undoList = cf.getTasks();
		assertEquals("Non-timed", undoList.get(undoList.size() - 1)
				.getTaskDescription());
		assertEquals(dateTimeDisplay.print(DateTime.now()), dateTimeDisplay.print(undoList.get(
				undoList.size() - 1).getTaskStartTime()));
		assertEquals(dateTimeDisplay.print(DateTime.now()), dateTimeDisplay.print(undoList.get(
				undoList.size() - 1).getTaskEndTime()));
		// Testing redo edit
		cf.testRedo();
		List<Task> redoList = cf.getTasks();
		assertEquals("timed task", testList.get(redoList.size() - 1)
				.getTaskDescription());
		assertEquals("12/06/14 14:00", dateTimeDisplay.print(redoList.get(
				redoList.size() - 1).getTaskStartTime()));
		assertEquals("12/06/14 17:00", dateTimeDisplay.print(redoList.get(
				redoList.size() - 1).getTaskEndTime()));

	}
	
	@Test
	/*Method to test the marking of task as done including undo/redo operation of done*/
	public void test3() {
	int lastIndex = cf.getLastIndex();
	System.out.println("done test last index:" + lastIndex);
	int displayID = cf.testGetDisplayId(lastIndex);
    String done = ("done " + displayID);
	Command testDoneCommand = new Command(done);
	cf.testDone(testDoneCommand);
	List<Task> testList = cf.getTasks();
	assertEquals(true, testList.get(lastIndex).getTaskDone());
	// Testing of undo done
	cf.testUndo();
	List<Task> undoList = cf.getTasks();
	assertEquals(false, undoList.get(lastIndex).getTaskDone());
	// Test redo done
	cf.testRedo();
	List<Task> redoList = cf.getTasks();
	assertEquals(true, redoList.get(lastIndex).getTaskDone());
	// Test undone
	String undone = ("undone " + 1);
	Command testUndoneCommand = new Command(undone);
	cf.testUndone(testUndoneCommand);
	List<Task> undoneList = cf.getTasks();
	assertEquals(false, undoneList.get(lastIndex).getTaskDone());
	}

	@Test
	/*Method to test the deletion of task including undo/redo operation of delete*/
	public void test4() {
		int lastIndex = cf.getLastIndex();
		int displayID = cf.testGetDisplayId(lastIndex);
	    String delete = ("delete " + displayID);
		Command testDeleteCommand = new Command(delete);
		cf.testDelete(testDeleteCommand);
		List<Task> testList = cf.getTasks();
		assertEquals(true, testList.get(lastIndex).getMarkAsDelete());
		// Testing of undo delete
		cf.testUndo();
		List<Task> undoList = cf.getTasks();
		assertEquals(false, undoList.get(lastIndex).getMarkAsDelete());
		// Test redo
		cf.testRedo();
	    List<Task> redoList = cf.getTasks();
		assertEquals(true, redoList.get(lastIndex).getMarkAsDelete());
		cf.clearUndoRedoAfterTesting();	
	}

	
	

	private LinkedHashMap<Integer, Integer> updateHashMapAfterDelete(
			LinkedHashMap<Integer, Integer> tasksMap, int fakeid) {
		LinkedHashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();

		Iterator<Entry<Integer, Integer>> it = tasksMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, Integer> pair = (Entry<Integer, Integer>) it.next();
			if (pair.getKey() < fakeid) {
				temp.put(pair.getKey(), pair.getValue());
			} else {
				if (it.hasNext()) {
					int key = pair.getKey();
					Entry<Integer, Integer> next = (Entry<Integer, Integer>) it
							.next();
					int value = next.getValue();
					temp.put(key, value);
				}
			}
		}
		return temp;
	}

	private LinkedHashMap<Integer, Integer> updateHashMapAfterDelete2(
			LinkedHashMap<Integer, Integer> tasksMap, int fakeid) {
		LinkedHashMap<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
		for (int i = 0; i < tasksMap.size(); i++) {
			if (i < fakeid) {
				temp.put(i, tasksMap.get(i));
			} else {
				temp.put(i, tasksMap.get(i + 1));
			}
		}
		temp.remove(tasksMap.size() - 1);
		return temp;
	}

}
