package com.yungouos.pay.wxpay;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.yungouos.pay.config.WxPayApiConfig;
import com.yungouos.pay.util.WxPaySignUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;

/**
 * 
 * 微信支付
 * 
 * @action
 *
 * @author YunGouOS技术部-029
 *
 * @time 2019年4月28日 下午2:26:46
 *
 *
 */
public class WxPay {

	/**
	 * 微信扫码支付
	 * 
	 * @param out_trade_no
	 *            订单号 不可重复
	 * @param total_fee
	 *            支付金额 单位：元 范围：0.01-99999
	 * @param mch_id
	 *            微信支付商户号 登录YunGouOS.com-》微信支付-》我的支付 查看商户号
	 * @param body
	 *            商品描述
	 * @param type
	 *            返回类型（1、返回微信原生的支付连接需要自行生成二维码；2、直接返回付款二维码地址，页面上展示即可。不填默认1 ）
	 * @param attach
	 *            附加数据 回调时原路返回 可不传
	 * @param notify_url
	 *            异步回调地址，不传无回调
	 * @param return_url
	 *            同步回调地址，收银台模式还没开发，暂时没什么卵用
	 * @param key
	 *            商户密钥 登录YunGouOS.com-》我的账户-》账户中心 查看密钥
	 * @return 支付二维码连接
	 */
	public static String nativePay(String out_trade_no, String total_fee, String mch_id, String body, String type, String attach, String notify_url, String return_url, String key) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String resultUrl = null;
		try {
			if (StrUtil.isBlank(out_trade_no)) {
				throw new Exception("订单号不能为空！");
			}
			if (StrUtil.isBlank(total_fee)) {
				throw new Exception("付款金额不能为空！");
			}
			if (StrUtil.isBlank(mch_id)) {
				throw new Exception("商户号不能为空！");
			}
			if (StrUtil.isBlank(body)) {
				throw new Exception("商品描述不能为空！");
			}
			params.put("out_trade_no", out_trade_no);
			params.put("total_fee", total_fee);
			params.put("mch_id", mch_id);
			params.put("body", body);
			// 上述必传参数签名
			String sign = WxPaySignUtil.createSign(params, key);
			if (StrUtil.isBlank(type)) {
				type = "2";
			}
			params.put("type", type);
			params.put("attach", attach);
			params.put("notify_url", notify_url);
			params.put("return_url", return_url);
			params.put("sign", sign);
			String result = HttpRequest.post(WxPayApiConfig.nativeApiUrl).form(params).execute().body();
			if (StrUtil.isBlank(result)) {
				throw new Exception("API接口返回为空，请联系客服");
			}
			JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
			if (jsonObject == null) {
				throw new Exception("API结果转换错误");
			}
			Integer code = jsonObject.getInteger("code");
			if (0 != code) {
				throw new Exception(jsonObject.getString("msg"));
			}
			resultUrl = jsonObject.getString("data");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return resultUrl;
	}

	/**
	 * 公众号支付
	 * 
	 * @param out_trade_no
	 *            订单号 不可重复
	 * @param total_fee
	 *            支付金额 单位：元 范围：0.01-99999
	 * @param mch_id
	 *            微信支付商户号 登录YunGouOS.com-》微信支付-》我的支付 查看商户号
	 * @param body
	 *            商品描述
	 * @param openId
	 *            用户openId 通过授权接口获得
	 * @param attach
	 *            附加数据 回调时原路返回 可不传
	 * @param notify_url
	 *            异步回调地址，不传无回调
	 * @param return_url
	 *            同步回调地址，收银台模式还没开发，暂时没什么卵用
	 * @param key
	 *            商户密钥 登录YunGouOS.com-》我的账户-》账户中心 查看密钥
	 * @return JSSDK支付需要的jspackage
	 */
	public static String jsapi(String out_trade_no, String total_fee, String mch_id, String body, String openId, String attach, String notify_url, String return_url, String key) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		String resultUrl = null;
		try {
			if (StrUtil.isBlank(out_trade_no)) {
				throw new Exception("订单号不能为空！");
			}
			if (StrUtil.isBlank(total_fee)) {
				throw new Exception("付款金额不能为空！");
			}
			if (StrUtil.isBlank(mch_id)) {
				throw new Exception("商户号不能为空！");
			}
			if (StrUtil.isBlank(body)) {
				throw new Exception("商品描述不能为空！");
			}
			params.put("out_trade_no", out_trade_no);
			params.put("total_fee", total_fee);
			params.put("mch_id", mch_id);
			params.put("body", body);
			params.put("openId", openId);
			// 上述必传参数签名
			String sign = WxPaySignUtil.createSign(params, key);
			params.put("attach", attach);
			params.put("notify_url", notify_url);
			params.put("return_url", return_url);
			params.put("sign", sign);
			String result = HttpRequest.post(WxPayApiConfig.jsapi).form(params).execute().body();
			if (StrUtil.isBlank(result)) {
				throw new Exception("API接口返回为空，请联系客服");
			}
			JSONObject jsonObject = (JSONObject) JSONObject.parse(result);
			if (jsonObject == null) {
				throw new Exception("API结果转换错误");
			}
			Integer code = jsonObject.getInteger("code");
			if (0 != code) {
				throw new Exception(jsonObject.getString("msg"));
			}
			resultUrl = jsonObject.getString("data");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		return resultUrl;
	}

}
