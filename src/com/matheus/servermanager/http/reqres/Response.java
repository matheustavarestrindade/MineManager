package com.matheus.servermanager.http.reqres;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import com.matheus.servermanager.http.HttpPageRender;
import com.sun.net.httpserver.HttpExchange;

public class Response {

	private HttpExchange exchange;
	private File basePath;

	public Response(HttpExchange exchange, File basePath) {
		this.exchange = exchange;
		this.basePath = basePath;
	}

	public void redirect(String context) {
		exchange.getResponseHeaders().add("Location", context);
		sendCode(ResponseCode.REDIRECTED);
	}

	public void sendText(String response, ResponseCode code) {
		try {
			exchange.sendResponseHeaders(code.getCode(), response.getBytes().length);
			OutputStream output = exchange.getResponseBody();
			output.write(response.getBytes());
			output.flush();
			exchange.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void renderPage(HttpPageRender render) {
		if (basePath == null) {
			System.out.println("Cannot render page: " + render.getFilePath());
			System.out.println("Base path does not exist!");
			return;
		}
		File f = new File(basePath.getAbsolutePath() + File.separator + render.getFilePath());
		if (!f.exists()) {
			System.out.println("Cannot render page: " + render.getFilePath());
			System.out.println("File does not exist!");
			return;
		}

		HashMap<String, String> subPagesHtml = new HashMap<>();

		for (Entry<String, String> subPages : render.getSubPages().entrySet()) {
			File subPage = new File(basePath.getAbsolutePath() + File.separator + subPages.getValue());
			if (!subPage.exists()) {
				System.out.println("Cannot render SubPage: " + subPages.getValue());
				System.out.println("File does not exist!");
				return;
			}
			try {
				FileReader fr = new FileReader(subPage);
				BufferedReader br = new BufferedReader(fr);
				StringBuilder htmlBuilder = new StringBuilder(1024);
				String s = "";
				while ((s = br.readLine()) != null) {
					htmlBuilder.append(s);
				}
				br.close();
				String html = htmlBuilder.toString();
				subPagesHtml.put(subPages.getKey(), html);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			StringBuilder htmlBuilder = new StringBuilder(1024);
			String s = "";
			while ((s = br.readLine()) != null) {
				htmlBuilder.append(s);
			}
			br.close();
			String html = htmlBuilder.toString();

			for (Entry<String, String> subPageHtml : subPagesHtml.entrySet()) {
				html = html.replace("${" + subPageHtml.getKey() + "}", subPageHtml.getValue().toString());
			}

			for (Entry<String, String> content : render.getParameters().entrySet()) {
				html = html.replace("${" + content.getKey() + "}", content.getValue());
			}

			sendText(html, ResponseCode.SUCCESS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendCode(ResponseCode code) {
		try {
			exchange.sendResponseHeaders(code.getCode(), 0);
			exchange.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
