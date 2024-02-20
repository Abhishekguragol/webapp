# Cloud Project

## Overview

This repository is designed to be a cloud native web application (backend services) for the course: CSYE6255 Network Structures and Cloud Computing. This application is primarily built using SpringBoot framework in Java.

## Tech stack and packages used

1. SpringBoot - For the server applications
2. MySql - For database  management
3. Postman - Test API endpoints
4. Rest Assured - Integration testing
5. Github Actions - Used workflows to ensure CI/CD

## API's Implemented

1. PUT - v1/user/self : Update user information
2. GET - v1/user/self: Get user information
3. POST - /v1/user : Add user
4. GET - /healthz : Check the health of the app ( If database connections works etc)

## Commands used

1. Stop mysql: brew services start mysql
2. Start mysql:  brew services stop mysql
3. To run the application: mvn spring-boot:run
4. To run test files: mvn test
5. To build the project: mvn clean package
6. packer init: Initialise packer
7. packer validate: checks the validity of packer file/template
8. gcloud iam service-accounts create packer \
  --project csye6225-dev-414923 \
  --description="Packer Service Account" \
  --display-name="Packer Service Account"  : Create a new service account called packer
9. gcloud projects add-iam-policy-binding csye6225-dev-414923\
    --member=serviceAccount:packer@csye6225-dev-414923.iam.gserviceaccount.com \
    --role=roles/compute.instanceAdmin.v1 : grant the packer account Instance Admin Role

10. gcloud projects add-iam-policy-binding csye6225-dev-414923 \
    --member=serviceAccount:packer@csye6225-dev-414923.iam.gserviceaccount.com \
    --role=roles/iam.serviceAccountUser  : Grant the packer account User role so it can use the SA

11. gcloud projects add-iam-policy-binding csye6225-dev-414923 \
    --member=serviceAccount:packer@csye6225-dev-414923.iam.gserviceaccount.com \
    --role=roles/iap.tunnelResourceAccessor : Grant  IAP Tunneling access for Packer VM to make requests to App Engine
