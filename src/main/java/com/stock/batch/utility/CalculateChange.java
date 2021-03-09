package com.stock.batch.utility;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.stock.batch.SpringBootBatchProjectApplication;
import com.stock.batch.model.NiftyStock;

public class CalculateChange {
	
	
    private JdbcTemplate JdbcTemplate;
	public CalculateChange()
	{
		JdbcTemplate jdbcTemplateExtr=SpringBootBatchProjectApplication.getApplicationContext().getBean(JdbcTemplate.class);
		this.JdbcTemplate=jdbcTemplateExtr;
	}

public List<Double> getFormattedNumber(String stockName) {
		
		StringBuffer queryDb=new StringBuffer();
		
		queryDb.append("select * from (SELECT id,stock_name,stock_price,stock_chg_date FROM daily_nifty where stock_name =");
		queryDb.append("\'"+stockName.trim()+"' order by stock_chg_date DESC LIMIT 5)sub order by stock_chg_date ASC");
		String toquery=queryDb.toString();
		
		List<NiftyStock> niftydailyresult = JdbcTemplate.query(toquery,
				new RowMapper<NiftyStock>() {
					@Override
					public NiftyStock mapRow(ResultSet rs, int row) throws SQLException {
						return new NiftyStock(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
					}
				});
		
		String pastPrice = niftydailyresult.get(0).getStockPrice();
		String currentPrice = niftydailyresult.get(niftydailyresult.size() - 1).getStockPrice();
		List<Double> addPrice = new ArrayList<>();
		Double storePast = 0.0;
		Double storeCurrentprice = 0.0;

		if (!pastPrice.isEmpty()) {
			pastPrice = pastPrice.replaceAll(",", "");
			storePast = Double.parseDouble(pastPrice);
		}
		if (!currentPrice.isEmpty()) {
			currentPrice = currentPrice.replaceAll(",", "");
			storeCurrentprice = Double.parseDouble(currentPrice);
		}
		addPrice.add(storePast);
		addPrice.add(storeCurrentprice);

		return addPrice;
	}

}
