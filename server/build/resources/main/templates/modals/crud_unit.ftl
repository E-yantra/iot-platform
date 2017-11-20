<div id='create_unit' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Create/Edit Unit <img src="/static/img/ajax-loader.gif" v-if="saveLoader"> </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label>Unit Name:</label>
                        <input type="text" name="name" class="form-control" v-model='createUnit.unitName' placeholder="Unit Name">
                    </div>
                    <div class="form-group">
                        <label>Unit Description:</label>
                        <input name="description" type="text" class="form-control" v-model='createUnit.description' placeholder="Description">
                    </div>
                    <div class="form-group">
                        <label>Unit Photo:</label>
                        <input name="photo" type="text" class="form-control" v-model='createUnit.photo' placeholder="Photo">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary save" v-on:click="saveUnit"><i class="fa fa-floppy-o" aria-hidden="true"></i>Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i>Close</button>
            </div>
        </div>
    </div>
</div>