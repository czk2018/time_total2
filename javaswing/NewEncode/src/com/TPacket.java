package com;

import java.util.ArrayList;
import java.util.List;

public class TPacket {
	//public byte[] TBytes = new byte[GlobalDefs.MAXPACKETSIZE];
	//public byte[] Data = new byte[GlobalDefs.MAXPACKETSIZE];
	
	public char[] TBytes = new char[GlobalDefs.MAXPACKETSIZE];
	public char[] Data = new char[GlobalDefs.MAXPACKETSIZE];
	
	public List<Integer> FieldPtrs = new ArrayList<Integer>();
	
	public static void main(String[] args) {
		TPacket p = new TPacket();
		p.Init("1234");
		System.out.println(p.GetTranCode() + ", " + p.Length());

		p.Append("abcd");
		p.Append("uiopk");
		p.Append("uiopk2");
		p.Append("uiopk3");
		p.Append("uiopk4");
		
		p.SetValue(2, "123456");
		
		StringBuffer Value = new StringBuffer();
		
		for (int i = 1; i < 10; i++) {
			Value.setLength(0);
			int len = p.GetValue(Value, i);
			System.out.println(i + "-> " + len + "," + Value.toString());
		}
	}
	
	// 用一个交易码初始化一个空包
	public void Init(final String TranCode) {
		int i = 0;
		
		for (i = 0; i < GlobalDefs.MAXPACKETSIZE; i++) {
			Data[i] = '0';
		}
		
		SetTranCode(TranCode);

		SetLen(6);
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
		return (int) (Data[5] * 256 + Data[4]);
	}
	
	public int Append(String Value) {
		return this.Append(Value, Value.length());
	}
	
	public int Append(String Value, int Len) {
		int j = Length();
		
		if ((j + 2 + Len > GlobalDefs.MAXPACKETSIZE)
				|| (Len < 0)
				|| (Len > GlobalDefs.MAXPACKETSIZE)) {
			return GlobalDefs.ERR_PACKET;
		}

		Data[j] = (char) (Len % 256);
		Data[j + 1] = (char) (Len / 256);
		
		FieldPtrs.add(j);
		
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
		
		int dataptr = FieldPtrs.get(Fieldno - 1);
		
		j = FieldLength(Fieldno);
		
		for (int k = 0; k < j; k++) {
			Value.append(Data[dataptr + 2 + k]);
		}
		
		return j;
	}
	
	public int SetValue(final int Fieldno, String Value) {
		return this.SetValue(Fieldno, Value, Value.length());
	}
	
	public int SetValue(final int Fieldno, String Value, int Len) {
		int j = Length();
		
		if (j > GlobalDefs.MAXPACKETSIZE || Fieldno < 1 || Fieldno > FieldPtrs.size()
				|| Len < 0 || Len > GlobalDefs.MAXPACKETSIZE) {
			return GlobalDefs.ERR_PACKET;
		}
		
		j = FieldLength(Fieldno);
		
		if (Length() + Len - j > GlobalDefs.MAXPACKETSIZE) {
			return GlobalDefs.ERR_PACKET;
		}
		
		int dataptr = FieldPtrs.get(Fieldno - 1);
		
		int i = Len - j;
		
		if (dataptr + 2 + j < Length() && Len != j) {
			for (int k = 0; k < Length() - (dataptr + 2 + j); k++) {
				if (Len > j) {
					Data[Length() + i - k - 1] = Data[Length() - k - 1];
				} else {
					Data[dataptr + 2 + Len + k - 1] = Data[dataptr + 2 + j + k - 1];
				}
			}
			
			for (int k = Fieldno + 1; k <= FieldPtrs.size(); k++) {
				FieldPtrs.set(k - 1, FieldPtrs.get(k - 1) + i);
			}
		}
		
		SetLen(Length() + Len - j);
		Data[dataptr] = (char) (Len % 256);
		Data[dataptr + 1] = (char) (Len / 256);
		
		for (i = 0; i < Len; i++) {
			Data[dataptr + 2 + i] = Value.charAt(i);
		}
		
		return Len;
	}
	
	public void SetLen(int Len) {
		Data[4] = (char) (Len % 256);
		Data[5] = (char) (Len / 256);
	}
	
	public String GetRetCode() {
		StringBuffer sb = new StringBuffer();
		this.GetValue(sb, 1);
		
		return sb.toString();
	}
	
	public int FieldLength(int Fieldno) {
		if (Fieldno < 1 || FieldPtrs.size() < Fieldno) {
			return -1;
		}

		int dataptr = FieldPtrs.get(Fieldno - 1);
		
		return (int) (Data[dataptr + 1] * 256 + Data[dataptr]);
	}
	
	public void InitFieldPtrs() {
		int l = Length();
		
		FieldPtrs.clear();
		
		if (l > GlobalDefs.MAXPACKETSIZE || l < 8) {
			return;
		}
		
	}
	
