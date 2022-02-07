package src;

import java.util.ArrayList;

/*
 * Created By - HIMANSHU MISHRA
 * Date - 7 Feb 2022
 * Description - 2048 game created using processing library of JAVA.
 */

import processing.core.*;

public class UserInterface extends PApplet{
	int[][] board = new int[4][4];

	//len,pad and bs variable are used to create the board , score and dead are used to play
	// game and to check if the game is over respectively
	int pad = 20, bs = 100, len = pad*(board.length+1)+bs*board.length,score = 0, dead = 1;
	
	public static void main(String[] args) {
        
		String[] processingArgs = {"MySketch"};
		
		UserInterface mySketch = new UserInterface();
		PApplet.runSketch(processingArgs, mySketch);
		
    }
	public void settings() {
		size(len,len);
	}
	public void setup() {
		restart();
		textFont(createFont("Courier", 40));
	}
	public void draw() {
		background(255);
		noStroke();
		  
		  //this function loops and creates 16 cells in our rectangular box
		for (int j = 0 ; j < board.length; j++) 
		  for (int i = 0 ; i < board[j].length; i++) {
		      fill(200);
		      rect(pad+(pad+bs)*i, pad+(pad+bs)*j, bs, bs, 5);
		    }
		  
		  //created a function which put text on the board and give different colors to different
		  // numbers that are on the board
		  //text function helps to write into the board
		  for (int j = 0 ; j < board.length; j++) 
		    for (int i = 0 ; i < board[j].length; i++) {
		      float x = pad+(pad+bs)*i; 
		      float y=pad+(pad+bs)*j;
		      if (board[j][i] > 0) {
		        float p = log(board[j][i])/log(2);
		        rectangle(x, y, bs, bs, 5, color(255-p*255/11, p*255/11, 0));
		        textt(""+board[j][i], x, y + 22, bs, bs, color(0), 40, CENTER);
		      }
		    }
		  textt("score: "+score,10,5,100,50,color(0),10, LEFT);
		  //this is used to check if game is over or not
		  if(dead>0) { 
		    rectangle(0,0,width,height,0,color(255,100)); 
		    textt("Gameover! Click to restart", 0,height/2,width,50,color(0),30,CENTER); 
		    if(mousePressed) restart(); 
		  }
		
	}
	
	//this method is used to reset the variables each time the user starts playing again
	void restart() {
		  board = new int[4][4];
		  spawn();
		  score = dead = 0;
	}
	
	//this is used to find empty spaces in th borad
	void spawn() {
		//xs stores the empty x positions and ys stores the empty y positions
		ArrayList<Integer> xs = new ArrayList<Integer>();
		ArrayList<Integer> ys = new ArrayList<Integer>();
		//find empty position in the board
		for (int j = 0 ; j < board.length; j++) {
			for (int i = 0 ; i < board[j].length; i++) {
				if (board[j][i]==0) {
				xs.add(i);
				ys.add(j);
				}
			}
		}
		
		  //get a random numbers
		 int rnd = (int)random(0, xs.size());
		 int y = ys.get(rnd); 
		 int x = xs.get(rnd);
		 //so in empty space we put a 2 or a for.I have used th logic that 80 percent of time 
		 // we would put a 2 on the floor
		 board[y][x] = random(0, 1) < 0.8 ? 2 : 4;
	}
	
	//this is used to create a rectangular cells in position x and y of width w and h and radius r and color c
	void rectangle(float x, float y, float w, float h, float r, int c) { 
		fill(c); 
		rect(x,y,w,h,r);  
	}
	
	//this function is used to fill up the rectangular board with the text
	void textt(String t, float x, float y, float w, float h, int c, float s, int align) {
	  fill(c); 
	  textAlign(align); 
	  textSize(s); 
	  text(t,x,y,w,h);  
	 }
	
	
	//This function checks for the keypress done either up down left or right
	//this is a inbuilt function of processing.io
	public void keyPressed() {
	  if (dead == 0) {
	    int dy=keyCode==UP ? -1 : (keyCode==DOWN ? 1 : 0), dx=keyCode==LEFT ? -1 : (keyCode==RIGHT ? 1 : 0);
	    //whenever a new input is pressed than this function is calls the game function and starts to play
	    int[][] newb =game(dy, dx, true);
	    if (newb != null) {
	      board = newb;
	      spawn();
	    }
	    if (gameover()) dead = 1;
	  }
	}

//this checks if the game is over or not 
	//it checks if any empty space is left or not
	boolean gameover() {
		  //dx is right = 1left  =-1
		  //dy is down =1up =-1
		  int[] dx = {1, -1, 0, 0}, dy = {0, 0, 1, -1};
		  boolean out = true;
		  for (int i = 0 ; i < 4; i++) if (game(dy[i], dx[i], false) != null) out = false;
		  return out;
		}

//this function is used to return a new board each time we play
//here dy is used to see if we move up or down and dx is used to see if we move left or right
	int[][] game(int dy, int dx, boolean updatescore) {
	
	      //this creates a copy of the board int temp board
		  int[][] tempBoard = new int[4][4];
		  for (int j = 0 ; j < 4; j++) 
			  for (int i = 0 ; i < 4; i++) 
				  tempBoard[j][i] = board[j][i];
		  boolean moved = false; 
		  if (dx != 0 || dy != 0) {
		    int d =  dx != 0 ? dx : dy;
		    //here we move the spaces based on d
		    for (int perp = 0; perp < board.length; perp++) 
		    	// if d>0 than we move to the end and start shifting from there or if
		    	// d<0 than we start from 1st index and start shifting 
		      for (int tang = (d > 0 ? board.length - 2 : 1); tang != (d > 0 ? -1 : board.length); tang-=d) {
		      //now we find what to shift perpendicular or tangential
		      int y = dx != 0 ? perp : tang;
		      int x = dx != 0 ? tang : perp;
		      //target element we want to shif is in ty and tx
		      int ty = y;
		      int tx = x;
		      if (tempBoard[y][x]==0) continue;
		      //this loop actually does the shifting
		      for (int i=(dx != 0 ? x : y)+d; i!= (d > 0 ? board.length : -1); i+=d) {
		        int r = dx != 0 ? y : i, c = dx != 0 ? i : x;
		        if (tempBoard[r][c] != 0 && tempBoard[r][c] != tempBoard[y][x]) break;
		        if (dx != 0) tx = i; 
		        else ty = i;
		      }
		      if ( (dx != 0 && tx == x) || (dy != 0 && ty == y)) continue;
		      
		      else if (tempBoard[ty][tx]==tempBoard[y][x]) {
		        tempBoard[ty][tx] *= 2;
		        if(updatescore) score += tempBoard[ty][tx];
		        moved = true;
		      }
		      
		      else if ( (dx != 0 && tx != x) || (dy != 0 && ty != y)) {
		        tempBoard[ty][tx] = tempBoard[y][x];
		        moved = true;
		      }
		      if (moved) tempBoard[y][x] = 0;
		    }
		  }
		  //if no movement was done than the game is over that is we need to return null
		  //other wise we return the temporary Board
		  return moved ? tempBoard : null;
}
}
