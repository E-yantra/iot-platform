<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
<main role="main" class="main">
    <form autocomplete="on">
        <div style="float: right;margin-bottom: 10px;" v-if="role=='ALL' || role=='WRITE'"><a href="/units/create" role="button"class="btn btn-primary">Add New</a></div>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Unit Name</th>
                <th scope="col">Description</th>
                <th scope="col">Photo</th>
                <th scope="col">Parent ID</th>
                <th scope="col">Action </th>
            </tr>
            </thead>
            <tbody>
            </tbody>
        </table>

    </form>
</main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    $(document).ready(function(){
        $.ajax({
            type: 'GET',
            url: '/unit/list/page/0',
            dataType: 'json',
            success: function(data) {
                for(var row in data){
                    console.log(data[row]);

                    $("#userTable").append("<tr><td>" + data[row].id + "</td><td>" + data[row].unitName + "</td><td>" + data[row].description+ "</td><td>" + data[row].photo + "</td><td><button type=\"button\" class= \"btn btn-primary btn-sm\" >Edit</button>&nbsp;<button type=\"button\" class= \"btn btn-danger btn-sm\" >Delete</button></td></tr>");
                }
            },
        });
    });

</script>
</body>
</html>