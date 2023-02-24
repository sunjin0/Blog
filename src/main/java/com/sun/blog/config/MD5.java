package com.sun.blog.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 创建者:Sun<br>
 * 创建时间:2023/1/3&nbsp;18:48<br>
 * 描述:com.sun.blog.config<br>
 */

public final class MD5 {
	
	public static String encrypt(String strSrc) {
		try {
			char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'a', 'b', 'c', 'd', 'e', 'f' };            // 用于输出编码的类型
			byte[] bytes = strSrc.getBytes();               // 字符串转化为 byte类型，一个字符转化为一个byte，比如 0为48,1为49，ascll码
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);        // 用指定的 byte 数组更新摘要
			bytes = md.digest();     // 通过执行诸如填充之类的最终操作完成哈希计算。在调用此方法之后，摘要被重置，得到固定长度为16的 bytes数组
			int len = bytes.length;  // 得到长度
			char[] chars = new char[len * 2];       //初始化字符数组容量,chars用于存储加密的内容，乘二是因为下面的处理，一个
			int k = 0;
			// byte类型表示-128~127的整数
			for (byte b : bytes) {
				chars[k++] = hexChars[b >>> 4 & 0xf];       // >>>表示无符号右移，oxf表示1111，hexChars为上面写的数组，hexChars的长度为16，经过 & 0xf 运算取最低4位得出下标位置取出字符串
				chars[k++] = hexChars[b & 0xf];
			}
			return new String(chars);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("MD5加密出错！！+" + e);
		}
	}
	
}
