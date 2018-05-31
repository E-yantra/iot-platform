exports.handler = (event, context, callback) => {
    var AWS = require('aws-sdk');
    var iotdata = new AWS.IotData({endpoint: 'akbmorjah98q5.iot.ap-southeast-1.amazonaws.com'}); 
    var params = { payload:JSON.stringify({"state": {"desired": JSON.parse(event.desired) } }), thingName:event.thingName };
    try {
        iotdata.updateThingShadow(params, function(err, data) {
            if (err) callback(err, err.stack); // an error occurred
            else callback(null, data);         // successful response
        });
    } catch(t) {
        callback(null,t);
    }
};
