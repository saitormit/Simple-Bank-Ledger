**Important: Don't forget to update the [Candidate README](#candidate-readme) section**

Real-time Transaction Challenge
===============================
## Overview
Welcome to Current's take-home technical assessment for backend engineers! We appreciate you taking the time to complete this, and we're excited to see what you come up with.

You are tasked with building a simple bank ledger system that utilizes the [event sourcing](https://martinfowler.com/eaaDev/EventSourcing.html) pattern to maintain a transaction history. The system should allow users to perform basic banking operations such as depositing funds, withdrawing funds, and checking balances. The ledger should maintain a complete and immutable record of all transactions, enabling auditability and reconstruction of account balances at any point in time.

## Details
The [included service.yml](service.yml) is the OpenAPI 3.0 schema to a service we would like you to create and host.

The service accepts two types of transactions:
1) Loads: Add money to a user (credit)

2) Authorizations: Conditionally remove money from a user (debit)

Every load or authorization PUT should return the updated balance following the transaction. Authorization declines should be saved, even if they do not impact balance calculation.


Implement the event sourcing pattern to record all banking transactions as immutable events. Each event should capture relevant information such as transaction type, amount, timestamp, and account identifier.
Define the structure of events and ensure they can be easily serialized and persisted to a data store of your choice. We do not expect you to use a persistent store (you can you in-memory object), but you can if you want. We should be able to bootstrap your project locally to test.

## Expectations
We are looking for attention in the following areas:
1) Do you accept all requests supported by the schema, in the format described?

2) Do your responses conform to the prescribed schema?

3) Does the authorizations endpoint work as documented in the schema?

4) Do you have unit and integrations test on the functionality?

Here’s a breakdown of the key criteria we’ll be considering when grading your submission:

**Adherence to Design Patterns:** We’ll evaluate whether your implementation follows established design patterns such as following the event sourcing model.

**Correctness**: We’ll assess whether your implementation effectively implements the desired pattern and meets the specified requirements.

**Testing:** We’ll assess the comprehensiveness and effectiveness of your test suite, including unit tests, integration tests, and possibly end-to-end tests. Your tests should cover critical functionalities, edge cases, and potential failure scenarios to ensure the stability of the system.

**Documentation and Clarity:** We’ll assess the clarity of your documentation, including comments within the code, README files, architectural diagrams, and explanations of design decisions. Your documentation should provide sufficient context for reviewers to understand the problem, solution, and implementation details.

# Candidate README
## Bootstrap instructions
This bank ledger system was developed by relying on Spring Boot framework. As a prerequisite to set up and install this application on the machine, *Java Development Kit (JDK) 8+* version is needed. In addition, we are also assuming whoever is cloning this program have Git and Maven already installed.

The service can be initialized by running the **src/main/java/dev/codescreen/TransactionApplication.java** on an IDE (e.g. IntelliJ). Considering a production environment and setting,
in order to get the bank ledger Spring Boot application up and running, follow the next steps:

#### 1. Clone the repository
``git clone https://github.com/codescreen/CodeScreen_q8psexpb.git``

#### 2. Navigate to Project Directory (CodeScreen_q8psexpb)
``cd CodeScreen_q8psexpb``

#### 3. Build the Application using Maven
``mvn clean package``

#### 4. Run the Application
``mvn spring-boot:run``

#### 5. Access the Application
As the service is supposed to run on a local machine, use Postman or open a web browser and navigate to http://localhost:8080 to access the bank ledger application.

## Usage
The application contains the use cases mentioned in the problem statement following the schemas given in the service.yml file. In order to make use of the service, the following API endpoints are exposed by the application.

### API Endpoints

* #### GET /ping
    * Description: verifies the availability of the service and returns the current server time
    * Request Body example: N/A
    * Response Body example:
      ```JSON
      {
      "serverTime": "2024-05-04T23:33:49.618717"
      }
      ```

* #### PUT /authorization
    * Description: removes funds from the user's balance if there's enough funds
    * Request Body:
      ```JSON
      {
      "messageId": "SomeMessageId",
      "userId": "1",
      "transactionAmount": {
          "amount": "1500",
          "currency": "USD",
          "debitOrCredit": "DEBIT"
          }
      }
      ```
    * Response Body:
      ```JSON
      {
      "messageId": "SomeMessageId",
      "userId": "1",
      "responseCode": "APPROVED",
      "balance": {
          "amount": "1000",
          "currency": "USD",
          "debitOrCredit": "DEBIT"
         }
      }
      ```

