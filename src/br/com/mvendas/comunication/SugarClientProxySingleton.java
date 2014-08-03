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

import android.os.AsyncTask;
import android.util.Log;
import br.com.mvendas.utils.StringUtil;

public class SugarClientProxySingleton {
	
	//public static final String host = "http://10.0.2.2/";
	public static final String host = "http://192.168.2.100/";
	public static final String urlLogin = host + "sugarclient/rest/login.php";
	public static final String urlCall  = host + "sugarclient/rest/call.php";
	
	private static SugarClientProxySingleton instance;
	private static String session;
	private static String result;
	
	private SugarClientProxySingleton() {}
			
	public static SugarClientProxySingleton getInstance() {
		if (instance == null) {
			instance = new SugarClientProxySingleton();
		}
		return instance;
	}
	
	public String getSession() {
		return session;
	}

	/***
	 * Método para fazer login no Sugar
	 * @param userName
	 * @param password
	 */
	public void login(String userName, String password) {
		try {
			session = httpPost(urlLogin);
			if (session.equals("") || session == null) {
				Log.i("info", "Login Não Efetuado");
			} else {
				Log.i("info", "Login Efetuado - Sessao = " + session);
			}
		} catch (Exception e) {
			Log.e("info", "httpPost - Erro ao chamar url. " + urlLogin + ". Motivo: " + e.toString());
		}
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
		String parameters[][] = {{"session", session}};
		// Chamando o método web
		String result = call("logout", parameters);
		Log.i("info", "Logout Efetuado - Result = " + result);
    }	
		
    /**
     * Método para chamada de webmétodos do WebService do Sugar através do SugarClient
     * @param method
     * @param parameters
     * @return result
     */
    public String call(String method, String parameters[][]) {
    	String restData = StringUtil.toRestData(parameters);
    	int time = 0;
        try {
        	result = null;
        	new httpPostTask().execute(method, restData);
        	while (result == null && time <= 7000) {
        		Thread.sleep(1000);
        		time += 1000;
        	}
		} catch (Exception e) {
			Log.e("info", "call - Erro ao chamar url. " + e.getMessage());			
		}
        return result;
    }
	

	public static String httpPost(String urlStr) throws Exception {
		
		String result = "";
		int responseCode;

		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setReadTimeout(3000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setAllowUserInteraction(false);
			conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
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
			Log.e("info", "httpPost - Erro ao chamar url. " + e.getMessage());			
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
	
	private class httpPostTask extends AsyncTask<String, String, String> {
		 
		@Override
        protected String doInBackground(String... params) {
            try {
                result = httpPost(params[0], params[1]);
                return result;
            } catch (Exception e) {
            	return e.getMessage();            	 
            }
        }
 
    }

}
