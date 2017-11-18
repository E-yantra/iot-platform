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
            <div class="float-right p-1
" v-if="role=='ALL' || roles=='WRITE'">
                <a href="" class="btn btn-outline-primary">Create Subunit</a>
                <a href="" class="btn btn-outline-primary">Create Thing</a>
                <a href="" class="btn btn-outline-primary">Import</a>
                <a href="" class="btn btn-outline-primary">Export</a>
                <a href="" class="btn btn-outline-primary">Add User</a>
            </div>
        </div>
        <div class="clearfix"></div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="card" >
                <div class="card-header">
                    {{ unit.unitName }} <span class="badge badge-success">{{role}}</span>
                </div>
                <div class="card-body">
                    <img height=150 src="${unit.photo}" class="float-left p-1"> <p>${unit.description}</p>
                    <div class="clear"></div>
                </div>
                <div class="card-footer">
                    <button class="btn btn-primary btn-sm float-right">EDIT</button>
                    <button class="btn btn-danger btn-sm float-left">DELETE</button>
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
                        <tr v-for="user in users"><td>{{user.id}}</td><td>
                            {{user.name}}</td>
                            <td>{{user.email}}</td></tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>
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
            unit:"",
            role:"",
            users:[]
        },
        methods:{
            "load":function(){

            }
        },
        mounted:function(){
            var that = this;
            $.ajax({
                url: "/unit/rights/"+unitId+"/"+userId,
                success: function (data) {
                    that.role = data[0].role;
                    that.unit = data[0].unit;
                }
            });

            $.ajax({
                url: "/unit/users/"+unitId,
                success: function (data) {
                    that.users = data;
                }
            });

        }
    })
</script>
</body>
</html>