* #### PUT /load
    * Description: adds funds to the user's balance
    * Request Body:
      ```JSON
      {
      "messageId": "SomeMessageId",
      "userId": "2",
      "transactionAmount": {
          "amount": "1200",
          "currency": "USD",
          "debitOrCredit": "CREDIT"
          }
      }
      ```
    * Response Body:
      ```JSON
      {
      "messageId": "SomeMessageId",
      "userId": "2",
      "responseCode": "APPROVED",
      "balance": {
          "amount": "2200",
          "currency": "USD",
          "debitOrCredit": "CREDIT"
          }
      }
      ```

* #### GET /
    * Description: gets a list of all users and their current balance
    * Request Body: N/A
    * Response Body:
      ```JSON
      [
      {
       "name": "Person A",
       "id": "1",
       "balance": 1000.0
      },
      {
       "name": "Person B",
       "id": "2",
       "balance": 2200.0
      },
      {
       "name": "Person C",
       "id": "3",
       "balance": 1600.0
      }
      ]
      ```

* #### GET /{id}
    * Description: gets the current balance of a user of a specific id
    * Request Body: N/A
    * Response Body:
      ```JSON
      {
       "name": "Person B",
       "id": "2",
       "balance": 2200.0
      }
      ```

* #### GET /events
  * Description: gets the history of all "load" and "authorization" requests made while the service is running 
  * Request Body: N/A
  * Response Body:
    ```JSON
    {
    "1": [
        {
            "userId": "1",
            "transactionId": "SomeTransactionID",
            "timeStamp": "2024-05-05T01:19:38.131743",
            "transactionType": "authorization",
            "amount": "1500",
            "responseCode": "APPROVED"
        },
        {
            "userId": "1",
            "transactionId": "SomeTransactionID",
            "timeStamp": "2024-05-05T01:19:47.703806",
            "transactionType": "authorization",
            "amount": "1500",
            "responseCode": "DECLINED"
        }
    ],
    "2": [
        {
            "userId": "2",
            "transactionId": "SomeTransactionID",
            "timeStamp": "2024-05-05T01:17:57.49806",
            "transactionType": "authorization",
            "amount": "1500",
            "responseCode": "APPROVED"
        }
    ]
    }
    ```

## Design considerations
The solution present here follows a model-view-controller (MVC) architectural design pattern. The different layers of the application fulfill their respective tasks, and ensure the service's functionality occurs in a coordinated matter.
The project tree is divided into five main components:
* Controller: entity responsible for handling the incoming http requests
* Model: POJO's classes and DTO's that are used to define and build the system entities
* Repository: Data storage layer 
* Service: layer which contains the business logic of the application
* TransactionApplication: the main class annotated with *@SpringBootApplication* that is used to bootstrap this service

## Assumptions
It is important to emphasize that considering the scope of this challenge, the security layer was neglected. Therefore,
no attention was put into authentication mechanisms. 
Moreover, as a simplification purpose, no database was utilized. Instead, only in-memory objects were created so that it could mimic the data storage and its CRUD operations.
This service contains a preset of 4 users of arbitrary id's and balances, that opt to withdraw and deposit some amount of money randomly.

Performance, transaction rollbacks, currency conversion, are also another areas that were not taken into consideration, as the main focus of this solution was to represent event sourcing reproduction.
## Bonus: Deployment considerations
Some potential options for deployment of this application would be using the server of the company who owns this software, as it would be possible to have full control of its infrastructure, security, and maintenance. 
## License

At CodeScreen, we strongly value the integrity and privacy of our assessments. As a result, this repository is under exclusive copyright, which means you **do not** have permission to share your solution to this test publicly (i.e., inside a public GitHub/GitLab repo, on Reddit, etc.). <br>

## Submitting your solution

Please push your changes to the `main branch` of this repository. You can push one or more commits. <br>

Once you are finished with the task, please click the `Submit Solution` link on <a href="https://app.codescreen.com/candidate/298e6560-4c9d-415b-86be-385621ed183e" target="_blank">this screen</a>.