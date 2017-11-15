<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
<div class="row">
<form class="form-signin col-md-4">
    <h2 class="form-signin-heading">Please Login</h2><br>
    <label for="inputEmail" class="sr-only">Email address</label>
    <input type="email" id="inputEmail" class="form-control" placeholder="Email address" required="" autofocus="" autocomplete="off" ><br><br>
    <label for="inputPassword" class="sr-only">Password</label>
    <input type="password" id="inputPassword" class="form-control" placeholder="Password" required="" autocomplete="off" >
    <br><br>
    <div class="checkbox">
        <label>
            <input type="checkbox" value="remember-me"> Remember me
        </label>
    </div>
    <button id="submit" class="btn btn-lg btn-primary btn-block" type="button">Sign in</button>
    <p>Don't have userid ? <router-link to="signup">Signup</router-link></p>
</form>
</div>
    </main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    $("#submit").on("click",function () {
        $.ajax({
            type: 'POST',
            url: "/auth/basic",
            data: $('.form-signin').serialize(),
            success: function(data){

            }
        });
    });
</script>
</body>
</html>