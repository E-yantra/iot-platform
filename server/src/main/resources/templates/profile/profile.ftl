<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
        <form>
            <div class="form-group">

                <input type="hidden" class="form-control" id="inputUserId" value="${user.id}" >
            </div>

            <div class="form-group">
                <label for="inputEmail4">User Name:</label>
                <input type="text" class="form-control" id="inputUsername" value="${user.name}" >
            </div>

            <div class="form-group">
                <label for="comment">email:</label>
                <input type="email"  class="form-control" id="userEmail" value="${user.email}" >
            </div>

            <div class="form-group">
                <label for="inputAddress">Password:</label>
                <input type="password" class="form-control" id="userPassword" value="${user.password}" >
            </div>

            <button type="button" class="btn btn-primary" id="userUpdate"><i class="fa fa-pencil" aria-hidden="true"></i>Update</button>
        </form>
    </main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    $(document).ready(function(){
        $("#userUpdate").on("click",function(){
            var userId = $("#inputUserId").val();
            var username = $("#inputUsername").val();
            var userEmail = $("#userEmail").val();
            var userPassword = $("#userPassword").val();

            //debugger;

            $.ajax({
                type: 'POST',
                url: '/user/update/' + userId,
                data: JSON.stringify({name:username,email:userEmail,password:userPassword}),
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                success: function(data) {
                    alert("User info updated");
                },
            });
        })
    });
</script>
</body>
</html>