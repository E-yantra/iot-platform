<div id='create_rule' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Create Rule<img src="/static/img/ajax-loader.gif" v-if="saveLoader"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form>
                    <div class="form-group">
                        <label>Name: </label>
                        <input type="text" name="name" class="form-control" v-model='createRule.name'
                               placeholder="Name for your rule">
                    </div>
                    <div class="form-group">
                        <label>Description: </label>
                        <input name="description" type="text" class="form-control" v-model='createRule.description'
                               placeholder="test@e-yantra.org">
                    </div>
                    <div class="form-group">
                        <label>Data: </label>
                        <input name="data" type="text" class="form-control" v-model='createRule.data' placeholder="">
                    </div>
                    <div class="form-group">
                        <label for="comment">Action: </label>
                        <select class="form-control combo-box" id="rightRoles" v-model='createRule.action'>
                            <option v-for="action in createRule.actionList" v-bind:value="action">{{action}}</option>
                        </select>
                    </div>
                    <div class="form-group" v-if="createRule.action == 'sns'">
                        <label>Topic: </label>
                        <input name="topic" type="text" class="form-control" v-model='createRule.sns_topic' placeholder="Name of SNS topic">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary save" v-on:click="saveRule"><i class="fa fa-floppy-o"
                                                                                            aria-hidden="true"></i>Save
                </button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times"
                                                                                        aria-hidden="true"></i>Close
                </button>
            </div>
        </div>
    </div>
</div>