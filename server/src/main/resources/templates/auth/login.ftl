    <#include "../common/header.ftl">
    <body>
    <div class="container-fluid" id="container-main">
    <main role="main" class="col-sm-12">
    <div>
        <section class="container">
            <section class="login-form">
                <form class="form-signin" autocomplete="on" method="post" action="" role="login">
                    <div id="app" v-if="error" class="alert alert-danger">
                        {{ message }}
                    </div>
                    <div style="text-align: center;font-size: 30px;"><a style="text-decoration: none;" href="#">IoT Platform</a></div>
                    <input type="email" name="email" id="inputEmail" class="form-control" placeholder="Email address" required="" autofocus="" autocomplete="off" />
                    <input type="password" name="password" id="inputPassword" class="form-control" placeholder="Password" required="" autocomplete="off" />
                    <div style="text-align: left" class="checkbox">
                            <input type="checkbox" value="remember-me"> Remember me
                    </div>
                    <button v-on:click.prevent="login" id="submit" class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
                    <div>
                        <p>Don't have userid?&nbsp;<a href="signup">Signup</a></p>
                    </div>
                </form>
            </section>
        </section>

    </div>
        </main>
    </div>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
    <script>
        var example1 = new Vue({
            el: '#container-main',
            data: {
                error:false,
                message: ""
            },
            methods: {
                login:function() {
                    var that = this;
                    $.ajax({
                        type: 'POST',
                        url: "/auth/token",
                        data: $('.form-signin').serialize(),
                        headers: {
                            "Authorization":"Basic grtrthj45h45h4j5h4kj5k45hjk4kh5j",
                            "My-Second-Header":"second value"
                        },
                        success: function(data){

                            if(!data.token){
                                $.removeCookie('name');
                                that.error = true;
                                that.message = "Could not authenticate you. Please check email/password."

                            }else{
                                $.cookie('authorization', data.token);
                                window.location = "/";
                            }
                        }
                    });
                }
            }
        })


    </script>
    <style>
        /*login form css */
        @CHARSET "UTF-8";

        * {
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            outline: none;
        }
        body {
            background: url(static/img/bg.jpg) no-repeat center center fixed;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
            font-family: 'Roboto', sans-serif !important;
        }

        .login-form {
            font: 16px/2em;
            margin: 100px auto;
            max-width: 400px;
        }

        form[role=login] {
            color: #5d5d5d;
            background: #f2f2f2;
            padding: 26px;
            border-radius: 10px;
            -moz-border-radius: 10px;
            -webkit-border-radius: 10px;
        }
        form[role=login] img {
            display: block;
            margin: 0 auto;
            margin-bottom: 35px;
        }
        form[role=login] input,
        form[role=login] button {
            font-size: 18px;
            margin: 10px 0;
        }
        form[role=login] > div {
            text-align: center;
        }

        .form-links {
            text-align: center;
            margin-top: 1em;
            margin-bottom: 50px;
        }
        .form-links a {
            color: #fff;
        }
        a {
            color: #20a8d8 !important;
        }

    </style>
    </body>
    </html>