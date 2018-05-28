package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.ThingBean;
import org.kyantra.dao.ThingDAO;
import org.kyantra.interfaces.Secure;
import org.kyantra.interfaces.Session;
import org.kyantra.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.nio.file.Paths;

// TODO: 5/24/18 Apply proper authorization 
@Api(value = "")
public class CertificateResource extends BaseResource {

    public CertificateResource(SecurityContext sc, HttpServletRequest request) {
        super(sc, request);
    }

    @GET
    @Path("get/{name}/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get(@PathParam("id") Integer id,
                        @PathParam("name") String name) {

        ThingBean bean = ThingDAO.getInstance().get(id);
        String certificateDirectory = Paths.get(Constant.CERT_ROOT,bean.getCertificateDir(),name+".pem").toString();

        File rootCA = new File(Constant.CERT_ROOT + "rootCA.pem");
        File certificateFile = new File(certificateDirectory);
        System.out.println(certificateFile.getAbsolutePath()+"\nExists: " + certificateFile.exists());

        if(name.equals("rootCA") && rootCA.exists()) {
            return Response.ok(rootCA, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + rootCA.getName() + "\"" )
                    .build();
        }

        if(certificateFile.exists()) {
            return Response.ok(certificateFile, MediaType.APPLICATION_OCTET_STREAM)
                    .header("Content-Disposition", "attachment; filename=\"" + certificateFile.getName() + "\"" ) //optional
                    .build();
        }
        return Response.status(404).build();
    }

}
