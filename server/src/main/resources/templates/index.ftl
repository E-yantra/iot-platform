<#include "common/header.ftl">
<body>
<#include "common/navbar.ftl"/>
<div class="container-fluid">
    <#if user??>
        <div class="row">
            <div class="col-md-3">Hello</div>
            <div class="col-md-9">
                <div id="app">
                    {{ message }}
                </div>
            </div>
        </div>
    <#else>
        <#include "common/login.ftl">
    </#if>

<script type="text/x-template">
  <#include "leftnav"
</script>
</div>
</body>
</html>