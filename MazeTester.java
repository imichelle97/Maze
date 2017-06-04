package sjsu.javan.luong.cs146.project3;

import java.util.ArrayList;
import org.junit.Test;
import junit.framework.TestCase;


public class MazeTester extends TestCase {
	
	// Tests if our BFS search for the shortest path works
	@Test
	public void testBFSShortestPath(){
		Maze test = new Maze(4);
		test.makeMaze();
		int[] testArray = {0, 1, 4, 5, 8, 12, 13 ,14 , 15};
		ArrayList<Integer> expectedIndices = new ArrayList<Integer>();
		for (int i : testArray){expectedIndices.add(i);}
		test.BFS();
		test.fillShortestPathBFS(test.vertices[15]);
		ArrayList<Integer> actualResult = new ArrayList<Integer>();
		for (int i = 0; i < test.totalCells; i++){
			if(test.vertices[i].inShortPath){
				actualResult.add(i);
			}
		}
		assertEquals(expectedIndices,actualResult);
	}
	
	// Tests if our DFS search for the shortest path works
	@Test
	public void testDFSShortestPath(){
		Maze test = new Maze(4);
		test.makeMaze();
		int[] testArray = {0, 1, 4, 5, 8, 12, 13 ,14 , 15};
		ArrayList<Integer> expectedIndices = new ArrayList<Integer>();
		for (int i : testArray){expectedIndices.add(i);}
		test.DFS();
		test.fillShortestPathBFS(test.vertices[15]);
		ArrayList<Integer> actualResult = new ArrayList<Integer>();
		for (int i = 0; i < test.totalCells; i++){
			if(test.vertices[i].inShortPath){
				actualResult.add(i);
			}
		}
		assertEquals(expectedIndices,actualResult);
	}
	
	// Tests time for BFS of a very large maze
	@Test
	public void testSpeed(){
		Maze test = new Maze(320);
		test.makeMaze();
		test.BFS();
	}
	
}