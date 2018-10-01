<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="main col-sm-9 ml-sm-auto col-md-10 pt-3 pb-5">
        <form class="user-info-form" autocomplete="on">
            <div class="form-group">

                <input type="hidden" class="form-control" id="inputUserId" value="${user.id}" >
            </div>

            <div class="form-group">
                <label for="inputEmail4">User Name:</label>
                <input type="text" name="name" class="form-control" id="inputUsername" value="${user.name}" required="">
            </div>

            <div class="form-group">
                <label for="comment">E-mail:</label>
                <input type="email" name="email" class="form-control" id="userEmail" value="${user.email}" required="">
            </div>

            <div class="form-group">
                <label for="inputAddress">Password:</label>
                <input type="password" name="password" class="form-control" id="userPassword" value="${user.password}" required="">
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
                type: 'PUT',
                url: '/user/update/' + userId,
                data: $('.user-info-form').serialize(),
                // data: JSON.stringify({name:username,email:userEmail,password:userPassword}),
                contentType: "application/x-www-form-urlencoded; charset=utf-8",
                dataType: 'json',
                success: function(data) {

                    if(!data.id) {
                        alert("User info not updated! Verify your fields and try again.");
                    }
                    else alert("User info updated");
                },
            });
        })
    });
</script>
</body>
</html>