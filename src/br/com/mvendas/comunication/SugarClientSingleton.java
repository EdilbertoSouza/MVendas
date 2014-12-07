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

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import br.com.mvendas.utils.StringUtil;

public class SugarClientSingleton {
	
	//public static final String host = "http://10.0.2.2/";
	public static final String host = "http://192.168.0.89/";
	public static final String urlLogin = host + "sugardev/service/v2/rest.php";
	public static final String urlCall  = host + "sugardev/service/v2/rest.php";
	
	private static SugarClientSingleton instance;
	private static String session;
	private static String result;
	
	private SugarClientSingleton() {}
			
	public static SugarClientSingleton getInstance() {
		if (instance == null) {
			instance = new SugarClientSingleton();
		}
		return instance;
	}
	
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		SugarClientSingleton.session = session;
	}

	/***
	 * Método para fazer login no Sugar
	 * @param userName
	 * @param password
	 */	
	@SuppressLint("NewApi")
	public void login(String userName, String password) throws Exception {
		String password1 = encryptor(password);

		JSONObject json = new JSONObject();
		json.put("user_name", userName);
		json.put("password", password1);

		JSONObject json2 = new JSONObject();
		json2.put("user_auth", json);
		json2.put("application", "mvendas");

		String restdata = json2.toString();
		String data = httpPost(urlLogin+"?method=login&input_type=json&response_type=json&rest_data="+restdata);
		JSONObject jsonData = (JSONObject) new JSONTokener(data).nextValue();	
		session = jsonData.getString("id");
		if (session.isEmpty()) {
			throw new Exception(jsonData.getString("description"));
		}
		Log.i("info", "Login Efetuado"); // + session);
	}
	
	/***
	 * Método para Criptografar a senha
	 * @param password
	 * @return passwordEncrypted
	 */
	public String encryptor(String password) {
		
		String pwd = password;
		String passwordEncrypted = null;

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
			passwordEncrypted = hexString.toString();
		} catch(NoSuchAlgorithmException nsae){
			System.out.println("No Such Algorithm found");
		}

		return passwordEncrypted;
	}

    /**
     * Método para fazer logout no Sugar
     */
    public void logout() {
		// Definindo os parametros para chamar o método web
		String parameters[][] = {{"session", getSession()}};
		// Chamando o método web
		try {
			xcall("logout", parameters);
			setSession(null);
			Log.i("info", "Logout Efetuado");
		} catch (Exception e) {
			Log.e("info", "Erro ao Efetuar Logout. Motivo: " + e.toString());
		}
    }	
		
    /**
     * Método para chamada de webmétodos do WebService do Sugar
     * @param method
     * @param parameters
     * @return result
     */	
    public String call(String method, String parameters[][]) throws Exception {
    	String restData = StringUtil.toRestData(parameters);
        String request = urlCall+"?method="+method+"&input_type=json&response_type=json&rest_data="+restData;
        //Log.i("info", "request = " + request);
        try {
    		result = httpPost(request);
            //Log.i("info", "result = " + result);    		
		} catch (Exception e) {
			throw new Exception("call - Erro ao chamar url. " + e.getMessage());
		}
        if (result != null) {
    		if (result.contains("description")) {
    			throw new Exception("Sugar Error = " + result);
    		}        	
        }
        return result;
    }

    public String xcall(String method, String parameters[][]) throws Exception {
    	String restData = StringUtil.toRestData(parameters);
        String request = urlCall+"?method="+method+"&input_type=json&response_type=json&rest_data="+restData;
        //Log.i("info", "request = " + request);
    	int time = 0;
        try {
        	result = null;
        	new httpPostTask().execute(request);
        	while (result == null && time <= 7000) {
        		Thread.sleep(1000);
        		time += 1000;
        	}
		} catch (Exception e) {
			throw new Exception("call - Erro ao chamar url. " + e.getMessage());
		}
        if (result != null) {
    		if (result.contains("description")) {
    			throw new Exception("Sugar Error = " + result);
    		}        	
        }
        return result;
    }
    
    public void close() {
		if (instance != null) {
			if (instance.getSession() != null) {
				instance.logout();				
			}
			instance = null;
		}    		
    }

	private class httpPostTask extends AsyncTask<String, String, String> {
		 
		@Override
	    protected String doInBackground(String... params) {
	        try {
	            result = httpPost(params[0]);
	            return result;
	        } catch (Exception e) {
	        	return e.getMessage();            	 
	        }
	    }
	
	}

	public static String httpPost(String urlStr) throws Exception {
		
		String result = "";
		int responseCode;

		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(7000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			//Log.i("info",conn.getResponseMessage());
			//conn.setRequestProperty("Content-Length", "" + Integer.toString(urlStr.getBytes().length));  		
			
			responseCode = conn.getResponseCode();
			if (responseCode != 200) {
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
			result = sb.toString();
			conn.disconnect();			
		} catch (Exception e) {
			//Log.e("info", "httpPost - Erro ao chamar url. " + e.toString());
			throw new Exception("httpPost Error " + e.toString());
		}
		return result;
	}
	
	public static String httpPost(String method, String parameters) throws Exception {
				
		String result = "";
			          
		try {	    
			HttpPost httppost = new HttpPost(urlCall);
			HttpClient httpclient = new DefaultHttpClient();
			
			// passando o método e os parametros como parametros
			ArrayList <NameValuePair> params = new ArrayList<NameValuePair>();			
			params.add(new BasicNameValuePair("method", method));
			params.add(new BasicNameValuePair("parameters", parameters));			
			httppost.setEntity(new UrlEncodedFormEntity(params));
			
			// definindo os parametros do post 
			HttpParams httpParams = new BasicHttpParams();  
		    HttpConnectionParams.setConnectionTimeout(httpParams, 7000);  
		    HttpConnectionParams.setSoTimeout(httpParams, 10000);		    
			httppost.setParams(httpParams);		    

		    // chamando o SugarClient (PHP)
			HttpResponse response = httpclient.execute(httppost);
			//Log.i("info", "response = " + response.toString());
			
			// verificando se houve algum na comunicação com o SugarClient
			Integer StatusCode = response.getStatusLine().getStatusCode();
			if (StatusCode != 200) {
				Log.e("info", "httpPost - Erro ao chamar url " + urlCall + " Motivo: " + StatusCode);
				//return Integer.toString(StatusCode).trim();
				throw new Exception(Integer.toString(StatusCode).trim());
			}
			
			// convertendo o retorno para String
			HttpEntity entity = response.getEntity();
			InputStream inputStr = entity.getContent();			
			BufferedReader rd = new BufferedReader(new InputStreamReader(inputStr, "utf-8"));
			String line = null;
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
			inputStr.close();
			//Log.i("info", "httpPost - Result. " + result);
		} catch (Exception e) {
			Log.e("info", "httpPost - Erro ao chamar url. " + e.toString());
		}
		return result;
	}

}
