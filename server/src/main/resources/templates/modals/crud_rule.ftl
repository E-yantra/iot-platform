<div id='create_rule' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title"><span v-if="ruleUpdate">Edit Rule</span>
                    <span v-else>Create Rule</span>
                    <img src="/static/img/ajax-loader.gif" v-if="saveLoader"></h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form autocomplete="on" v-on:keyup.enter="updateRule">
                    <div class="row">
                        <div class="col">
                            <div class="form-group">
                                <label>Name</label>
                                <input v-bind:readOnly="ruleUpdate" type="text" name="name" class="form-control" v-model='createRule.name' placeholder="temperature_rule">
                                <small class="text-muted">Name of the rule will be prefixed with &lt;thing-id&gt;_&lt;rule-type&gt;</small>
                            </div>
                            <div class="form-group">
                                <label>Description</label>
                                <input name="description" type="text" class="form-control"
                                       v-model='createRule.description'
                                       placeholder="Rule to send notifications via SMS">
                            </div>
                            <div class="form-group">
                                <label>Data</label>
                                <input name="data" type="text" class="form-control" v-model='createRule.data'
                                       placeholder="e.g. * or state.reported.deviceXX.XX, ...">
                            </div>
                            <div class="form-group">
                                <label>Condition(Optional)</label>
                                <input name="condition" type="text" class="form-control" v-model='createRule.condition'
                                       placeholder="e.g. where state.reported.deviceXX.XX > 50">
                            </div>
                        </div>
                        <div class="col" v-show="ruleUpdate == false">
                            <div class="form-group">
                                <label for="comment">Type</label>
                                <select class="form-control combo-box" id="rightRoles" v-model='createRule.action'>
                                    <option v-for="action in ruleActionList" v-bind:value="action">{{action}}
                                    </option>
                                </select>
                            </div>
                            <template v-if="createRule.action == 'SNS'">
                                <div class="form-group">
                                    <label>SNS Topic</label>
                                    <input name="topic" type="text" class="form-control"
                                           v-model='createRule.sns_topic'
                                           placeholder="my_subscription_topic">
                                </div>
                                <div class="form-group">
                                    <label for="subject">Subject</label>
                                    <input class="form-control" type="text" v-model="createRule.subject"
                                           placeholder="temperature is high">
                                </div>
                                <div class="form-group">
                                    <label for="message">Message</label>
                                    <textarea class="form-control" id="message" v-model="createRule.message"
                                              rows="2"></textarea>
                                </div>
                                <div class="form-group">
                                    <label for="interval">Interval(in minutes)</label>
                                    <input type="number" class="form-control" v-model="createRule.interval"
                                           placeholder="20">
                                </div>
                            </template>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button v-if="ruleUpdate" type="button" class="btn btn-primary save" v-on:click="updateRule"><i
                        class="fa fa-floppy-o" aria-hidden="true"></i>Save
                </button>
                <button v-else type="button" class="btn btn-primary save" v-on:click="saveRule"><i
                        class="fa fa-floppy-o" aria-hidden="true"></i>Save
                </button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times"
                                                                                        aria-hidden="true"></i>Close
                </button>
            </div>
        </div>
    </div>
</div>