<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
        <a style="color:#536c79 !important;"  class="navbar-brand" href="#=/">IoT Platform</a>
        <div class="collapse navbar-collapse" id="navbarText">
            <ul class="ml-md-auto, mx-md-auto"></ul>
            <span class="navbar-text text-white">
                <#if user??>
                   <span style="color:#536c79 !important;">${user.name}</span>
                </#if>
            </span>
        </div>
    </nav>
</header>
