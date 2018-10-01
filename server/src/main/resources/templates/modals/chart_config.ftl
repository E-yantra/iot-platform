<div id="gauge_settings" class="modal" tabindex="-1" role="dialog" v-show="selectedAttribute!=-1">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Gauge settings<img src="/static/img/ajax-loader.gif" v-if="saveLoader"> </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form autocomplete="on">
                    <div class="form-group">
                        <div class="row">
                            <div class="col">
                                <label>Max limit:</label>
                            </div>
                            <div class="col">
                                <input type="text" name="name" class="form-control" v-model='chartConfig[selectedAttribute].Gauge.maxLimit' placeholder="Gauge upper limit">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col">
                                <label>Min limit:</label>
                            </div>
                            <div class="col">
                                <input type="text" name="name" class="form-control" v-model='chartConfig[selectedAttribute].Gauge.minLimit' placeholder="Gauge lower limit">
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="row">
                            <div class="col">
                                <label>Red region from:</label>
                            </div>
                            <div class="col">
                                <input type="text" name="name" class="form-control" v-model='chartConfig[selectedAttribute].Gauge.redFrom' placeholder="Between min to max">
                            </div>
                            <div class="col">
                                <label> to:</label>
                            </div>
                            <div class="col">
                                <input type="text" name="name" class="form-control" v-model='chartConfig[selectedAttribute].Gauge.redTo' placeholder="Between min to max greater ">
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i>Close</button>
            </div>
        </div>
    </div>
</div>
