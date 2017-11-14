<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
<main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
    <form>
        <div class="form-group">
            <label for="inputEmail4">Unit Name:</label>
            <input type="text" class="form-control" id="inputUnitname" placeholder="Unit Name">
        </div>


        <div class="form-group">
            <label for="comment">Description:</label>
            <textarea class="form-control" rows="5" id="description"></textarea>
        </div>
        <div class="form-group">
            <label for="inputAddress">Photo:</label>
            <input type="text" class="form-control" id="photo" placeholder="Photo">
        </div>
        <div class="form-group">
            <label for="inputAddress">Parent ID:</label>
            <input type="text" class="form-control" id="parentsID" placeholder="Parent ID">
        </div>

        <button type="button" id="unitCreate" class="btn btn-primary">Create</button>
    </form>
</main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    $(document).ready(function(){
        $("#unitCreate").on("click",function(){
            var unitName = $("#inputUnitname").val();
            var description = $("#description").val();
            var photo = $("#photo").val();
            var parentsID = $("#parentsID").val();
            $.ajax({
                type: 'POST',
                url: '/unit/create',
                data: JSON.stringify({unitName:unitName,description:description,photo:photo,parent:parentsID}),
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                success: function(data) { alert('data: ' + data); },
            });
        })
    });
</script>
</body>
</html>