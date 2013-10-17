package br.com.mvendas.dao;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import br.com.mvendas.comunication.SugarClientSingleton;

public class EquipamentoDao {

	private SugarClientSingleton sc;
	private String session;

	public EquipamentoDao(SugarClientSingleton sc) {
		this.sc = sc;
		this.session = this.sc.getSessionId();
	}

	public String recuperarId(String sitio) {
		String equipamento_id = "";
		
		// Definindo os parametros para chamar o método web
		String fields[] = {"id"};
		String select_fields = this.sc.toArrayData(fields);
		String parameters[][] = { 
			{"session", this.session}, 
			{"module_name", "os_Equipamentos"},
			{"query", "name='" + sitio + "'"},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "1"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};
					
		// Chamando o método web
		String result = this.sc.call("get_entry_list", parameters);
		
		// Tratando o retorno para devolver apenas o id
		try {
			JSONObject jsonResult  		= (JSONObject) new JSONTokener(result).nextValue();
			String entryList      		= jsonResult.getJSONArray("entry_list").get(0).toString();
			JSONObject jsonEntryList	= (JSONObject) new JSONTokener(entryList).nextValue();
			JSONObject jsonNameValueList= jsonEntryList.getJSONObject("name_value_list");
			JSONObject jsonId          	= jsonNameValueList.getJSONObject("id");
			equipamento_id 		   		= jsonId.getString("value").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return equipamento_id;
	}

	public String recuperarStatus(String id) throws JSONException {
		String status = "";
		
		// Definindo os parametros para chamar o método web
		String fields[] = {"status"};
		String select_fields = this.sc.toArrayData(fields);
		String parameters[][] = {
			{"session", this.session}, 
			{"module_name", "os_Equipamentos"},
			{"query", "os_equipamentos.id = '" + id + "'"},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "1"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};

		// Chamando o método web
		String result = this.sc.call("get_entry_list", parameters);

		// Tratando o retorno para devolver apenas o id
		try {
			JSONObject jsonResult  		= (JSONObject) new JSONTokener(result).nextValue();
			String entryList      		= jsonResult.getJSONArray("entry_list").get(0).toString();
			JSONObject jsonEntryList	= (JSONObject) new JSONTokener(entryList).nextValue();
			JSONObject jsonNameValueList= jsonEntryList.getJSONObject("name_value_list");
			JSONObject jsonStatus 		= jsonNameValueList.getJSONObject("status");
			status		 		   		= jsonStatus.getString("value").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
				
		return status;
	}

	public String atualizarStatus(String id, String status)	{
		// Definindo os parametros para chamar o método web
		/*
		"name_value_list":[
		{"name":"id","value":"431847da-9b3c-847e-ca18-4f0c27d443d0"},
		{"name":"status","value":"ativo"}]
		*/
		String name_value_list[][][] = {
			{{"name", "id"}, {"value", id}},
			{{"name", "status"}, {"value", status}}				
		};
		String value_list = this.sc.toRestData(name_value_list);
		
		String parameters[][] = {
				{"session", this.session}, 
				{"module_name", "os_Equipamentos"},
				{"name_value_list", value_list}
		};
		// Chamando o método web
		String result = this.sc.call("set_entry", parameters);
		return result;
	}

	public void recuperarRegistroBy(String where) {
		//String equipamento_id = "";
		String fields[] = {"name, status, endereco"};
		String select_fields = this.sc.toArrayData(fields);
		
		// Definindo os parametros para chamar o método web
		String parameters[][] = { 
			{"session", this.session}, 
			{"module_name", "os_Equipamentos"},
			{"query", where},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", select_fields}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "3"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};
					
		// Chamando o método web
		String result = this.sc.call("get_entry_list", parameters);
		
		// Tratando o retorno para devolver apenas a lista
		String split[];
		int result_count = 0;
		String entry_list, entry_list2;
		String name_value_list;
		String name_value_field;
		try {
			split = result.split(",", 3);
			result_count = Integer.parseInt(split[0].split(":")[1]);
			entry_list = split[2];
			entry_list2 = entry_list.split(":", 1)[1];
			name_value_list = entry_list2.split(",", 3).toString();
			name_value_field = name_value_list.split(",", 3).toString();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONObject jsonResult = null;
		JSONArray jsonEntryList = null;
		String entryList = null;
		JSONObject jsonEntryList2 = null;
		String nameValueList = "";
		JSONArray jsonNameValueList = null;
		JSONObject jsonNameValueField = null;
		String name, value;
		ArrayList <NameValuePair> equipamentos = new ArrayList<NameValuePair>();		
		try {
			jsonResult 		= (JSONObject) new JSONTokener(result).nextValue();
			jsonEntryList	= jsonResult.getJSONArray("entry_list");			
			for (int i = 0; i < jsonEntryList.length(); i++) {
				entryList 			= jsonEntryList.get(i).toString();
				jsonEntryList2		= (JSONObject) new JSONTokener(entryList).nextValue();
				jsonNameValueList 		= jsonEntryList2.getJSONArray("name_value_list");
				//jsonNameValueList   = (JSONArray) new JSONTokener(toJSONObject(jsonNameValueList)).nextValue();
				for (int j = 0; j < jsonNameValueList.length(); j++) {
					jsonNameValueField	= (JSONObject) jsonNameValueList.get(j);
					name		 		= jsonNameValueField.getString("name").toString();
					value				= jsonNameValueField.getString("value").toString();
					equipamentos.add(new BasicNameValuePair("name", name));
					equipamentos.add(new BasicNameValuePair("value", value));				
				}				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		
		return;
	}

}
