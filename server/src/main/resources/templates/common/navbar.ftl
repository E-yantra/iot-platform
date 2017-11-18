<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="#=/">IoT Platform</a>
        <div class="collapse navbar-collapse" id="navbarText">
            <ul class="ml-md-auto, mx-md-auto"></ul>
            <span class="navbar-text text-white">
                <#if user??>
                    ${user.name}
                </#if>
            </span>
        </div>
    </nav>
</header>
