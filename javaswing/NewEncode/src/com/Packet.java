package com;

import java.util.ArrayList;
import java.util.List;

public class Packet {
	public byte[] TBytes = new byte[GlobalDefs.MAXPACKETSIZE];
	public byte[] Data = new byte[GlobalDefs.MAXPACKETSIZE];
	
	public List<Integer> FieldPtrs = new ArrayList<Integer>();
	
	// ��һ���������ʼ��һ���հ�
	public void Init(final String TranCode) {
		int i = 0;
		
		for (i = 0; i < GlobalDefs.MAXPACKETSIZE; i++) {
			Data[i] = 0;
		}
		
		SetTranCode(TranCode);

		SetLen(6);
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
		
		for (i = 1; i <= 4; i++) {
			Data[i] = Byte.(TranCode1.charAt(i));
		}
	}
}
