
using periodic commit
load csv with headers from "file:///import.csv" 

	AS row
	FIELDTERMINATOR '\t'

	MERGE ( store: STORE { ID : row.ledger_account_no } ) 
		ON CREATE SET store.KOSS=row.koss_id
		ON MATCH SET store.KOSS=row.koss_id;



using periodic commit
load csv with headers from "file:///import.csv" 

	AS row
	FIELDTERMINATOR '\t'

	MERGE ( customer: CUSTOMER { ID : row.customer_id } );


using periodic commit
load csv with headers from "file:///import.csv" 

	AS row
	FIELDTERMINATOR '\t'


	MATCH ( store: STORE { ID : row.ledger_account_no } ) 
	MATCH ( customer: CUSTOMER { ID : row.customer_id } )

		MERGE (customer) -[:BOUGHT_AT]-> (store);







