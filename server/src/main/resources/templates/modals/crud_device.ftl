<div id='create_device' class="modal" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-lg" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Create/Edit Device <img src="/static/img/ajax-loader.gif" v-if="saveLoader"> </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <form autocomplete="on" v-on:keyup.enter="saveDevice">
                    <div class="form-group">
                        <label class="col-form-label" for="formGroupExampleInput">Device Name</label>
                        <input v-model="createDevice.name" type="text" class="form-control" id="formGroupExampleInput" placeholder="Device Name">
                    </div>
                </form>
                <form class="form-inline p-1" autocomplete="on" v-for="(value, key) in createDevice.deviceAttributes">
                    <label class="sr-only" for="inlineFormInputName2">Name</label>
                    <input type="text" class="form-control mb-2 mr-sm-2 mb-sm-0" v-model="createDevice.deviceAttributes[key].name" placeholder="Name">

                    <label class="sr-only" for="inlineFormInputName2">Type</label>
                    <select class="custom-select mb-2 mr-sm-2 mb-sm-0" v-model="createDevice.deviceAttributes[key].type">
                        <option value="Boolean">Boolean</option>
                        <option value="Double">Double</option>
                        <option value="String">String</option>
                        <option value="Integer">Integer</option>
                    </select>

                    <label class="sr-only" for="inlineFormInputName2">Default</label>
                    <input type="text" class="form-control mb-2 mr-sm-2 mb-sm-0" v-model="createDevice.deviceAttributes[key].def" placeholder="Default">
                    <input type="checkbox" class="form-control mb-2 mr-sm-2 mb-sm-0" v-model="createDevice.deviceAttributes[key].actuator"/> Actuator &nbsp;
                    <button type="button" class="btn btn-danger" v-on:click="removeAttr(key)">x</button>
                </form>
                <form class="form-inline p-1" autocomplete="on">
                    <input type="text" class="form-control mb-2 mr-sm-2 mb-sm-0"  v-model="cttr.name" placeholder="Name">
                    <label class="sr-only" for="inlineFormInputName2">Type</label>
                    <select class="custom-select mb-2 mr-sm-2 mb-sm-0" v-model="cttr.type">
                        <option value="Boolean" selected>Boolean</option>
                        <option value="Double">Double</option>
                        <option value="String">String</option>
                        <option value="Integer">Integer</option>
                    </select>
                    <input type="text" class="form-control mb-2 mr-sm-2 mb-sm-0" placeholder="Default Value" v-model="cttr.def">
                    <input type="checkbox" class="form-control mb-2 mr-sm-2 mb-sm-0" v-model="cttr.actuator"/> Actuator &nbsp;
                    <button type="button" class="btn btn-primary" v-on:click="addAttr">Add</button>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary save" v-on:click="saveDevice"><i class="fa fa-floppy-o" aria-hidden="true"></i>Save</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal"><i class="fa fa-times" aria-hidden="true"></i>Close</button>
            </div>
        </div>
    </div>
</div>