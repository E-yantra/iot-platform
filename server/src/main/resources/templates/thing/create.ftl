<#include "../common/header.ftl">
<body>
<#include "../common/navbar.ftl"/>
<div class="container-fluid" id="container-main">
<#include "../common/sidenavbar.ftl"/>
<main role="main" class="col-sm-9 ml-sm-auto col-md-10 pt-3">
    <form>
        <div class="form-group">
            <label for="inputEmail4">Thing Name:</label>
            <input type="text" class="form-control" id="inputThingname" placeholder="Thing Name">
        </div>


        <div class="form-group">
            <label for="comment">Description:</label>
            <textarea class="form-control" rows="5" id="description"></textarea>
        </div>
        <div class="form-group">
            <label for="inputAddress">IP:</label>
            <input type="text" class="form-control" id="ip" placeholder="IP">
        </div>

        <button type="button" id="thingCreate" class="btn btn-primary"><i class="fa fa-plus" aria-hidden="true"></i>Create</button>
    </form>
</main>
</div>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.js"></script>
<script src="/static/js/app.js"></script>
<script>
    $(document).ready(function(){
        $("#thingCreate").on("click",function(){
            var unitName = $("#inputThingname").val();
            var description = $("#description").val();
            var ip = $("#ip").val();
            $.ajax({
                type: 'POST',
                url: '/thing/create',
                data: JSON.stringify({name:unitName,description:description,ip:ip}),
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                success: function(data) {  },
            });
        })
    });
</script>
</body>
</html>