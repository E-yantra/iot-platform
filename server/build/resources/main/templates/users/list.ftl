<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
        <main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
            <form>
          <div style="float: right;margin-bottom: 10px;"><a href="/users/create" role="button"class="btn btn-primary">Add New</a></div>
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">User Name</th>
                        <th scope="col">email</th>
                        <th scope="col">Password</th>

                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <th scope="row">1</th>
                        <td>Mark</td>
                        <td>Otto@sd.com</td>
                        <td>
                            <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">2</th>
                        <td>Mark</td>
                        <td>Otto@sd.com</td>
                        <td>
                            <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">3</th>
                        <td>Mark</td>
                        <td>Otto@sd.com</td>
                        <td>
                            <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">4</th>
                        <td>Mark</td>
                        <td>Otto@sd.com</td>
                        <td>
                            <button type="button" class="btn btn-primary btn-sm">Edit</button>&nbsp;
                            <button type="button" class="btn btn-danger btn-sm">Delete</button>
                        </td>
                    </tr>
                    <tr>
                        <th scope="row">5</th>
                        <td>Mark</td>
                        <td>Otto@sd.com</td>
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