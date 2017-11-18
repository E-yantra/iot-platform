<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
    <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
            <form>
                <div style="float: right;margin-bottom: 10px;"><a href="/rights/create" role="button"class="btn btn-primary">Add New</a></div>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Unit ID</th>
                        <th scope="col">Roles</th>
                        <th scope="col">Action </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row">1</th>
                        <td>Otto</td>
                        <td>READ</td>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td>@TwBootstrap</td>
                        <td>WRITE</td>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td>@TwBootstrap</td>
                        <td>ALL</td>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">4</th>
                        <td>@TwBootstrap</td>
                        <td>READ</td>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">5</th>
                        <td>@TwBootstrap</td>
                        <td>READ</td>
                        <td>
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>

                    </tbody>
                </table>

            </form>


    </main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
</body>
</html>

