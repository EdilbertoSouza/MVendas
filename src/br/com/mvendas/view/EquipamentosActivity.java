package br.com.mvendas.view;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import br.com.example.mvendas.R;
import br.com.mvendas.adapter.EquipamentosListAdapter;
import br.com.mvendas.dao.EquipamentoDao;
import br.com.mvendas.model.Equipamento;

import com.google.inject.Inject;

@ContentView(R.layout.activity_equipamentos_listar)
public class EquipamentosActivity extends RoboActivity implements OnItemClickListener {
	
	private EquipamentosListAdapter adapter;
		
	@Inject
	private EquipamentoDao equipamentoDao;

	@InjectView(R.id.lvEquipamentos)
	private ListView lvEquipamentos;

	//@InjectResource(R.array.menu_opcoes)
	//private String [] menu_opcoes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Instancia o Adapter
		adapter = new EquipamentosListAdapter(getApplicationContext(),
				R.layout.adapter_equipamento_item, null);
		// Configura o ListView
		lvEquipamentos.setClickable(true);
		lvEquipamentos.setOnItemClickListener(this);
		registerForContextMenu(lvEquipamentos);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Obtem a lista dos equipamentos do SugarCRM
		List<Equipamento> equipamentos = listarEquipamentos();
		// Adiciona os equipamentos na tela
		adapter.newList(equipamentos);
		lvEquipamentos.setAdapter(adapter);
	}

	private List<Equipamento> listarEquipamentos() {
		List<Equipamento> equipamentos = null;
		try {
			// Criando um objeto do tipo Equipamento
			EquipamentoDao equipamentoDao = new EquipamentoDao();
			equipamentos = equipamentoDao.listar("name like 'FS%'");
		} catch (Exception e) {
			Log.e("Info", "Erro ao Listar Equipamentos");
			Log.e("Info", e.toString());
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

	/*
	@SuppressWarnings("unused")
	private void testarClientSugar() {
		String sitio = "";
		String equipamento_id = "";
		String status = "";
		String novo_status = "";
		// Criando um objeto do tipo SugarClient
		SugarClientSingleton sc = SugarClientSingleton.getInstance();
		try {
			// Criando um objeto do tipo Equipamento
			EquipamentoDao equipamentoDao = new EquipamentoDao();
			// Recuperando o id e status atual
			sitio = "FS001";
			equipamento_id = equipamentoDao.recuperarId(sitio);
			// Verificando o status para poder alternar entre ativo e inativo
			status = equipamentoDao.recuperarStatus(equipamento_id);
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
			//Log.i("Info", "Endereco: " + registro.endereco.value);
			// Listando equipamentos
			//List<Equipamento> equipamentos = equipamentoDao.listar("name like 'FS00%'");
		} catch (Exception e) {
			Log.e("Info", "testarClienteSugar com erro");
			e.getMessage();
			e.printStackTrace();
		}
	}
	*/
}
