package com.abdurrozaq.sistem_informasi.berita.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BeritaModel{

	@SerializedName("success")
	private Integer success;

	@SerializedName("berita")
	private List<BeritaItem> berita;

	public void setSuccess(Integer success){
		this.success = success;
	}

	public Integer getSuccess(){
		return success;
	}

	public void setBerita(List<BeritaItem> berita){
		this.berita = berita;
	}

	public List<BeritaItem> getBerita(){
		return berita;
	}
}