package qqhl.andaalarmmaster.service;


import java.util.Collection;
import java.util.Map;

import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 短信发送
 */
public class SendSms {
	private static final String ALISMS_URL = "http://gw.api.taobao.com/router/rest";
	private static final String ALISMS_APPKEY = "23461088";
	private static final String ALISMS_SECRET = "ceff0aa07ca8e85c4f9c1be70e0d96f9";
	private static final String ALISMS_FREE_SIGN_NAME = "呼贝";

	/**
	 * @param recNums 接收号码
	 * @param smsTemplateCode 模板代码
	 * @param params 模板参数
	 * @return
	 */
	public static String send(Collection<String> recNums, String smsTemplateCode, Map<String, String> params) throws Exception {
		TaobaoClient client = new DefaultTaobaoClient(ALISMS_URL, ALISMS_APPKEY, ALISMS_SECRET);
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		// req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName(ALISMS_FREE_SIGN_NAME);
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
