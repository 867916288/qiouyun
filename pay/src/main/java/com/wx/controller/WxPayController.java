package com.wx.controller;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.wx.common.vo.OrderPayForm;
import com.wx.myconfig.MyConfig;
import com.wx.pay.constants.WXPayConstants;
import com.wx.sdk.WXPay;
import com.wx.sdk.WXPayConfig;
import com.wx.sdk.WXPayUtil;
import com.wx.service.WxpayService;
import com.wx.test.testb;
import com.wx.common.vo.HttpRequest;

@Controller
@RequestMapping("/jp888094/wxpay")

public class WxPayController {
	@Autowired 
	WxpayService wxpayService;
	@Autowired 
	private  MyConfig config;
	final static String APPID = WXPayConstants.APP_ID;
	final static String PATERNER_KEY = WXPayConstants.API_KEY;//商户key(财务)
	
	final static String GETOPENID_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	//获得openid 的url
	
	
	
	/*
	 * 统一下单 调起支付 
	 * @param 字符串code (获得openid)  , 字符串totalFee (表单上的价格) ，字符串  orderid (订单实际价格)
     * @return
     * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("/order")
	public String order(HttpServletRequest request,String code,String totalFee,String orderId, ModelMap retMap) throws Exception{
		
		
		
		//获得openid
		String getOpenIdparam= "appid="+APPID+"&secret="+PATERNER_KEY+"&code="+code+"&grant_type=authorization_code";
        String getOpenIdUrl = GETOPENID_URL+"?"+getOpenIdparam;
        //System.out.println("***getOpenId:"+getOpenIdUrl);
        RestTemplate rest = new RestTemplate();
        rest.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
		
        String resString = rest.getForObject(new URI(getOpenIdUrl), String.class);
        JSONObject opidJsonObject = JSONObject.parseObject(resString);
        //System.out.println("***opidJsonObject:"+opidJsonObject);
        String openid = opidJsonObject.get("openid").toString();//获取到了openid
		
		
		config= new MyConfig();
		
		//获得订单号
	    String order = orderId;
        Double verify = 0d;
        //根据订单号获得订单总价格
        // Verify = cmsCrmService.findById(cmsId);
             
        if((int)(verify*100) != (int)(Double.parseDouble(totalFee)*100)){
            retMap.addAttribute("chymsg", "金额错误");
            return "codeDemo";
        }
        
        
        //获得价格
        int total =(int)(Double.parseDouble(totalFee)*100);
        String s = String.valueOf(total);
        
		Map<String, String> map = WxpayService.unifiedOrder(s,openid,order);
		String prepayId = "";
		prepayId = (String) map.get("prepay_id");
		
		Map<String, String> payMap = new HashMap<String, String>();
		
		payMap.put("appId", config.getAppID());
		payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
		payMap.put("nonceStr", WXPayUtil.generateNonceStr());
		payMap.put("signType", "MD5");
		payMap.put("package", "prepay_id=" + prepayId);
		String paySign = WXPayUtil.generateSignature(payMap, PATERNER_KEY);
		payMap.put("paySign", paySign);
		retMap.addAttribute("data", payMap);
		
		return "codeDmeo";
	}
	


    @ResponseBody
    @RequestMapping("/Pay")                         
    public OrderPayForm pay(@RequestBody OrderPayForm orderPayForm) throws Exception {
        //PayOrderImpl payOrderImpl = new PayOrderImpl();
        //Map<String,String> resp = payOrderImpl.PayOrder("dsafsaf", orderPayForm.getOrderNo(), 0.01,orderPayForm.getOpenid());
    	System.out.println(orderPayForm.getOrderNo());
        return orderPayForm;

    }
	
	
	
	
	
	
	
	@RequestMapping("/test")
	@ResponseBody
	public Map<String, String> saveWxPayUnifiedOrder() throws Exception{
		
		//Map retMap= new HashMap<String, String>();
		Map<String, String> map = WxpayService.unifiedorder();
		String prepayId = "";
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
	
	
	
  //微信回调 
	@RequestMapping("/wxnotify")
    public void wxnotify(HttpServletRequest request, HttpServletResponse response) {
		//返回成功xml
	    String resSuccessXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";    
	    //返回失败xml  
	    String resFailXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml>";
        String resXml = "";
        InputStream inStream;
        
         //读取字节流
        try {
            inStream = request.getInputStream();
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
            }

            WXPayUtil.getLogger().info("wxnotify:微信支付----start----");
            
            // 获取微信调用我们notify_url的返回信息
            String result = new String(outSteam.toByteArray(), "utf-8");
            
            //resultMap
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);
            
            //boolean isSuccess = false;
            
            //校验返回信息是否成功
            if (WXPayConstants.SUCCESS.equalsIgnoreCase(resultMap.get(WXPayConstants.RESULT_CODE))) {
                WXPayUtil.getLogger().info("wxnotify:微信支付----返回成功");
                //校验签名合法
                if (WXPayUtil.isSignatureValid(resultMap, WXPayConstants.API_KEY)) {
                	
                	WXPayUtil.getLogger().info("wxnotify:微信支付----验证签名成功");
                    
                	// 订单处理 操作 
                	String ordersSn = resultMap.get("out_trade_no");//商户订单号 
                    String amountpaid = resultMap.get("total_fee");//实际支付的订单金额:单位 分
                 
                    BigDecimal amountPay = (new BigDecimal(amountpaid).divide(new BigDecimal("100"))).setScale(2);//将分转换成元-实际支付金额:元
                    System.out.println("***notifyUrl.htm data:"+ordersSn+"---"+amountPay);
                    
                    //根据订单号获得订单表金额
                    //Double price = orderservie.getpricebyid();
                    
                    //校验金额
                    if(amountpaid.equals("1")) {
                    // 修改订单表状态	 
                    
                    	
                    // 进行业务逻辑操作   
                    operation(ordersSn,ordersSn);
                    }
                    
                    
                   
                   //返回结果
                    resXml = resSuccessXml;
                    //isSuccess = true;
                } else {
                    WXPayUtil.getLogger().error("wxnotify:微信支付----判断签名错误");
                    resXml = resFailXml;
                }

            } else {
                WXPayUtil.getLogger().error("wxnotify:支付失败,错误信息：" + resultMap.get(WXPayConstants.ERR_CODE_DES));
                resXml = resFailXml;
            }
            
            
            // 付款记录修改 & 记录付款日志
            
            
            
            
            
            // 回调方法，处理业务 - 修改订单状态
            /*WXPayUtil.getLogger().info("wxnotify:微信支付回调：修改的订单===>" + resultMap.get("out_trade_no"));
            int updateResult = 0;
            if (updateResult > 0) {
                WXPayUtil.getLogger().info("wxnotify:微信支付回调：修改订单支付状态成功");
            } else {
                WXPayUtil.getLogger().error("wxnotify:微信支付回调：修改订单支付状态失败");
            }
          */

        } catch (Exception e) {
            WXPayUtil.getLogger().error("wxnotify:支付回调发布异常：", e);
        } finally {
            try {
                // 处理业务完毕
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
            } catch (IOException e) {
                WXPayUtil.getLogger().error("wxnotify:支付回调发布异常:out：", e);
            }
        }
    }


  /*	 
       查询订单     请求方式 get    url orderid
   */
	@ResponseBody
	@RequestMapping(value="/find/{orderid}",method= RequestMethod.GET,produces="application/json;charset=UTF-8")
	private Map<String, String> operation(@PathVariable String orderid) throws Exception {
		// TODO Auto-generated method stub
    WXPay wxpay = new WXPay(config);
    Map<String, String> data = new HashMap<String, String>();
    data.put("out_trade_no",orderid);
    //4200000348201908288665670045
    //data.put("transaction_id", "4200000348201908288665670045");//4200000348201908288665670045
   
        Map<String, String> resp = wxpay.orderQuery(data);
        if(wxpay.isPayResultNotifySignatureValid(resp)) {
        System.out.println(resp);
        System.out.println(WXPayUtil.mapToXml(resp));
       
    
   
	}
    return resp;
	}
	public void operation(String ordersSn, String ordersSn2) {
		
	}

}