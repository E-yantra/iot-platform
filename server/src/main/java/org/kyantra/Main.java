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
import org.kyantra.services.HibernateService;

import java.net.URI;

public class Main {

    public static HttpServer startServer(int port) {

        String resources = "org.kyantra.resources";
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
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

        ClassLoader loader = Main.class.getClassLoader();
        CLStaticHttpHandler docsHandler = new CLStaticHttpHandler(loader, "swagger-ui/");
        docsHandler.setFileCacheEnabled(false);
        HttpServer server;

        server = GrizzlyHttpServerFactory.createHttpServer(URI.create("http://0.0.0.0:"+port+"/"), rc);
        ServerConfiguration cfg = server.getServerConfiguration();
        cfg.addHttpHandler(docsHandler, "/docs/");
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
