# AWS Setup
#### This guide is to setup additional services such as Crons, Email/SMS Notifications, DynamoDB storage, etc. You won't need to follow this if you just want to connect devices to internet and interchange basic data. 

**Note:** *Before following steps in this document you should have completed the steps in README.md. Also note that you must be an admin IAM user for performing most of the steps in this document such as writing roles, etc. and pushing it to AWS.*

Additional Setup
----
1. For creating AWS IoT rules that will interact with other services such as SNS, DynamoDB, AWS Lambda, you need to create roles. These roles will give permissions to AWS IoT so that it can access these services.
    1. Create an IAM role for AWS IoT. A role grants a service like IoT, the right to access other services.
    2. Give permissions to the roles by attaching policies (SNS Full Access policy, DynamoDB policy).
    3. Put the ARN of this role into configset table as *IoTRoleARN*.
2. For creating CRON rules
    1. Create a [Lambda function](https://github.com/manjrekarom/iot-platform/blob/master/aws-setup/lambda-cron.js) and put it's ARN into configset as a value to the key *lambdaCronArn*.
    2. Assign another role to it giving it permissions to access required services.
3. Create two DynamoDB tables to hold data
    1. *NotificationDetail*
          - Primary Partition Key: ruleName (String)
    2. *ThingDB*
          - Primary Partition Key: id (String)
          - Primary Sort Key: timestamp (Number)
4. For using notification service
    1. Create a [Lambda function](https://github.com/manjrekarom/iot-platform/blob/master/aws-setup/lambda-notification.js) and put it's ARN into configset as a value to the key *lambdaNotificationArn*.
    2. Assign it a role with policies that give it access to DynamoDB and SNS.
          
