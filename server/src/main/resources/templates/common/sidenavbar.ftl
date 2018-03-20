<nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
    <#if user??>
        <ul class="nav nav-pills flex-column">
            <li class="nav-item">
               <a class="nav-link <#if active?? && active=="home">active</#if>" href="/">Home  <i style="float: right;" class="fa fa-home fa-lg" aria-hidden="true"></i></a>
            </li>
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="profile">active</#if>" href="/profile">My Profile<i style="float: right;"  class="fa fa-user fa-lg" aria-hidden="true"></i></a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-red" href="/logout">Logout <i style="float: right;" class="fa fa-sign-out fa-lg" aria-hidden="true"></i></a>
            </li>
        </ul>
    </#if>
</nav>