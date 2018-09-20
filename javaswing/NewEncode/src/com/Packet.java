package com;

import java.util.ArrayList;
import java.util.List;

public class Packet {
	//public byte[] TBytes = new byte[GlobalDefs.MAXPACKETSIZE];
	//public byte[] Data = new byte[GlobalDefs.MAXPACKETSIZE];
	
	public char[] TBytes = new char[GlobalDefs.MAXPACKETSIZE];
	public char[] Data = new char[GlobalDefs.MAXPACKETSIZE];
	
	public List<Integer> FieldPtrs = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		Packet p = new Packet();
		p.SetTranCode("1234");
		System.out.println(p.GetTranCode());
		
		int num3 = 254;
		char ch3 = (char) (num3 + 48);
		System.out.println("ch3 = " + ch3); 
	}
	
	// 用一个交易码初始化一个空包
	public void Init(final String TranCode) {
		int i = 0;
		
		for (i = 0; i < GlobalDefs.MAXPACKETSIZE; i++) {
			Data[i] = '0';
		}
		
		SetTranCode(TranCode);

		//SetLen(6);
	}
	
	// 设置一个包的交易码: 4位数字
	public void SetTranCode(final String TranCode) {
		int i = 0;
		String TranCode1 = "";
		
		for (i = 0; i < TranCode.length(); i++) {
			if (i < 4) {
				TranCode1 += TranCode.charAt(i);
			}
		}
		
		for (i = 0; i < 4; i++) {
			Data[i] = TranCode1.charAt(i);
		}
	}
	
	public String GetTranCode() {
		return Data[0] + "" + Data[1] + "" + Data[2] + "" + Data[3];
	}
	
	public int Length() {
		return (int) (Data[6] * 256 + Data[5]);
	}
	
	public int Append(String Value, int Len) {
		int j = Length();
		
		if ((j + 2 + Len > GlobalDefs.MAXPACKETSIZE)
				|| (Len < 0)
				|| (Len > GlobalDefs.MAXPACKETSIZE)) {
			return GlobalDefs.ERR_PACKET;
		}

		int i_mod = Len % 256;
		int i_div = Len / 256;
		
		Data[j + 1] = (char) (i_mod);
		Data[j + 2] = (char) (i_div);
		
		FieldPtrs.add(j + 1);
		
		for (int i = 0; i < Len; i++) {
			Data[j + 2 + i] = Value.charAt(i);
		}
		
		SetLen(j + Len + 2);
		
		return Len;
	}
	
	public int GetValue(StringBuffer Value, final int Fieldno) {
		if (Fieldno > FieldPtrs.size()) {
			return 0;
		}
		
		int j = Length();
		
		if (j > GlobalDefs.MAXPACKETSIZE || Fieldno < 1) {
			return GlobalDefs.ERR_PACKET;
		}
		
		int pos = FieldPtrs.get(Fieldno - 1);
		
		j = FieldLength(Fieldno);
		
		for (int k = 0; k < j; k++) {
			Value.append(Data[pos + 1 + k]);
		}
		
		return j;
	}
	
	public void SetLen(int Len) {
		Data[5] = (char) (Len % 256);
		Data[6] = (char) (Len / 256);
	}
	
	public int FieldLength(int Fieldno) {
		if (Fieldno < 1 || FieldPtrs.size() < Fieldno) {
			return -1;
		}
		
		return (int) (Data[FieldPtrs.get(Fieldno-1)+1]*256 + Data[FieldPtrs.get(Fieldno-1)]);
	}
	

	public String strTo16(String s) {

	    String str = "";

	    for (int i = 0; i < s.length(); i++) {

	        int ch = (int) s.charAt(i);

	        String s4 = Integer.toHexString(ch);

	        str = str + s4;

	    }

	    return str;

	}
}
