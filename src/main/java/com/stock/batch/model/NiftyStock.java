package com.stock.batch.model;

import java.io.Serializable;

public class NiftyStock implements Serializable{
	private static final long serialVersionUID = -640206892361458778L;
	
	private int id;
	private String stockName;
	private String stockPrice;
	private String stockChangeDate;
	public NiftyStock()
	{
		
	}
	public NiftyStock(String stockName, String stockPrice, String stockChangeDate) {
		super();
		this.stockName = stockName;
		this.stockPrice = stockPrice;
		this.stockChangeDate = stockChangeDate;
	}
	
	public NiftyStock(int id,String stockName, String stockPrice, String stockChangeDate) {
		super();
		this.stockName = stockName;
		this.stockPrice = stockPrice;
		this.stockChangeDate = stockChangeDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStockName() {
		return stockName;
	}
	public void setStockName(String stockName) {
		this.stockName = stockName;
	}
	public String getStockPrice() {
		return stockPrice;
	}
	public void setStockPrice(String stockPrice) {
		this.stockPrice = stockPrice;
	}
	public String getStockChangeDate() {
		return stockChangeDate;
	}
	public void setStockChangeDate(String stockChangeDate) {
		this.stockChangeDate = stockChangeDate;
	}

	@Override
	public String toString() {
		return "NiftyStock [stockName=" + stockName + ", stockPrice=" + stockPrice + ", stockChangeDate="
				+ stockChangeDate + "]";
	}
	
	
	

}
