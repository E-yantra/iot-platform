<div id='create_thing' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Create/Edit Thing <img src="/static/img/ajax-loader.gif" v-if="saveLoader"> </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label>Thing Name:</label>
                        <input type="text" name="name" class="form-control" v-model='device.name' placeholder="Thing Name">
                    </div>
                    <div class="form-group">
                        <label>Thing Description:</label>
                        <input name="description" type="text" class="form-control" v-model='device.description' placeholder="Description">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary save">Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>