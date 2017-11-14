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
    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    <p>Don't have userid ? <router-link to="signup">Signup</router-link></p>
</form>
</div>