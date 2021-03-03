package com.stock.batch.model;

public class NiftyStockCount {
	private int id;
	private String stockName;
	private int stock_count;
	
	public NiftyStockCount()
	{
		
	}
	

	
	public NiftyStockCount(int id, String stockName, int stock_count) {
		super();
		this.id = id;
		this.stockName = stockName;
		this.stock_count = stock_count;
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

	public int getStock_count() {
		return stock_count;
	}

	public void setStock_count(int stock_count) {
		this.stock_count = stock_count;
	}

	@Override
	public String toString() {
		return "NiftyStockCount [stockName=" + stockName + ", stock_count=" + stock_count + "]";
	}
	
	

}
