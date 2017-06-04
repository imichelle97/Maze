package sjsu.javan.luong.cs146.project3;

public class Cell {
	boolean northWall;
	boolean eastWall;
	boolean southWall;
	boolean westWall;
	boolean beenVisited;
	int row;
	int col;
	String color;
	int distance;
	int finish;
	boolean inShortPath = false;
	Cell parent;
	int visitOrder;
	
	public Cell() {
		// All walls are intact 
		beenVisited = false;
		northWall = true;
		eastWall = true;
		southWall = true;
		westWall = true;
		row = 0;
		col = 0;		
	}	
	
	public void setPosition(int r, int c) {
		row = r;
		col = c;
	}
}