package com.sun.blog.tool;

import com.alibaba.fastjson.JSON;
import org.springframework.core.codec.CodecException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 创建者:Sun<br>
 * 创建时间:2023/1/22&nbsp;18:59<br>
 * 描述:com.sun.blog.tool<br>
 */
public class IpToAddressTool {
	public static String IpToAddress(String IP){
		String address = null;
		try {
			if (IP == null) {
				IP= Arrays.toString(InetAddress.getLocalHost().getAddress()).split(",")[0];
			}
			String[] headers={
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:65.0) Gecko/20100101 Firefox/65.0",
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/12.0.3 Safari/605.1.15",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.140 Safari/537.36 Edge/18.17763",
					"Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko",
					"Mozilla/5.0 (iPhone; CPU iPhone OS 7_0_4 like Mac OS X) AppleWebKit/537.51.1 (KHTML, like Gecko) CriOS/31.0.1650.18 Mobile/11B554a Safari/8536.25",
					"Mozilla/5.0 (iPhone; CPU iPhone OS 8_3 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Version/8.0 Mobile/12F70 Safari/600.1.4",
					"Mozilla/5.0 (Linux; Android 4.2.1; M040 Build/JOP40D) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Mobile Safari/537.36",
					"Mozilla/5.0 (Linux; U; Android 4.4.4; zh-cn; M351 Build/KTU84P) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
					"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36"
			};
			Random random = new Random();
			int i = random.nextInt(headers.length);
			
			String header=headers[i];
			URL url = new URL("https://ip.useragentinfo.com/json?ip="+IP);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(3000);
			httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			httpURLConnection.setRequestProperty("User-Agent",header);
			InputStreamReader input = new InputStreamReader(httpURLConnection.getInputStream(), StandardCharsets.UTF_8);
			String line;
			BufferedReader bufferedReader = new BufferedReader(input);
			StringBuilder contentBuf = new StringBuilder();
			while ((line=bufferedReader.readLine())!=null){
				contentBuf.append(line);
			}
			HashMap<String,Object> hashMap = JSON.parseObject(contentBuf.toString(), HashMap.class);
			if ((Integer)hashMap.get("code")!=200) {
				address= "本机";
				System.out.println("API响应号为"+hashMap.get("code"));
				return address;
			}
			String country = (String) hashMap.get("country");
			String province = (String) hashMap.get("province");
			String city = (String) hashMap.get("city");
			String net = (String) hashMap.get("net");
			address = country + "," + province + "," + city + "," + net;
			System.out.println(address);
			return address;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return address;
	}
}
