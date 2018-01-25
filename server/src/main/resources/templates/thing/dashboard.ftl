<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="main col-sm-9 ml-sm-auto col-md-10 pt-3">
        <div class="row">
            <div v-for="d in devices" class="card col-md-6 p-0">
                <div class="card-header p-d0">{{d.name}}</div>
                <div class="card-body p-1">
                    <div class="m-1" v-for="att in d.deviceAttributes">
                        <div v-if="!att.actuator">
                        <#--<div v-if="att.type=='Double'" v-bind:id="'att_'+att.id">-->
                        <#--<img src="/static/img/ajax-loader.gif"/>-->
                        <#--</div>-->
                            <canvas v-if="att.type=='Double' || att.type=='Integer'" v-bind:id="'att_'+att.id" width="400" height="300">
                            <#--<img src="/static/img/ajax-loader.gif"/>-->
                            </canvas>
                        </div>
                        <div v-if="att.actuator">
                            <div v-if="att.type=='Boolean'" v-bind:id="'att_'+att.id">
                                <button v-if="att.value" class="btn btn-success" v-on:click="toggle(att)">{{att.name}}
                                    ON
                                </button>
                                <button v-if="!att.value" class="btn btn-danger" v-on:click="toggle(att)">{{att.name}}
                                    OFF
                                </button>
                            </div>

                            <div v-if="att.type=='Double'" v-bind:id="'att_'+att.id">
                                <input class="input form-control" type="number" v-on:change="setValue(att)"/>
                            </div>
                            <div v-if="att.type=='Integer'" v-bind:id="'att_'+att.id">
                                <input class="input form-control" type="number" v-on:change="setValue(att)"/>
                            </div>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.1/Chart.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    // google.charts.load('current', {'packages':['gauge', 'corechart']});
    var token = $.cookie("authorization");
    var userId = ${user.id};
    var thingId = ${thing.id};
    var series = {};

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
            devices: [],
            // hold chart objects to modify them later
            doubleCharts: {},
            integerCharts: {},
            // chart max data limit
            maxDataLimit: 20,
            // chart colors
            chartColors: {
                red: 'rgb(255, 99, 132)',
                orange: 'rgb(255, 159, 64)',
                yellow: 'rgb(255, 205, 86)',
                green: 'rgb(75, 192, 192)',
                blue: 'rgb(54, 162, 235)',
                purple: 'rgb(153, 102, 255)',
                grey: 'rgb(201, 203, 207)'
            }
        },
        computed: {
          chartColorList: function() {
              var list = [];
              for(var color in this.chartColors){
                  list.push(this.chartColors[color]);
              }
              return list;
          }
        },
        methods: {
            "setValue": function (att) {
                $.ajax({
                    url: "/pubsub/value/" + att.id,
                    "method": "POST",
                    "data": {"value": $(document.getElementById('att_' + att.id)).val()},
                    success: function (data) {
                        setTimeout(function () {
                            that.refresh();
                        }, 3000);
                    }
                });
            },
            "toggle": function (att) {
                var that = this;
                $.ajax({
                    url: "/pubsub/value/" + att.id,
                    "method": "POST",
                    "data": {"value": att.value ? 0 : 1},
                    success: function (data) {
                        setTimeout(function () {
                            that.refresh();
                        }, 3000);
                    }
                });
            },
            "setIntegerChart": function (att) {
                // $(document.getElementById('att_' + att.id)).html(att.name + ': &nbsp; <strong>' + att.value + '</strong>');

                var ctx = document.getElementById('att_' + att.id).getContext('2d');
                var chart = new Chart(ctx, {
                    // The type of chart we want to create
                    type: 'line',

                    // The data for our dataset
                    data: {
                        labels: [],
                        datasets: [{
                            label: att.name,
                            backgroundColor: this.chartColorList[att.id % this.chartColorList.length],
                            borderColor: this.chartColorList[att.id % this.chartColorList.length],
                            data: []
                        }]
                    },

                    // Configuration options go here
                    options: {}
                });
                this.integerCharts[att.id] = chart;
            },
            "updateIntegerChart": function (att) {
                var date = new Date();
                var labels = this.integerCharts[att.id].config.data.labels;
                var data = this.integerCharts[att.id].config.data.datasets[0].data;
                labels.push(date.toLocaleTimeString());
                data.push(att.value);
                var removeLength = labels.length - this.maxDataLimit;
                if(removeLength > 0) {
                    labels.splice(0, removeLength);
                    data.splice(0, removeLength);
                }
                this.integerCharts[att.id].update();
            },
            "updateGauge": function (att) {

                var data = google.visualization.arrayToDataTable([
                    ['Label', 'Value'],
                    [att.name, att.value]
                ]);
                var options = {
                    width: 400, height: 120
                };
                var chart = new google.visualization.Gauge(document.getElementById('att_' + att.id));
                chart.draw(data, options);

            },
            "setDoubleChart": function (att) {
                // $(document.getElementById('att_' + att.id)).html(att.name + ': &nbsp; <strong>' + att.value + '</strong>');

                var ctx = document.getElementById('att_' + att.id).getContext('2d');
                var chart = new Chart(ctx, {
                    // The type of chart we want to create
                    type: 'line',

                    // The data for our dataset
                    data: {
                        labels: [],
                        datasets: [{
                            label: att.name,
                            backgroundColor: this.chartColorList[att.id % this.chartColorList.length],
                            borderColor: this.chartColorList[att.id % this.chartColorList.length],
                            data: []
                        }]
                    },

                    // Configuration options go here
                    options: {}
                });
                this.doubleCharts[att.id] = chart;
            },
            "updateDoubleChart": function (att) {
                var date = new Date();
                var labels = this.doubleCharts[att.id].config.data.labels;
                var data = this.doubleCharts[att.id].config.data.datasets[0].data;
                labels.push(date.toLocaleTimeString());
                data.push(att.value);
                var removeLength = labels.length - this.maxDataLimit;
                if(removeLength > 0) {
                    labels.splice(0, removeLength);
                    data.splice(0, removeLength);
                }
                this.doubleCharts[att.id].update();
            },
            "refresh": function (isInit) {
                var that = this;

                $.ajax({
                    url: "/pubsub/shadow/" + thingId,
                    "method": "GET",
                    success: function (data) {
                        data = data["state"];
                        for (var key in data["reported"]) {
                            var val = data["reported"][key];
                            for (var d in that.devices) {
                                var device = that.devices[d];
                                for (var ak in device.deviceAttributes) {
                                    var att = device.deviceAttributes[ak];
                                    var dName = "device" + device.id + "." + att.id;
                                    if (dName === key) {
                                        console.log(att);
                                        if (att.type === "Integer") {
                                            Vue.set(att, 'value', val);
                                            if (isInit)
                                                that.setIntegerChart(att);
                                            else
                                                that.updateIntegerChart(att);
                                        } else if (att.type === "Double") {
                                            Vue.set(att, 'value', val);
                                            if (isInit)
                                                that.setDoubleChart(att);
                                            else
                                                that.updateDoubleChart(att);
                                            // that.updateGauge(att);
                                        } else {
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
                        that.refresh(true);
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
            }, 10000);
        }
    })
</script>
</body>
</html>

