package br.com.mvendas.view;

import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import br.com.example.mvendas.R;
import br.com.mvendas.adapter.ClientesListAdapter;
import br.com.mvendas.dao.ClienteDao;
import br.com.mvendas.model.Cliente;
import br.com.mvendas.utils.Sms;

import com.google.inject.Inject;

@ContentView(R.layout.activity_clientes_listar)
public class ClientesActivity extends RoboActivity implements OnItemClickListener {
	
	private ClientesListAdapter adapter;
		
	@Inject
	private ClienteDao clienteDao;

	@InjectView(R.id.lvClientes)
	private ListView lvClientes;

	@InjectResource(R.array.cliente_menu_opcoes)
	private String [] menu_opcoes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Instancia o Adapter
		adapter = new ClientesListAdapter(getApplicationContext(), R.layout.adapter_cliente_item, null);
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
		if (clientes == null) {
			Toast.makeText(ClientesActivity.this, "Não foi possivel recuperar clientes", Toast.LENGTH_LONG).show();
			super.onBackPressed();
		} else {
			// Adiciona os clientes na tela
			adapter.newList(clientes);
			lvClientes.setAdapter(adapter);			
		}			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.clientes, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_novo:
			// Inicia a Activity do Formulario
			startActivity(new Intent(this, ClientesFormActivity.class));			
			break;
		case R.id.menu_buscar:
			// Inicia a Activity do Buscar
			startActivity(new Intent(this, ClientesBuscarActivity.class));			
			break;
		}		
		return true;
	}
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	
    	if(v.getId() == R.id.lvClientes){    		
			AdapterView.AdapterContextMenuInfo info = 
					(AdapterView.AdapterContextMenuInfo) menuInfo;
			ClientesListAdapter adapter = (ClientesListAdapter) lvClientes.getAdapter();				
			menu.setHeaderTitle(adapter.getClientes().get(info.position).getName());
			
			for (int i = 0; i < menu_opcoes.length; i++) {
				menu.add(Menu.NONE, i, i, menu_opcoes[i]);
			}		
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	ClientesListAdapter adapter = (ClientesListAdapter) lvClientes.getAdapter();				
				
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		final Cliente selecionado = adapter.getClientes().get(info.position);
		
		int menuItemIndex = item.getItemId();
		String menuItemName = menu_opcoes[menuItemIndex];
		
//		String listItemName = selecionado.getName();
//		Log.i("info", "cliente " + String.format("Selected %s for item %s", menuItemName, listItemName));
		
		if(selecionado != null){					
			if(menuItemName.equalsIgnoreCase("Fazer uma Ligação")){
				fazerLigacao(selecionado);
				
			} else if(menuItemName.equalsIgnoreCase("Enviar SMS")){						
				enviarSms(selecionado);				
				
			} else if(menuItemName.equalsIgnoreCase("Buscar no Mapa")){
				localizarEndereco(selecionado);
				
			} else if(menuItemName.equalsIgnoreCase("Exibir Site")){
				exibirSite(selecionado);

			} else if(menuItemName.equalsIgnoreCase("Editar")){
				editar(selecionado);
							
			}
		}
		
		return true;
    }

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {		
		Cliente selecionado = (Cliente) adapter.getItem(position);		
		editar(selecionado);
	}
	
	private List<Cliente> listarClientes() {
		List<Cliente> clientes = null;
		try {
			ClienteDao clienteDao = new ClienteDao();
			clientes = clienteDao.listar("Accounts.assigned_user_id = 'f38e2557-d7f2-6ce2-ea05-528e97fd1519'");
		} catch (Exception e) {
			Log.e("Info", "Erro ao Listar Clientes. Motivo: " + e.getMessage());
		}
		return clientes;
	}

	private void fazerLigacao(final Cliente selecionado) {
		Uri uri = Uri.parse("tel:" + selecionado.getPhone());
		Intent it = new Intent(Intent.ACTION_CALL, uri);
		startActivity(it);
	}

	private void localizarEndereco(final Cliente selecionado) {
		String endereco = selecionado.getStreet().trim() + ",Fortaleza,CE";
		endereco = endereco.replace(" ", "+");
		
		Uri uri = Uri.parse("geo:0,0?q=" + endereco);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
	}

	private void enviarSms(final Cliente selecionado) {
		// Obtem a GUI do XML
		LayoutInflater li = getLayoutInflater();
		View dialogSms = li.inflate(R.layout.dialog_sms, null);
		Button btDialogEnviar = (Button) dialogSms.findViewById(R.id.btDialogEnviar);
		final EditText etDialogEnviar = (EditText) dialogSms.findViewById(R.id.etDialogEnviar);
		
		// Instancia um AlertDialog com o layout definido no XML
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("SMS para " + selecionado.getName());
		builder.setView(dialogSms);
		
		// Se torna visivel para o usuario
		final AlertDialog alerta = builder.create();
		alerta.show();

		// Evento ao clicar no botao enviar SMS
		btDialogEnviar.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View arg0) {
		    	String texto = etDialogEnviar.getText().toString().trim();

				// Enviar um SMS para o numero indicado		    	
		    	boolean isEnviado = Sms.enviarSms(getApplicationContext(), selecionado.getPhone(), texto);
				
				if(isEnviado){
					Toast.makeText(getApplicationContext(), "Mensagem enviada!", Toast.LENGTH_SHORT).show();
				} else{
					Toast.makeText(getApplicationContext(), "Falha ao enviar a mensagem!", Toast.LENGTH_SHORT).show();
				}
				
				// Vibra apos o envio
				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(new long[]{ 100, 250, 100, 500 }, -1);
				
				// Fecha a tela de envio de SMS
		        alerta.dismiss();
		    }
		});
	}
	
	private void exibirSite(Cliente cliente) {
		if(cliente != null){			
			String site = cliente.getWebsite();
			Uri uri = Uri.parse(site);
			Intent it = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(it);			
		}
	}

	private void editar(Cliente cliente) {
		if(cliente != null){			
			//String id = cliente.getId();			
			Intent it = new Intent(this, ClientesFormActivity.class);
			it.putExtra(ClientesFormActivity.INTENT_EXTRA_DATA_CLIENTE, cliente);			
			startActivity(it);
		}
	}

	/*
	private void remover(final Cliente cliente) {
		if(cliente != null){
			
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Confirmação");
			alert.setMessage("Deseja remover o(a) cliente(a) " + cliente.getName() + "?");
			
			alert.setPositiveButton("Sim", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//clienteDao.deletar(cliente);					
					onResume();
				}
			});
			
			alert.setNegativeButton("Não", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			alert.show();
		}
	}
	*/
	
}
