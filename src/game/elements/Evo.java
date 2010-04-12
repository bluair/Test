package game.elements;

import game.Images;
import game.Rand;
import game.elements.resized.ResizingElement;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class Evo extends Sprite {

	private static final long MOVE_TIME = 37;
	private static final long FIRST_MOVE = 10;
	private static final int KMH100_BRAKE_TIME = 800;
	private static final int KMH1_SPEEDUP_TIME = 10;
	private static final int MAX_SPEED = 250;
	public static final int YELLOW = 0;
	public static final int BLUE = 1;
	public int currentVibra = 0;
	Image[] images;
	Level level;

	long lastLeftWheelTime = 0;
	long lastRightWheelTime = 0;
	long lastLeftWheelTimeNonZero = 0;
	long lastRightWheelTimeNonZero = 0;
	long lastBrakeStart = 0;
	int brakeStartSpeed = 0;
	long lastSpeedUp;
	int speed = 0;
	int brake = 0;
	private boolean paused = false;
	int currImg;
	private int collisionsSlow = 0;
	private int collisionsMove = 0;
	private int collisionsRandom = 0;
	private long collisionsSlowLast = 0;
	private long collisionsMoveLast = 0;
	private Road road;
	// private short x, y;
	protected short roadMaxLeft, roadMaxRight, roadCenter;
	protected int lastPosition;
	private EvoResized evoOpponent;
	ResizingElement lastCollision = null;
	ResizingElement currCollision = null;
	int retMult = 1;
	private boolean collisionLockedWheel = false;
	private int startX;
	private int startY;
	int color;
	private long endLockedEvoCollision = 0;
	private int movePixToEat;
	public byte bump;

	// private boolean dontMoveEvo;

	public Evo(Road road, int color, boolean x) {
		super(Images.pixelImage);
		this.road = road;
		this.color = color;
		int minusMaxMove;
		if (Images.porting == Images.BIG || Images.porting == Images.MIDDLE) {
			minusMaxMove = 45;
			roadMaxLeft = (short) (road.x - road.startOffsetX - road.getWidth() / 2 + minusMaxMove);
			roadMaxRight = (short) (road.x - road.startOffsetX + road.getWidth() / 2 - minusMaxMove);
		} else {

			minusMaxMove = 85;
			roadMaxLeft = (short) (road.x - road.startOffsetX - road.getWidth() / 2 + minusMaxMove);
			roadMaxRight = (short) (road.x - road.startOffsetX + road.getWidth() / 2 - minusMaxMove);
			System.out.println("rrr " + roadMaxRight);
		}
		roadCenter = (short) ((roadMaxLeft + roadMaxRight) / 2);
		currImg = -1; // need for first img change

		// changeImage();
		currImg = 0;
		speed = 0;
	}

	public Evo(Road road, int color) {
		super(Images.pixelImage);

		this.road = road;

		switch (color) {
		case BLUE:
			images = Images.imgsEvoBlue;
			break;
		case YELLOW:
			images = Images.imgsEvoYellow;
			break;
		default:
			break;

		}
		this.color = color;

		int minusMaxMove;
		if (Images.porting == Images.BIG || Images.porting == Images.MIDDLE) {
			minusMaxMove = 45;
			roadMaxLeft = (short) (road.x - road.startOffsetX - road.getWidth() / 2 + minusMaxMove);
			roadMaxRight = (short) (road.x - road.startOffsetX + road.getWidth() / 2 - minusMaxMove);
		} else {
			minusMaxMove = 25;
			roadMaxLeft = (short) (road.x - road.startOffsetX - road.getWidth() / 2 + minusMaxMove);
			roadMaxRight = (short) (road.x - road.startOffsetX + road.getWidth() / 2 - minusMaxMove);
			System.out.println("rrr " + roadMaxRight);
		}

		roadCenter = (short) ((roadMaxLeft + roadMaxRight) / 2);
		currImg = -1; // need for first img change

		changeImage();
		currImg = 0;
		speed = 0;
	}

	public void setOpponent(EvoResized evoOpponent) {
		this.evoOpponent = evoOpponent;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public byte getCurrentImageIndex() {
		return (byte) currImg;
	}

	public void brake(int downKeeped) {

		if (downKeeped > 0) {
			if (brake == 0) {
				brake = downKeeped;
				lastBrakeStart = System.currentTimeMillis();
				changeImage();
				brakeStartSpeed = speed;
				lastSpeedUp = 0;
			}
		} else if (brake > 0) {
			lastBrakeStart = 0;
			brake = 0;
			changeImage();
		}

	}

	public short wheel(int leftKeeped, int rightKeeped) {
		long move = 0;

		checkCollisions();
		int ap = applyCollisions();
		if (ap != 0) {
			collisionLockedWheel = true;
			if (ap > 0) {
				lastLeftWheelTime = System.currentTimeMillis();
				leftKeeped = 1;
			} else {
				lastRightWheelTime = System.currentTimeMillis();
				rightKeeped = 1;
			}
			// System.out.println("mowe " + ap);
			move = ap;
		} else {
			collisionLockedWheel = false;
		}

		if (leftKeeped > 0) {
			lastRightWheelTime = 0;
			if (lastLeftWheelTime == 0) {
				lastLeftWheelTime = System.currentTimeMillis();
				move += -FIRST_MOVE;
			} else {
				move += -(System.currentTimeMillis() - lastLeftWheelTime) / Evo.MOVE_TIME;
			}

		} else {
			lastLeftWheelTime = 0;
			if (rightKeeped > 0) {
				if (lastRightWheelTime == 0) {
					lastRightWheelTime = System.currentTimeMillis();
					move += FIRST_MOVE;
				} else {
					move += (System.currentTimeMillis() - lastRightWheelTime) / Evo.MOVE_TIME;
				}
			} else {
				lastRightWheelTime = 0;

			}
		}

		long newMove = checkEvoCollisions((int) move);
		if (newMove != move) {
			lastRightWheelTime = 0;
			lastLeftWheelTime = 0;
			move = newMove;
		}

		if (checkIfMovePossible((int) -move)) {
			move = 0;
			lastRightWheelTime = 0;
			lastLeftWheelTime = 0;
			// System.out.println("possible ");
		} else {
			// System.out.println("possible");
		}

		changeImage();

		return (short) -move;
	}

	boolean checkIfMovePossible(int move) {
		// System.out.println("roda "+road.x+" "+roadMaxRight);
		return (road.x + move > roadMaxRight) || (road.x + move < roadMaxLeft);
	}

	private void changeImage() {
		int SECOND_STAGE_TURN = 250;
		int TURN_ROUND_TIME = 111;

		long curr = System.currentTimeMillis();
		if (lastLeftWheelTime > 0)
			lastLeftWheelTimeNonZero = curr;
		if (lastRightWheelTime > 0)
			lastRightWheelTimeNonZero = curr;

		// if (lockedCollision == false) {
		int newImg = 0;
		if (brake > 0) {
			if (lastRightWheelTime > 0 && collisionLockedWheel == false) {
				newImg = 5;
				if (System.currentTimeMillis() - lastRightWheelTime > SECOND_STAGE_TURN) {
					newImg = 9;
				}
			} else {
				if (lastLeftWheelTime > 0 && collisionLockedWheel == false) {
					newImg = 4;
					if (System.currentTimeMillis() - lastLeftWheelTime > SECOND_STAGE_TURN) {
						newImg = 8;
					}
				} else {
					// System.out.println("last " + lastLeftWheelTimeNonZero);
					long per1 = System.currentTimeMillis() - lastLeftWheelTimeNonZero;
					long per2 = System.currentTimeMillis() - lastRightWheelTimeNonZero;
					boolean r = false;
					if (per1 > per2) {
						r = true;
						per1 = per2;
					}

					if (road.x > roadCenter + 40) {
						newImg = 5;
						if (r == false) {
							if (per1 < 2 * TURN_ROUND_TIME) {
								if (per1 < TURN_ROUND_TIME)
									newImg = 4;
								else
									newImg = 1;
							}
						}
					} else {
						if (road.x < roadCenter - 40) {
							newImg = 4;
							if (r == true) {
								if (per1 < 2 * TURN_ROUND_TIME) {
									if (per1 < TURN_ROUND_TIME)
										newImg = 5;
									else
										newImg = 1;
								}

							}
						} else {
							newImg = 1;
							if (per1 < TURN_ROUND_TIME) {
								if (r == true) {
									newImg = 5;
								} else {
									newImg = 4;
								}

							}
						}
					}

				}
			}
		} else {
			if (lastRightWheelTime > 0 && collisionLockedWheel == false) {
				newImg = 3;
				if (System.currentTimeMillis() - lastRightWheelTime > SECOND_STAGE_TURN) {
					newImg = 7;
				}
			} else {
				if (lastLeftWheelTime > 0 && collisionLockedWheel == false) {
					newImg = 2;
					if (System.currentTimeMillis() - lastLeftWheelTime > SECOND_STAGE_TURN) {
						newImg = 6;
					}
				} else {
					// System.out.println("last " + lastLeftWheelTimeNonZero);
					long per1 = System.currentTimeMillis() - lastLeftWheelTimeNonZero;
					long per2 = System.currentTimeMillis() - lastRightWheelTimeNonZero;
					boolean r = false;
					if (per1 > per2) {
						r = true;
						per1 = per2;
					}

					if (road.x > roadCenter + 40) {
						newImg = 3;
						if (r == false) {
							if (per1 < 2 * TURN_ROUND_TIME) {
								if (per1 < TURN_ROUND_TIME)
									newImg = 2;
								else
									newImg = 0;
							}

						}
					} else {
						if (road.x < roadCenter - 40) {
							newImg = 2;
							if (r == true) {
								if (per1 < 2 * TURN_ROUND_TIME) {
									if (per1 < TURN_ROUND_TIME)
										newImg = 3;
									else
										newImg = 0;
								}

							}
						} else {
							// newImg = 0;
							if (per1 < TURN_ROUND_TIME) {
								if (r == true) {
									newImg = 3;
								} else {
									newImg = 2;
								}

							}

						}
					}

				}
			}
		}
		// System.out.println("niu img" + newImg);
		if (newImg != currImg) {
			setImage(images[newImg], images[newImg].getWidth(), images[newImg].getHeight());
			currImg = newImg;
		}
		// }
	}

	public void changeSpeed() {
		if (paused == false) {
			// if (checkCollisions()) {
			// applyCollisions();
			// } else {
			if (lastBrakeStart > 0) {
				if (speed > 0) {
					speed = (int) Math.max(0, (long) (brakeStartSpeed)
							- (100 * (System.currentTimeMillis() - lastBrakeStart)) / Evo.KMH100_BRAKE_TIME);
				}

			} else {

				if (lastSpeedUp == 0) {
					lastSpeedUp = System.currentTimeMillis();
				} else {
					long now = System.currentTimeMillis();
					int sUp = (int) (now - lastSpeedUp - speed / 3) / Evo.KMH1_SPEEDUP_TIME;
					if (sUp > 0) {
						lastSpeedUp = now;
						speed = Math.min(Evo.MAX_SPEED, speed + sUp);
					}
				}
			}
		}
		// } else {

		// }
	}

	public void pauseSpeed() {
		lastSpeedUp = 0;
		lastLeftWheelTime = 0;
		lastRightWheelTime = 0;
		lastBrakeStart = 0;
	}

	private int applyCollisions() {
		int ret = 0;

		// System.out.println("collisions slow " + collisionsSlow);
		if (collisionsSlow > 0) {
			if (collisionsSlowLast == 0) {
				collisionsSlowLast = System.currentTimeMillis();
			} else {
				if (lastSpeedUp > 0) {
					lastSpeedUp = 0;

				}
				int diff = (int) -(System.currentTimeMillis() - collisionsSlowLast) * collisionsSlow / 500;
				if (diff != 0) {
					speedUpDown(diff);
					collisionsSlowLast = System.currentTimeMillis();
					collisionsSlow = Math.max(0, collisionsSlow + diff);
				}
			}
		} else {
			collisionsSlowLast = 0;
		}

		if (collisionsMove != 0) {
			if (collisionsMoveLast == 0) {
				collisionsMoveLast = System.currentTimeMillis();
				if (collisionsRandom > 0) {
					retMult = (Rand.getRandFrom1(2));
					if (retMult > 1) {
						retMult = -1;
					} else {
						retMult = 1;
					}
				}
			} else {
				if (lastSpeedUp > 0) {
					lastSpeedUp = 0;
				}
				int diff = (int) -(System.currentTimeMillis() - collisionsMoveLast) / 100;
				if (diff != 0) {
					collisionsMoveLast = System.currentTimeMillis();
					if (collisionsMove > 0)
						collisionsMove--;
					else
						collisionsMove++;

					ret += collisionsMove;
				}

			}
			ret *= retMult;
		} else {
			collisionsMoveLast = 0;
			collisionsMove = 0;
		}

		if (collisionsRandom > 0) {

			// int move = (Rand.getRandFrom1(2));
			// if (move > 1) {
			// ret = -ret;
			// }
			collisionsRandom = 0;
		}

		return ret;
	}

	private void speedUpDown(int s) {
		speed = Math.max(speed + s, 5);

	}

	private boolean checkCollisions() {

		checkRoadCollisions();
		checkBricksCollisions();
		return (collisionsSlow > 0 || collisionsMove != 0 || collisionsRandom > 0);
	}

	private void checkRoadCollisions() {
		// if (speed > 80) {
		// System.out.println();
		if (Images.porting == Images.BIG || Images.porting == Images.MIDDLE) {
			if (roadCenter < road.x - 85 || roadCenter > road.x + 85) {
				collisionsSlow = Math.min(10, collisionsSlow + speed / 30);
			}
		} else {
			if (roadCenter < road.x - 65 || roadCenter > road.x + 65) {
				collisionsSlow = Math.min(10, collisionsSlow + speed / 30);
			}
		}

		// }
	}

	public void checkBricksCollisions() {

		ResizingElement res = level.getCurrentCollisionElement(4, this);
		currCollision = res;
		if (res != null) {

			collisionsMove = res.getCollisionMove(speed);
			int wi1 = res.getWidth() / 2;
			int wi2 = this.getWidth() / 2;
			int collDegree = res.getX() + wi1 - (this.getX() + wi2);
			int mn = 1;
			if (collDegree < 0)
				mn = -1;
			collDegree = wi1 + wi2 - Math.abs(collDegree);
			if (mn < 0) {
				collDegree = -collDegree;
			}

			collisionsSlow = res.getCollisionSlow(speed);

			if (collDegree != 0) {
				collisionsMove = (collisionsMove * collDegree) / (wi1 + wi2);
				collisionsSlow = (collisionsSlow * collDegree) / (wi1 + wi2);
			}

			int deg = (collDegree * 100) / (wi1 + wi2);
			if (deg > 60 && collisionsSlow > 0) {
				currentVibra = 2 * deg;
			}

			int back = res.getCollisionMoveBack(speed);
			if (back > 0) {
				road.setCollisionBack(back);
				collisionsSlow = speed;
				currentVibra = 500;
			}

			collisionsRandom = res.getCollisionRandom(speed);

		} else {

		}
	}

	public long checkEvoCollisions(int m) {
		int newMove = m;
		int move = m;
		if (evoOpponent != null) {
			move(move, 0);
			int movere = -move;
			if (Math.abs(evoOpponent.sequenceDifference) < 6) {
				if (this.collidesWith(evoOpponent, true)) {
					newMove = 0;

					if (move > 0) {
						for (int i = 1; i < move + 40; i++) {
							move(-1, 0);

							movere++;
							// System.out.println("1 "+ move);
							if (!this.collidesWith(evoOpponent, true)) {
								// System.out.println("2");
								newMove = move - i;
								break;
							}
						}
					} else {
						for (int i = 1; i < Math.abs(move) + 40; i++) {
							move(1, 0);
							movere--;
							// System.out.println("3 "+ move);
							if (!this.collidesWith(evoOpponent, true)) {
								// System.out.println("4");
								newMove = move + i;
								break;
							}
						}
					}
					currentVibra = 100;
				}
			}
			if (movere != 0)
				move(movere, 0);
		}

		long cur = System.currentTimeMillis();
		bump = 0;
		if (m != newMove) {
			endLockedEvoCollision = cur + Math.max(50, 2 * speed);
			movePixToEat = Math.max(5, speed / 10);
			bump = (byte) (movePixToEat / 2);
			if (m > newMove)
				bump = (byte) -bump;

		} else {
			if (endLockedEvoCollision > cur) {
				if (getX() > evoOpponent.getX()) {

					newMove++;
				} else {
					newMove--;
				}
			}
		}

		// System.out.println("mm " + m + " " + newMove);
		return newMove;
	}

	public void startBump(int b) {
		long cur = System.currentTimeMillis();
		endLockedEvoCollision = cur + 20 * speed;
		movePixToEat = Math.max(5, speed / 10);
	}

	public int getSpeed() {
		changeSpeed();
		return speed;
		// return 10;
	}

	public void setPosition(int x, int y) {
		int newX = x - getWidth() / 2;
		super.setPosition(newX, y);
	}

	public void speedup(int upKeeped) {

	}

	public void reset() {
		lastSpeedUp = 0;
		lastLeftWheelTime = 0;
		lastRightWheelTime = 0;
		lastBrakeStart = 0;
		brakeStartSpeed = 0;
		currImg = 0;
		brake = 0;
		currImg = -1; // need for first img change
		changeImage();
		speed = 0;
		setPosition(startX, startY);
	}

	public void setStartPosition(int x, int y) {
		startX = x;
		startY = y;
		setPosition(x, y);
	}

	public byte getCurrentImageIndexToSend() {
		byte ret = getCurrentImageIndex();
		if (ret > 5)
			ret = (byte) (ret - 4);
		return ret;
	}
}