package com.abdurrozaq.sistem_informasi.alamat;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseAlamat{

	@SerializedName("provinsi")
	private List<ProvinsiItem> provinsi;

	public void setProvinsi(List<ProvinsiItem> provinsi){
		this.provinsi = provinsi;
	}

	public List<ProvinsiItem> getProvinsi(){
		return provinsi;
	}
}