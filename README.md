# Bank Account
What is this project for?

The goal of this mini project is to write a simple micro web service to mimic a “Bank Account”. 
With the help of this program we can create bank users who can have accounts and make transactions with those accounts.
Users can have two roles USER and ADMIN. Users are created with default role USER, and only ADMIN user can change it's role.
ADMIN user can create accounts for another users with currencies AMD, USD, EUR, RUR. 
If the currency is not given, then creates an account with default currency AMD.
Also if the account balance is not given at the created time, then it is 0 by default.
Users can make transactions (DEPOSIT or WITHDRAWAL) with accounts and only ADMIN users can accept the transactions.
If the transaction is not accepted by the ADMIN user yet, then the user can cancel it.

###### Rest Endpoints for User

| URL                                                                 | Http Verb    | 
| ----------------------------------                                  |:------------:|
| http://localhost:8080/api/user/{id}                                 | GET          |
| http://localhost:8080/api/user/register                             | POST         |
| http://localhost:8080/api/user/{id}/update                          | PUT          |
| http://localhost:8080/api/user/{userId}/role/{adminId}              | PUT          |
| http://localhost:8080/api/user/transaction/{id}/cancel              | PATCH        |
| http://localhost:8080/api/user/{id}/transaction                     | GET          |
| http://localhost:8080/api/user/{id}/transaction/filter              | GET          |
| http://localhost:8080/api/user/account/{accountId}                  | GET          |
                
                                
###### Rest Endpoints for Account                                 
                                
| URL                                                                 | Http Verb    | 
| ----------------------------------                                  |:------------:|
| http://localhost:8080//api/account/{id}                             | GET          |
| http://localhost:8080//api/account/{adminId}/{userId}               | POST         |
                                
###### Rest Endpoints for Transaction                                 
                                
| URL                                                                 | Http Verb    | 
| ----------------------------------                                  |:------------:|
| http://localhost:8080/api/transaction                               | POST         |
| http://localhost:8080/api/transaction/{id}/confirm/{adminId}        | PATCH        |
| http://localhost:8080/api/transaction/{id}                          | GET          |
| http://localhost:8080/api/transaction/{transId}/user                | GET          |
| http://localhost:8080/api/transaction/filter/date                   | GET          |
| http://localhost:8080/api/transaction/filter                        | GET          |

