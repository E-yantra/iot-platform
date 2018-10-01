<div id='add_subscription' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add Subscription<img src="/static/img/ajax-loader.gif" v-if="saveLoader"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form autocomplete="on" v-on:keyup.enter="addSubscription">
                    <div class="form-group">
                        <label>Protocol:</label>
                        <input type="text" name="type" class="form-control" v-model='createSubscription.type' placeholder="email">
                    </div>
                    <div class="form-group">
                        <label>Endpoint:</label>
                        <input name="value" type="text" class="form-control" v-model='createSubscription.value' placeholder="test@e-yantra.org">
                    </div>
                    <#--<div class="form-group">-->
                        <#--<label for="comment">Roles:</label>-->
                        <#--<select class="form-control combo-box" id="rightRoles" v-model='createUser.Role'>-->
                            <#--<option>READ</option>-->
                            <#--<option>WRITE</option>-->
                            <#--<option>ALL</option>-->
                        <#--</select>-->
                    <#--</div>-->
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary save" v-on:click="addSubscription"><i class="fa fa-floppy-o" aria-hidden="true"></i>Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i>Close</button>
            </div>
        </div>
    </div>
</div>