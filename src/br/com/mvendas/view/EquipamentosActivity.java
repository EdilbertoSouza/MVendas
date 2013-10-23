package br.com.mvendas.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import br.com.example.mvendas.R;

public class EquipamentosActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipamentos);
	}

	public void listarEquipamentosClick(View v) {
		Intent i = new Intent(this, ListarEquipamentosActivity.class);
		startActivity(i);		
	}


}
