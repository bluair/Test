package game.elements.resized;

import javax.microedition.lcdui.Graphics;

public interface ResizingElementInterface {

	public void paintResized(Graphics g, short x);
	
	public int getCollisionMaxSequences();
	
	public int getCollisionMove(int speed);

	public int getCollisionSlow(int speed);
	
	public int getCollisionRandom(int speed);


}
