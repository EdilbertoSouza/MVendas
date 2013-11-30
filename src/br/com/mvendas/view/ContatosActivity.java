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
import br.com.mvendas.adapter.ContatosListAdapter;
import br.com.mvendas.dao.ContatoDao;
import br.com.mvendas.model.Contato;
import br.com.mvendas.utils.Sms;

import com.google.inject.Inject;

@ContentView(R.layout.activity_contatos_listar)
public class ContatosActivity extends RoboActivity implements OnItemClickListener {
	
	private ContatosListAdapter adapter;
	private List<Contato> contatos = null;
		
	@Inject
	private ContatoDao contatoDao;

	@InjectView(R.id.lvContatos)
	private ListView lvContatos;

	@InjectResource(R.array.contatos_menu_opcoes)
	private String [] menu_opcoes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Instancia o Adapter
		adapter = new ContatosListAdapter(getApplicationContext(), R.layout.adapter_contato_item, null);
		// Configura o ListView
		lvContatos.setClickable(true);
		lvContatos.setOnItemClickListener(this);
		registerForContextMenu(lvContatos);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Obtem a lista dos contatos do SugarCRM
		listarContatos();
		if (contatos == null) {
			Toast.makeText(ContatosActivity.this, "Não foi possivel recuperar contatos", Toast.LENGTH_LONG).show();
			super.onBackPressed();
		} else {
			// Adiciona os contatos na tela
			adapter.newList(contatos);
			lvContatos.setAdapter(adapter);			
		}			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.contatos, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_novo:
			// Inicia a Activity do Formulario
			startActivity(new Intent(this, ContatosFormActivity.class));
			break;
		case R.id.menu_buscar:
			// Inicia a Activity do Buscar
			//startActivity(new Intent(this, ContatosBuscarActivity.class));			
			break;
		}		
		return true;
	}
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {    	
    	if(v.getId() == R.id.lvContatos){    		
			AdapterView.AdapterContextMenuInfo info = 
					(AdapterView.AdapterContextMenuInfo) menuInfo;
			ContatosListAdapter adapter = (ContatosListAdapter) lvContatos.getAdapter();				
			menu.setHeaderTitle(adapter.getContatos().get(info.position).getName());
			
			for (int i = 0; i < menu_opcoes.length; i++) {
				menu.add(Menu.NONE, i, i, menu_opcoes[i]);
			}		
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	ContatosListAdapter adapter = (ContatosListAdapter) lvContatos.getAdapter();				
				
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		final Contato selecionado = adapter.getContatos().get(info.position);
		
		int menuItemIndex = item.getItemId();
		String menuItemName = menu_opcoes[menuItemIndex];
		
//		String listItemName = selecionado.getName();
//		Log.i("info", "contato " + String.format("Selected %s for item %s", menuItemName, listItemName));
		
		if(selecionado != null){					
			if(menuItemName.equalsIgnoreCase("Fazer uma Ligação")){
				fazerLigacao(selecionado);
				
			} else if(menuItemName.equalsIgnoreCase("Enviar SMS")){						
				enviarSms(selecionado);				
				
			} else if(menuItemName.equalsIgnoreCase("Buscar no Mapa")){
				localizarEndereco(selecionado);
				
			} else if(menuItemName.equalsIgnoreCase("Editar")){
				editar(selecionado);
							
			}
		}
		
		return true;
    }

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {		
		Contato selecionado = (Contato) adapter.getItem(position);		
		editar(selecionado);
	}
		
	private void listarContatos() { // List<Contato>
		try {
			ContatoDao contatoDao = new ContatoDao();
			contatos = contatoDao.listar("contacts.assigned_user_id = 'f38e2557-d7f2-6ce2-ea05-528e97fd1519'");
		} catch (Exception e) {
			Log.e("Info", "Erro ao Listar Contatos. Motivo: " + e.getMessage());
		}		
	}

	private void fazerLigacao(final Contato selecionado) {
		Uri uri = Uri.parse("tel:" + selecionado.getPhone());
		Intent it = new Intent(Intent.ACTION_CALL, uri);
		startActivity(it);
	}

	private void localizarEndereco(final Contato selecionado) {
		String endereco = selecionado.getStreet().trim() + ",Fortaleza,CE";
		endereco = endereco.replace(" ", "+");
		
		Uri uri = Uri.parse("geo:0,0?q=" + endereco);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(it);
	}

	private void enviarSms(final Contato selecionado) {
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
	
	private void editar(Contato contato) {
		if(contato != null){			
			//String id = contato.getId();
			Intent it = new Intent(this, ContatosFormActivity.class);
			it.putExtra(ContatosFormActivity.INTENT_EXTRA_DATA_CONTATO, contato);			
			startActivity(it);
		}
	}

	/*
	private void remover(final Contato contato) {
		if(contato != null){
			
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Confirmação");
			alert.setMessage("Deseja remover o(a) contato(a) " + contato.getName() + "?");
			
			alert.setPositiveButton("Sim", new OnClickListener() {				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//contatoDao.deletar(contato);					
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
