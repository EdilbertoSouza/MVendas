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
import android.widget.Toast;
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
		if (equipamentos == null) {
			Toast.makeText(EquipamentosActivity.this, "Não foi possivel recuperar produtos", Toast.LENGTH_LONG).show();
			super.onBackPressed();
		} else {
			// Adiciona os equipamentos na tela
			adapter.newList(equipamentos);
			lvEquipamentos.setAdapter(adapter);			
		}
	}

	private List<Equipamento> listarEquipamentos() {
		List<Equipamento> equipamentos = null;
		try {
			// Criando um objeto do tipo Equipamento
			EquipamentoDao equipamentoDao = new EquipamentoDao();
			equipamentos = equipamentoDao.listar("name+like+'FS%'");
		} catch (Exception e) {
			Log.e("Info", "Erro ao Listar Equipamentos. Motivo: " + e.getMessage());
		}
		return equipamentos;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Equipamento selecionado = (Equipamento) adapter.getItem(position);		
		editar(selecionado);
	}

	private void editar(Equipamento equipamento) {
		Toast.makeText(EquipamentosActivity.this, "Rotina a Implementar...", Toast.LENGTH_LONG).show();
	}

}
