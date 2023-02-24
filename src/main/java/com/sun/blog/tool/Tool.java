package com.sun.blog.tool;

/**
 * 创建者:Sun<br>
 * 创建时间:2023/1/17&nbsp;13:50<br>
 * 描述:com.sun.blog.tool<br>
 */
public  class Tool {
	/**
	 * 如: s="[Java,Spring,HTML]"
	 * @param s 数组变成字符串的字符
	 * @return String[]
	 */
	public static String[] StringToArray(String s){
		if (s == null) {
			return new String[]{"null"};
		}
		String s1 = s.substring(1, s.length() - 1);
		for (int i = 0; i < s1.length(); i++) {
			s1 = s1.replace(" ", "");
		}
		return  s1.split(",");
	}
	
}
