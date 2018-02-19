<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="main col-sm-9 ml-sm-auto col-md-10 pt-3">
        <div class="row">
            <div class="col-md-12 clearfix">
                <div class="float-right p-1">
                    Last message from device: {{lastMessageTime}}
                </div>
            </div>
        </div>
        <div class="row">
            <div v-for="d in devices" class="card col-md-6 p-0">
                <div class="card-header p-d0">{{d.name}}</div>
                <div class="card-body p-1">
                    <div class="m-1" v-for="att in d.deviceAttributes">
                        <div v-if="!att.actuator">
                            <select class="form-control" v-model="chartTypesSelected[att.id]"
                                    v-on:change="(chartInitFunctions[chartTypesSelected[att.id]])(att)">
                                <option disabled value="">Please select one</option>
                                <option v-for="chartType in chartTypes[att.type]" v-bind:value="chartType.value">
                                    {{ chartType.text }}
                                </option>
                            </select>
                        <#--canvas is required by chartsJS-->
                            <canvas v-if="chartTypeRequires[chartTypesSelected[att.id]]=='canvas'"
                                    v-bind:id="'att_'+att.id"
                                    width="400" height="300">
                            </canvas>
                        <#--div is required by GCharts-->
                            <div v-else v-bind:id="'att_'+att.id">
                                <img src="/static/img/ajax-loader.gif"/>
                            </div>
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
    google.charts.load('current', {'packages': ['gauge']});
    var token = $.cookie("authorization");
    var userId = ${user.id};
    var thingId = ${thing.id};
    var series = {};

    var app = new Vue({
        el: '#container-main',
        data: {
            lastMessageTime: "N.A.",
            lastMessageVersion: 0,
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
            interval: 10000,
            // hold chart objects to modify them later
            chartObjects: {},
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
            },
            chartTypes: {
                // define charts for other types here
                // add multiple charts for a type in array
                'Double': [
                    {text: 'Gauge', value: 'Gauge'},
                    {text: 'Line', value: 'Line'}
                ],
                'Integer': [
                    {text: 'Gauge', value: 'Gauge'},
                    {text: 'Line', value: 'Line'}
                ]
            },
            chartTypeRequires: {
                // chartsjs requires canvas; google charts need div
                'Gauge': 'div',
                'Line': 'canvas'
            },
            // value of selected chart type for individual device attributes
            chartTypesSelected: {}
        },
        computed: {
            chartColorList: function () {
                // generates allowed colors array for chartColors
                var list = [];
                for (var color in this.chartColors) {
                    list.push(this.chartColors[color]);
                }
                return list;
            },
            chartInitFunctions: function () {
                // initialise charts
                return {
                    'Gauge': this.updateGauge,
                    'Line': this.setLineChart
                };
            },
            chartUpdateFunctions: function () {
                // update charts
                return {
                    'Gauge': this.updateGauge,
                    'Line': this.updateLineChart
                };
            }
            // renderGauge: function (id) {
            //     return this.chartType[id] == true && (att.type == 'Double' || att.type=='Integer');
            // }
        },
        methods: {
            "largestTimestamp": function (obj, timestamp, that) {
                for (var key in obj) {
                    if (obj.hasOwnProperty(key)) {
                        if(typeof obj[key] == 'object') {
                            timestamp = that.largestTimestamp(obj[key], timestamp, that);
                        }
                        else if(key == 'timestamp') {
                            // console.log(obj[key]);
                            if (obj[key] > timestamp) {
                                timestamp = obj[key];
                            }
                        }
                    }
                }
                return timestamp;
            },
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
            "setLineChart": function (att) {
                var that = this;
                setTimeout(function () {
                    var ctx = document.getElementById('att_' + att.id).getContext('2d');
                    var chart = new Chart(ctx, {
                        type: 'line',
                        data: {
                            labels: [],
                            datasets: [{
                                label: att.name,
                                backgroundColor: that.chartColorList[att.id % that.chartColorList.length],
                                borderColor: that.chartColorList[att.id % that.chartColorList.length],
                                data: []
                            }]
                        },
                        // Configuration options go here
                        options: {}
                    });
                    that.chartObjects[att.id] = chart;
                    console.log(att.id);
                    console.log(that.chartObjects[att.id]);
                }, 1500);
            },
            "updateLineChart": function (att) {
                console.log(att.id);
                var date = new Date();
                var labels = this.chartObjects[att.id].config.data.labels;
                var data = this.chartObjects[att.id].config.data.datasets[0].data;
                labels.push(date.toLocaleTimeString());
                data.push(att.value);
                var removeLength = labels.length - this.maxDataLimit;
                if (removeLength > 0) {
                    labels.splice(0, removeLength);
                    data.splice(0, removeLength);
                }
                this.chartObjects[att.id].update();
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
            "refresh": function (isInit) {
                var that = this;

                $.ajax({
                    url: "/pubsub/shadow/" + thingId,
                    "method": "GET",
                    success: function (data) {
                        if (that.lastMessageVersion < data.version) {
                            that.lastMessageVersion = data.version;

                            var reportedObj = data.metadata.reported;
                            var timestamp = that.largestTimestamp(reportedObj, 0, that).toString();
                            timestamp = parseInt(timestamp.padEnd(13,"0"));

                            that.lastMessageTime = new Date(timestamp).toLocaleString();

                            console.log(that.lastMessageTime);
                            console.log(data.version);
                        }
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
                                            if (isInit){
                                                // that.chartTypesSelected[att.id] = 'Gauge';
                                                that.updateGauge(att);
                                            }
                                            else
                                                (that.chartUpdateFunctions[that.chartTypesSelected[att.id]])(att);
                                        } else if (att.type === "Double") {
                                            Vue.set(att, 'value', val);
                                            if (isInit){
                                                // that.chartTypesSelected[att.id] = 'Gauge';
                                                that.updateGauge(att);
                                            }
                                            else
                                                (that.chartUpdateFunctions[that.chartTypesSelected[att.id]])(att);
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
            }, that.interval);
        }
    });
</script>
</body>
</html>

