package br.com.comunication.mvendas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class SugarClient {

	private String url;
	private String session;
/*	
	private String user;
	private String session;
	private String pass;
*/
	/**
	 * Construtor da classe 
	 * @param string url com o endereço do webservice REST (ponto de entrada)
	 */
	public SugarClient(String url)  {
		this.url = url;
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
		String data = httpPost(this.url+"?method=login&input_type=json&response_type=json&rest_data="+restdata);
		JSONObject jsonData = (JSONObject) new JSONTokener(data).nextValue();	
		this.session = jsonData.getString("id");
		return this.session;
	}
	
    /**
     * Método para fazer logout no sistema
     */
    public void logout() {
		// Definindo os parametros para chamar o método web
		String parameters[][] = {
			{"session", this.session}
		};
		// Chamando o método web
		this.call("logout", parameters);
    }	
	
	public String getSessionId() {
		return this.session;
	}
	
    public String call(String method, String parameters[][]) {
    	String restData = toRestData(parameters);
        String urlRequest = this.url+"?method="+method+"&input_type=json&response_type=json&rest_data="+restData;
        String result = "";
        Log.i("info", "request = " + urlRequest);
        try {
			result = httpPost(urlRequest);
	        Log.i("info", "result = " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return result;
    }
    
    public String toRestData(String params[][]) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("{");
		for (int i = 0; i < params.length; i++) {
			sb.append(Character.toString((char) 34));
			sb.append(params[i][0]);
			sb.append(Character.toString((char) 34));
			sb.append(":");
			sb.append(Character.toString((char) 34));
			sb.append(params[i][1]);
			sb.append(Character.toString((char) 34));
			if (i < params.length - 1)	sb.append(",");
		}
		sb.append("}");    	
    	
    	return sb.toString();
    }
    
	public static String httpPost(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setAllowUserInteraction(false);
		conn.setRequestProperty("Content-Type",	"application/x-www-form-urlencoded");

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

	/*
	public final String[] post(String url, String json) {
		String[] result = new String[2];

		try {
			HttpPost httpPost = new HttpPost(new URI(url));
			httpPost.setHeader("content-type", "application/json");
			StringEntity sEntity = new StringEntity(json);
			httpPost.setEntity(sEntity);

			HttpResponse response;
			response = HttpClientSingleton.getHttpClientInstance().execute(httpPost);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				result[0] = String.valueOf(response.getStatusLine().getStatusCode());
				InputStream instream = entity.getContent();
				result[1] = toString(instream);
				instream.close();

				Log.d("post", "Result from post JsonPost : " + result[0] + " : " + result[1]);
			}
		} catch (Exception e) {
			Log.e("NGVL", "Falha ao acessar Web service", e);
			result[0] = "0";
			result[1] = "Falha na rede!";
		}
		
		return result;
	}

	private String toString(InputStream is) throws IOException {
		byte[] bytes = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		int lidos;		
		while ((lidos = is.read(bytes)) > 0) {
			baos.write(bytes, 0, lidos);
		}
		
		return new String(baos.toByteArray());
	}
	*/	 
}