	public boolean IsGlobusFieldName(String str1) {
		boolean bRet = false;
		
		String str = str1.trim();
		if (str.indexOf(',') > -1) {
			return bRet;
		}
		
		int i = str.indexOf(':');
		if (i < 2) {
			return bRet;
		}
		
		str = str.substring(i + 1);
		
		i = str.indexOf(':');
		if (i < 1) {
			return bRet;
		}
		
		int l = str.length();
		if (i > l - 1) {
			return bRet;
		}
		
		for (int j = 0; j <= i - 2; j++) {
			if (str.charAt(j) < '0' || str.charAt(j) > '9') {
				return bRet;
			}
		}
		
		String str2 = str.substring(0,  i);
		if (Integer.parseInt(str2) <= 0) {
			return bRet;
		}
		
		for (int j = i + 1; j < l; j++) {
			if (str.charAt(j) < '0' || str.charAt(j) > '9') {
				return bRet;
			}
		}
		
		str2 = str.substring(i, l - 1);
		if (Integer.parseInt(str2) <= 0) {
			return bRet;
		}
		
		bRet = true;
		
		return bRet;
	}
	
	/*
   功能: 从Globus交易处理返回的字段名及数据列表数值串中
         根据Globus字段名和多值编号获取对应的返回数值

   参数：Value:         用于获取返回的字段数值
         TranFieldName: Globus 交易字段名称
         valstr：      字段名及数据列表串
         mvno:          指定Globus字段的多值序号，缺省1，单值字段恒定为1
         subno:         指定Globus字段的子序号，缺省1，单值字段恒定为1

   返回：返回的数值存入Value中， 所有类型的返回值两端均无任何引号
         函数返回值： >=0  成功取得数值的长度
             ERR_NODATA  未发现指定的 Globus字段名和多值编号
             ERR_PACKET  数据包格式错误  数据包中无指定的字段 PackFieldno
             var Value:string;
             const TranFieldName:string;
             const valstr:string;
             const mvno:integer=1;
             const subno:integer=1
	 */
	public int GetTFValByName(StringBuffer Value,
			String TranFieldName,
			String valstr,
			int mvno,
			int subno) {
		
		int k = 0;
		String fdname = "";
		Value.setLength(0);
		String pfldval = valstr;
		
		fdname = String.format("%s%s%d%s%d=", TranFieldName.trim(),
				":", mvno, ":", subno);
		
		int i = pfldval.indexOf(fdname);
		if (i <= -1) {
			return GlobalDefs.ERR_NODATA;
		}
		
		
		pfldval = pfldval.substring(i + fdname.length());
		
		while (pfldval.length() > 0) {
			int j = pfldval.indexOf(',');
			int l = j;
			
			if (j <= -1) {
				j = pfldval.length();
			}
			
			Value.append(pfldval.substring(0, j));
			
			pfldval = pfldval.substring(j + 1);//???
			
			k = pfldval.indexOf('=');
			if (k > -1) {
				fdname = pfldval.substring(0, k);
				if (IsGlobusFieldName(fdname)) {
					break;
				}
			}
			
			if (l != -1) {
				Value.append(',');
			}
		}
		
		return Value.length();
	}
	
	/*
	功能: 从Globus交易处理返回包中的（字段名及数据列表）字段中
         根据Globus字段名和多值编号获取对应的返回数值
         针对S等function 返回的数据，格式是：字段名:多值序号:子序号=数值

   	参数：Value:         用于获取返回的字段数值
         TranFieldName: Globus 交易字段名称
         PackFieldno：  （字段名及数据列表）字段在数据包中的序号，对3001返回包，缺省是4
         mvno:          指定Globus字段的多值序号，缺省1，单值字段恒定为1
         subno:         指定Globus字段的子序号，缺省1，单值字段恒定为1

   	返回：返回的数值存入Value中， 所有类型的返回值两端均无任何引号
         函数返回值： >=0  成功取得数值的长度
             ERR_NODATA  未发现指定的 Globus字段名和多值编号
             ERR_PACKET  数据包格式错误  数据包中无指定的字段 PackFieldno
             var Value:string;
             const TranFieldName:string;
             const PackFieldno:integer=4;
             const mvno:integer=1;
             const subno:integer=1
	 */
	public int GetTranFieldByName(StringBuffer Value,
			String TranFieldName,
			int PackFieldno,
			int mvno,
			int subno) {
		StringBuffer fldval = new StringBuffer();
		
		int ret = 0;
		if ((ret = GetValue(fldval, PackFieldno)) < 0) {
			return ret;
		}
		
		return GetTFValByName(Value, TranFieldName, fldval.toString(), mvno, subno);
	}
	
