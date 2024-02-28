The crypto wallet for storing cryptocurrency, exchanging it, and buying and selling it with orders.

The main goal of the project is to provide users with a secure, convenient and functional tool for storing, exchanging and trading cryptocurrencies.

A roadmap for the design and implementation of application services:
1. User Creation (DTO).
2. Connection of authorization, authentication and password encryption method.
3. Creating the password change method.
4. Adding basic cryptocurrencies - Bitcoin, Tether.
5. Creating a method to generate public addresses for accounts in each cryptocurrency of the application.
6. Creating user accounts (happens automatically for all application currencies when creating a user)
7. Creating a method to get the current exchange rate of each cryptocurrency (by chart).
8. Execution of exchange operations - replenishment of the account balance, withdrawal of funds from the account, sale or purchase of cryptocurrency.
9. Realization of trading operations - creating an order to buy or sell cryptocurrency.
10. Realization of documentation and saving of exchange and trade operations.
11. Realization of the order execution method (including, partially).
12. Realization of the method of canceling an order.
13. Creation of the application administrator account for profit calculation.
14. Creating a method for calculating the exchange rate for each cryptocurrency (adding fields - buying rate and selling rate).
15. Adjusting the method of exchange transactions - buying is done at the buy rate, selling is done at the sell rate.
16. Creating a method for obtaining the total user balance for all accounts (in Bitcoin and Tether projection)
17. Adjustment to exchange transaction method - created parallel accounting to calculate application profit.
18. Creating a method for calculating the application profit
19. Creating a robot that automatically executes orders when the corresponding exchange rate is reached.
20. Creation of trading bots to keep statistics of this or that trading strategy (now the 1st bot buys Bitcoin in the morning and sells it in the evening. And the 2nd one - vice versa (by chart).
21. Listing of a new cryptocurrency - Ethereum (automatic creation of accounts for this currency for all users)

Plan for upcoming updates:
1. Add fee for a trade operation.
2. Add the ability to transfer funds between accounts of the application users.
3. Add a fee for transfers.
4. Change the method of calculating the app's profit.
5. Add a method for calculating the maximum possible amount of cryptocurrency purchase by a user.
6. Add the ability to buy/sell cryptocurrency for a specified % of the current account balance.
7. Fix application bugs.
8. Improve error and exception handling service.    

Stages of development:
1. initialization of the spring project.
JDK 17, SpringBoot
2. Plugging in dependencies.
Maven, JPA, Spring Security, Lombok, Hibernate, Validator, Jacoco, Swagger UI,
3. Database connection.
PostgreSQL
4. Creating and describing application entity layer.
DTO, ENUM, VALIDATE
5. Creating a repository layer for each project entity.
6. Creating CRUD methods for each repository.
JPA, Hibernate, SQL, JPQL
7. Creating a layer of services to work with each application repository.
8. Creating and describing the implementation classes of each application service.
9. Creation of service utilities for efficient operation of methods in the services layer (such as generators, validators, data access methods)
10. Creation and description of exceptions.
11. Creating a layer of controllers
12. Description of the application business logic and its realization in the services layer.
13. Creating unit and integration tests of the application.
14. Creating application documentation using Javadoc

Roadmap for developing and implementing application services:
1. User Creation (DTO)
2. Creating user accounts (happens automatically for all currencies of the application, when creating a user)
3. Realization of exchange operations - replenish account balance, withdraw funds from the account, sell or buy cryptocurrency.
4. Realization of documentation and saving of exchange operations.
5. Realization of trading operations - creating an order to buy or sell cryptocurrency.
