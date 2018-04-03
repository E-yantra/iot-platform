<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#if user??>
    <#include "../common/sidenavbar.ftl"/>
</#if>
    <main role="main" class="main col-sm-9 ml-sm-auto col-md-10 pt-3">
        <div class="row">
            <div class="col-md-12">
                <div class="float-right p-1" v-if="role=='ALL' || role=='WRITE'">
                    <button v-on:click="newDevice" class="btn btn-outline-primary">Add Device</button>
                    <button v-on:click="importThing" class="btn btn-outline-primary">Import Device</button>
                    <button v-on:click="addCron" class="btn btn-outline-primary">Add Cron</button>
                    <button v-on:click="newRule" class="btn btn-outline-primary">Create Rule</button>
                </div>
            </div>
            <div class="clearfix"></div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        {{ thing.name }}
                        <div class="float-right">
                            <div class="row clearfix">
                                <img src="/static/img/ajax-loader.gif" v-if="saveLoaderStorage">
                                <div class="col"><label class="badge badge-primary" for="storage">Enable storage</label>
                                </div>
                                <div class="col"><input type="checkbox" id="storage" class="form-check-input"
                                                        v-model="storageEnabled" v-on:change="enableStorage"></div>
                            </div>
                        </div>
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
                            <button v-on:click="downloadCertificates" class="btn btn-primary btn-sm">DOWNLOAD
                                CERTIFICATES
                            </button>
                            <button v-on:click="dashboard" class="btn btn-primary btn-sm">DASHBOARD</button>
                        </div>
                        <button v-on:click="deleteThing" class="btn btn-danger btn-sm float-left text-white"><i
                                class="fa fa-trash-o fa-lg"></i>DELETE
                            THING
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="crons.length != 0" class="row">
            <div class="col-md-12">
                <p>&nbsp;</p>
                <div class="card">
                    <div class="card-header">
                        Crons
                    </div>
                    <div class="card-body p-0">
                        <table class="table">
                            <tr v-for="cron in crons">
                                <td>
                                    {{cron.cronExpression}}
                                </td>
                                <td>
                                    {{cron.desiredState}}
                                </td>
                                <td>
                                    <button class="btn btn-danger btn-sm text-white" v-on:click="deleteCron(cron)">Delete</button>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="card-footer">
                        <button v-on:click="addCron" class="btn btn-outline-primary">Add Cron</button>
                        <#--<button v-on:click="deleteCron(cron)" class="btn btn-sm btn-default">EDIT</button>-->
                    </div>
                </div>
            </div>
        </div>
        <div v-if="rules.length != 0" class="row">
            <div class="col-md-12">
                <p>&nbsp;</p>
                <div class="card">
                    <div class="card-header">
                        Rules
                    </div>
                    <div class="card-body p-0">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Actions</th>
                                    <th>Settings</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="(rule,idx) in rules">
                                    <td>
                                        {{rule.name}}
                                    </td>
                                    <td>
                                        {{rule.description}}
                                    </td>
                                    <td>
                                        <button class="btn btn-danger btn-sm text-white" v-on:click="deleteRule(rule, idx)">DELETE</button>
                                        <button class="btn btn-sm btn-default">EDIT</button>
                                    </td>
                                    <td>
                                        <a v-bind:href="'/rules/' + rule.type + '/' + rule.snsAction.id" class="btn btn-primary btn-sm btn-primary" role="button" aria-pressed="true">CHANGE</a>
                                        <#--<button v-on:click="" class="btn btn-sm btn-success">CHANGE</button>-->
                                    </td>
                                </tr>
                        </table>
                    </div>
                    <div class="card-footer">
                        <button v-on:click="newRule" class="btn btn-outline-primary">Create Rule</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="row hidden hide" style="display: none;">
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
<#include "../modals/crud_cron.ftl"/>
<#include "../modals/generate.ftl"/>
<#include "../modals/crud_rule.ftl"/>
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
            saveLoaderStorage: false,
            role: "",
            unit: {},
            thing: {},
            devices: [],
            rules: [],
            createDevice: {
                "deviceAttributes": []
            },
            createRule: {},
            createSubscription: {},
            cttr: {},
            generateCode: "",
            generateMessage: "",
            cron: {},
            cronDevice: {},
            cronAttribute: {},
            cronExpression: "",
            cronAttributeValue: "",
            crons: [],
            storageEnabled: ""
        },
        methods: {
            "enableStorage": function () {
                var that = this;
                that.saveLoaderStorage = true;
                $.ajax({
                    url: "/rule/ddb/enable/" + thingId,
                    "method": "POST",
                    "data": {
                        enable: that.storageEnabled
                    },
                    success: function (data) {
                        // console.log(data);
                        that.saveLoaderStorage = false;
                        // alert("Storage settings updated!");
                    }
                });
            },

            //TODO: Fetch list of supported rule actoins from server
            "newRule": function () {
                this.createRule = {
                    name: "",
                    description: "",
                    data: "",
                    condition: "",
                    topic: "",
                    action: "",
                    actionList: ["sns"],
                    sns_topic: ""
                };
                $('#create_rule').modal('show');
            },


            "saveRule": function () {
                var that = this;
                that.saveLoader = true;

                var data = {
                    "name": that.createRule.name,
                    "description": that.createRule.description,
                    "data": that.createRule.data,
                    "topic": that.createRule.topic,
                    "condition": that.createRule.condition,
                    "action": that.createRule.action
                };

                if (that.createRule.action == 'sns') {
                    data.sns_topic = that.createRule.sns_topic;
                }

                $.ajax({
                    "url": "/rule/" + this.createRule.action + "/create/" + thingId,
                    "method": "POST",
                    "data": data,
                    success: function (data) {
                        that.saveLoader = false;
                        that.rules.push(data);
                        $('#create_rule').modal('hide');
                    }
                });
            },

            "deleteRule": function (rule, idx) {
                var that = this;
                $.ajax({
                    "url": "/rule/" + rule.type + "/delete/" + rule.id,
                    "method": "GET",
                    "success": function (data) {
                        if(data.success === true) {
                            alert("Rule deleted!");
                            that.rules.splice(idx, 1);
                        }
                    }

                })
            },

            "dashboard": function () {
                window.location = "/things/dashboard/" + thingId;
            },

            "downloadCertificates": function () {
                var that = this;
                var fileNames = ["certificate.crt", "private.key", "public.key"];
                fileNames.forEach(function (fileName) {
                    window.open("/thing/certificate/get/" + fileName + "/" + thingId, "_blank");
                });
                window.open("/thing/certificate/get/" + "rootCA" + "/" + thingId, "_blank");
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
                        that.storageEnabled = data.storageEnabled;
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
                $.ajax({
                    url: "/rule/sns/thing/" + thingId,
                    success: function (data) {
                        that.rules = data;
                    }
                });
                that.getCrons();

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

            "deleteDevice": function (device) {
                alert(device.id);
                this.saveLoader = true;
                var that = this;

                if (confirm("Are you sure you want to delete device?") && confirm("Are you really sure?")) {
                    $.ajax({
                        url: "/device/delete/" + device.id,
                        "method": "DELETE",
                        success: function (data) {
                            that.saveLoader = false;
                            alert(data);
                            //that.deleteDeviceAttributes(device.id);
                            that.load();
                        }
                    });
                }
            },

            "deleteDeviceAttributes": function (deviceId) {
                if (confirm("Are you sure you want to delete device?") && confirm("Are you really sure?")) {
                    $.ajax({
                        url: "/attribute/delete/" + deviceId,
                        "method": "DELETE",
                        success: function (data) {
                            alert('Device attributes deleted');
                            this.load();
                        }
                    });
                }
            },

            "deleteThing": function () {
                alert(thingId);
                if (confirm("Are you sure you want to delete thing?") && confirm("Are you really sure?")) {
                    $.ajax({
                        url: "/thing/delete/" + thingId,
                        "method": "DELETE",
                        success: function (data) {
                            alert('Thing deleted');
                            this.load();
                        }
                    });
                }
            },

            "newDevice": function () {
                this.createDevice = {
                    deviceAttributes: []
                };
                $("#create_device").modal('show');
            },

            "addCron": function () {

                $("#create_cron").modal('show');
            },

            "edit": function () {

            },

            "deleteCron": function (cron) {
                var that = this;
                if (confirm("Are you sure you want to delete this cron?")) {
                    $.ajax({
                        url: "/cron/delete/" + cron.id,
                        "method": "DELETE",
                        success: function (data) {
                            that.getCrons();
                        }
                    });

                }
            },

            "getCrons": function () {
                var that = this;
                $.ajax({
                    url: "/cron/thing/" + thingId,
                    "method": "GET",
                    success: function (data) {
                        that.crons = data;
                    }
                });
            },

            "saveCron": function () {

                var that = this;
                that.saveLoader = true;
                var desired = {};

                desired["device" + that.cronDevice.id + "." + that.cronAttribute.id] = (that.cronAttribute.type === 'Integer' || that.cronAttribute.type === 'Boolean') ? parseInt(that.cronAttributeValue, 10) : that.cronAttributeValue;
                if (that.cronAttribute.type === 'Double') {
                    desired["device" + that.cronDevice.id + "." + that.cronAttribute.id] = parseFloat(that.cronAttributeValue);
                }
                $.ajax({
                    url: "/cron/create",
                    "method": "POST",
                    "data": {
                        "thingId": thingId,
                        "cronExpression": that.cronExpression,
                        "desiredState": JSON.stringify(desired)
                    },
                    success: function (data) {
                        that.saveLoader = false;
                        $("#create_cron").modal('hide');
                        that.getCrons();
                    }
                });
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
                        that.generateCode = data.substr(0, data.search("{") - 1);
                        that.generateMessage = data.substr(data.search("{"));
                    }
                });
            },

            "copyToClipboard": function () {
                $("#shadow-message").select();
                document.execCommand("Copy");
            },

            "importThing": function () {
                //TODO
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
                            $("#create_device").modal("hide");
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
