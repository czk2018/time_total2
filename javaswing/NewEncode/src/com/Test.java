package com;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "669";
		
		Integer i = Integer.parseInt(str);
		
		Integer i_mod = i % 256;
		Integer i_div = i / 256;

		String str_mod = Integer.toHexString(i_mod).toUpperCase();
		String str_div = Integer.toHexString(i_div).toUpperCase();
		if (str_div.length() == 1)
		{
			str_div = "0" + str_div;
		}
		
		int value = Integer.parseInt(str_mod, 16);
		
		System.out.println(value + " " + str_mod + " " + str_div);
		
		System.out.println(strTo16("123N8"));
		
		char c6 = 0x02;
		char c5 = 0x9D;
		
		char c = (char) (c6 * 256 + c5);
		System.out.println((int) c);
		
		js();
	}
	
	public static void js() {

		int len = 669;
		
		char i_mod = (char) (len % 256);
		char i_div = (char) (len / 256);
		
		short ss = (short) (i_div * 256 + i_mod);
		
		System.out.println(ss);
	}

	public static String strTo16(String s) {

	    String str = "";

	    for (int i = 0; i < s.length(); i++) {

	        int ch = (int) s.charAt(i);

	        String s4 = Integer.toHexString(ch);

	        str = str + s4;

	    }

	    return str;

	}
}
