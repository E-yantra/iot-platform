<#include "../../common/header.ftl">
<body>
<#include "../../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../../common/sidenavbar.ftl"/>
    <main role="main" class="main col-sm-9 ml-sm-auto col-md-10 pt-3 pb-5">
        <h2 class="pt-2">Settings</h2>
        <hr/>
        <div class="row pb-3 pt-3">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        SNS Settings
                    </div>
                    <div class="card-body p-0">
                        <table class="table mb-0">
                            <thead>
                            <tr>
                                <th>Name</th>
                                <th>Value</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                                    SNS Topic
                                </td>
                                <td>
                                    {{sns.topic}}
                                </td>
                            </tr>
                            <tr>
                                <td>
                                    Topic ARN
                                </td>
                                <td>
                                    {{sns.topicARN}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                <#--<div class="card-footer">-->
                <#--<button type="button" v-on:click="editSns" class="float-right btn btn-sm btn-primary">EDIT</button>-->
                <#--</div>-->
                </div>
            </div>
        </div>
        <div class="row pb-3 pt-3">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        SNS Subscriptions
                    </div>
                    <div class="card-body p-0">
                        <table class="table mb-0" v-if="sns.subscriptions.length != 0">
                            <thead>
                            <tr>
                                <th>Protocol</th>
                                <th>Value</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="sub in sns.subscriptions">
                                <td>
                                    {{sub.type}}
                                </td>
                                <td>
                                    {{sub.value}}
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <div class="h3 pt-2 pb-2 text-center text-muted" v-else>No subscriptions</div>
                    </div>
                    <div class="card-footer">
                        <button type="button" v-on:click="newSubscription"
                                class="float-right btn btn-sm btn-primary">ADD SUBSCRIPTION
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <#include "../../modals/crud_sns.ftl"/>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    var token = $.cookie("authorization");
    var snsId = ${sns.id};
    <#--var parentId = ${thing.id};-->
    var app = new Vue({
        el: '#container-main',
        data: {
            sns: {},
            createSubscription: {},
            saveLoader: false
        },
        methods: {

            newSubscription: function () {
                this.createSubscription = {
                    type: '',
                    value: ''
                };
                $('#add_subscription').modal('show');
            },

            addSubscription: function () {
                var that = this;
                that.saveLoader = true;
                console.log('Inside add subscription!');
                $.ajax({
                    'url': '/rule/sns/subscribe/' + snsId,
                    'method': 'POST',
                    'data': {
                        'type': that.createSubscription.type,
                        'value': that.createSubscription.value
                    },
                    success: function (data) {
                        that.saveLoader = false;
                        if(!that.sns.subscriptions)
                            that.sns.subscriptions = [];
                        that.sns.subscriptions.push(data);
                        $('#add_subscription').modal('hide');
                    }
                });
            },

            load: function () {
                var that = this;
                $.ajax({
                    'url': '/sns/get/' + snsId,
                    'success': function (data) {
                        that.sns = data;
                    }
                });
            }
        },
        mounted: function () {
            this.load();
        }
    });
</script>
</body>
</html>