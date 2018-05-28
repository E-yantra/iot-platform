package org.kyantra;

import io.swagger.jaxrs.config.BeanConfig;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.kyantra.filters.AuthorizationFilter;
import org.kyantra.filters.SessionFilter;
import org.kyantra.exception.AppExceptionMapper;
import org.kyantra.resources.AuthResource;
import org.kyantra.services.HibernateService;

import java.net.URI;

public class Main {

    public static HttpServer startServer(int port) {

        String resources = "org.kyantra.resources";
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("E-Yantra IoT Platform API");
        beanConfig.setDescription("Below are endpoints defined for E-Yantra IoT Platform. Note that you need to have an account so that you can get the token which is mandatory to make requests.");
        beanConfig.setVersion("1.2.3");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage(resources);
        beanConfig.setScan(true);

        final ResourceConfig rc = new ResourceConfig().packages(resources);
        rc.property(FreemarkerMvcFeature.TEMPLATE_BASE_PATH,"/templates");
        rc.register(FreemarkerMvcFeature.class);
        rc.register(io.swagger.jaxrs.listing.ApiListingResource.class);
        rc.register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
        rc.register(JacksonFeature.class);
        rc.register(SessionFilter.class);
        rc.register(AuthorizationFilter.class);
        rc.register(AppExceptionMapper.class);
        rc.register(AuthResource.class);

        ClassLoader loader = Main.class.getClassLoader();
        CLStaticHttpHandler docsHandler = new CLStaticHttpHandler(loader, "swagger-ui/");
        CLStaticHttpHandler staticHttpHandler = new CLStaticHttpHandler(loader,"static/");
        docsHandler.setFileCacheEnabled(false);
        staticHttpHandler.setFileCacheEnabled(true);
        HttpServer server;

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://0.0.0.0:"+port+"/"), rc);
        ServerConfiguration cfg = server.getServerConfiguration();
        cfg.addHttpHandler(docsHandler, "/docs/");
        cfg.addHttpHandler(staticHttpHandler,"/static/");
        return server;
    }

    public static void main(String[] args) throws ParseException {
        Options options = new Options();
        options.addOption("port", true, "Port to run on");
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse( options, args);
        int port = Integer.parseInt(cmd.getOptionValue("port","8002"));
        HibernateService hibernateService = HibernateService.getInstance(); //initialized hibernate service
        final HttpServer server = startServer(port);
    }
}
