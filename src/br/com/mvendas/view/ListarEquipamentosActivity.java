package br.com.mvendas.view;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.example.mvendas.R;
import br.com.mvendas.adapter.ListAdapterEquipamentos;
import br.com.mvendas.comunication.SugarClientSingleton;
import br.com.mvendas.dao.EquipamentoDao;
import br.com.mvendas.model.Equipamento;

import com.google.inject.Inject;

@ContentView(R.layout.activity_equipamentos_listar)
public class ListarEquipamentosActivity extends RoboActivity implements OnItemClickListener {
//	extends RoboActivity implements OnItemClickListener {
	
	ProgressDialog dialog;

	private ListAdapterEquipamentos adapter = null;
		
	@Inject
	private EquipamentoDao equipamentoDao;

	@InjectView(R.id.lvEquipamentos)
	private ListView lvEquipamentos;

	//@InjectResource(R.array.menu_opcoes)
	//private String [] menu_opcoes;

	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipamentos_listar);

		dialog = ProgressDialog.show(ListarEquipamentosActivity.this, "", "Listando Equipamentos...", true); 
		new Thread(this).start();		
	}
	*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Instancia o Adapter
		adapter = new ListAdapterEquipamentos(getApplicationContext(),
				R.layout.adapter_equipamento_item, null);

		// Configura o ListView
		lvEquipamentos.setClickable(true);
		lvEquipamentos.setOnItemClickListener(this);
		registerForContextMenu(lvEquipamentos);
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		dialog = ProgressDialog.show(ListarEquipamentosActivity.this, "", "Listando Equipamentos...", true); 

		// Obtem a lista dos equipamentos do SugarCRM
		List<Equipamento> equipamentos = listarEquipamentos();
		
		// Adiciona os equipamentos na tela
		adapter.newList(equipamentos);
		lvEquipamentos.setAdapter(adapter);

		dialog.dismiss();
	}
	
	
	// Método da classe Runnable, executa ao iniciar a classe.	
	//public void run() { listarEquipamentos();	}

	private List<Equipamento> listarEquipamentos() {
		List<Equipamento> equipamentos = null;
		// Criando um objeto do tipo SugarClient
		SugarClientSingleton sc = SugarClientSingleton.getInstance();
		try {
			// Entrando no sistema
			String session = sc.login("fotomonitor", "fotomon");			
			Log.i("info", "sessao = " + session);
			// Criando um objeto do tipo Equipamento
			EquipamentoDao equipamentoDao = new EquipamentoDao();
			equipamentos = equipamentoDao.listar("name like 'FS00%'");
			// Saindo do sistema
			sc.logout();
		} catch (Exception e) {
			Log.e("Info", e.toString());
			e.getMessage();
			e.printStackTrace();
		}
    	return equipamentos;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Equipamento selecionado = (Equipamento) adapter.getItem(position);		
		editar(selecionado);
	}

	private void editar(Equipamento equipamento) {
		if(equipamento != null){
//			String idStr = String.valueOf(aluno.getId());			
			//Intent it = new Intent(this, FormActivity.class);
			//it.putExtra(FormActivity.INTENT_EXTRA_DATA_ALUNO, equipamento);			
			//startActivity(it);
		}
	}

	@SuppressWarnings("unused")
	private void testarClientSugar() {
		String sitio = "";
		String equipamento_id = "";
		String status         = "";
		// Criando um objeto do tipo SugarClient
		SugarClientSingleton sc = SugarClientSingleton.getInstance();
		try {
			// Entrando no sistema
			String session = sc.login("fotomonitor", "fotomon");			
			Log.i("info", "sessao = " + session);
			// Criando um objeto do tipo Equipamento
			EquipamentoDao equipamentoDao = new EquipamentoDao();
			// Recuperando o id e status atual
			sitio = "FS001";
			equipamento_id = equipamentoDao.recuperarId(sitio);
//			Log.i("Info", "Sitio: " + sitio);
//			Log.i("Info", "ID: " + equipamento_id);
			
/*			sitio = "FS005";
			equipamento_id = equipamento.recuperarId(sitio);
			Log.i("Info", "Sitio: " + sitio);
			Log.i("Info", "ID: " + equipamento_id);			
*/			
			status = equipamentoDao.recuperarStatus(equipamento_id);
			// Verificando o status para poder alternar entre ativo e inativo
			String novo_status;
			if (status.equals("ativo")) {
			      novo_status = "Inativo";
			} else {
			      novo_status = "ativo";
			}
			// Atualizando o status do equipamento
			equipamentoDao.atualizarStatus(equipamento_id, novo_status);
			// Exibindo id, sitio, status anterior e atual
			Log.i("Info", "ID: " + equipamento_id);
			Log.i("Info", "Sitio: " + sitio);
			Log.i("Info", "Status Anterior: " + status);
			Log.i("Info", "Status Atual...: " + novo_status);
			// Recuperando o registro pelo id
			//registro = equipamento.recuperarRegistro(equipamento_id);
			List<Equipamento> equipamentos = equipamentoDao.listar("name like 'FS00%'");
			// Exibindo id, sitio, status anterior e atual
			//Log.i("Info", "Endereco: " + registro.endereco.value);
			// Saindo do sistema
			sc.logout();
		} catch (Exception e) {
			Log.e("Info", "testarClienteSugar com erro");
			e.getMessage();
			e.printStackTrace();
		}
	}

}
