<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
<main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
    <form>
        <div class="form-group">
            <label for="inputEmail4">User Name:</label>
            <input type="text" class="form-control" id="inputUnitname" placeholder="User Name">
        </div>


        <div class="form-group">
            <label for="comment">Roles:</label>
            <select class="form-control" id="exampleFormControlSelect1">
                <option>READ</option>
                <option>WRITE</option>
                <option>ALL</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Create</button>
    </form>
</main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
</body>
</html>