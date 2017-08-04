package io.apiman.cli.command;

import org.kohsuke.args4j.Option;

/**
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
public abstract class AbstractGatewayCommand extends AbstractCommand {

    private static final String DEFAULT_SERVER_ADDRESS = "http://localhost:8080/apiman-gateway-api";
    private static final String DEFAULT_GATEWAY_USER = "apimanager";
    private static final String DEFAULT_GATEWAY_PASSWORD = "apiman123!";

    @Option(name = "--server", aliases = {"-s"}, usage = "Gateway API server address")
    protected String serverAddress = DEFAULT_SERVER_ADDRESS;

    @Option(name = "--serverUsername", aliases = {"-su"}, usage = "Gateway API server username")
    private String serverUsername = DEFAULT_GATEWAY_USER;

    @Option(name = "--serverPassword", aliases = {"-sp"}, usage = "Gateway API server password")
    private String serverPassword = DEFAULT_GATEWAY_PASSWORD;

    public AbstractGatewayCommand() {
        super();
    }

    protected String getGatewayApiEndpoint() {
        // TODO consider reading from config file/environment. Also think about alternative auth methods.
        return serverAddress;
    }

    private String getGatewayApiUsername() {
        // TODO consider reading from config file/environment. Also think about alternative auth methods.
        return serverUsername;
    }

    private String getGatewayApiPassword() {
        // TODO consider reading from config file/environment. Also think about alternative auth methods.
        return serverPassword;
    }
}
