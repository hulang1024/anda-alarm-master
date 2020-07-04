package qqhl.andaalarmmaster.service;


import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import qqhl.andaalarmmaster.dao.devicealarm.DeviceAlarmEventTypeDict;

/**
 * 短信发送
 */
@Service
public class SendSmsService {
	@Value("${aliSms.url}")
	private String url;
	@Value("${aliSms.appKey}")
	private String appKey;
	@Value("${aliSms.secret}")
	private String secret;
	@Value("${aliSms.freeSignName}")
	private String freeSignName;

	/**
	 * @param recNums 接收号码
	 * @param smsTemplateCode 模板代码
	 * @param params 模板参数
	 * @return
	 */
	public String send(Collection<String> recNums, String smsTemplateCode, Map<String, String> params) throws Exception {
		TaobaoClient client = new DefaultTaobaoClient(url, appKey, secret);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType("normal");
		req.setSmsFreeSignName(freeSignName);
		req.setSmsParamString(new Gson().toJson(params));
		req.setRecNum(StringUtils.join(recNums.toArray(), ","));
		req.setSmsTemplateCode(smsTemplateCode);
		AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		if (rsp.isSuccess()) {
			return "0";
		} else {
			return rsp.getErrorCode();
		}
	}
}
