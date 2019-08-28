package com.wx.myconfig;

import java.io.*;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wx.pay.constants.WXPayConstants;
import com.wx.sdk.IWXPayDomain;
import com.wx.sdk.WXPayConfig;

//import com.wx.pay.IWXPayDomain;
//import com.wx.pay.WXPayConfig;
//import com.wx.pay.WXPayConstants;
@Configuration
public class MyConfig extends WXPayConfig{
    private byte[] certData;

    public MyConfig() throws Exception {
        String certPath = "C:/abc/a.p12";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return "wxcf1014d13278b3c9";
    }

    public String getMchID() {
        return "1283041301";
    }

    public String getKey() {
        return "jiapuwang201511230smilespeed8888";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
       // Do any additional configuration here
       return builder.build();
    }
    public IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {

            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }

			
        };
        return iwxPayDomain;
    }
    

	
}