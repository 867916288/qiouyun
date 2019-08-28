package com.wx.service;

import com.wx.myconfig.MyConfig;
import com.wx.sdk.WXPay;
import com.wx.sdk.WXPayUtil;

import java.util.Map;

/**
 * @Description: TODO 验证签名
 * @Author zh
 * @Date 2019/8/27 9:31
 */
public class VerifySign {
    public static void main(String[] args) throws Exception {

        String notifyData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\r\n" + 
        		"<xml>\r\n" + 
        		"<transaction_id>4200000348201908288665670045</transaction_id>\r\n" + 
        		"<nonce_str>OwBuow8hkpgJJeFR</nonce_str>\r\n" + 
        		"<trade_state>SUCCESS</trade_state>\r\n" + 
        		"<bank_type>CFT</bank_type>\r\n" + 
        		"<openid>ody5zs0Z5RFQgJWw3sf2y1_M5VTM</openid>\r\n" + 
        		"<sign>16040F84F9EDB4B32254021DF77D2705</sign>\r\n" + 
        		"<return_msg>OK</return_msg>\r\n" + 
        		"<fee_type>CNY</fee_type>\r\n" + 
        		"<mch_id>1283041301</mch_id>\r\n" + 
        		"<cash_fee>1</cash_fee>\r\n" + 
        		"<out_trade_no>4667892855489742321378748</out_trade_no>\r\n" + 
        		"<cash_fee_type>CNY</cash_fee_type>\r\n" + 
        		"<appid>wxcf1014d13278b3c9</appid>\r\n" + 
        		"<total_fee>1</total_fee>\r\n" + 
        		"<trade_state_desc>支付成功</trade_state_desc>\r\n" + 
        		"<trade_type>JSAPI</trade_type>\r\n" + 
        		"<result_code>SUCCESS</result_code>\r\n" + 
        		"<attach/>\r\n" + 
        		"<time_end>20190828112706</time_end>\r\n" + 
        		"<is_subscribe>Y</is_subscribe>\r\n" + 
        		"<return_code>SUCCESS</return_code>\r\n" + 
        		"</xml>"; // 支付结果通知的xml格式数据

        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map

        if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
            System.out.println("----------签名验证成功----------");
        }
        else {
            System.out.println("----------签名验证失败----------");
        }
    }
}
