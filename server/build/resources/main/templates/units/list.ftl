<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
<main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
    <form>
        <div style="float: right;margin-bottom: 10px;"><a href="/units/create" role="button"class="btn btn-primary">Add New</a></div>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Unit Name</th>
                <th scope="col">Description</th>
                <th scope="col">Photo</th>
                <th scope="col">Parent ID</th>
                <th scope="col">Action </th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <th scope="row">1</th>
                <td>Mark</td>
                <td>Otto</td>
                <td>@mdo</td>
                <td>@mdo</td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
                    <button type="button" class="btn btn-danger btn-sm">Delete</button>
                </td>
            </tr>
            <tr>
                <th scope="row">2</th>
                <td>Mark</td>
                <td>Otto</td>
                <td>@TwBootstrap</td>
                <td>@mdo</td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
                    <button type="button" class="btn btn-danger btn-sm">Delete</button>
                </td>
            </tr>
            <tr>
                <th scope="row">3</th>
                <td>Mark</td>
                <td>Otto</td>
                <td>@TwBootstrap</td>
                <td>@mdo</td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
                    <button type="button" class="btn btn-danger btn-sm">Delete</button>
                </td>
            </tr>
            <tr>
                <th scope="row">4</th>
                <td>Mark</td>
                <td>Otto</td>
                <td>@TwBootstrap</td>
                <td>@mdo</td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
                    <button type="button" class="btn btn-danger btn-sm">Delete</button>
                </td>
            </tr>
            <tr>
                <th scope="row">5</th>
                <td>Mark</td>
                <td>Otto</td>
                <td>@TwBootstrap</td>
                <td>@mdo</td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
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