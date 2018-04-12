/*
 * Copyright 2017 Pete Cornish
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apiman.cli.managerapi.command.api.command;

import com.beust.jcommander.DynamicParameter;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.common.collect.Lists;
import io.apiman.cli.command.api.model.Api;
import io.apiman.cli.command.api.model.ApiConfig;
import io.apiman.cli.command.api.model.ApiGateway;
import io.apiman.cli.command.api.model.EndpointProperties;
import io.apiman.cli.exception.CommandException;
import io.apiman.cli.managerapi.command.api.ApiMixin;
import io.apiman.cli.managerapi.command.api.VersionAgnosticApi;
import io.apiman.cli.managerapi.management.ManagementApiUtil;
import io.apiman.cli.managerapi.service.ManagementApiService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Create an API.
 *
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
@Parameters(commandDescription = "Create an API")
public class ApiCreateCommand extends AbstractApiCommand implements ApiMixin {
    private static final Logger LOGGER = LogManager.getLogger(ApiCreateCommand.class);

    @Parameter(names = {"--name", "-n"}, description = "API name", required = true)
    private String name;

    @Parameter(names = {"--description", "-d"}, description = "Description")
    private String description;

    @Parameter(names = {"--initialVersion", "-v"}, description = "Initial version", required = true)
    private String initialVersion;

    @Parameter(names = {"--endpoint", "-e"}, description = "Endpoint", required = true)
    private String endpoint;

    @Parameter(names = {"--endpointType", "-t"}, description = "Endpoint type")
    private String endpointType = "rest";

    @Parameter(names = {"--public", "-p"}, description = "Public API", required = true)
    private boolean publicApi;

    @Parameter(names = {"--gateway", "-g"}, description = "Gateway")
    private String gateway = "TheGateway";

    @DynamicParameter(names = {"--endpointProperty", "-ep"},
            description = "Endpoint properties (-ep foo=bar -ep fizz=buzz)")
    private Map<String, String> endpointProperties = new LinkedHashMap<>();

    @Inject
    public ApiCreateCommand(ManagementApiService managementApiService) {
        super(managementApiService);
    }

    @Override
    public void performFinalAction(JCommander parser) throws CommandException {
        LOGGER.debug("Creating {}", this::getModelName);

        final Api api = new Api(
                name,
                description,
                initialVersion);

        final ApiConfig config = new ApiConfig(
                endpoint,
                endpointType,
                publicApi,
                Lists.newArrayList(new ApiGateway(gateway)));

        if (!endpointProperties.isEmpty()) {
            EndpointProperties endpointProperties = Optional.ofNullable(config.getEndpointProperties())
                    .orElse(new EndpointProperties());
            endpointProperties.setArbitraryFields(this.endpointProperties);
            config.setEndpointProperties(endpointProperties);
        }

        // create
        final VersionAgnosticApi apiClient = getManagerConfig()
                .buildServerApiClient(VersionAgnosticApi.class, getManagerConfig().getServerVersion());
        ManagementApiUtil.invokeAndCheckResponse(() -> apiClient.create(orgName, api));

        // configure
        ManagementApiUtil.invokeAndCheckResponse(() -> apiClient.configure(orgName, name, initialVersion, config));
    }
}
