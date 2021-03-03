package com.stock.batch.processor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.stock.batch.model.NiftyStock;
import com.stock.batch.model.NiftyStockCount;


public class StockCountProcessor implements ItemProcessor<NiftyStock, NiftyStock>{
	
	@Autowired
	private  JdbcTemplate jdbcTemplate;


	private static final Logger log = LoggerFactory.getLogger(StockCountProcessor.class);
	
	@Override
	public NiftyStock process(NiftyStock niftyItem) throws Exception {
		
		
		 DataSource s=jdbcTemplate.getDataSource();
		 //String s1=s.toString();
		  String convertPrice = niftyItem.getStockPrice();
		  //convertPrice = convertPrice.replaceAll(",",".");
			final NiftyStock niftyStock=new NiftyStock(niftyItem.getStockName(), convertPrice, niftyItem.getStockChangeDate());
			
			
			int count=3;

			List<NiftyStockCount> results = jdbcTemplate.query("SELECT id,stock_name, count FROM daily_nifty_count where stock_name='"+niftyItem.getStockName()+"'", new RowMapper<NiftyStockCount>() {
				@Override
				public NiftyStockCount mapRow(ResultSet rs, int row) throws SQLException {
					return new NiftyStockCount(rs.getInt(1),rs.getString(2), rs.getInt(3));
				}
			});
       
		     if(results.isEmpty())
	            {       int default_count=1;
	            	   String query="insert into daily_nifty_count(stock_name,count) values('"+niftyItem.getStockName()+"',"+default_count+")";  
	            			     jdbcTemplate.update(query);  
	            			     log.info("stock inserted with count ====1:"+niftyItem.getStockName());
	            }
			for (NiftyStockCount niftycount : results) {
				//log.info("Nifty Count Found <" + niftycount + "> in the database.");
	
		         
		            	Integer stock_count_incr=1;
		            	Integer stock_count=niftycount.getStock_count();
		            	stock_count_incr=stock_count_incr+stock_count;
		            	  String query="update daily_nifty_count set count='"+stock_count_incr+"'where id='"+niftycount.getId()+"' ";  
		            			   jdbcTemplate.update(query); 
		            			   log.info("");
		            			   log.info("stock inserted with updated count:"+niftycount.getStockName());
		            
				
			}

			//log.info(niftyStock.toString());
		return niftyStock;
	}

	



	
}
