package sjsu.javan.luong.cs146.project3;

// The goal of this project is to generate random, perfect mazes, and solve the maze and find its shortest path by using 
// Depth-First Searches (DFS) and Breadth-First Searches (BFS).  Each time the program runs, it will print out the maze and 
// the solutions to the maze.  

import java.util.LinkedList;
import java.util.Random;

public class Maze {
	Cell[] vertices;					
	int totalCells;
	LinkedList<Integer>[] adjList;
	int n;
	int visitCount= 0;
	
	public Maze(int n) {
		totalCells = n*n;
		vertices = new Cell[totalCells];
		this.n = n;
		adjList = (LinkedList<Integer>[]) new LinkedList[totalCells];
		
		for (int i = 0; i < totalCells; i++) {
			adjList[i] = new LinkedList<Integer>();
			vertices[i] = new Cell();
		}
		
		//Set position in the maze
		for(int i = 0; i < n; i++) 			//ROW
			for(int j = 0; j < n; j++) {	//COL
				vertices[n*i + j].setPosition(i,j);
			}
	}
	
	// Resets inShortPath to false
	public void reset() {
		for(Cell c : vertices) {
			c.inShortPath = false;
		}
	}	
	
	// makeMaze() method is to randomly generate perfect mazes based on the condition, r.  
	public void makeMaze() {
		Cell[] cellStack = new Cell[totalCells];
		int stackPointer = n*n;
		int visitedCells = 1;
		Cell currentCell = vertices[0];
		currentCell.beenVisited = true;
		Random generator = new Random();
		
		while(visitedCells < totalCells) {
			//Pick a random number, r, between 0 and 3			
			int r = generator.nextInt(4);
			
			//For r = 0, look at this current cell and its north wall.  If the north wall is untouched, knock it down
			if((r == 0) && (currentCell.row > 0) && (!vertices[(currentCell.row - 1) * n + currentCell.col].beenVisited)) {
				currentCell.northWall = false;
				adjList[(currentCell.row * n) + currentCell.col].addFirst(((currentCell.row - 1)*n) + currentCell.col);//Add north neighbor to adjList
				adjList[((currentCell.row - 1) * n) + currentCell.col].addFirst(((currentCell.row)*n) + currentCell.col);//Add current cell to north neighbors adj list
				stackPointer--;
				cellStack[stackPointer] = currentCell;
				currentCell = vertices[(currentCell.row - 1) * n + currentCell.col];
				currentCell.southWall = false;
				currentCell.beenVisited = true;
				visitedCells++;
			}
			
			//For r = 1, look at this current cell and its south wall.
			else if((r == 1) && (currentCell.row < n-1) && (!vertices[(currentCell.row + 1) * n + currentCell.col].beenVisited)) {
				currentCell.southWall = false;
				adjList[(currentCell.row * n) + currentCell.col].addFirst(((currentCell.row + 1)*n) + currentCell.col);
				adjList[((currentCell.row + 1) * n) + currentCell.col].addFirst(((currentCell.row)*n) + currentCell.col);
				stackPointer--;
				cellStack[stackPointer] = currentCell;
				currentCell = vertices[(currentCell.row + 1) * n + currentCell.col];
				currentCell.northWall = false;
				currentCell.beenVisited = true;
				visitedCells++;
			}
			
			//For r = 2, look at this current cell and its west wall.
			else if((r == 2) && (currentCell.col > 0) && (!vertices[currentCell.row * n + (currentCell.col - 1)].beenVisited)) {
				currentCell.westWall = false;
				adjList[(currentCell.row * n) + currentCell.col].addFirst(((currentCell.row)*n) + currentCell.col - 1);
				adjList[(currentCell.row * n) + currentCell.col - 1].addFirst(((currentCell.row)*n) + currentCell.col);
				stackPointer--;
				cellStack[stackPointer] = currentCell;
				currentCell = vertices[currentCell.row * n + (currentCell.col - 1)];
				currentCell.eastWall = false;
				currentCell.beenVisited = true;
				visitedCells++;
			}
			
			//For r = 3, look at this current cell and its east wall.
			else if((r == 3) && (currentCell.col < n-1) && (!vertices[currentCell.row * n + (currentCell.col + 1)].beenVisited)) {
				currentCell.eastWall = false;
				adjList[(currentCell.row * n) + currentCell.col].addFirst(((currentCell.row)*n) + currentCell.col + 1);
				adjList[(currentCell.row * n) + currentCell.col + 1].addFirst(((currentCell.row)*n) + currentCell.col);
				stackPointer--;
				cellStack[stackPointer] = currentCell;
				currentCell = vertices[currentCell.row * n + (currentCell.col + 1)];
				currentCell.westWall = false;
				currentCell.beenVisited = true;
				visitedCells++;
			}
			
			//Else, pop the stack, make it the current cell
			else {
				if((stackPointer < n*n) &&
				   ((currentCell.row == 0) ||
				    (currentCell.row > 0) && 
				    (vertices[(currentCell.row - 1) * n + currentCell.col].beenVisited)) &&
				   ((currentCell.row == (n-1)) ||
				    (currentCell.row < n-1) && 
				    (vertices[(currentCell.row + 1) * n + currentCell.col].beenVisited)) &&
				   ((currentCell.col == 0) || 
				    (currentCell.col > 0) && 
				    (vertices[currentCell.row * n + (currentCell.col - 1)].beenVisited)) &&
				   ((currentCell.col == (n-1)) || 
				    (currentCell.col < n-1) && 
				    (vertices[currentCell.row * n + (currentCell.col + 1)].beenVisited)))   
				
				{
					currentCell = cellStack[stackPointer];
					stackPointer++;
				}
			}
			vertices[0].northWall = false;
			vertices[(n*n) - 1].southWall = false;
		}
	}
	// Breadth-First Search, always starting at top left
	public void BFS() {
		for (int i = 1; i < totalCells; i++) {
			vertices[i].color = "white";
			vertices[i].distance = Integer.MAX_VALUE;
			vertices[i].parent = null;//Parent = nil
		}
		vertices[0].color = "gray";
		vertices[0].distance = 0;
		vertices[0].parent = null;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.addFirst(0);
		int visitCount = 0;
		outer:
		while(!queue.isEmpty()) {
			int sourceCell = queue.getLast();
			for (int i : adjList[queue.removeLast()]) {
				if(vertices[i].color.equals("white")) {
					vertices[i].color = "gray";
					vertices[i].distance = vertices[sourceCell].distance + 1;
					vertices[i].parent = vertices[sourceCell];
					queue.addFirst(i);
					vertices[i].visitOrder = ++visitCount;
				}
				if(i == (n*n)-1) {
					break outer;
				}
			}
			vertices[sourceCell].color = "black";
		}
	}
	
