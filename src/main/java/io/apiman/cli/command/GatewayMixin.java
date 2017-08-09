package io.apiman.cli.command;

import org.kohsuke.args4j.Option;

/**
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
public interface GatewayMixin {

    String DEFAULT_SERVER_ADDRESS = "http://localhost:8080/apiman-gateway-api";
    String DEFAULT_GATEWAY_USER = "apimanager";
    String DEFAULT_GATEWAY_PASSWORD = "apiman123!";

    @Option(name = "--server", aliases = {"-s"}, usage = "Gateway API server address")
    String serverAddress = DEFAULT_SERVER_ADDRESS;

    @Option(name = "--serverUsername", aliases = {"-su"}, usage = "Gateway API server username")
    String serverUsername = DEFAULT_GATEWAY_USER;

    @Option(name = "--serverPassword", aliases = {"-sp"}, usage = "Gateway API server password")
    String serverPassword = DEFAULT_GATEWAY_PASSWORD;

    default String getGatewayApiEndpoint() {
        // TODO consider reading from config file/environment. Also think about alternative auth methods.
        return serverAddress;
    }

    default String getGatewayApiUsername() {
        // TODO consider reading from config file/environment. Also think about alternative auth methods.
        return serverUsername;
    }

    default String getGatewayApiPassword() {
        // TODO consider reading from config file/environment. Also think about alternative auth methods.
        return serverPassword;
    }
}
