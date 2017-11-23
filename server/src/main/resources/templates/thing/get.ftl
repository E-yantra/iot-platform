<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#if user??>
    <#include "../common/sidenavbar.ftl"/>
</#if>
    <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
        <div class="row">
            <div class="col-md-12">
                <div class="float-right p-1" v-if="role=='ALL' || role=='WRITE'">
                    <button v-on:click="newDevice" class="btn btn-outline-primary">Add Device</button>
                    <button class="btn btn-outline-primary">Import Device</button>
                </div>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        {{ thing.name }}
                    </div>
                    <div class="card-body p-0">
                        <table class="table">
                            <tr>
                                <th>Device Name</th>
                                <th>Actions</th>
                            </tr>
                            <tr v-for="d in devices">
                                <td>{{d.name}}</td>
                                <td>
                                    <Button v-on:click="deleteDevice(d)" class="btn btn-sm btn-danger text-white">
                                        DELETE
                                    </Button>
                                    <Button v-on:click="editDevice(d)" class="btn btn-sm btn-default">EDIT</Button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="card-footer">
                        <div class="float-right">
                            <button v-on:click="edit" class="btn btn-primary btn-sm">EDIT</button>
                            <button v-on:click="generate" class="btn btn-primary btn-sm">GENERATE CLIENT</button>
                        </div>
                        <button class="btn btn-danger btn-sm float-left text-white"><i class="fa fa-trash-o fa-lg"></i>DELETE
                            THING
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-6">
                <p>&nbsp;</p>
                <div class="card">
                    <div class="card-header">
                        Test Topics
                    </div>

                    <div class="card-body p-1">
                        <form>
                            <div class="form-group">
                                <label class="col-form-label" for="formGroupExampleInput">Topic Name</label>
                                <input v-model="testTopic" type="text" class="form-control" id="formGroupExampleInput"
                                       placeholder="/topic_name">

                            </div>
                            <div class="form-group">
                                <input type="text" class="form-control" v-model="payload" placeholder="payload">
                            </div>
                            <div class="form-group">
                                <button v-on:click="publish" class="btn btn-default" type="button">Publish</button>
                                <button v-on:click="subscribe" class="btn btn-default" type="button">Subscribe</button>
                            </div>
                        </form>
                        <div>
                            <pre class="p-1" v-for="s in subscribed">{{s}}</pre>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
<#include "../modals/crud_device.ftl"/>
<#include "../modals/generate.ftl"/>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    var token = $.cookie("authorization");
    var userId = ${user.id};
    var thingId = ${thing.id};
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
            createDevice: {
                "deviceAttributes": []
            },
            cttr: {},
            generateCode: ""
        },
        methods: {
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
                    }
                });

            },
            "removeAttr": function (key) {
                if (key !== -1) {
                    array.splice(key, 1);
                }
            },
            "addAttr": function () {
                if (this.cttr.name && this.cttr.type) {
                    this.createDevice.deviceAttributes.push(Object.assign({}, this.cttr));
                }

            },
            "deleteDevice": function () {

            },
            "newDevice": function () {
                this.createDevice = {
                    deviceAttributes: []
                };
                $("#create_device").modal('show');
            },
            "edit": function () {

            },
            "generate": function () {

                $("#generate_code").modal('show');
                saveLoader = true;
                var that = this;
                $.ajax({
                    url: "/device/generate/" + thingId,
                    "method": "GET",
                    success: function (data) {
                        that.saveLoader = false;
                        that.generateCode = JSON.stringify(data, null, 4);
                    }
                });
            },
            "saveDevice": function () {
                var that = this;
                this.saveLoader = true;
                that.createDevice.ownerUnitId = that.thing.parentUnit.id;
                that.createDevice.parentThingId = that.thing.id;
                if (this.createDevice.id) {
                    $.ajax({
                        url: "/device/update/" + that.createDevice.id,
                        "method": "POST",
                        data: that.createDevice,
                        success: function (data) {
                            that.saveLoader = false;
                            that.saveAttributes(data.id, that.createDevice.deviceAttributes);
                            that.load();
                        }
                    });
                } else {
                    $.ajax({
                        url: "/device/create",
                        data: that.createDevice,
                        "method": "POST",
                        success: function (data) {

                            that.saveLoader = false;
                            that.saveAttributes(data.id, that.createDevice.deviceAttributes);
                            that.load();

                        }
                    });
                }

            },
            "editDevice": function (device) {
                this.createDevice = device;
                $("#create_device").modal('show');
            },
            "saveAttributes": function (deviceId, attributes) {
                $.ajax({
                    "url": "/attribute/add/" + deviceId,
                    "method": "POST",
                    "data": JSON.stringify(attributes),
                    contentType: "application/json; charset=utf-8",
                    "success": function (data) {
                        that.saveLoader = false;
                        that.load();
                    }
                });
            },
            "saveUnit": function () {
                this.saveLoader = true;
                var that = this;
                if (!this.createUnit.id) {
                    that.createUnit.parentUnitId = unitId;
                    $.ajax({
                        "url": "/unit/create",
                        "method": "POST",
                        "data": that.createUnit,
                        "success": function (data) {
                            that.saveLoader = false;
                            $("#create_unit").modal('hide');

                            that.load();
                        }
                    });
                } else {
                    $.ajax({
                        "url": "/unit/update/" + unitId,
                        "method": "POST",
                        "data": that.createUnit,
                        "success": function (data) {
                            that.saveLoader = false;
                            $("#create_unit").modal('hide');
                            that.load();
                        }

                    });
                }
            }
        },
        mounted: function () {
            this.load()

        }
    })
</script>
</body>
</html>
