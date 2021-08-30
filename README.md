# Tution Reimbursement Management System

## Project Description
Allows employees to submit reimbursement requests to a company. These reimbursement requests would then go throught the companies reimbursement pipeline to be approved or denied by supervisors, department heads, and benefit coordinators.
--- 
## Technologies Used
- Java
- Datastax
- Javalin
- Log4J
- Maven
- AWS KeySpaces
- JUnit
- S3

---
## Features
### Completed Features
- Submit reimbursement requests
- Approve reimbursements
- Escalation of Approval through departments
- Add Files to Requests
- Role Based Security
- Grading System for requests
### TO DO
- Auto-Approve Requests after duration for Supervisor and Head of departments

## Getting Started
To run the project locally, ensure that the following have been setup:

- Java 8 runtime environment
- AWS Keyspace
- S3 bucket
- AWS IAM user with programatic access to the Keyspace
- AWS IAM user with programatic access to the S3 bucket configued with the AWS CLI

---

Clone the project using the link:
```sh 
[git clone https://github.com/210712-Richard/michael.mcinerney.p1](https://github.com/210712-Richard/joshua.muckey.p1)
```
Set two environmental varaibles, AWS_USER and AWS_PASS, to a AWS IAM user with programmtic access to the Keyspace being used.
Go into the directory and build the application using:
```sh
mvn package
```

Run the following command to run the project:
```sh
java -cp target/Project1-0.0.1-SNAPSHOT.jar com.revature.Driver
```

The application should be running on http://localhost:8080. 
