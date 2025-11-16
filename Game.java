/* Game.java
 * Tron Lightcycle
 * Amadis Hali & Kevin Zhu
 * A game where bikes move on their own after pressing a move key, while drawing a trail of bike colour behind the bikes.
 * If a bike hits an enemy trail, hits its own trail, or goes outside the playable screen, the opposing bike gets a point.
 */



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
import java.awt.geom.*;
import java.util.*;



public class Game extends JFrame implements ActionListener{
	GamePanel game= new GamePanel();


    public Game() {
    	super("Tron Lightcycle");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,1000);
		add(game);

		javax.swing.Timer tm = new javax.swing.Timer(10, this);
		tm.start();
		setVisible(true);

    }
   	public void actionPerformed(ActionEvent evt){
		game.move();
		game.repaint();
		if(game.restart){ //call restart method
			game.restarter();
			}

	}
    public static void main(String[] arguments) {
		Game frame = new Game();

    }


}
class GamePanel extends JPanel implements KeyListener{
	private int ybikex,ybikey,bbikex,bbikey;//x and y coordinates of yellow and blue bikes
	private boolean []keys;//keyboard
	public static final int RIGHT = 1, LEFT = 2, UP = 3, DOWN = 4, START = 5;//keyboard direction values
	private int ydirection,bdirection,yscore,bscore;//direction of each bike & the score of points for each bike
	private Image yellowbikeR,yellowbikeL,yellowbikeU,yellowbikeD,bluebikeL,bluebikeR,bluebikeU,bluebikeD,background,menuscreen,endscreen;//all of the images
	private boolean ykeyconfirm=false;//boolean to determine if key is pressed
	private boolean bkeyconfirm=false;
	private ArrayList<Integer>ybikepoints=new ArrayList<Integer>();//point trail for each bike
	private ArrayList<Integer>bbikepoints=new ArrayList<Integer>();
	boolean restart=false;//variable to restart the game after a collision
	public String page="menu";//determining which page to show (menu or game, or endscreen)
	private String winner;







	public GamePanel(){

		yellowbikeR = new ImageIcon("yellowbikeRIGHT.png").getImage();
		yellowbikeR = yellowbikeR.getScaledInstance(70,30,Image.SCALE_SMOOTH);
		yellowbikeL = new ImageIcon("yellowbikeLEFT.png").getImage();
		yellowbikeL = yellowbikeL.getScaledInstance(70,30,Image.SCALE_SMOOTH);
		yellowbikeU = new ImageIcon("yellowbikeUP.png").getImage();
		yellowbikeU = yellowbikeU.getScaledInstance(30,70,Image.SCALE_SMOOTH);
		yellowbikeD = new ImageIcon("yellowbikeDOWN.png").getImage();
		yellowbikeD = yellowbikeD.getScaledInstance(30,70,Image.SCALE_SMOOTH);
		bluebikeL = new ImageIcon("bluebikeLEFT.png").getImage();
		bluebikeL = bluebikeL.getScaledInstance(70,30,Image.SCALE_SMOOTH);
		bluebikeR = new ImageIcon("bluebikeRIGHT.png").getImage();
		bluebikeR = bluebikeR.getScaledInstance(70,30,Image.SCALE_SMOOTH);
		bluebikeU = new ImageIcon("bluebikeUP.png").getImage();
		bluebikeU = bluebikeU.getScaledInstance(30,70,Image.SCALE_SMOOTH);
		bluebikeD = new ImageIcon("bluebikeDOWN.png").getImage();
		bluebikeD = bluebikeD.getScaledInstance(30,70,Image.SCALE_SMOOTH);
		background = new ImageIcon("backgroundgrid.png").getImage();
		background = background.getScaledInstance(1000,1000,Image.SCALE_SMOOTH);
		menuscreen= new ImageIcon("menu.jpg").getImage();
		menuscreen = menuscreen.getScaledInstance(1000,1000,Image.SCALE_SMOOTH);
		endscreen= new ImageIcon("endscreen.jpg").getImage();
		endscreen = endscreen.getScaledInstance(1000,1000,Image.SCALE_SMOOTH);



		keys = new boolean[KeyEvent.KEY_LAST+1];
		ybikex = 170; //yellow bike x and y
        ybikey = 400;
        bbikex = 720; //blue bike x and y
        bbikey = 400;

		addKeyListener(this);
		setFocusable(true);
		requestFocus();
	}

