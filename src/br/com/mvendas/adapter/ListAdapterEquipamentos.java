package br.com.mvendas.adapter;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.example.mvendas.R;
import br.com.mvendas.model.Equipamento;

public class ListAdapterEquipamentos extends RoboBaseAdapter {
	
	@InjectView(R.id.adapter_equipamento_item_tvItem)
	private TextView adapter_equipamento_item_tvItem;
	

	private List<Equipamento> equipamentos;

	public ListAdapterEquipamentos(Context context, int layoutId, List<Equipamento> equipamentos) {
		super(context, layoutId);
		
		this.equipamentos = (equipamentos == null) ? new ArrayList<Equipamento>() : equipamentos;
	}
	
	@Override
	public int getCount() {
		return equipamentos.size();
	}

	@Override
	public Object getItem(int position) {
		return equipamentos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<Equipamento> getEquipamentos() {
		return this.equipamentos;
	}
	
	public void add(Equipamento equipamento) {
		this.equipamentos.add(equipamento);
	}
	
	public void addAll(List<Equipamento> equipamentos) {
		this.equipamentos = (equipamentos == null) ? new ArrayList<Equipamento>() : equipamentos;
	}
	
	public void clear() {
		this.equipamentos.clear();
	}
	
	public void newList(List<Equipamento> equipamentos) {
		clear();
		addAll(equipamentos);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		
		Equipamento equipamento = (Equipamento) getItem(position);
		adapter_equipamento_item_tvItem.setText(equipamento.getName());
		
		return view;
	}

}
