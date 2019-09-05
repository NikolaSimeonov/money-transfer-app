# Money transfer app

Overview

    This application simulates distributed money transfers
    between accounts in the same or different banks.
    
Highlights
    
    Full transactional history is stored in each account.
    
    Multiple step approach to transfers, money are reserved prior to
    being withdrawn from the account.
    
    Thread safe, when multiple requests are executed on account
    
    No money is created out of thin air, every balance is computed
    based on last transaction log.
    
Limitations

    Single currency per account.(In order to support more separate
    transaction logs will be required in future)
    
    Syncronius money transfers. (Ideally money transfers should be
    event-driven, but this would have increased scope tremendously)
    

## Pre-Requisite

    Java 8+
    Maven 
 
## How to start

Pull the project from Github, after that execute the follow

    mvn clean install


To run the app use following command:

    java -jar target/money-transfer-app.jar


The application will start on the default port 8080


# API

BankResource
 Create
 
    POST http://localhost:8080/api/bank/create/
    
    Example Body:
    
     {
      "id" : "bankId01"
     }
  Get 
  
    GET http://localhost:8080/api/bank/{BankId}
    
    Example Request:
    
    http://localhost:8080/api/bank/bankId01
    
AccountResource
  Create
  
    POST http://localhost:8080/api/account/create/
    
    Example Body:
    
    {
        "id": "bankId01accountId1",
        "bankId": "bankId01",
        "transactionLogList": {
            "transactionLogId1": {
                "transaction": {
                    "id": "transactionId1",
                    "amount": 2000,
                    "timestamp": "2015-08-04T10:11:30",
                    "originatorAccountId": "bankId02accountId1",
                    "receiverAccountId": "bankId01accountId1",
                    "transactionStatus": "EXECUTED",
                    "currency": "EUR"
                },
                "balanceAfterTransaction": 2000,
                "reservedBalanceAfterTransaction": 0,
                "currency" : "EUR"
            }
        }
    }
    
  Get
   
    GET http://localhost:8080/api/account/{AccountId}
    
    Example Request:
       
      http://localhost:8080/api/account/bankId01accountId1

TransferResource
    Transfer money between accounts
        
        POST http://localhost:8080/api/transfer/initiate
        
        Example Body:
        
        {
            "transferAmount": 800,
            "originatorAccountId":"bankId02accountId1",
            "receiverAccountId":"bankId01accountId1",
            "currency": "EUR"
        }