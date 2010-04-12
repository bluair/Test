package game;

public class Utils {

	public static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i + offset] & 0x000000FF) << shift;
		}
		return value;
	}

	public static int byteArrayToInt(byte[] b) {
		return byteArrayToInt(b, 0);
	}
	
	
	public static String pl2unicode(String s) {
        s = s.replace((char)0xb1,'\u0105');//a,
        s = s.replace((char)0xe6,'\u0107');//c'
        s = s.replace((char)0xea,'\u0119');//e,
        s = s.replace((char)0xb3,'\u0142');//l
        s = s.replace((char)0xf1,'\u0144');//n'
        s = s.replace((char)0xf3,'\u00F3');//o'
        s = s.replace((char)0xb6,'\u015B');//s'
        s = s.replace((char)0xbf,'\u017C');//z.
        s = s.replace((char)0xbc,'\u017A');//z'
        s = s.replace((char)0xa1,'\u0104');//A,
        s = s.replace((char)0xc6,'\u0106');//C'
        s = s.replace((char)0xca,'\u0118');//E,
        s = s.replace((char)0xa3,'\u0141');//L
        s = s.replace((char)0xd1,'\u0143');//N'
        s = s.replace((char)0xd3,'\u00D3');//O'
        s = s.replace((char)0xa6,'\u015A');//S'
        s = s.replace((char)0xaf,'\u017B');//Z.
        s = s.replace((char)0xac,'\u0179');//Z'
        return s;
    } 

//	public static float power(float number, int power) {
//		float ret = 1;
//
//		for (int i = 0; i < power; i++) {
//			ret = ret * number;
//		}
//
//		return ret;
//	}
}
