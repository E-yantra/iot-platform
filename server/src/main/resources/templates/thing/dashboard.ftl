<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="main">
        <div class="row">
            <div v-for="d in devices" class="card col-md-6 p-0">
                <div class="card-header p-d0">{{d.name}}</div>
                <div class="card-body p-1">
                    <div class="m-1" v-for="att in d.deviceAttributes">
                        <div v-if="!att.actuator">
                            <div v-if="att.type=='Double'" v-bind:id="'att_'+att.id">
                                <img src="/static/img/ajax-loader.gif"/>
                            </div>
                        </div>
                        <div v-if="att.actuator">
                            <div v-if="att.type=='Boolean'" v-bind:id="'att_'+att.id">
                                <button v-if="att.value" class="btn btn-success" v-on:click="toggle(att)">{{att.name}} ON</button>
                                <button v-if="!att.value" class="btn btn-danger"  v-on:click="toggle(att)">{{att.name}} OFF</button>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>

    </main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    google.charts.load('current', {'packages':['gauge']});
    var token = $.cookie("authorization");
    var userId = ${user.id};
    var thingId = ${thing.id};
    var series = {

    };

    var app = new Vue({
        el: '#container-main',
        data: {
            subscribeHandle: null,
            subscribed: [],
            testTopic: "",
            payload: "",
            device: {},
            saveLoader: false,
            role: "",
            unit: {},
            thing: {},
            devices: []
        },
        methods: {
            "toggle":function (att) {
                var that = this;
                $.ajax({
                    url: "/pubsub/value/"+att.id,
                    "method": "POST",
                    "data":{ "value": att.value?0:1} ,
                    success: function (data) {
                        setTimeout(function () {
                            that.refresh();
                        },3000);

                    }
                });
            },
            "updateGauge":function (att) {

                var data = google.visualization.arrayToDataTable([
                    ['Label', 'Value'],
                    [att.name,att.value]
                ]);
                var options = {
                    width: 400, height: 120
                };
                var chart = new google.visualization.Gauge(document.getElementById('att_'+att.id));
                chart.draw(data, options);

            },
            "refresh": function () {
                var that = this;

                $.ajax({
                        url: "/pubsub/shadow/"+thingId,
                        "method": "GET",
                        success: function (data) {
                            data = data["state"];
                            for(var key in data["reported"]){
                                var val = data["reported"][key];
                                for(var d in that.devices){
                                    var device = that.devices[d];
                                    for(var ak in device.deviceAttributes){
                                        var att = device.deviceAttributes[ak];
                                        var dName = "device"+device.id+"."+att.id;
                                        if(dName===key){console.log(att);
                                            if(att.type!=="Boolean"){
                                                Vue.set(att, 'value', val);
                                                that.updateGauge(att);
                                            }else{
                                                Vue.set(att, 'value', val !== 0);

                                            }

                                        }
                                    }
                                }
                            }

                        }
                    });

            },
            "publish": function () {
                var that = this;
                $.ajax({
                    url: "/pubsub/publish",
                    "method": "POST",
                    "data": {
                        "topic": that.testTopic,
                        "payload": that.payload
                    },
                    success: function (data) {

                    }
                });
            },
            "load": function () {

                var that = this;
                $.ajax({
                    url: "/thing/get/" + thingId,
                    success: function (data) {
                        that.thing = data;
                        that.unit = that.thing.parentUnit;
                        that.devices = data.devices;
                        that.refresh();
                        $.ajax({
                            url: "/unit/rights/" + that.unit.id + "/" + userId,
                            success: function (data) {
                                that.role = data[0].role;
                            }
                        });
                    }
                });

            }
        },
        mounted: function () {
            this.load()
            var that = this;

            setInterval(function () {
                that.refresh();
            },10000);
        }
    })
</script>
</body>
</html>

