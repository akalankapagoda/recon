This is the Transaction Reconciliation back-end.

Pre-Requisites
-----------------

1. Java 8+
2. Maven 3.8 +


How to Build
--------------

Navigate to the project base directory using terminal and run

    `mvn clean install'
	
How to Run
------------

Navigate to the project base directory using the terminal and run

	`mvn spring-boot:run`
	
API Documentation
------------------

1. Healthcheck Endpoint

GET /file/hello

Returns a list of Transaction objects with one Transaction where the description is set to 'Hello'

2. File Upload Endpoint

Uploads a file to be used for reconciliation. Starts pre-processing the submitted file.

POST /file

Request Form Parameters
	file - Multipart File
	identifier - A unique identifier for the file

Responds with a JSON body including 'status' and 'message' fields.

3. File pre-processing status check endpoint

GET /file/status

Request Query Parameters
	identifier - The unique file identifier

4. Reconciliation Endpoint

POST /reconcile

Request Form Parameters
	source - The source file identifier
	target - The target file identifier

Responds with a JSON body including 'status' and 'message' fields.

5. Reconciliation Results Endpoint

GET /reconcile/results

Request Query Parameters
	source - The source file identifier
	target - The target file identifier

Retrives completed reconciliation results or the reconciliation job status

Responds with a JSON body including 'status' and 'message' fields.
If the job is completed, the response includes the match count, source and target record count, unmatched records and match suggestions.

