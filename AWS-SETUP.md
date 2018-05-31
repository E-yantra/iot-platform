# AWS Setup
#### This guide is to setup additional services such as Crons, notification rules, DynamoDB storage. You won't need to follow this if you just have to connect devices to internet and interchange basic data. If you haven't followed instructions in README.md please come back after doing so.


**Note: You must be an admin IAM user for writing roles and pushing it to AWS.** 

For writing rules on the platform you need to do additional setup on AWS.
1. For creating rules that interact with other services such as SNS, DynamoDB, AWS Lambda. This is required for writing rules for storing data 
in dynamoDB, sending notifications with SNS, invoking lambda functions, etc.
    1. Create an IAM role for AWS IoT. A role grants a service like IoT, the right to access other services.
    2. Give permissions to the roles by attaching policies (SNS Full Access policy, DynamoDB policy).
    3. Put the ARN of this role into configset table as *IoTRoleARN*.
2. For creating CRON rules
    1. Create this Lambda function and put it's ARN into configset as *lambdaFunctionArn*.
    2. Assign another role to it giving it permissions to access required services.
3. Create two DynamoDB tables to hold data
    1. *NotificationDetail*
          - primaryPartitionKey: ruleName (String)
    2. *ThingDB*
          - primarySortKey: timestamp (Number)
4. For using notification service
    1. Create this Lambda function.
    2. Assign it a role with policies that give it access to DynamoDB and SNS
          
