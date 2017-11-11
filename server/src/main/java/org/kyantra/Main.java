package org.kyantra;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.mvc.freemarker.FreemarkerMvcFeature;
import org.kyantra.services.HibernateService;

import java.net.URI;

public class Main {

    public static HttpServer startServer(int port) {

        final ResourceConfig rc = new ResourceConfig().packages("org.kyantra.resources");
        rc.property(FreemarkerMvcFeature.TEMPLATE_BASE_PATH,"/templates");
        rc.register(FreemarkerMvcFeature.class);

        return GrizzlyHttpServerFactory.createHttpServer(URI.create("http://0.0.0.0:"+port+"/"), rc);
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
