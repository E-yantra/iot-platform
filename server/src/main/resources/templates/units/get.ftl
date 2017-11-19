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
                <button v-on:click="newUnit" class="btn btn-outline-primary">Create Subunit</button>
                <button v-on:click="newThing" class="btn btn-outline-primary">Create Thing</button>
                <a href="" class="btn btn-outline-primary">Import</a>
                <a href="" class="btn btn-outline-primary">Export</a>
                <a href="" class="btn btn-outline-primary">Add User</a>
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
                            <img height=150 src="${unit.photo!""}" class="float-left p-1"> <p>${unit.description!""}</p>
                            <div class="clear"></div>
                        </div>
                        <div class="card-footer">
                            <button v-on:click="edit" class="btn btn-primary btn-sm float-right">EDIT</button>
                            <button class="btn btn-danger btn-sm float-left text-white">DELETE</button>
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
                            <table class="table table-striped">
                                <tr><th>Name</th><th>Actions</th></tr>
                                <tr v-for="u in things"><td><a v-bind:href="'/things/get/'+u.id">{{u.name}}</a></td><td><button v-on:click="editThing(u)" class="btn btn-default btn-sm">EDIT</button> </td></tr>
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
                            <ul>
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
                        <tr><th>ID</th><th>Name</th><th>Email</th><th>Role</th></tr>
                        <tr v-for="right in rights"><td>{{right.user.id}}</td><td>
                            {{right.user.name}}</td>
                            <td>{{right.user.email}}</td>
                            <td>{{right.role}}</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>
<#include "../modals/crud_unit.ftl"/>
<#include "../modals/crud_thing.ftl"/>
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
            saveLoader:false,
            subunits:[],
            things:[]
        },
        methods:{
            "load":function(){

                var that = this;
                $.ajax({
                    url: "/unit/rights/"+unitId+"/"+userId,
                    success: function (data) {
                        that.role = data[0].role;
                    }
                });

                $.ajax({
                    url: "/unit/get/"+unitId,
                    success: function (data) {
                        that.unit = data;
                    }
                });


                $.ajax({
                    url: "/unit/users/"+unitId,
                    success: function (data) {
                        that.rights = data;
                    }
                });

                $.ajax({
                    url: "/unit/subunits/"+unitId,
                    success: function (data) {
                        that.subunits = data;
                    }
                });
                $.ajax({
                    url: "/thing/unit/"+unitId,
                    success: function (data) {
                        that.things = data;
                    }
                });

            },
            "newUnit":function(){
                this.createUnit = {
                    "parentUnit_id":unitId
                };
                $("#create_unit").modal('show')
            },
            "newThing":function(){
                this.createThing = {
                    "parentUnitId":unitId
                };
                $("#create_thing").modal('show')
            },
            "editThing":function(thing){
                this.createThing = thing;
                $("#create_thing").modal('show')
            },
            "edit":function(){
                this.createUnit = this.unit;
                $("#create_unit").modal('show')
            },
            "saveThing":function(){
                this.saveLoader = true;
                var that = this;
                if(!this.createThing.id) {
                    that.createThing.parentUnitId = unitId;
                    $.ajax({
                        "url": "/thing/create",
                        "method":"POST",
                        "data": that.createThing,
                        "success":function (data) {
                            that.saveLoader = false;
                            $("#create_thing").modal('hide');
                            that.load();
                        }
                    });
                }else{
                    $.ajax({
                        "url": "/thing/update/"+that.createThing.id,
                        "method":"POST",
                        "data": that.createThing,
                        "success":function (data) {
                            that.saveLoader = false;
                            $("#create_thing").modal('hide');
                            that.load();
                        }

                    });
                }
            },
            "saveUnit":function(){
                this.saveLoader = true;
                var that = this;
                if(!this.createUnit.id) {
                    that.createUnit.parentUnitId = unitId;
                    $.ajax({
                        "url": "/unit/create",
                        "method":"POST",
                        "data": that.createUnit,
                        "success":function (data) {
                            that.saveLoader = false;
                            $("#create_unit").modal('hide');
                            that.load();
                        }
                    });
                }else{
                    $.ajax({
                        "url": "/unit/update/"+unitId,
                        "method":"POST",
                        "data": that.createUnit,
                        "success":function (data) {
                            that.saveLoader = false;
                            $("#create_unit").modal('hide');
                            that.load();
                        }

                    });
                }
            }
        },
        mounted:function(){
            this.load()

        }
    })
</script>
</body>
</html>
