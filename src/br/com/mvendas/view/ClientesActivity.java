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
import br.com.mvendas.adapter.ClientesListAdapter;
import br.com.mvendas.dao.ClienteDao;
import br.com.mvendas.model.Cliente;

import com.google.inject.Inject;

@ContentView(R.layout.activity_clientes_listar)
public class ClientesActivity extends RoboActivity implements OnItemClickListener {
	
	private ClientesListAdapter adapter;
		
	@Inject
	private ClienteDao clienteDao;

	@InjectView(R.id.lvClientes)
	private ListView lvClientes;

	//@InjectResource(R.array.menu_opcoes)
	//private String [] menu_opcoes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Instancia o Adapter
		adapter = new ClientesListAdapter(getApplicationContext(),
				R.layout.adapter_cliente_item, null);
		// Configura o ListView
		lvClientes.setClickable(true);
		lvClientes.setOnItemClickListener(this);
		registerForContextMenu(lvClientes);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Obtem a lista dos clientes do SugarCRM
		List<Cliente> clientes = listarClientes();
		// Adiciona os clientes na tela
		adapter.newList(clientes);
		lvClientes.setAdapter(adapter);
	}

	private List<Cliente> listarClientes() {
		List<Cliente> clientes = null;
		try {
			// Criando um objeto do tipo Cliente
			ClienteDao clienteDao = new ClienteDao();
			clientes = clienteDao.listar("accounts.name like '%AMC%'");
		} catch (Exception e) {
			Log.e("Info", "Erro ao Listar Clientes");
			Log.e("Info", e.toString());
		}
    	return clientes;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		Cliente selecionado = (Cliente) adapter.getItem(position);		
		editar(selecionado);
	}

	private void editar(Cliente cliente) {
		if(cliente != null){
//			String idStr = String.valueOf(aluno.getId());			
			//Intent it = new Intent(this, FormActivity.class);
			//it.putExtra(FormActivity.INTENT_EXTRA_DATA_ALUNO, equipamento);			
			//startActivity(it);
		}
	}

}
