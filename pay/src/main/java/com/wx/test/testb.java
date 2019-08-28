package com.wx.test;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.wx.myconfig.MyConfig;

import com.wx.sdk.WXPayUtil;
import com.wx.sdk.WXPay;
import com.wx.service.WxpayService;


public class testb {
	private static Map<String, String> retMap;
//	@Autowired
//	static WXPayConfigImpl configImpl;
	@Autowired 
	public static MyConfig config;
	public static void main(String[] args) throws Exception {
		testb b = new testb();
		Map a = new HashMap<String, String>();
		a = b.pay();
		System.out.println(a);
	}
	public Map<String, String> pay() throws Exception {
	
	
	
		 config= new MyConfig();
		 WXPay wxpay = new WXPay(config);
		 //WXPay wxPay = new WXPay(WXPayConfigImpl.getInstance());
		 //Map<String, String> resultMap = wxPay.unifiedOrder(retMap);
//		//configImpl = WXPayConfigImpl.getInstance(); 
//		System.out.println(configImpl);
//		WXPay wxPay = new WXPay(configImpl);
//		 Map<String, String> resultMap = wxPay.unifiedOrder(retMap);
//		 System.out.println(resultMap);
//		
		Map<String, String> map = WxpayService.unifiedorder();
		//System.out.println(map);
		String prepayId ="";
		prepayId = (String) map.get("prepay_id");	
		
		Map<String, String> payMap = new HashMap<String, String>();
		
		payMap.put("appId", config.getAppID());
		payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
		payMap.put("nonceStr", WXPayUtil.generateNonceStr());
		payMap.put("signType", "MD5");
		payMap.put("package", "prepay_id=" + prepayId);
		String paySign = WXPayUtil.generateSignature(payMap, "jiapuwang201511230smilespeed8888");
		payMap.put("paySign", paySign);
		return payMap;
	
	}
	


	
}