'use strict';

const AWS = require('aws-sdk');
console.log('Loading function');

// Set the region 
AWS.config.update({region: 'ap-southeast-1'});
    
// create DynamoDB service object
const ddb = new AWS.DynamoDB({apiVersion: '2012-10-08'});
const TableName = "NotificationDetail";
    
// create SNS service object
const sns = new AWS.SNS({apiVersion: '2010-03-31'});

exports.handler = (event, context, callback) => {
    
    let notificationDetailParams = {
        TableName,
        Key: {
            "ruleName": {
                S: event.ruleName
            }
        }
    };
    
    //console.log('Received event:', JSON.stringify(event, null, 2));

    ddb.getItem(notificationDetailParams, function(err, data) {
        if (err) {
            console.log(err, err.stack);   
        }
        else {
            
            console.log(data);
            console.log(event);
            data = data['Item'];
            let diff = new Date() - data.timestamp['N'];
            let diffMins = (((diff % 86400000) / 3600000))*60;
            
            let subject = data.subject['S'] || 'TEST';
            let thresholdTime = data.interval['N'] || 1;
            let message = data.message['S'] || "ALERT! ALERT! ALERT!";
            
            console.log(diffMins); 
            if(diffMins >= thresholdTime) {
                   
                let snsParams = {
                    Message: message + '\n' + JSON.stringify(event),
                    Subject: subject,
                    TopicArn: data.topicArn['S']
                };
                
                sns.publish(snsParams, function(err, data) {
                    if (err) console.log(err, err.stack); // an error occurred
                    else {
                        console.log(data);           // successful response
                        let ddbParams = {
                            TableName,
                            ExpressionAttributeNames: {
                               "#TS": "timestamp"
                            }, 
                            Key: {
                                "ruleName": {
                                    S: event.ruleName
                                }
                            },
                            ExpressionAttributeValues: {
                               ":y": {
                                 N: new Date().getTime().toString()
                                }
                             },
                            UpdateExpression: "SET #TS = :y"
                        };
                        ddb.updateItem(ddbParams, function(err, data) {
                           if(err) console.log(err, err.stack);
                           else console.log(data);
                        });
                    }
                });
                            
            }
        
        }
    });
    
    callback(null, event.state.reported);  // Echo back the first key value
};
