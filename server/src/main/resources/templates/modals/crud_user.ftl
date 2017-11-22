<div id='create_user' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Create/Edit User<img src="/static/img/ajax-loader.gif" v-if="saveLoader"> </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label>User Name:</label>
                        <input type="text" name="name" class="form-control" v-model='createUser.name' placeholder="User Name">
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input name="email" type="text" class="form-control" v-model='createUser.email' placeholder="test@e-yantra.org">
                    </div>
                    <div class="form-group">
                        <label>Password:</label>
                        <input name="password" type="text" class="form-control" v-model='createUser.password' placeholder="">
                    </div>

                    <div class="form-group">
                        <label for="comment">Roles:</label>
                        <select class="form-control combo-box" id="rightRoles" v-model='createUser.Role'>
                            <option>READ</option>
                            <option>WRITE</option>
                            <option>ALL</option>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary save" v-on:click="saveUser"><i class="fa fa-floppy-o" aria-hidden="true"></i>Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i>Close</button>
            </div>
        </div>
    </div>
</div>