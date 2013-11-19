package br.com.mvendas.comunication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class SugarClientSingleton {

	private static final String url = "http://10.0.2.2/sugardev/service/v2/rest.php";
	private static String session;
	private static SugarClientSingleton instance;
	
	private SugarClientSingleton() {}
			
	public static SugarClientSingleton getInstance() {
		if (instance == null) {
			instance = new SugarClientSingleton();
		}
		return instance;
	}

	public String login(String userName, String password) throws Exception {
		String password1 = encryptor(password);

		JSONObject json = new JSONObject();
		json.put("user_name", userName);
		json.put("password", password1);

		JSONObject json2 = new JSONObject();
		json2.put("user_auth", json);
		json2.put("application", "application_name");

		String restdata = json2.toString();
		String data = httpPost(url+"?method=login&input_type=json&response_type=json&rest_data="+restdata);
		JSONObject jsonData = (JSONObject) new JSONTokener(data).nextValue();	
		session = jsonData.getString("id");
		
		Log.i("info", "Login Efetuado - Sessao = " + session);
		
		return session;
	}

	public String encryptor(String password) {
		
		String pwd = password;
		String temppass = null;

		byte[] defaultBytes = pwd.getBytes();
		try{
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(defaultBytes);
			byte messageDigest[] = algorithm.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i=0;i<messageDigest.length;i++) {
				hexString.append(String.format("%02x", 0xFF & messageDigest[i])); // formatting to have the leading zeros
			}
			temppass = hexString.toString();
		} catch(NoSuchAlgorithmException nsae){
			System.out.println("No Such Algorithm found");
		}

		return temppass;
	}

    /**
     * Método para fazer logout no sistema
     */
    public void logout() {
		// Definindo os parametros para chamar o método web
		String parameters[][] = {
			{"session", session}
		};
		// Chamando o método web
		String result = call("logout", parameters);
		Log.i("info", "Logout Efetuado - Result = " + result);
    }	
	
	public String getSessionId() {
		return session;
	}
	
    public String call(String method, String parameters[][]) {
    	String restData = toRestData(parameters);
        String result = "";
        try {
        	result = httpPost(method, restData);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return result;
    }    
    
    public String toRestData(String params[][][]) {
    	String parameters[][];
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < params.length; i++) {
			parameters = params[i];			
			sb.append(toRestData(parameters));
			if (i < params.length - 1)	sb.append(",");
		}
		sb.append("]");
    	return sb.toString();
    }

    public String toRestData(String params[][]) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		for (int i = 0; i < params.length; i++) {
			sb.append(Character.toString((char) 34));
			sb.append(params[i][0]);
			sb.append(Character.toString((char) 34));
			sb.append(":");
			if (colocaAspas(params[i][0]))	sb.append(Character.toString((char) 34));
			sb.append(params[i][1]);
			if (colocaAspas(params[i][0]))	sb.append(Character.toString((char) 34));
			if (i < params.length - 1)	sb.append(",");
		}
		sb.append("}");    	
    	
    	return sb.toString();
    }
    
    public boolean colocaAspas(String name) {
    	boolean ret = true;
		if(name.equals("select_fields")) {
			ret = false;
		} else if(name.equals("link_name_to_fields_array")) {
			ret = false;
		} else if(name.equals("max_results")) {
			ret = false;			
		} else if(name.equals("Favorites")) {
			ret = false;
		} else if(name.equals("name_value_list")) {
			ret = false;
		}    	
    	return ret;
    }

    public String toArrayData(String params[]) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("[");
		for (int i = 0; i < params.length; i++) {
			sb.append(Character.toString((char) 34));
			sb.append(params[i]);
			sb.append(Character.toString((char) 34));
			if (i < params.length - 1)	sb.append(",");
		}
		sb.append("]");    	
    	
    	return sb.toString();
    }

	public static String httpPost(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(5000);
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");
		conn.setRequestProperty("charset", "utf-8");
		//conn.setRequestProperty("Content-Length", "" + Integer.toString(urlStr.getBytes().length));  		

		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();

		conn.disconnect();
		return sb.toString();
	}

	public static String httpPost(String method, String parameters) {
				
		HttpParams httpParameters = new BasicHttpParams();  
	    HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);  
	    HttpConnectionParams.setSoTimeout(httpParameters, 5000);  
	          
		String url = "http://10.0.2.2/sugarclient/rest/Call.php";
		InputStream inputStr = null;
		String result = "";
		try {
			// enviando os parametros para o php
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			ArrayList <NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("method", method));
			params.add(new BasicNameValuePair("parameters", parameters));
			//Log.i("info", "params = " + params.toString());
			httppost.setEntity(new UrlEncodedFormEntity(params));
		    httppost.setParams(httpParameters);		    
			HttpResponse response = httpclient.execute(httppost);
			
			HttpEntity entity = response.getEntity();
			inputStr = entity.getContent();			
			// convertendo o retorno para string
			BufferedReader rd = new BufferedReader(new InputStreamReader(inputStr, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = rd.readLine()) != null) {
				sb.append(line); // + "\n");
				result = line;
			}
			rd.close();
			inputStr.close();
		} catch (Exception e) {
			Log.e("info", "httpPost - Erro ao chamar " + e.toString());
		}		
		return result;
	}

}
