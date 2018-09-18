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
	
	// ��һ���������ʼ��һ���հ�
	public void Init(final String TranCode) {
		int i = 0;
		
		for (i = 0; i < GlobalDefs.MAXPACKETSIZE; i++) {
			Data[i] = '0';
		}
		
		SetTranCode(TranCode);

		//SetLen(6);
	}
	
	// ����һ�����Ľ�����: 4λ����
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
		return String.valueOf(Data[0])
				+ String.valueOf(Data[1])
				+ String.valueOf(Data[2])
				+ String.valueOf(Data[3]);
	}
	
	public int Length() {
		int high = Data[6] - '0';
		high = high * 256;
		int low = Data[5] - '0';
		return high + low;
	}
	/*
	public int Append(String Value, int Len) {
		int j = Length();
		
		if ((j + 2 + Len > GlobalDefs.MAXPACKETSIZE)
				|| (Len < 0)
				|| (Len > GlobalDefs.MAXPACKETSIZE)) {
			return GlobalDefs.ERR_PACKET;
		}
		
		char low = (char) (Len % 256) & 0xff;
		Data[j + 1] = (char) () + '0';
		Data[j + 2] = Len / 256;
	}*/
}
