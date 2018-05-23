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
                <button v-on:click="newUnit" class="btn btn-outline-primary">Create Subunit</button>
                <button v-on:click="newThing" class="btn btn-outline-primary">Create Thing</button>
                <button v-on:click="importUnit" class="btn btn-outline-primary">Import</button>
                <button v-on:click="exportUnit" class="btn btn-outline-primary">Export</button>
                <button v-on:click="newUser" class="btn btn-outline-primary">Add User</button>
            </div>
        </div>
        <div class="clearfix"></div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="row">
                <div class="col-md-12">
                    <div class="card" >
                        <div class="card-header">
                            {{ unit.unitName }} <span class="badge badge-success">{{role}}</span>
                        </div>
                        <div class="card-body">
                            <img height=150 v-bind:src="unit.photo || ''" class="float-left p-1"><p>{{unit.description || ''}}</p>
                            <div class="clear"></div>
                        </div>
                        <div class="card-footer">
                            <button v-on:click="edit" class="btn btn-primary btn-sm float-right">EDIT</button>
                            <button v-on:click="deleteUnit" class="btn btn-danger btn-sm float-left text-white">DELETE</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <p>&nbsp;</p>
                    <div class="card" >
                        <div class="card-header">
                            Things
                        </div>
                        <div class="card-body p-0">
                            <span v-if="things.length==0">
                                No Things for this Unit.
                            </span>
                            <table v-if="things.length>0" class="table table-striped">
                                <tr><th>Name</th><th>Actions</th></tr>
                                <tr v-for="u in things">
                                    <td><a v-bind:href="'/things/get/'+u.id">{{u.name}}</a></td>
                                    <td><button v-on:click="editThing(u)" class="btn btn-default btn-sm">EDIT</button> </td>
                                </tr>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <p>&nbsp;</p>
                    <div class="card" >
                        <div class="card-header">
                            Subunits
                        </div>
                        <div class="card-body">
                            <span v-if="subunits.length==0">
                                No Subunits for this Unit.
                            </span>
                            <ul v-if="subunits.length>0">
                                <li v-for="u in subunits">
                                    <a v-bind:href="'/units/get/'+u.id">{{u.unitName}}</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

        </div>
        <div class="col-md-6">
            <div class="card" >
                <div class="card-header">
                    Authorized Users
                </div>
                <div class="card-body p-0">
                    <table class="table table-striped">
                        <tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th><th>Actions</th></tr>
                        <tr v-for="right in rights"><td>{{right.user.id}}</td><td>
                            {{right.user.name}}</td>
                            <td>{{right.user.email}}</td>
                            <td>{{right.role}}</td>
                            <td><button v-on:click="editUser(right)" class="btn btn-default btn-sm">EDIT</button>
                            <button v-on:click="deleteUser(right)" class="btn btn-default btn-sm">DELETE</button> </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>


