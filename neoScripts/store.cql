// 1. STORE
using periodic commit
load csv with headers from "file:///import.csv" 

	AS row
	FIELDTERMINATOR '\t'

	MERGE ( store: STORE { ID : row.ledger_account_no } ) 
		ON CREATE SET store.KOSS=row.koss_id
		ON MATCH SET store.KOSS=row.koss_id;

// 2. CUSTOMER
using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'
	WITH row

	WHERE (row.customer_id <> 0)

	MERGE ( customer: CUSTOMER { ID : row.customer_id + ":" + row.receipt_number} )
		ON CREATE SET customer.CUSTOMER_ID=row.customer_id, customer.RECEIPT=row.receipt_number
		ON MATCH SET customer.CUSTOMER_ID=row.customer_id, customer.RECEIPT=row.receipt_number;

// 3. PRODUCT

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'

	MERGE ( prod: PRODUCT { ID : row.EAN_SKU } ) 
		ON CREATE SET prod.NAME=row.article_name
		ON MATCH SET prod.NAME=row.article_name;


// 4. NONMEMBER

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'
	WITH row

	WHERE (row.customer_id = "0")

	MERGE ( non: NONMEMBER { RECEIPT_NO : row.receipt_number } ) 
		ON CREATE SET non.CUSTOMER_ID=row.customer_id, non.RECEIPT=row.receipt_number
		ON MATCH SET non.CUSTOMER_ID=row.customer_id, non.RECEIPT=row.receipt_number;

// 5. STORE -> CUSTOMER

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'
	WITH row

	WHERE (row.customer_id <> "0")

	MATCH ( store: STORE { ID : row.ledger_account_no } ) 
	MATCH ( customer: CUSTOMER { ID : row.customer_id } )

		MERGE (customer) -[:BOUGHT_AT]-> (store);

// 6. CUSTOMER -> PRODUCT

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'
	WITH row

	WHERE (row.customer_id <> "0")

	MATCH ( prod: PRODUCT { ID : row.EAN_SKU } ) 
	MATCH ( customer: CUSTOMER { ID : row.customer_id } )

		MERGE (customer) -[:CUSTOMER_BOUGHT]-> (prod);

// 7. NONMEMBER -> PRODUCT

using periodic commit
load csv with headers from "file:///import.csv" 

	AS row FIELDTERMINATOR '\t'
	WITH row

	WHERE (row.customer_id = "0")

	MATCH ( prod: PRODUCT { ID : row.EAN_SKU } ) 
	MATCH ( non: NONMEMBER { RECEIPT_NO : row.receipt_number } ) 

		MERGE (non) -[:NONMEMBER_BOUGHT]-> (prod);
