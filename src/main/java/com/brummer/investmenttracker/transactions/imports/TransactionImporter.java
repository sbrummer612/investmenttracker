package com.brummer.investmenttracker.transactions.imports;

import com.brummer.investmenttracker.transactions.Transaction;

public interface TransactionImporter {

	Transaction parseLine(String line);
	
}
