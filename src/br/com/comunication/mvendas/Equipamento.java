package br.com.comunication.mvendas;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Equipamento {

	private SugarClient sc;
	private String session;

	public Equipamento(SugarClient sc) {
		this.sc = sc;
		this.session = this.sc.getSessionId();
	}

	public String recuperarId(String sitio) throws Exception {
		// Definindo os parametros para chamar o método web
		String parameters[][] = { 
			{"session", this.session}, 
			{"module_name", "os_Equipamentos"},
			{"query", "name='" + sitio + "'"},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", "['id']"}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "1"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};
					
		// Chamando o método web
		String result = this.sc.call("get_entry_list", parameters);
		
		// Tratando o retorno para devolver apenas o id
		JSONObject jsonData = (JSONObject) new JSONTokener(result).nextValue();	
		String entry_list = jsonData.getJSONArray("entry_list").get(0).toString();		
		JSONObject jsonData2 = (JSONObject) new JSONTokener(entry_list).nextValue();		
		String equipamento_id = jsonData2.getString("id").toString();
				
		return equipamento_id;
	}

	public String recuperarStatus(String id) throws JSONException {
		// Definindo os parametros para chamar o método web
		String parameters[][] = {
			{"session", this.session}, 
			{"module_name", "os_Equipamentos"},
			{"query", "os_equipamentos.id = '" + id + "'"},
			{"order_by", ""},
			{"offset", "0"},
			{"select_fields", "['status']"}, 
			{"link_name_to_fields_array", "[]"}, 
			{"max_results", "1"},
			{"deleted", "0"},
			{"Favorites", "false"}
		};

		// Chamando o método web
		String result = this.sc.call("get_entry_list", parameters);

		// Retornando o status do equipamento
        //$equipamento_id = $result->entry_list[0]->id;
        
		//$fields         = $result->entry_list[0]->name_value_list;
		//$status         = $fields->status->value;
				
		return result;
	}

	public String atualizarStatus(String id, String status)	{
		// Definindo os parametros para chamar o método web
		//"name_value_list":[{"name":"id","value":"431847da-9b3c-847e-ca18-4f0c27d443d0"},{"name":"status","value":"ativo"}]
		/*
		String name_value_list[][][] = {
			{{"name", "id"}, {"value", id}},
			{{"name", "status"}, {"value", status}}				
		};
		String s = this.sc.toRestData(name_value_list);
		*/
		String parameters[][] = {
				{"session", this.session}, 
				{"module_name", "os_Equipamentos"},
				{"name_value_list", "[{'name':'id','value':"+id+"}," +
						"             {'name':'status','value':"+status+"}]"}
		};
		// Chamando o método web
		String result = this.sc.call("set_entry", parameters);
		return result;
	}

	/*
	function recuperarRegistro($id)
	{
		// Definindo os parametros para chamar o mÃ©todo web
		$parameters = array(
				'session' => $this->session,
				'module_name' => "os_Equipamentos",
				'query' => "os_equipamentos.id = '" . $id . "'",
				'order_by' => "",
				'offset' => '0',
				'select_fields' => "",
				'link_name_to_fields_array' => array(),
				'max_results' => 1,
				'deleted' => '0',
				'Favorites' => false,
				);

		// Chamando o mÃ©todo web
		$result = $this->sc->call("get_entry_list", $parameters);

		// Retornando o registro do equipamento
		$fields = $result->entry_list[0]->name_value_list;
		return $fields;
	}
	*/
}
