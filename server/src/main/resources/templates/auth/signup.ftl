<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
<div class="row">
    <form class="form-signin col-md-5">
        <h2 class="form-signin-heading">Please Signup</h2><br>
        <label for="inputEmail" class="sr-only">Name</label>
        <input type="text" id="inputName" class="form-control" placeholder="User Name" required="" autofocus="" autocomplete="off" ><br><br>
        <label for="inputEmail" class="sr-only">Email address</label>
        <input type="email" id="inputEmail" class="form-control" placeholder="Email address" required="" autofocus="" autocomplete="off" ><br><br>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" class="form-control" placeholder="Password" required="" autocomplete="off" >
        <br><br>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign Up</button>
        <p>Already have userid? <router-link to="login">Login</router-link></p>
    </form>
</div>
</main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
</body>
</html>