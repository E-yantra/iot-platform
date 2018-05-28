package org.kyantra.resources;

import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.model.*;
import com.amazonaws.services.iotdata.AWSIotData;
import com.amazonaws.services.iotdata.model.GetThingShadowRequest;
import com.amazonaws.services.iotdata.model.GetThingShadowResult;
import io.swagger.annotations.Api;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.AuthorizationDAO;
import org.kyantra.dao.ThingDAO;
import org.kyantra.dao.UnitDAO;
import org.kyantra.helper.UnitHelper;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.utils.AwsIotHelper;
import org.kyantra.utils.Constant;
import org.kyantra.utils.ThingHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.PrintWriter;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
@Path("/thing")
@Api(value="thing")
public class ThingResource extends BaseResource {

    int limit = 10;

    @GET
    @Session
    @Secure(roles = {RoleEnum.ALL, RoleEnum.WRITE, RoleEnum.READ})
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id) throws AccessDeniedException {
        ThingBean thingBean = ThingDAO.getInstance().get(id);

        UnitBean targetUnit = thingBean.getParentUnit();
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if(UnitHelper.getInstance().checkAccess(userBean, targetUnit)) {
            return gson.toJson(thingBean);
        }
        else throw new AccessDeniedException();
    }

    @GET
    @Session
    @Path("list/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@PathParam("page") Integer page) {
        List<ThingBean> things= ThingDAO.getInstance().list(page,limit);
        return gson.toJson(things);
    }

    @PUT
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "thing", subjectField = "parentId")
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String update(@PathParam("id") Integer id,
                         @FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("ip") String ip) throws AccessDeniedException{
        //TODO: create/update will only add/edit current entity values and not its parent/children attributes
        ThingBean bean = ThingDAO.getInstance().get(id);
        UnitBean targetUnit = bean.getParentUnit();
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if(UnitHelper.getInstance().checkAccess(userBean, targetUnit)) {
            ThingDAO.getInstance().update(id, name, description, ip);
            return gson.toJson(bean);
        }
        else throw new AccessDeniedException();
    }

    @DELETE
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "thing", subjectField = "parentId")
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id) throws AccessDeniedException{
        ThingBean bean = ThingDAO.getInstance().get(id);
        UnitBean targetUnit = bean.getParentUnit();
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if(UnitHelper.getInstance().checkAccess(userBean, targetUnit)) {
            try {
                ThingDAO.getInstance().delete(id);
                // TODO: 5/24/18 return proper response 
                return "{}";
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return "{}";
        }
        else throw new AccessDeniedException();
    }

    // TODO: 5/24/18 Zip the certificates
    @POST
    @Session
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "thing", subjectField = "parentId")
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String create(@FormParam("name") String name,
                         @FormParam("description") String description,
                         @FormParam("ip") String ip,
                         @FormParam("parentUnitId") Integer parentUnitId) throws AccessDeniedException{

        UnitBean targetUnit = UnitDAO.getInstance().get(parentUnitId);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if(UnitHelper.getInstance().checkAccess(userBean, targetUnit)) {
            try {
                String s = "Create thing";
                //System.out.println(gson.toJson(bean));
                ThingBean thing = new ThingBean();
                thing.setName(name);
                thing.setDescription(description);
                thing.setIp(ip);
                thing.setParentUnit(UnitDAO.getInstance().get(parentUnitId));
                thing.setStorageEnabled(false);

                //Generate certificates as strings
                CreateKeysAndCertificateRequest certificateRequest = new CreateKeysAndCertificateRequest();
                certificateRequest.withSetAsActive(true);
                CreateKeysAndCertificateResult certificateResult= AwsIotHelper.getIotClient().createKeysAndCertificate(certificateRequest);
                String pem = certificateResult.getCertificatePem();
                String privateKey = certificateResult.getKeyPair().getPrivateKey();
                String publicKey = certificateResult.getKeyPair().getPublicKey();

                //Create a directory for storing certificate files on server's filesystem
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                //System.out.println(Constant.CERT_ROOT + timeStamp);
                File certificateDir = new File(Constant.CERT_ROOT + timeStamp);
                if (!certificateDir.exists()) {
                    //System.out.println("creating directory: " + certificateDir.getName());

                    try {

                        if(certificateDir.mkdirs()) {
                            //Storing certificate files in the directory
                            PrintWriter file = new PrintWriter(certificateDir.getAbsolutePath() + "/certificate.crt.pem");
                            file.write(pem);
                            file.close();

                            file = new PrintWriter(certificateDir.getAbsolutePath() + "/private.key.pem");
                            file.write(privateKey);
                            file.close();

                            file = new PrintWriter(certificateDir.getAbsolutePath() + "/public.key.pem");
                            file.write(publicKey);
                            file.close();
                        }

                    }
                    catch(SecurityException se) {
                        //handle it
                    }
                }

                thing.setCertificateDir(certificateDir.getName());

                ThingBean thingBean = ThingDAO.getInstance().add(thing);

                //Create thing in the AWS IoT
                AWSIot client = AwsIotHelper.getIotClient();
                CreateThingResult thingResult = client.createThing(new CreateThingRequest()
                        .withThingName("thing" + thingBean.getId()));

                //Get policy and attach it to certificate
                AttachPrincipalPolicyRequest policyRequest = new AttachPrincipalPolicyRequest();
                policyRequest.withPolicyName(Constant.DEFAULT_POLICY)
                        .withPrincipal(certificateResult.getCertificateArn());
                AwsIotHelper.getIotClient().attachPrincipalPolicy(policyRequest);
                //TODO: Create and attach more secure policies

                //Attach certificate to the thing
                AttachThingPrincipalRequest thingPrincipalRequest = new AttachThingPrincipalRequest();
                thingPrincipalRequest.withPrincipal(certificateResult.getCertificateArn())
                        .withThingName(thingResult.getThingName());
                AwsIotHelper.getIotClient().attachThingPrincipal(thingPrincipalRequest);

                // call createStorageRule to create a rule by default
                ThingHelper.getThingHelper().createStorageRule(thingBean.getId());
                return gson.toJson(thingBean);

            } catch (Throwable t) {
                t.printStackTrace();
            }

            return "{\"success\":false}";

        }
        else throw new AccessDeniedException();
    }

    @GET
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Path("unit/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getByUnit(@PathParam("id") Integer id) throws AccessDeniedException {
        // should have access to read parent units details
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();
        UnitBean targetUnit = UnitDAO.getInstance().get(id);

        if (UnitHelper.getInstance().checkAccess(userBean, targetUnit)) {
            Set<ThingBean> things = ThingDAO.getInstance().getByUnitId(id);
            return gson.toJson(things);
        }
        else throw new AccessDeniedException();
    }

    // TODO: 5/24/18 Debug this resource method: Gives NPE always
    @GET
    @Path("shadow/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String getThingShadow(@PathParam("id") Integer thingId) throws AWSIotException {
        ThingBean thingBean = ThingDAO.getInstance().get(thingId);
        String shadowName = "thing"+thingBean.getId();

        AWSIotData client1 = AwsIotHelper.getIotDataClient();
        GetThingShadowResult result = client1.getThingShadow(new GetThingShadowRequest()
                .withThingName(shadowName));
        byte[] bytes = new byte[result.getPayload().remaining()];
        result.getPayload().get(bytes);
        String resultString = new String(bytes);
        client1.shutdown();

        return resultString;
    }

    @Path("/certificate")
    public CertificateResource getCertificateResource() {
        return new CertificateResource(sc, request);
    }
}