	public void move(){
		//turning the bikes
		if(keys[KeyEvent.VK_W] ){
			if(ydirection!=DOWN) {
				ydirection = UP;
				ykeyconfirm=true;
			}
		}
		else if(keys[KeyEvent.VK_S] ){
			if(ydirection!=UP) {
				ydirection = DOWN;
				ykeyconfirm=true;
			}
		}
		else if(keys[KeyEvent.VK_A] ){
			if(ydirection!=RIGHT) {
				ydirection = LEFT;
				ykeyconfirm=true;
			}
		}
		else if(keys[KeyEvent.VK_D] ){
			if(ydirection!=LEFT) {
				ydirection = RIGHT;
				ykeyconfirm=true;
			}
		}

		if(ydirection == UP){
			ybikey -= 5;

		}
		else if(ydirection == DOWN){
			ybikey += 5;
		}
		else if(ydirection == LEFT){
			ybikex -= 5;
		}
		else if(ydirection == RIGHT){
			ybikex += 5;
		}

		if(keys[KeyEvent.VK_UP] ){
			if(bdirection!=DOWN) { //only allowing to change perpendicularly, not parallel
				bdirection = UP;
				bkeyconfirm=true;

			}
		}
		else if(keys[KeyEvent.VK_DOWN] ){
			if(bdirection!=UP) {
				bdirection = DOWN;
				bkeyconfirm=true;

			}
		}
		else if(keys[KeyEvent.VK_LEFT] ){
			if(bdirection!=RIGHT) {
				bdirection = LEFT;
				bkeyconfirm=true;

			}
		}
		else if(keys[KeyEvent.VK_RIGHT] ){
			if(bdirection!=LEFT) {
				bdirection = RIGHT;
				bkeyconfirm=true;
			}
		}
		if(bdirection == UP){
			bbikey -= 5;
		}
		else if(bdirection == DOWN){
			bbikey += 5;
		}
		else if(bdirection == LEFT){
			bbikex -= 5;
		}
		else if(bdirection == RIGHT){
			bbikex += 5;
		}

		if(bbikepoints.size()>=2){  //blue colliding with self, making sure blue array has at least 1 set of coords
		 		for(int i=0; i<bbikepoints.size()-2; i+=2) { //goes up by 2s to check x and y properly
		 			if(bbikepoints.get(i) == bbikex && bbikepoints.get(i+1)== bbikey){ //if stored bluebike x = current bikex position and stored bluebike y (index i+1)=current bluebike y pos
		 				yscore+=1; //opposing bike gets a point
		 				restart=true; //reset to start next round
		 			}
		 		}
		 		for(int i=0; i<ybikepoints.size()-2; i+=2) { //blue collide into yellow path
		 			if(ybikepoints.get(i) == bbikex && ybikepoints.get(i+1)== bbikey){
		 				yscore+=1;
		 				restart=true;
		 			}
		 		}
		 	}

         	if(ybikepoints.size()>=2 && ybikepoints.size()%2==0){  //yellow colliding with self
		 		for(int i=0; i<ybikepoints.size()-2; i+=2) {
		 			if(ybikepoints.get(i) == ybikex && ybikepoints.get(i+1)== ybikey){
		 				bscore+=1;
		 				restart=true;


		 			}
		 		}
		 		for(int i=0; i<bbikepoints.size()-2; i+=2) { //yellow colliding into bluepath
		 			if(bbikepoints.get(i) == ybikex && bbikepoints.get(i+1)== ybikey){
		 				bscore+=1;
		 				restart=true;

		 			}
		 		}
		 	}
		 	if(bbikex<=0||bbikex>=1000||bbikey<=0||bbikey>=1000) { //to ensure bike within the 1000x1000 screen, otherwise opponent gets a point
		 		yscore+=1;
             	restart=true;
         	}
		 	if(ybikex<=0||ybikex>=1000||ybikey<=0||ybikey>=1000) {
		 		bscore+=1;
             	restart=true;
		 	}

		if(page=="menu"){
			if(keys[KeyEvent.VK_SPACE]){
				page="game";//opening game from menu
			}
		}
		if(page=="end"){
			if(keys[KeyEvent.VK_SPACE]){
				page="game";//play again after game end
			}
		}
	}

    
	public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {

        keys[e.getKeyCode()] = true;

    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;

    }

