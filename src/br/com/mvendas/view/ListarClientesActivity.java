package br.com.mvendas.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import br.com.example.mvendas.R;
import br.com.mvendas.comunication.SugarClientSingleton;
import br.com.mvendas.dao.EquipamentoDao;

public class ListarClientesActivity extends Activity implements Runnable {
	
	ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clientes_listar);
				
		dialog = ProgressDialog.show(ListarClientesActivity.this, "", "Listando Clientes...", true); 
		new Thread(this).start();		
	}
	
	// Método da classe Runnable, executa ao iniciar a classe.	
	public void run() { testarWS();	}

	public void testarWS() {
    	
		testarClientSugar();
    	dialog.dismiss();
	}
	
	/**
	 * 
	 */
	private void testarClientSugar() {
		String sitio = "";
		String equipamento_id = "";
		String status         = "";
		// Criando um objeto do tipo SugarClient
		SugarClientSingleton sc = SugarClientSingleton.getInstance("http://10.0.2.2/sugardev/service/v2/rest.php");
		try {
			// Entrando no sistema
			String session = sc.login("fotomonitor", "fotomon");			
			Log.i("info", "sessao = " + session);
			// Criando um objeto do tipo Equipamento
			EquipamentoDao equipamento = new EquipamentoDao(sc);
			// Recuperando o id e status atual
			sitio = "FS001";
			equipamento_id = equipamento.recuperarId(sitio);
//			Log.i("Info", "Sitio: " + sitio);
//			Log.i("Info", "ID: " + equipamento_id);
			
/*			sitio = "FS005";
			equipamento_id = equipamento.recuperarId(sitio);
			Log.i("Info", "Sitio: " + sitio);
			Log.i("Info", "ID: " + equipamento_id);			
*/			
			status = equipamento.recuperarStatus(equipamento_id);
			// Verificando o status para poder alternar entre ativo e inativo
			String novo_status;
			if (status.equals("ativo")) {
			      novo_status = "Inativo";
			} else {
			      novo_status = "ativo";
			}
			// Atualizando o status do equipamento
			equipamento.atualizarStatus(equipamento_id, novo_status);
			// Exibindo id, sitio, status anterior e atual
			Log.i("Info", "ID: " + equipamento_id);
			Log.i("Info", "Sitio: " + sitio);
			Log.i("Info", "Status Anterior: " + status);
			Log.i("Info", "Status Atual...: " + novo_status);
			// Recuperando o registro pelo id
			//registro = equipamento.recuperarRegistro(equipamento_id);
			equipamento.recuperarRegistroBy("name in ('FS001','FS005')");
			// Exibindo id, sitio, status anterior e atual
			//Log.i("Info", "Endereco: " + registro.endereco.value);
			// Saindo do sistema
			sc.logout();
		} catch (Exception e) {
			Log.e("Erro", "testarClienteSugar com erro");
			e.getMessage();
			//e.printStackTrace();
		}
	}


}
