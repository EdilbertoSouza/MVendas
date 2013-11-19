package br.com.mvendas.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Cliente implements Parcelable {

	private String id;
	private String name;
	private String street;
	private String city;
	private String state;
	private String phone;
	private String email;
	private String website;
	
	public Cliente(String id, String nome, String telefone, String endereco,
			String cidade, String estado) {
		setId(id);
		setName(nome);
		setPhone(telefone);
		setStreet(endereco);
		setCity(cidade);
		setState(estado);		
	}
	
	public Cliente(Parcel in) {
		readFromParcel(in);
	}
	
	public Cliente(String cliente) {
		
		String[] linhas = cliente.split(";");
		
		for (int i = 0; i < linhas.length; i++) {
			String [] split = linhas[i].toString().split("=");
			String chave = split[0];
			String valor = split[1];
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

	//////////////////////////////////////////////////////////////
	// Parcelable
	
	public int describeContents() {
		return 0;
	}
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		//dest.writeParcelable(foto, flags);
		dest.writeString(name);
		dest.writeString(phone);
		dest.writeString(street);
		dest.writeString(city);
		dest.writeString(state);
	}
	
	private void readFromParcel(Parcel in) {
		id = in.readString();
		//foto = in.readParcelable(Bitmap.class.getClassLoader());
		name = in.readString();
		phone = in.readString();
		street = in.readString();
		city = in.readString();
		state = in.readString();
	}

	public static final Parcelable.Creator<Cliente> CREATOR = new Parcelable.Creator<Cliente>() {
		public Cliente createFromParcel(Parcel in) {
			return new Cliente(in);
		}

		public Cliente[] newArray(int size) {
			return new Cliente[size];
		}
	};

}
