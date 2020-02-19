package com.question.admin.utils;


import com.alibaba.fastjson.JSON;
import com.question.admin.config.wx.WxConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class WxUtil {

    /**
     * 微信的 code2session 接口 获取微信用户信息
     * 官方说明 : https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     */
    public static String code2Session(WxConfig wxConfig, String jsCode) {
//        String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session?appid={appid}&secret={secret}&js_code={jsCode}&grant_type=authorization_code";
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("appid", wxConfig.getAppid());
//        params.add("secret", wxConfig.getSecret());
//        params.add("js_code", jsCode);
//        params.add("grant_type", "authorization_code");
//        URI code2Session = HttpUtil.getURIwithParams(code2SessionUrl, params);
        log.info("wxConfig: {}; jsCode:{}", JSON.toJSONString(wxConfig), jsCode);
//        Mono<String> stringMono = WebClient.create(code2SessionUrl).get().
//                uri(wxConfig.getAppid(), wxConfig.getSecret(), jsCode).retrieve().bodyToMono(String.class);
        String jscode2session = WebClient.create().get().uri(uriBuilder -> uriBuilder.scheme("https").host("api.weixin.qq.com").path("/sns/jscode2session").queryParam("appid", wxConfig.getAppid()).queryParam("secret", wxConfig.getSecret()).queryParam("js_code", jsCode).queryParam("grant_type", "authorization_code").build()).retrieve().bodyToMono(String.class).block();
        return jscode2session;
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
//            HttpGet httpGet = new HttpGet(code2Session.toString());
//            HttpEntity responseEntity = httpClient.execute(httpGet).getEntity();
//            if (responseEntity != null) {
//                String responseStr = EntityUtils.toString(responseEntity);
//                return responseStr;
//            }
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//        }
//        return null;
    }
}