	// Depth-First Search 
	public void DFS() {
		for (Cell i : vertices) {
			i.color = "white";
			i.parent = null;
			i.visitOrder = 0;
		}
		int time = 0;
		for (int i = 0; i < totalCells; i++) {
			if(vertices[i].color.equals("white")) {
				DFSVisit(i, time);
			}
		}
	}
	
	public void DFSVisit(int v, int theTime) {
		theTime++;
		vertices[v].visitOrder = visitCount++;
		vertices[v].distance = theTime;
		vertices[v].color = "gray";
		for (int i : adjList[v]){
			if (vertices[i].color.equals("white")) {
				vertices[i].parent = vertices[v];
				DFSVisit(i,theTime);
			}
		}
		vertices[v].color = "black";
		theTime++;
		vertices[v].finish = theTime;
	}
	
	// Finds the shortest path
	public void fillShortestPathBFS(Cell nextCell) {
		//Pass in target cell to start recursion
		if(nextCell == null) {
			return;
		}
		nextCell.inShortPath = true;
		fillShortestPathBFS(nextCell.parent);
	}
	
	//PRINTS MAZE
	public void printMaze() {
		for(int i = 0; i < (2*n) + 1; i++) {
			//Even row, make + and -
			if (i % 2 == 0) {
				for(int j = 0; j < (2*n) + 1; j++) {
					if (j % 2 == 0) {
						System.out.print('+');
					}
					else {
						if(i == 0 || i == 2*n) {    //If on the border just print
							if(j == 1 && i == 0) {  //Make entrance in top left
								System.out.print(' ');
							}
							else if(i == 2*n && j == 2*n - 1) {  //Make exit in bottom right
								System.out.print(' ');
							}
							else {
								System.out.print('-');
							}
							
						}
						else if(vertices[((i/2)*n) + (j/2)].northWall) {
							System.out.print('-');
						}
						else {
							System.out.print(' ');
						}
					}
				}
			}
			else {
				for(int j = 0; j < (2*n) + 1; j++) {
					//Even col, make |
					if (j % 2 == 0) {
						if(j == 0 || j == 2*n) {  //If on the border just print
							System.out.print('|');
						}
						else if(vertices[((i/2)*n) + (j/2)].westWall) {
							System.out.print('|');
						}
						else {
							System.out.print(' ');
						}
					}
					else{
							System.out.print(' ');
						}
				}
			}	
				System.out.println("");
		}
	}
	
