<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
        <div class="row">
            <div v-for="d in devices" class="card col-md4 p-0">
                <div class="card-header p-d0">{{d.name}}</div>
                <div class="card-body p-1">
                    <div class="m-1" v-for="att in d.deviceAttributes">
                        <div v-if="!att.actuator">
                            <div v-if="att.type=='Double'" v-bind:id="'att_'+att.id">
                                Loading...
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
            "toggle":function () {

            },
            "updateGauge":function (att) {

                $.ajax({
                    url: "/pubsub/value/"+att.id,
                    "method": "GET",
                    success: function (data) {

                        var data = google.visualization.arrayToDataTable([
                            ['Label', 'Value'],
                            [att.name,data.value]
                        ]);

                        var options = {
                            width: 400, height: 120
                        };

                        var chart = new google.visualization.Gauge(document.getElementById('att_'+att.id));

                        chart.draw(data, options);
                    }
                });
            },
            "updateButton":function (att) {
                $.ajax({
                    url: "/pubsub/value/"+att.id,
                    "method": "GET",
                    success: function (data) {
                        att.value = false;
                        if(data.value>0 || data.value=="true"){
                            att.value = true;
                        }
                    }
                });
            },
            "refresh": function () {
                var that = this;
                for(var i in that.devices){
                    var d = that.devices[i];
                    for(var j in d.deviceAttributes){
                        var att =  d.deviceAttributes[j];
                        if(att.type=='Double'){
                            that.updateGauge(att);
                        }
                        if(att.type=='Boolean'){
                            that.updateButton(att);
                        }
                    }
                }
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
            "subscribe": function () {
                try {
                    clearInterval(this.subscribeHandle);
                } catch (ex) {
                }
                var that = this;

                $.ajax({
                    url: "/pubsub/subscribe",
                    "method": "POST",
                    "data": {
                        "topic": that.testTopic
                    },
                    success: function (data) {

                    }
                });

                this.subscribeHandle = setInterval(function () {
                    $.ajax({
                        url: "/pubsub/messages",
                        "method": "POST",
                        "data": {
                            "topic": that.testTopic
                        },
                        success: function (data) {
                            if (data.length > 0) {
                                for (var i in data) {
                                    that.subscribed.push(JSON.stringify(data[i], null, 4));
                                }

                            }
                        }
                    });
                }, 8000);
            },
            "load": function () {

                var that = this;
                $.ajax({
                    url: "/thing/get/" + thingId,
                    success: function (data) {
                        that.thing = data;
                        that.unit = that.thing.parentUnit;
                        $.ajax({
                            url: "/unit/rights/" + that.unit.id + "/" + userId,
                            success: function (data) {
                                that.role = data[0].role;
                            }
                        });
                    }
                });
                $.ajax({
                    url: "/device/thing/" + thingId,
                    success: function (data) {
                        that.devices = data;
                        for(var i in that.devices){
                            var d = that.devices[i];
                            for(var j in d.deviceAttributes){
                                var att = d.deviceAttributes[j];
                                $.ajax({
                                    url: "/pubsub/subscribe/"+att.id,
                                    "method": "GET",
                                    success: function (data) {

                                    }
                                });
                            }
                        }

                    }
                });

            }
        },
        mounted: function () {
            this.load()
            var that = this;

            setInterval(function () {
                that.refresh();
            },5000);
        }
    })
</script>
</body>
</html>