<#include "../modals/crud_unit.ftl"/>
<#include "../modals/crud_thing.ftl"/>
<#include "../modals/crud_user.ftl"/>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    var token = $.cookie("authorization");
    var userId = ${user.id};
    var unitId = ${unit.id};
    var app = new Vue({
        el: '#container-main',
        data: {
            unit:{},
            role:"",
            rights:[],
            createUnit:{},
            createThing:{},
            createUser:{
                "rights":[]
            },
            saveLoader:false,
            subunits:[],
            things:[]
            // deleteUnit:{}
        },
        methods: {
            "load": function () {

                var that = this;
                $.ajax({
                    url: "/unit/rights/" + unitId + "/" + userId,
                    success: function (data) {
                        that.role = data[0].role;
                    }
                });

                $.ajax({
                    url: "/unit/get/" + unitId,
                    success: function (data) {
                        that.unit = data;
                    }
                });


                $.ajax({
                    url: "/unit/users/" + unitId,
                    success: function (data) {
                        that.rights = data;
                    }
                });

                $.ajax({
                    url: "/unit/subunits/" + unitId,
                    success: function (data) {
                        data.sort(function (a,b) {
                           return a.unitName < b.unitName ? -1 : 1;
                        });
                        that.subunits = data;
                    }
                });
                $.ajax({
                    url: "/thing/unit/" + unitId,
                    success: function (data) {
                        that.things = data;
                    }
                });

            },
            "newUnit": function () {
                this.createUnit = {
                    "parentUnit_id": unitId
                };
                $("#create_unit").modal('show')
            },
            "newThing": function () {
                this.createThing = {
                    "parentUnitId": unitId
                };
                $("#create_thing").modal('show')
            },
            "editThing": function (thing) {
                this.createThing = thing;
                $("#create_thing").modal('show')
            },
            "newUser": function () {
                this.createUser = {
                    "unitId": unitId,
                    "userId": 0
                };
                $("#create_user").modal('show')
            },
            "exportUnit": function () {
                alert(JSON.stringify(this.unit));
            },
            "importUnit": function () {
                //TODO
            },
            "editUser": function (user) {
                this.createUser = user;
                $("#create_user").modal('show')
            },
            "deleteUser": function (right) {
                var that = this;

                if (confirm("Are you sure you want to delete rights of the user for this unit?") && confirm("Are you really sure?")) {
                    /*$.ajax({
                        url: "/user/delete/" + right.user.id,
                        "method": "DELETE",
                        success: function (data) {
                            alert('User deleted');
                            that.deleteRight(right.id);
                            that.load();
                        }
                    });*/

                    $.ajax({
                        url: "/right/delete/" + right.id,
                        "method": "DELETE",
                        success: function (data) {
                            alert('rights deleted');
                            that.load();
                        }
                    });
                }
            },
            "deleteRight": function (rightId) {
                alert(rightId);
                $.ajax({
                    url: "/right/delete/" + rightId,
                    "method": "DELETE",
                    success: function (data) {
                        alert('rights deleted');
                        this.load();
                    }
                });
            },
            "edit": function () {
                this.createUnit = this.unit;
                $("#create_unit").modal('show')
            },
            "saveThing": function () {
                this.saveLoader = true;
                var that = this;
                if (!this.createThing.id) {
                    that.createThing.parentUnitId = unitId;
                    $.ajax({
                        "url": "/thing/create",
                        "method": "POST",
                        "data": that.createThing,
                        "success": function (data) {
                            that.saveLoader = false;
                            $("#create_thing").modal('hide');
                            that.load();
                        }
                    });
                } else {
                    $.ajax({
                        "url": "/thing/update/" + that.createThing.id,
                        "method": "POST",
                        "data": that.createThing,
                        "success": function (data) {
                            that.saveLoader = false;
                            $("#create_thing").modal('hide');
                            that.load();
                        }

                    });
                }
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
                            that.unit = data;
                            Vue.set(that.unit, 'id', data.id);
                            Vue.set(that.unit, 'description', data.description);
                            Vue.set(that.unit, 'unitName', data.unitName);
                            Vue.set(that.unit, 'photo', data.photo);
                            that.load();
                        }
                    });
                } else {
                    $.ajax({
                        "url": "/unit/update/" + unitId,
                        "method": "PUT",
                        "data": that.createUnit,
                        "success": function (data) {
                            that.saveLoader = false;
                            $("#create_unit").modal('hide');
                            that.load();
                        }
                    });
                }
            },
            "saveUser": function () {
                this.saveLoader = true;
                var that = this;
                if (!this.createUser.id) {
                    //
                    $.ajax({
                        "url": "/user/create",
                        "method": "POST",
                        "data": that.createUser,
                        "success": function (data) {
                            that.saveLoader = false;
                            $("#create_user").modal('hide');
                            that.createUser.userId = data.id;
                            that.saveRights(that.createUser);
                            that.load();
                        }
                    });
                } else {
                    $.ajax({
                        "url": "/user/update/" + userId,
                        "method": "POST",
                        "data": that.createUser,
                        "success": function (data) {
                            that.saveLoader = false;
                            $("#create_user").modal('hide');
                            that.createUser.userId = data.id;
                            that.saveRights(that.createUser);
                            that.load();
                        }
                    });
                }
            },
            "saveRights": function (attributes) {

                var that = this;
                $.ajax({
                    "url": "/right/create",
                    "method": "POST",
                    "data": attributes,
                    //contentType: "application/json; charset=utf-8",
                    "success": function (data) {
                        that.saveLoader = false;
                        that.load();
                    }
                });
            },
            "deleteUnit": function () {
                if (confirm("Are you sure you want to delete unit?") && confirm("Are you really sure?")) {
                    $.ajax({
                        url: "/unit/delete/" + unitId,
                        "method": "DELETE",
                        success: function (data) {
                            alert('Unit deleted');
                            this.load();
                        }
                    });
                }
            }
        },
        mounted:function(){
            this.load()
        }
    });
</script>
</body>
</html>
