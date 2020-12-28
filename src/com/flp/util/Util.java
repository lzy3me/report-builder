package com.flp.util;

public class Util {
	
	private Util() {
		
	}
	
	public static int checkBoolReturnInt(boolean bool) {
		return bool ? 1 : 0;
	}
	
	public static int checkStringReturnInt(String value) {
		return value==null || value=="" ? 1 : 0;
	}
	
	public static String checkNullReturnDash(String str) {
		return str==null || str=="" ? "-" : str;
	}
	
	public static String checkNullReturnZero(String str) {
		return str==null || str=="" ? "0" : str;
	}
	
	public static int checkNullReturnZeroInt(String str) {
		return str==null || str=="" ? 0 : Integer.parseInt(str);
	}
	
}
