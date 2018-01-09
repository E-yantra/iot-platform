<div id='generate_code' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">MQTT Connection Details for Client<img src="/static/img/ajax-loader.gif" v-if="saveLoader"> </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <h6>MQTT Thing Shadow Topics and Thing ID:</h6>
                <pre>{{generateCode}}</pre>
                <h6>Thing Shadow Message:</h6>
                <textarea class="form-control" id="shadow-message" rows="10" readonly="">{{generateMessage}}</textarea>
            </div>
            <div class="modal-footer">
                <button v-on:click="copyToClipboard" type="button" class="btn btn-primary">Copy to Clipboard</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i>Close</button>
            </div>
        </div>
    </div>
</div>