package com.abdurrozaq.sistem_informasi.santri.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseSantri{

	@SerializedName("success")
	private Integer success;

	@SerializedName("santri")
	private List<SantriItem> santri;

	public void setSuccess(Integer success){
		this.success = success;
	}

	public Integer getSuccess(){
		return success;
	}

	public void setSantri(List<SantriItem> santri){
		this.santri = santri;
	}

	public List<SantriItem> getSantri(){
		return santri;
	}
}