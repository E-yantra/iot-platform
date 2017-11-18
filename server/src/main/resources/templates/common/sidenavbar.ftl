<nav class="col-sm-3 col-md-2 d-none d-sm-block bg-light sidebar">
    <#if user??>
        <ul class="nav nav-pills flex-column">
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="home">active</#if>" href="/">Home</a>
            </li>
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="right">active</#if>" href="/rights/list">Right</a>
            </li>
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="thing">active</#if>" href="/things/list">Thing</a>
            </li>
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="unit">active</#if>" href="/units/list">Unit</a>
            </li>
            <li class="nav-item">
                <a class="nav-link <#if active?? && active=="user">active</#if>" href="/users/list">User</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/logout">Logout</a>
            </li>
        </ul>
    </#if>
</nav>