package com.qiandi.utils;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {

    public static final String VALIDATE_CODE = "yourSMS";//发送短信验证码

    public static void sendShortMessage(String templateCode, String phoneNumbers, String param) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "yourack", "yoursk");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", "" + phoneNumbers + "");
        request.putQueryParameter("SignName", "传智健康");
        request.putQueryParameter("TemplateCode", "" + templateCode + "");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + param + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
	}


}

