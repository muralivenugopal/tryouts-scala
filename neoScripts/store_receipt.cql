// 1. STORE
using periodic commit
load csv with headers from "file:///import.csv" 

	AS row
	FIELDTERMINATOR '\t'

	MERGE ( store: STORE { ID : row.ledger_account_no } ) 
		ON CREATE SET store.KOSS=row.koss_id
		ON MATCH SET store.KOSS=row.koss_id;

// 2. RECEIPT

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'
	WITH row

	WHERE (row.customer_id = "0")

	MERGE ( prod: RECEIPT { NUMBER : row.receipt_number } );

// 3. STORE -> RECEIPT

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'
	WITH row

	WHERE (row.customer_id = "0")

	MATCH ( store: STORE { ID : row.ledger_account_no } ) 
	MATCH ( receipt: RECEIPT { NUMBER : row.receipt_number } )

		MERGE (receipt) -[:BOUGHT_AT]-> (store);

// 4. PRODUCT

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'

	MERGE ( prod: PRODUCT { ID : row.EAN_SKU } ) 
		ON CREATE SET prod.NAME=row.article_name
		ON MATCH SET prod.NAME=row.article_name;

 // 5. RECEIPT -> PRODUCT

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'
	WITH row

	WHERE (row.customer_id = "0")

	MATCH ( prod: PRODUCT { ID : row.EAN_SKU } ) 
	MATCH ( receipt: RECEIPT { NUMBER : row.receipt_number } )

		MERGE (receipt) -[contains:CONTAINS]-> (prod)
			ON CREATE SET contains.KEY=row.receipt_number + ":" + row.receipt_line
			ON MATCH SET contains.KEY=row.receipt_number + ":" + row.receipt_line;

//MERGE (receipt) -[contains:CONTAINS {KEY : row.receipt_number + ":" + row.receipt_line }]-> (prod);
