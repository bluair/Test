package game.elements.resized;

import game.Images;
import game.elements.Road;

public class Lamp extends Tree7{
	

	public static final int LAMP_LEFT = 0;
	public static final int LAMP_RIGHT = 1;
	private int type;

	public Lamp(Road road, short maxVisibleSequences,int type) {
		super(road, maxVisibleSequences,type);
		if (type == LAMP_RIGHT)
			images = Images.imgsLampR;
		//else
			//images = Images.imgsLampR;
		
		this.type = type;
	}

	public void computePaintY() {
		super.computePaintY();
		// x = road.getEdgeLeft(Math.max(0, (y - 1) - roadY);
		switch (type) {
		case LAMP_LEFT:
			x = road.getX() - ((y - road.getY()) * 10) / 10;
			break;
		case LAMP_RIGHT:
			x = road.getX() + ((y - road.getY()) * 20) / 10;
			break;

		}

	}
}
