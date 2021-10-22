package com.abdurrozaq.sistem_informasi.album.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseAlbum{

	@SerializedName("album")
	private List<AlbumItem> album;

	@SerializedName("success")
	private Integer success;

	public void setAlbum(List<AlbumItem> album){
		this.album = album;
	}

	public List<AlbumItem> getAlbum(){
		return album;
	}

	public void setSuccess(Integer success){
		this.success = success;
	}

	public Integer getSuccess(){
		return success;
	}
}