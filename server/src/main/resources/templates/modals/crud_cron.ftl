<div id='create_cron' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Create a cron<img src="/static/img/ajax-loader.gif" v-if="saveLoader"> </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form autocomplete="on" v-on:keyup.enter="saveCron">
                    <div class="form-group">
                        <label>Name:</label>
                        <input class="form-control" v-model="cronName" placeholder="Irrigation Pump Cron" type="text" name="cronName">
                    </div>
                    <div class="form-group">
                        <label>Cron Expression:</label>
                        <input class="form-control" v-model="cronExpression" placeholder="* * * * ? *">
                        <p class="text-sm-left">The cron expression should be of format <a target="_new" href="http://docs.aws.amazon.com/AmazonCloudWatch/latest/events/ScheduledEvents.html#CronExpressions">specified here</a> </p>
                        <p class="text-sm-left">Time here is <strong>UTC</strong></p>
                    </div>
                    <#--TODO: Ability to select multiple actuators-->
                    <div class="form-group">
                        <label>Device:</label>
                        <select v-model="cronDevice" class="form-control custom-select">
                            <option v-for="device in devices" v-bind:value="device">
                                {{ device.name }}
                            </option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Attribute:</label>
                        <select v-model="cronAttribute" class="form-control custom-select">
                            <option v-for="attr in cronDevice.deviceAttributes" v-bind:value="attr" v-if="attr.actuator">
                                {{ attr.name }}
                            </option>
                        </select>
                    </div>
                    <div class="form-group" v-if="cronAttribute">
                        <label>Set Value:</label>
                        <input v-if="cronAttribute.type=='Double'" v-model="cronAttributeValue" type="text" class="form-control"  placeholder="Type a float">
                        <input v-if="cronAttribute.type=='Integer'" v-model="cronAttributeValue" type="number" class="form-control"  placeholder="Type and integer">
                        <input v-if="cronAttribute.type=='String'" v-model="cronAttributeValue" type="number" class="form-control"  placeholder="Type a string">
                        <select v-if="cronAttribute.type=='Boolean'" v-model="cronAttributeValue" class="form-control custom-select">
                            <option value="1">
                                True
                            </option>
                            <option value="0">
                                False
                            </option>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary save" v-on:click="saveCron"><i class="fa fa-floppy-o" aria-hidden="true"></i>Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i>Close</button>
            </div>
        </div>
    </div>
</div>