	// PRINTS BFS MAZE
	public void printMazeBFS() {
		for(int i = 0; i < (2*n) + 1; i++) {
			//Even row, make + and -
			if (i % 2 == 0) {
				for(int j = 0; j < (2*n) + 1; j++) {
					if (j % 2 == 0) {
						System.out.print('+');
					}
					else{
						if(i == 0 || i == 2*n) {   //If on the border just print
							if(j == 1 && i == 0) { //Make entrance in top left
								System.out.print(' ');
							}
							else if(i == 2*n && j == 2*n - 1) {  //Make exit in bottom right
								System.out.print(' ');
							}
							else {
								System.out.print('-');
							}
							
						}
						else if(vertices[((i/2)*n) + (j/2)].northWall) {
							System.out.print('-');
						}
						else {
							System.out.print(' ');
						}
					}
				}
			}
			else {
				for(int j = 0; j < (2*n) + 1; j++) {
					//Even col, make |
					if (j % 2 == 0) {
						if(j == 0 || j == 2*n) {  //If on the border just print
							System.out.print('|');
						}
						else if(vertices[((i/2)*n) + (j/2)].westWall) {
							System.out.print('|');
						}
						else {
							System.out.print(' ');
						}
					}
					else {
							if(!((vertices[((i/2)*n) + (j/2)].visitOrder) == 0)) {
								System.out.print((vertices[((i/2)*n) + (j/2)].visitOrder)%10);
							}
							else {
								System.out.print(' ');
							}
						}
				}
			}	
				System.out.println("");
		}
	}
	
	// PRINTS DFS MAZE
	public void printMazeDFS() {
		for(int i = 0; i < (2*n) + 1; i++) {
			//Even row, make + and -
			if (i % 2 == 0) {
				for(int j = 0; j < (2*n) + 1; j++) {
					if (j % 2 == 0) {
						System.out.print('+');
					}
					else {
						if(i == 0 || i == 2*n) {   //If on the border just print
							if(j == 1 && i == 0) { //Make entrance in top left
								System.out.print(' ');
							}
							else if(i == 2*n && j == 2*n - 1) {  //Make exit in bottom right
								System.out.print(' ');
							}
							else {
								System.out.print('-');
							}
							
						}
						else if(vertices[((i/2)*n) + (j/2)].northWall) {
							System.out.print('-');
						}
						else {
							System.out.print(' ');
						}
					}
				}
			}
			else{
				for(int j = 0; j < (2*n) + 1; j++) {
					//Even col, make |
					if (j % 2 == 0) {
						if(j == 0 || j == 2*n) { //If on the border just print
						
							System.out.print('|');
						}
						else if(vertices[((i/2)*n) + (j/2)].westWall) {
							System.out.print('|');
						}
						else {
							System.out.print(' ');
						}
					}
					else {
							if(!((vertices[((i/2)*n) + (j/2)].visitOrder) == 0)) {
							System.out.print((vertices[((i/2)*n) + (j/2)].visitOrder)%10);
							}
							else {
								System.out.print(' ');
							}
						}
				}
			}	
				System.out.println("");
		}
	}
	
	// PRINTS SHORTEST PATH
	public void printMazeShortestPath() {
		for(int i = 0; i < (2*n) + 1; i++) {
			//Even row, make + and -
			if (i % 2 == 0) {
				for(int j = 0; j < (2*n) + 1; j++) {
					if (j % 2 == 0){
						System.out.print('+');
					}
					else {
						if(i == 0 || i == 2*n) {   //If on the border just print
							if(j == 1 && i == 0) { //Make entrance in top left
								System.out.print(' ');
							}
							else if(i == 2*n && j == 2*n - 1) {  //Make exit in bottom right
								System.out.print(' ');
							} 
							else {
								System.out.print('-');
							}
							
						}
						else if(vertices[((i/2)*n) + (j/2)].northWall) {
							System.out.print('-');
						}
						else {
							System.out.print(' ');
						}
					}
				}
			}
			else {
				for(int j = 0; j < (2*n) + 1; j++) {
					//Even col, make |
					if (j % 2 == 0){
						if(j == 0 || j == 2*n) {  //If on the border just print
							System.out.print('|');
						}
						else if(vertices[((i/2)*n) + (j/2)].westWall) {
							System.out.print('|');
						}
						else {
							System.out.print(' ');
						}
					}
					else{
							if((vertices[((i/2)*n) + (j/2)].inShortPath)) {
							System.out.print('#');
							}
							else {
								System.out.print(' ');
							}
						}
				}
			}	
				System.out.println("");
		}
	}	
	
	public static void main(String[] args) {
		Maze test = new Maze(4);
		test.makeMaze();
		System.out.println("MAZE:");
		test.printMaze();
		System.out.println("");
		test.BFS();
		System.out.println("BFS:");
		test.printMazeBFS();
		System.out.println("");
		test.fillShortestPathBFS(test.vertices[test.totalCells - 1]);
		System.out.println("BFS SHORTEST PATH:");
		test.printMazeShortestPath();
		System.out.println("");
		test.DFS();
		System.out.println("DFS:");
		test.printMazeDFS();
		System.out.println("");
		test.reset();
		test.fillShortestPathBFS(test.vertices[test.totalCells - 1]);
		System.out.println("DFS SHORTEST PATH:");
		test.printMazeShortestPath();
	}
}