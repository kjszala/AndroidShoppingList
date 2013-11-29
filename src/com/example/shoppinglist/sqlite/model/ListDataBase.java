package com.example.shoppinglist.sqlite.model;

public class ListDataBase {
	int id;
	String name;
	
	public ListDataBase(){
		
	}
	
	public ListDataBase(String name){
		this.name=name;
	}
	
	public ListDataBase(int id, String name){
		this.id = id;
		this.name = name;
	}
	
	//getters
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	//setters

	public void setId(int id){
		this.id=id;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
}
