package com.wx.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.wx.common.vo.HttpRequest;
import com.wx.myconfig.MyConfig;
import com.wx.pay.*;
import com.wx.sdk.WXPayUtil;
import com.wx.sdk.WXPay;
@Service
public class WxpayService {
	//配置信息
	public static MyConfig config;
	Map<String, String> reqData;
	
	
	//第一次下单 返回值 map
	public static Map<String, String> unifiedOrder(String totalfee,String  openid,String orderId) throws Exception {
		MyConfig config = new MyConfig();
		WXPay wxpay = new WXPay(config);
		
		Map<String, String> data = new HashMap<String, String>();
		//data.put("appid", config.getAppID());
		data.put("mch_id", config.getMchID());
		//data.put("nonce_str", WXPayUtil.generateNonceStr());
		data.put("body", "商品描述");
		data.put("out_trade_no", orderId);
		data.put("total_fee", totalfee);
		data.put("spbill_create_ip", "192.168.2.50");
		data.put("notify_url", "http://video.jp9527.com/video_zb/jp888094/wxpay/wxnotify");
		data.put("trade_type", "JSAPI");  // 此处指定为扫码支付
		data.put("openid", openid);
		//ody5zs4AL5-DinXuo2kmQRPC3djw
		
		
		//判断wxpay是否封装了sign
		String sign = WXPayUtil.generateSignature(data,config.getKey());
		data.put("sign",sign);
		//System.out.println("getmaptoxml");
		//String mapToXml = WXPayUtil.mapToXml(data);
		//System.out.println(mapToXml);
		//String resp = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", mapToXml);
		Map<String, String> map = wxpay.unifiedOrder(data);
		//Map<String, String> map = WXPayUtil.xmlToMap(resp);
		return map;
	}
	public static Map<String, String> unifiedorder() throws Exception {
		MyConfig config = new MyConfig();
		WXPay wxpay = new WXPay(config);
		
		Map<String, String> data = new HashMap<String, String>();
		//data.put("appid", config.getAppID());
		//data.put("mch_id", config.getMchID());
		//data.put("nonce_str", WXPayUtil.generateNonceStr());
		data.put("body", "商品描述");
		data.put("out_trade_no", "466789285123124214124111243");
		data.put("total_fee","1");
		data.put("spbill_create_ip", "192.168.2.50");
		data.put("notify_url", "http://video.jp9527.com/video_zb/jp888094/wxnotify");
		data.put("trade_type", "JSAPI");  // 此处指定为扫码支付
		data.put("openid", "ody5zs4AL5-DinXuo2kmQRPC3djw");
		//ody5zs4AL5-DinXuo2kmQRPC3djw
		//String sign = WXPayUtil.generateSignature(data,config.getKey());
		//data.put("sign",sign);
		//System.out.println("getmaptoxml");
		//String mapToXml = WXPayUtil.mapToXml(data);
		//System.out.println(mapToXml);
		//String resp = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", mapToXml);
		Map<String, String> map = wxpay.unifiedOrder(data);
		//Map<String, String> map = WXPayUtil.xmlToMap(resp);
		return map;
	}
	
	
	
//	
//	 /**
//     * 向 Map 中添加 appid、mch_id、nonce_str、sign_type、sign <br>
//     * 该函数适用于商户适用于统一下单等接口，不适用于红包、代金券接口
//     *
//     * @param reqData
//     * @return
//     * @throws Exception
//     */
//	public Map<String, String> fillRequestData(Map<String, String> reqData) throws Exception {
//        reqData.put("appid", config.getAppID());
//        reqData.put("mch_id", config.getMchID());
//        reqData.put("nonce_str", WXPayUtil.generateNonceStr());
//        //reqData.put("sign_type", WXPayConstants.MD5);
//        reqData.put("sign", WXPayUtil.generateSignature(reqData, config.getKey()));
//        return reqData;
//    }
}
