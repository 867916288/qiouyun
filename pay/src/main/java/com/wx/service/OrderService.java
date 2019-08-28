package com.wx.service;

import com.wx.myconfig.MyConfig;
import com.wx.sdk.WXPay;
import com.wx.sdk.WXPayUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * @Description: TODO 订单查询
 * @Author zh
 * @Date 2019/8/26 16:36
 */
@Service
public class OrderService {
    public static void orderquery() throws Exception{

        MyConfig config = new MyConfig();
        WXPay wxpay = new WXPay(config);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", "4667892855489742321378748");//4200000348201908288665670045
        //data.put("transaction_id", "4200000348201908288665670045");//4200000348201908288665670045

        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            if(wxpay.isPayResultNotifySignatureValid(resp)) {
            System.out.println(resp);
            System.out.println(WXPayUtil.mapToXml(resp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
