package io.apiman.cli.gw.command;

import io.apiman.cli.core.api.GatewayApi;
import io.apiman.cli.core.common.command.ModelCreateCommand;
import io.apiman.cli.core.gateway.GatewayMixin;
import io.apiman.cli.core.gateway.command.AbstractGatewayCreateCommand;
import io.apiman.cli.core.gateway.model.GatewayType;
import io.apiman.cli.exception.CommandException;
import org.kohsuke.args4j.Option;

/**
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
public class DeleteCommand  extends ModelCreateCommand<DeleteCommand, GatewayApi> {

    @Option(name = "--endpoint", aliases = {"-e"}, usage = "Endpoint", required = true)
    private String endpoint;

    @Option(name = "--username", aliases = {"-u"}, usage = "Username")
    private String username;

    @Option(name = "--password", aliases = {"-p"}, usage = "Password")
    private String password;

    @Override
    protected DeleteCommand buildModelInstance() throws CommandException {
        return null;
    }

    @Override
    public Class<DeleteCommand> getModelClass() {
        return DeleteCommand.class;
    }

    @Override
    public Class<GatewayApi> getApiClass() {
        return GatewayApi.class;
    }
}