	/*
	功能: 从Globus交易处理返回的字段名及数据列表数值串中
         根据Globus字段名和多值编号获取对应的返回数值
         针对I等function 返回的数据，格式是：字段名=数值:多值序号:子序号

   	参数：Value:         用于获取返回的字段数值
         TranFieldName: Globus 交易字段名称
         valstr：      字段名及数据列表串
         mvno:          指定Globus字段的多值序号，缺省1，单值字段恒定为1
         subno:         指定Globus字段的子序号，缺省1，单值字段恒定为1

   	返回：返回的数值存入Value中， 所有类型的返回值两端均无任何引号
         函数返回值： >=0  成功取得数值的长度
             ERR_NODATA  未发现指定的 Globus字段名和多值编号
             ERR_PACKET  数据包格式错误  数据包中无指定的字段 PackFieldno
             
             var Value:string;
             const TranFieldName:string;
             const valstr:string;
             const mvno:integer=1;
             const subno:integer=1
	 */
	public int GetTFValByName1(StringBuffer Value,
			String TranFieldName,
			String valstr,
			int mvno,
			int subno) {
		int i,j;
		String fdname,pfldval;
		
		pfldval = "," + valstr;
		
		fdname = String.format(",%s=", TranFieldName.trim());
		
		i = pfldval.indexOf(fdname);
		if (i <= -1) {
			return GlobalDefs.ERR_NODATA;
		}
		
		pfldval = pfldval.substring(i + fdname.length());
		
		fdname = String.format(":%d:%d", mvno, subno);
		
		while (pfldval.length() > 0) {
			i = pfldval.indexOf(fdname);
			if (i <= -1) {
				Value.append(pfldval);
				return Value.length();
			}
			
			j = i + fdname.length();
			if (j >= pfldval.length() || pfldval.charAt(j) == ',') {
				Value.append(pfldval.substring(0, i));
				return Value.length();
			}
			
			Value.append(pfldval.substring(0, j - 1));
			pfldval.substring(j);
		}
		
		return Value.length();
	}
	
	/*
	功能: 从Globus交易处理返回包中的（字段名及数据列表）字段中
         根据Globus字段名和多值编号获取对应的返回数值
         针对I等function 返回的数据，格式是：字段名=数值:多值序号:子序号

   参数：Value:         用于获取返回的字段数值
         TranFieldName: Globus 交易字段名称
         PackFieldno：  （字段名及数据列表）字段在数据包中的序号，对3001返回包，缺省是4
         mvno:          指定Globus字段的多值序号，缺省1，单值字段恒定为1
         subno:         指定Globus字段的子序号，缺省1，单值字段恒定为1

   返回：返回的数值存入Value中， 所有类型的返回值两端均无任何引号
         函数返回值： >=0  成功取得数值的长度
             ERR_NODATA  未发现指定的 Globus字段名和多值编号
             ERR_PACKET  数据包格式错误  数据包中无指定的字段 PackFieldno
	 var Value:string;
	 const TranFieldName:string;
	 const PackFieldno:integer=4;
	 const mvno:integer=1;
	 const subno:integer=1
	 */
	public int GetTranFieldByName1(StringBuffer Value, String TranFieldName,
			int PackFieldno,
			int mvno,
			int subno) {
		StringBuffer fldval = new StringBuffer();
		
		int ret = 0;
		if ((ret = GetValue(fldval, PackFieldno)) < 0) {
			return ret;
		}
		
		return GetTFValByName1(Value, TranFieldName, fldval.toString(), mvno, subno);
	}
	
	//var Value:string;const sno:integer;const PackFieldno:integer=4
	public int GetEnqRecord(StringBuffer Value, final int sno, final int PackFieldno) {
		int i,j,k,l,qflag;
		StringBuffer tmpstr = new StringBuffer();
		StringBuffer pfldval = new StringBuffer();
		
		Value.setLength(0);
		
		int ret = 0;
		if (0 > (ret = this.GetValue(pfldval, PackFieldno))) {
			return ret;
		}
		if (ret == 0) {
			return GlobalDefs.ERR_NODATA;
		}
		
		l = ret;
		
		try {
			i = GetValue(tmpstr, 3);
			if (i <= 0 || sno <= 0 || sno > Integer.parseInt(tmpstr.toString())) {
				return GlobalDefs.ERR_NODATA;
			}
		} catch (Exception e) {
			return GlobalDefs.ERR_NODATA;
		}
		
		k = 1;
		j = 0;
		qflag = 0;
		while (k < sno && j < l) {
			if (pfldval.charAt(j) == '"') {
				qflag++;
				if (qflag == 2) {
					qflag = 0;
				}
			}
			
			if (qflag == 0 && pfldval.charAt(j) == ',') {
				k++;
			}
			
			j++;
		}
		
		if (k < sno || j > l) {
			return GlobalDefs.ERR_NODATA;
		}
		
		k = j;
		qflag = 0;
		
		while (k < l) {
			if (pfldval.charAt(j) == '"') {
				if (qflag == 2) {
					qflag = 0;
				}
			}
			
			if (qflag == 0 && pfldval.charAt(j) == ',') {
				break;
			}
			
			k++;
		}
		
		Value.append(str);
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
