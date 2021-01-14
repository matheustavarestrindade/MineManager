package com.matheus.servermanager.http.modules;

import java.lang.management.ManagementFactory;

import com.google.gson.JsonObject;
import com.matheus.servermanager.http.HttpModule;
import com.matheus.servermanager.http.reqres.Request;
import com.matheus.servermanager.http.reqres.Response;
import com.matheus.servermanager.http.reqres.ResponseCode;
import com.matheus.servermanager.utils.Matematica;
import com.sun.management.OperatingSystemMXBean;

@SuppressWarnings("restriction")
public class CPUModule extends HttpModule {
	private OperatingSystemMXBean bean;

	public CPUModule() {
		super("/cpu");
		bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	}

	public void disable() {

	}

	@Override
	public void post(Request req, Response res) {

		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("cpu_usage_process", Matematica.round(bean.getProcessCpuLoad() * 100, 2));
		jsonResponse.addProperty("cpu_usage_system", Matematica.round(bean.getSystemCpuLoad() * 100, 2));

		String response = jsonResponse.toString();
		res.sendText(response, ResponseCode.SUCCESS);

	}

}