	public void restarter(){
		//restarting game after collision
		ybikex = 170; //yellow bike x and y
        ybikey = 400;
        bbikex = 720; //blue bike x and y
        bbikey = 400;
        ykeyconfirm=false;
        bkeyconfirm=false;
        ybikepoints.clear();
        bbikepoints.clear();
        ydirection=0; //so bikes dont move after restart until you press a move key again
        bdirection=0;
        restart=false;
		}


	@Override
	public void paintComponent(Graphics g){
		 Font Consolas=new Font("Consolas",Font.BOLD,25);//font constructor
		 g.setFont(Consolas);
		 if(page=="menu"){
		 	g.drawImage(menuscreen,0,0,this);
		 }
		 else if(page=="game"){
         	g.drawImage(background,0,0,this);

		 	if(ykeyconfirm==false){ //if w/a/s/d hasn't been pressed yet, default yellow bike image drawn(facing right, not moving until w/a/s/d is pressed so keyconfirm is true)
		 		g.drawImage(yellowbikeR,ybikex,ybikey,this);
		 	}
		 	if(bkeyconfirm==false){ //same but for blue bike
		 		g.drawImage(bluebikeL,bbikex,bbikey,this);
		 	}

		 	if(ykeyconfirm==true){
		 		g.setColor(Color.yellow);
		 		if(ybikepoints.size()>=2){//at least 2 to get at least 1 x and y value
		 			for(int i=0;i<ybikepoints.size();i+=2){ //by 2 to .get x and y in order
		 				g.fillRect(ybikepoints.get(i)+13,ybikepoints.get(i+1)+13,5,5); //13 to ensure trail rect is centered
		 			}
		 		}
		 	}
		 	if(bkeyconfirm==true){
		 		g.setColor(Color.cyan);
		 		if(bbikepoints.size()>=2){
		 			for(int i=0;i<bbikepoints.size();i+=2){
		 				g.fillRect(bbikepoints.get(i)+13,bbikepoints.get(i+1)+13,5,5);
		 			}
		 		}
		 	}
         	if(ydirection == RIGHT){
         		g.drawImage(yellowbikeR,ybikex,ybikey,this); //draw right-facing image of yellowbike
				ybikepoints.add(ybikex); //add x and y coords of yellow bike constantly while moving any directionto ybikepoints arrayList
				ybikepoints.add(ybikey);

         	}
         	else if(ydirection == LEFT){
         		g.drawImage(yellowbikeL,ybikex,ybikey,this);
				ybikepoints.add(ybikex);
				ybikepoints.add(ybikey);

         	}
         	else if(ydirection == UP){
         		g.drawImage(yellowbikeU,ybikex,ybikey,this);
				ybikepoints.add(ybikex);
				ybikepoints.add(ybikey);
         	}
         	else if(ydirection == DOWN){
         		g.drawImage(yellowbikeD,ybikex,ybikey,this);
 				ybikepoints.add(ybikex);
				ybikepoints.add(ybikey);
         	}
         	if(bdirection == LEFT){
         		g.drawImage(bluebikeL,bbikex,bbikey,this);
				bbikepoints.add(bbikex);
				bbikepoints.add(bbikey);
         	}
         	else if(bdirection == RIGHT){
         		g.drawImage(bluebikeR,bbikex,bbikey,this);
 				bbikepoints.add(bbikex);
				bbikepoints.add(bbikey);
         	}
         	else if(bdirection == UP){
         		g.drawImage(bluebikeU,bbikex,bbikey,this);
				bbikepoints.add(bbikex);
				bbikepoints.add(bbikey);
         	}
         	else if(bdirection == DOWN){
         		g.drawImage(bluebikeD,bbikex,bbikey,this);
				bbikepoints.add(bbikex);
				bbikepoints.add(bbikey);
         	}

         	g.setColor(Color.white); //for drawing text displaying the score
         	g.drawString("Yellow: "+yscore,140,25);
         	g.drawString("Blue: "+bscore,710,25);


         	if(yscore>=10||bscore>=10){ //first to reach listed points
         		if(yscore>=10){
         			winner="Yellow";
         		}
         		else if(bscore>=10){
         			winner="Blue";
         		}
		 		page="end";
		 		yscore=0;
		 		bscore=0;
		 	}
		 }
		 else if(page=="end"){
		 	g.drawImage(endscreen,0,0,this);
		 	g.setColor(Color.white);
			g.drawString(winner+" WINS!",435,25);
			g.drawString("Press SPACE to Play Again!",330,900);
		 }
    }
}