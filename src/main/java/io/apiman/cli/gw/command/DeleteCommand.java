package io.apiman.cli.gw.command;

import com.beust.jcommander.Parameter;
import io.apiman.cli.core.api.GatewayApi;
import io.apiman.cli.core.common.command.ModelCreateCommand;
import io.apiman.cli.exception.CommandException;

/**
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
public class DeleteCommand  extends ModelCreateCommand<DeleteCommand, GatewayApi> {

    @Parameter(names = {"--endpoint", "-e"}, description = "Endpoint", required = true)
    private String endpoint;

    @Parameter(names = {"--username", "-u"}, description = "Username")
    private String username;

    @Parameter(names = {"--password", "-p"}, description = "Password")
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
