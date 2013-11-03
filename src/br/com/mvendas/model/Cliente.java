package br.com.mvendas.model;

import java.util.List;

import android.util.Log;

public class Cliente {

	private String id;
	private String name;
	private String street;
	private String city;
	private String state;
	private String phone;
	private String email;
	private String website;
	
	public Cliente(String[] campos, List<String> valores) {
		String chave;
		String valor;
		try {
			for (int i = 0; i < campos.length; i++) {
				chave = campos[i];
				if (i < valores.size()) {
					valor = valores.get(i);
				} else {
					valor = "";
				}
				if(chave.equalsIgnoreCase("id")){
					setId(valor);
				} if(chave.equalsIgnoreCase("name")){
					setName(valor);
				} if(chave.equalsIgnoreCase("billing_address_street")){
					setStreet(valor);
				} if(chave.equalsIgnoreCase("billing_address_city")){
					setCity(valor);
				} if(chave.equalsIgnoreCase("billing_address_state")){
					setState(valor);
				} if(chave.equalsIgnoreCase("phone_office")){
					setPhone(valor);
				} if(chave.equalsIgnoreCase("email")){
					setEmail(valor);
				} if(chave.equalsIgnoreCase("website")){
					setWebsite(valor);
				}			
			}			
		} catch (Exception e) {
			Log.e("info", "Erro ao criar objeto Cliente");
		}
	}

	public Cliente(String[] campos, String[] valores) {
		String chave;
		String valor;
		try {
			for (int i = 0; i < campos.length; i++) {
				chave = campos[i];
				if (i < valores.length) {
					valor = valores[i];
				} else {
					valor = "";
				}
				if(chave.equalsIgnoreCase("id")){
					setId(valor);
				} if(chave.equalsIgnoreCase("name")){
					setName(valor);
				} if(chave.equalsIgnoreCase("billing_address_street")){
					setStreet(valor);
				} if(chave.equalsIgnoreCase("billing_address_city")){
					setCity(valor);
				} if(chave.equalsIgnoreCase("billing_address_state")){
					setState(valor);
				} if(chave.equalsIgnoreCase("phone_office")){
					setPhone(valor);
				} if(chave.equalsIgnoreCase("email")){
					setEmail(valor);
				} if(chave.equalsIgnoreCase("website")){
					setWebsite(valor);
				}			
			}			
		} catch (Exception e) {
			Log.e("info", "Erro ao criar objeto Cliente");
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
		
}
