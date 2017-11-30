<nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
    <#if user??>
        <ul class="nav nav-pills flex-column">
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="home">active</#if>" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="profile">active</#if>" href="/profile">My Profile</a>
            </li>
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="rules">active</#if>" href="/rules">Rules</a>
            </li>
            <li class="nav-item">
                <a class="nav-link text-red" href="/logout">Logout</a>
            </li>
        </ul>
    </#if>
</nav>