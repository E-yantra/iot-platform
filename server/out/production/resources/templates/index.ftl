<#include "common/header.ftl">
<body>
<#include "common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
    <sidebar></sidebar>
    <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
        <router-view>
        </router-view>
    </main>
</div>
<script type="application/x-template" id="sidebar-template">
<#include "common/sidenavbar.ftl"/>
</script>
<script type="application/x-template" id="login-template">
<#include "auth/login.ftl"/>
</script>
<script type="application/x-template" id="signup-template">
<#include "auth/signup.ftl"/>
</script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
</body>
</html>