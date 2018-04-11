package org.kyantra.resources;

import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import io.swagger.annotations.Api;
import org.glassfish.grizzly.compression.lzma.impl.Base;
import org.kyantra.beans.ThingBean;
import org.kyantra.dao.ThingDAO;
import org.kyantra.utils.AwsIotHelper;
import org.kyantra.utils.StringConstants;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.nio.file.Paths;

public class CertificateResource extends BaseResource {

    @GET
    @Path("get/{name}/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response get(@PathParam("id") Integer id,
                        @PathParam("name") String name) {

        ThingBean bean = ThingDAO.getInstance().get(id);
        String certificateDirectory = Paths.get(StringConstants.CERT_ROOT,bean.getCertificateDir(),name+".pem").toString();

        File rootCA = new File(StringConstants.CERT_ROOT + "rootCA.pem");
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
