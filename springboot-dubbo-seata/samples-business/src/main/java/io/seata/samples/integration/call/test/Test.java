package io.seata.samples.integration.call.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.seata.samples.integration.common.dto.BusinessDTO;

public class Test {
	public static void main(String[] args) {
		BusinessDTO obj = new BusinessDTO();
		System.out.println(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue));
	}
}
