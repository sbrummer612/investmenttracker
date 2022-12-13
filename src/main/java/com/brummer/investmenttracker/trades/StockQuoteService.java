package com.brummer.investmenttracker.trades;

import java.util.List;
import java.util.Map;

import org.patriques.AlphaVantageConnector;
import org.patriques.BatchStockQuotes;
import org.patriques.TimeSeries;
import org.patriques.input.timeseries.Interval;
import org.patriques.input.timeseries.OutputSize;
import org.patriques.output.AlphaVantageException;
import org.patriques.output.quote.BatchStockQuotesResponse;
import org.patriques.output.quote.data.StockQuote;
import org.patriques.output.timeseries.Daily;
import org.patriques.output.timeseries.IntraDay;
import org.patriques.output.timeseries.data.StockData;
import org.springframework.stereotype.Service;

@Service
public class StockQuoteService {

	// https://github.com/patriques82/alphavantage4j
	// https://www.alphavantage.co/documentation/
	
	
	private static final String apiKey = "U1QTDTR2CX2DKCMS";
	int timeout = 3000;
	
	AlphaVantageConnector apiConnector = new AlphaVantageConnector(apiKey, timeout);
	TimeSeries stockTimeSeries = new TimeSeries(apiConnector);
	
	BatchStockQuotes batchStockQuote = new BatchStockQuotes(apiConnector) ;
	
	public void getCurrentStockQuote(Trade trade) {
		
		try {
			IntraDay response = stockTimeSeries.intraDay(trade.getSymbol(),  Interval.ONE_MIN, OutputSize.COMPACT);
//			Daily response = stockTimeSeries.daily(stockSymbol);
			
			Map<String, String> metaData = response.getMetaData();
			System.out.println("Information: " + metaData.get("1. Information"));
			System.out.println("Stock: " + metaData.get("2. Symbol"));
			
			List<StockData> stockData = response.getStockData();
//			stockData.forEach(stock -> {
//				if(stock.getDateTime().toString().endsWith("T16:00")) {
//					trade.setMostRecentPrice(Double.valueOf(stock.getClose()));
//				}
//					
//				System.out.println("date: " + stock.getDateTime());
//					System.out.println("open:   " + stock.getOpen());
//			        System.out.println("high:   " + stock.getHigh());
//			        System.out.println("low:    " + stock.getLow());
//			        System.out.println("close:  " + stock.getClose());
//			        System.out.println("volume: " + stock.getVolume());
//				
//			});
			trade.setMostRecentPrice( stockData.get(0).getClose() );
			
		} catch(AlphaVantageException e) {
		      System.out.println("something went wrong");
	    }
	}
	
//	public void batchStockQuote(String stockSymbol) {
//		BatchStockQuotesResponse response = batchStockQuote.quote(stockSymbol);
//		Map<String, String> metaData = response.getMetaData();
//		System.out.println("Information: " + metaData.get("1. Information"));
//		System.out.println("Stock: " + metaData.get("2. Symbol"));
//		
//		List<StockQuote> stockQuotes = response.getStockQuotes();
//		stockQuotes.forEach(stock -> {
//			System.out.println("date: " + stock.getTimestamp());
//			System.out.println("price:  " + stock.getPrice());
//	        System.out.println("volume: " + stock.getVolume());
//		});
//	}
	
}
