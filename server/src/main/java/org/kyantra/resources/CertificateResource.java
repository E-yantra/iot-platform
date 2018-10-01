package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.ThingBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.ThingDAO;
import org.kyantra.exceptionhandling.DataNotFoundException;
import org.kyantra.exceptionhandling.ExceptionMessage;
import org.kyantra.helper.AuthorizationHelper;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.nio.file.Paths;

// auth and eh present
@Api(value = "")
public class CertificateResource extends BaseResource {

    public CertificateResource(SecurityContext sc, HttpServletRequest request) {
        super(sc, request);
    }


    @GET
    @Path("get/{name}/{id}")
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get(@PathParam("id") Integer id,
                        @PathParam("name") String name) {

        ThingBean bean = ThingDAO.getInstance().get(id);
        UserBean userBean = (UserBean)getSecurityContext().getUserPrincipal();

        if (bean == null)
            throw new DataNotFoundException(ExceptionMessage.DATA_NOT_FOUND);

        if (!AuthorizationHelper.getInstance().checkAccess(userBean, bean))
            throw new ForbiddenException(ExceptionMessage.FORBIDDEN);

        String certificateDirectory = Paths.get(Constant.CERT_ROOT,bean.getCertificateDir(), name+".pem").toString();

        File rootCA = new File(Constant.CERT_ROOT + "rootCA.pem");

        File certificateFile = new File(certificateDirectory);

        System.out.println(certificateFile.getAbsolutePath()+"\nExists: " + certificateFile.exists());

        if (name.equals("rootCA") && rootCA.exists()) {
            return Response.ok(rootCA, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + rootCA.getName() + "\"" )
                    .build();
        }

        if (certificateFile.exists()) {
            return Response.ok(certificateFile, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" +
                            certificateFile.getName() + "\"" ) //optional
                    .build();
        }

        return Response.status(404).build();
    }


    @GET
    @Path("zip/{id}")
    @Session
    @Secure(roles = {RoleEnum.READ, RoleEnum.WRITE, RoleEnum.ALL})
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getZip(@PathParam("id") Integer id) {
        ThingBean thingBean = ThingDAO.getInstance().get(id);

        String certificateDirectory = Paths.get(Constant.CERT_ROOT,thingBean.getCertificateDir()).toString();

        File certificateZip = new File(certificateDirectory + ".zip");

        if (!certificateZip.exists())
            throw new NotFoundException();

        return Response.ok(certificateZip, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + "certificates.zip" + "\"" )
                .build();
    }
}
