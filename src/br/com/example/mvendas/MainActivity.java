package br.com.example.mvendas;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button btClientes = (Button) findViewById(R.id.btClientes);  
        
		btClientes.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
    			//startActivity(new Intent(this, ClientesActivity.class));
    			Uri uri = Uri.parse("http://www.developer.android.com"); 
    			Intent it = new Intent(Intent.ACTION_VIEW, uri);     			
    			startActivity(it); 
            }  
        });  
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
		
}
