package com;

import java.util.ArrayList;
import java.util.List;

public class GlobalDefs {
	// 缺省参数
	public String DSERVERIP = "127.0.0.1";
	public int DSERVERPORT = 12380;

	public int DWAITTIME = 300000;   // 5 分钟
	public int DREADTIMEOUT = 10;     // 超时一次, 10分钟
	public int DWRITETIMEOUT = 10;    // 10分钟
	public int DTRANSFERSIZE = 32000; // 每次网络传输包的最大字节数

	public static int MAXPACKETSIZE = 32766;  // APPC.DLL 限制， 包大小不能超过0x7fff
	public int MAXFIELDLEN = 32000;

	// 交易包返回码
	// 通用
	public String TRANOK = "0000";
	public String TRANSYSERROR  = "9001";
	public String TRANDATAERROR = "9003";
	public String TRANNETWORK   = "9005";
	public String TRANINVALIDIC = "9006";
	public String TRANVERIFYSIG = "9007";
	public String TRANDECRYPT   = "9008";
	public String TRANPACKERROR = "9009";
	public String TRANCODEERROR = "9010";
	public String TRANENCTYPE   = "9011";
	public String TRANTERMINATE = "9012";
	public String TRANOTHERERROR= "9999";

	// 内部错误码
	// 需终止线程的错误，-1 ~ -30
	public static int ERR_WTIMEOUT  = -1;
	public static int ERR_TERMINATE = -2;
	public static int ERR_PACKET    = -3;
	public static int ERR_NETWORK   = -4;
	public static int ERR_SOCKRECV  = -5;
	public static int ERR_SOCKSEND  = -6;
	public static int ERR_CONNECT  = -10;
	public static int ERR_RTIMEOUT  = -11;
	public static int ERR_DISCONNECT  = -29;

	// 可继续处理请求的
	public int ERR_CODEERROR = -91;
	public int ERR_NODATA = -94;
	public int ERR_DATAERROR = -95;
	public int ERR_NOTALLOW = -96;
	public int ERR_RECTOOLONG = -97;
	public int ERR_PASSWORD = -98;
	public int ERR_BREAK  = -99;

	public int ERR_DATETIME = -85;
	public int ERR_RMTERROR  = -86;

	public int ERR_OTHERERROR  = -99;

	// TeleCode: array[1..10000] of string; // chinese 2bytes chars
	public String[] TeleCode = new String[10001];
	// TeleCodeExt: array[1..10000] of string; // 20080520
	public String[] TeleCodeExt = new String[10001];

	// HKHONS_CreditAC: TStringList; // 账户数组 格式：HKD2094030021001
	public List<String> HKHONS_CreditAC = new ArrayList<String>();
	// USD2094030032001
	// HKGLOBUS_DebitAC: TStringList; // 借方账户数组 格式：HKDHKD144140001
	public List<String> HKGLOBUS_DebitAC = new ArrayList<String>();
	// Offshore_AC: TStringList; // 20131122
	public List<String> Offshore_AC = new ArrayList<String>();
	// FTU_CrAC: TStringList; // 20171019
	public List<String> FTU_CrAC = new ArrayList<String>();
	// HKRTGS_CreditAC: TStringList; // 贷方账户数组 格式：HKD18775
	public List<String> HKRTGS_CreditAC = new ArrayList<String>();
	// HKOT_CreditAC: TStringList; // 汇款贷方账户数组
	// 格式：EUR20028327,USD20028432,JPY20028319,GBP20028335,AUD20028343
	public List<String> HKOT_CreditAC = new ArrayList<String>();
	// HKAIO_CreditAC: TStringList; // 两地贷方账户数组 格式：HKD20990007
	public List<String> HKAIO_CreditAC = new ArrayList<String>();
	// Globus_RTGSCommission: TStringList; // 汇款收费表
	// 格式：003HKD40,###HKD180,004USD5,###USD25
	public List<String> Globus_RTGSCommission = new ArrayList<String>();

	// XYP 2006-02-08: 增加字符串转换功能， 根据转换表的内容转换，
	  //ConvTab1: array[1..500] of string;    // string convert table , from
	public String[] ConvTab1 = new String[501];
	  //ConvTab2: array[1..500] of string;    // string convert table , to
	public String[] ConvTab2 = new String[501];

	// 系统参数变量
	public String GOMSERVERIP;// : string;
	public int GOMSERVERPORT;// : integer;
	public String GLOBUSUSERID;// : string;
	public String GLOBUSUSERPWD;// : string;
	public int LOGLINES;// : integer; // 事件窗口保留日志的最大行数
	public String LogDIR;// : string; // log directory
	public int STARTTIME, CLOSETIME;// : integer; // 允许服务器运行的时间范围，否则会自动关闭退出

	public int WAITTIME;// : integer; // 一次超时的时间 (300秒,毫秒为单位)
	public int READTIMEOUT;// : integer; // 读超时次数, 2分钟
	public int WRITETIMEOUT;// : integer; // 写超时次数, 2分钟
	public int TRANSFERSIZE;// : integer; // 每次网络传输包的最大字节数
}
