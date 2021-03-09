package com.stock.batch.listener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.opencsv.CSVWriter;
import com.stock.batch.model.NiftyStock;
import com.stock.batch.model.NiftyStockCount;
import com.stock.batch.model.Person;
import com.stock.batch.utility.CalculateChange;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

			StringBuffer sbf = new StringBuffer();

			BufferedWriter bwr = null;
			try {
				bwr = new BufferedWriter(
						new FileWriter(new File("C:\\Users\\NV5053984\\Desktop\\Stock List\\out.csv")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<NiftyStockCount> results = jdbcTemplate.query(
					"SELECT id,stock_name, count FROM daily_nifty_count where count >=2",
					new RowMapper<NiftyStockCount>() {
						@Override
						public NiftyStockCount mapRow(ResultSet rs, int row) throws SQLException {
							return new NiftyStockCount(rs.getInt(1), rs.getString(2), rs.getInt(3));
						}
					});

			for (NiftyStockCount niftycount : results) {
				List<NiftyStock> niftydailyresult = jdbcTemplate.query(
						"SELECT id,stock_name,stock_price,stock_chg_date FROM daily_nifty where stock_name='"
								+ niftycount.getStockName() + "'order by stock_chg_date desc",
						new RowMapper<NiftyStock>() {
							@Override
							public NiftyStock mapRow(ResultSet rs, int row) throws SQLException {
								return new NiftyStock(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
							}
						});
				sbf.append(niftycount.getStockName()).append(",");
				CalculateChange chg=new CalculateChange();
				List<Double> getComaprePrice = chg.getFormattedNumber(niftycount.getStockName());

				// Float change_price=(fist_price-current_price);
				BigDecimal pastPrice = new BigDecimal(getComaprePrice.get(0));
				BigDecimal curPrice = new BigDecimal(getComaprePrice.get(getComaprePrice.size() - 1));
				BigDecimal result = curPrice.subtract(pastPrice);
				sbf.append(result).append(",");
				for (NiftyStock niftystock : niftydailyresult) {
					{
						String decimalValuePrint = "\"" + niftystock.getStockPrice() + "\"";
						sbf.append(niftystock.getStockChangeDate()).append(",").append(decimalValuePrint).append(",");
					}
				}

				sbf.append(System.getProperty("line.separator"));
			}

			// write contents of StringBuffer to a file
			try {
				bwr.write(sbf.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// flush the stream
			try {
				bwr.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// close the stream
			try {
				bwr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Content of StringBuffer written to File.");

		}
	}
}