<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
<main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
<form autocomplete="on">
    <div class="form-group">
        <label for="inputEmail4">User Name:</label>
        <input type="text" class="form-control" id="inputUsername" placeholder="User Name">
    </div>


    <div class="form-group">
        <label for="comment">email:</label>
        <input type="email"  class="form-control" id="userEmail" value="email@example.com">
    </div>
    <div class="form-group">
        <label for="inputAddress">Password:</label>
        <input type="password" class="form-control" id="userPassword" placeholder="Password">
    </div>

    <button type="button" class="btn btn-primary" id="userCreate"><i class="fa fa-plus" aria-hidden="true"></i>Create</button>
</form>
    </main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    $(document).ready(function(){
        $("#userCreate").on("click",function(){
            var username = $("#inputUsername").val();
            var userEmail = $("#userEmail").val();
            var userPassword = $("#userPassword").val();
            $.ajax({
                type: 'POST',
                url: '/user/create',
                data: JSON.stringify({name:username,email:userEmail,password:userPassword}),
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                success: function(data) {},
            });
        })
    });
</script>
</body>
</html>