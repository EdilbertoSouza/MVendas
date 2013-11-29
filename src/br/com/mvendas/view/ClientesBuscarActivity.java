package br.com.mvendas.view;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.example.mvendas.R;
import br.com.mvendas.dao.ClienteDao;
import br.com.mvendas.model.Cliente;

import com.google.inject.Inject;

@ContentView(R.layout.activity_clientes_buscar)
public class ClientesBuscarActivity extends RoboActivity implements OnClickListener {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 100;
	private String idioma = "Default";
    
	@Inject
	private ClienteDao clienteDao;
	
	@InjectView(R.id.etBuscarCliente)
	private EditText etBuscar;

	@InjectView(R.id.clientes_buscar_spIdioma)
	private Spinner spIdioma;

	@InjectView(R.id.clientes_buscar_imgBtVoz)
	private ImageButton imgBtVoz;

	@InjectView(R.id.btBuscarCliente)
	private Button btBuscar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		carregaConfiguracoesDeVoz();
		
		//imgBtVoz.setOnClickListener(this);
		btBuscar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == btBuscar){
			buscarCliente();			
		} else if(v == imgBtVoz){
			startVoiceRecognitionActivity();			
		}		
	}

	private void buscarCliente() {
		String valorParaBuscar = etBuscar.getText().toString().trim();
		Cliente cliente = null;
		
		cliente = clienteDao.buscar(valorParaBuscar);
		
		if(cliente != null){
			Intent it = new Intent(this, ClientesFormActivity.class);
			it.putExtra(ClientesFormActivity.INTENT_EXTRA_DATA_CLIENTE, cliente);
			
			startActivity(it);
		} else{
			Toast.makeText(getApplicationContext(), "Cliente não encontrado: " + valorParaBuscar, Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		
		// Especifica o pacote chamado para identificar sua aplicacao
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
		
		// Mostra uma janela para o usuario falar
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "");
		
		// Dado uma dica para reconhecer o que o usuario vai dizer
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		
		// Especifica o numero max de resultados recebidos.
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
		
		idioma = spIdioma.getSelectedItem().toString();
		
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, idioma);
		
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK){
			ArrayList<String> matches = data.getStringArrayListExtra(
					RecognizerIntent.EXTRA_RESULTS);
			
			selecionarOpcao(matches);
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void carregaConfiguracoesDeVoz() {
		sendOrderedBroadcast(RecognizerIntent.getVoiceDetailsIntent(getApplicationContext()), null,
				receiver, null, Activity.RESULT_OK, null, null);
	}

	private void updateSupportedLanguages(List<String> languages){
		languages.add(0, "Default");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_spinner_item, languages);		
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spIdioma.setAdapter(adapter);
	}
	
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context paramContext, Intent intent) {
			final Bundle extra = getResultExtras(false);
			
			if (getResultCode() != Activity.RESULT_OK) {
				Log.e("info", "Error code:" + getResultCode());
            }

            if (extra == null) {
            	Log.d("info", "No extra!");
            }

            if (extra.containsKey(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)) {
            	// Lista os idiomas que o dispositivo suporta
                updateSupportedLanguages(extra.getStringArrayList(
                        RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES));
            }

            if (extra.containsKey(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE)) {
            	// Mostra o idioma obtido a partir do que está no dispositivo
            	Log.d("info", "Default: " + extra.getString(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE));
            }
        }
	};

	private void selecionarOpcao(List<String> matches) {
		LayoutInflater li = getLayoutInflater();
		View dialogVoz = li.inflate(R.layout.dialog_voz, null);
		ListView lvVoz = (ListView) dialogVoz.findViewById(R.id.lvVoz);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(),
				android.R.layout.simple_list_item_1, matches);
		
		lvVoz.setAdapter(adapter);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Selecione");
		builder.setView(dialogVoz);
		final AlertDialog alerta = builder.create();
		alerta.show();

		lvVoz.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position,
					long id) {
				
				String selecionado = (String) adapterView.getAdapter().getItem(position);				
				etBuscar.setText(selecionado);
				
		        alerta.dismiss();
			}
		});
	}
}
