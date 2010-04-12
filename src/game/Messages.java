package game;

public class Messages {

	public static final int MESSAGE_PLAY_PUSHED_INT = 9;

	public static final byte[] MESSAGE_LINE1 = new String("1").getBytes();
	public static final byte[] MESSAGE_LINE2 = new String("2").getBytes();
	public static final byte[] MESSAGE_LINE3 = new String("3").getBytes();
	public static final byte[] MESSAGE_LINE4 = new String("4").getBytes();

	public static final byte[] MESSAGE_PLAY_PUSHED = new Integer(MESSAGE_PLAY_PUSHED_INT).toString()
			.getBytes();

	public static final byte[] MESSAGE_GAME_END_FROM_LOOSER = new String("0").getBytes();
	public static final int MESSAGE_GAME_END_FROM_LOOSER_INT = 0;
	public static final Integer MESSAGE_CODE1 = new Integer(1);
	public static final Integer MESSAGE_CODE_END_GAME = new Integer(2);
	public static final Integer MESSAGE_CODE_PLAY_PUSHED = new Integer(3);
	public static final Integer MESSAGE_CODE_WALL_OF_FAME = new Integer(4);;
	public static final Integer MESSAGE_CODE_LEVEL = new Integer(5);
	public static final Integer MESSAGE_CODE_NEW_GAME = new Integer(6);
	public static final Integer MESSAGE_CODE_BUMP = new Integer(7);

}
