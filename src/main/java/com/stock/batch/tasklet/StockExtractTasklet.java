package com.stock.batch.tasklet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.stock.batch.SpringBootBatchProjectApplication;
import com.stock.batch.model.NiftyStock;
import com.stock.batch.model.NiftyStockCount;

public class StockExtractTasklet implements Tasklet {

	@Autowired
    private ApplicationContext appContext;
	
    
    
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		JdbcTemplate jdbcTemplateExtr=SpringBootBatchProjectApplication.getApplicationContext().getBean(JdbcTemplate.class);
		// TODO Auto-generated method stub
        // appContext.getBean(JdbcTemplate.class);
		
		StringBuffer sbf = new StringBuffer();

		// StringBuffer contents
		//sbf.append("StringBuffer contents first line.");
		// new line
		//sbf.append(System.getProperty("line.separator"));
		// second line
		//sbf.append("StringBuffer contents second line.");

		/*
		 * To write contents of StringBuffer to a file, use BufferedWriter class.
		 */
		
	 DataSource s=jdbcTemplateExtr.getDataSource();
	 String s1=s.toString();

		BufferedWriter bwr = new BufferedWriter(
				new FileWriter(new File("C:\\Users\\NV5053984\\Desktop\\Stock List\\out.csv")));
		
		List<NiftyStockCount> results = jdbcTemplateExtr.query(
				"SELECT id,stock_name, count FROM daily_nifty_count where count >=2",
				new RowMapper<NiftyStockCount>() {
					@Override
					public NiftyStockCount mapRow(ResultSet rs, int row) throws SQLException {
						return new NiftyStockCount(rs.getInt(1), rs.getString(2), rs.getInt(3));
					}
				});

		for (NiftyStockCount niftycount : results) {
			List<NiftyStock> niftydailyresult = jdbcTemplateExtr.query(
					"SELECT id,stock_name,stock_price,stock_price FROM daily_nifty where stock_name='"+niftycount.getStockName()+"'",
					new RowMapper<NiftyStock>() {
						@Override
						public NiftyStock mapRow(ResultSet rs, int row) throws SQLException {
							return new NiftyStock(rs.getInt(1), rs.getString(2), rs.getString(3),rs.getString(4));
						}
					});
			sbf.append(niftycount.getStockName()).append(",");
			for (NiftyStock niftystock : niftydailyresult) {
				{
					sbf.append(niftystock.getStockChangeDate()).append(",").append(niftystock.getStockPrice());
				}
			}
			
			sbf.append(System.getProperty("line.separator"));
		}



		// write contents of StringBuffer to a file
		bwr.write(sbf.toString());

		// flush the stream
		bwr.flush();

		// close the stream
		bwr.close();

		System.out.println("Content of StringBuffer written to File.");

		return RepeatStatus.FINISHED;
	}

}
