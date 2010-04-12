package game.elements.resized;

import game.Images;
import game.elements.Road;

public class Finish extends Brick2{

	public Finish(Road road, short maxVisibleSequences) {
		super(road, maxVisibleSequences);
		this.images = Images.imgsFinish;
	}

}
