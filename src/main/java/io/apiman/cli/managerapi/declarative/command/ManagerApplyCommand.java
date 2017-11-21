/*
 * Copyright 2017 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apiman.cli.managerapi.declarative.command;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import io.apiman.cli.core.declarative.command.AbstractApplyCommand;
import io.apiman.cli.core.declarative.model.BaseDeclaration;
import io.apiman.cli.managerapi.core.common.model.ManagementApiVersion;
import io.apiman.cli.service.DeclarativeService;
import io.apiman.cli.service.ManagementApiService;
import io.apiman.cli.service.PluginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;

import static java.util.Optional.ofNullable;

@Parameters(commandDescription = "Apply Apiman Manager declaration")
public class ManagerApplyCommand extends AbstractApplyCommand {
    private static final Logger LOGGER = LogManager.getLogger(ManagerApplyCommand.class);

    @Parameter(names = {"--serverVersion", "-sv"}, description = "Management API server version")
    private ManagementApiVersion serverVersion = ManagementApiVersion.DEFAULT_VERSION;

    private final ManagementApiService managementApiService;
    private final DeclarativeService declarativeService;
    private final PluginService pluginService;

    @Inject
    public ManagerApplyCommand(ManagementApiService managementApiService,
                        DeclarativeService declarativeService, PluginService pluginService) {
        this.managementApiService = managementApiService;
        this.declarativeService = declarativeService;
        this.pluginService = pluginService;
    }

    /**
     * Apply the given Declaration.
     *
     * @param declaration the Declaration to apply.
     */
    @Override
    protected void applyDeclaration(BaseDeclaration declaration) {
        LOGGER.debug("Applying declaration");

        // add gateways
        ofNullable(declaration.getSystem().getGateways()).ifPresent(declarativeService::applyGateways);

        // add plugins
        ofNullable(declaration.getSystem().getPlugins()).ifPresent(pluginService::addPlugins);

        // add org and APIs
        ofNullable(declaration.getOrg()).ifPresent(org -> {
            declarativeService.applyOrg(org);

            ofNullable(org.getApis()).ifPresent(apis ->
                    declarativeService.applyApis(serverVersion, apis, org.getName()));
        });

        LOGGER.info("Applied declaration");
    }

    public void setServerVersion(ManagementApiVersion serverVersion) {
        this.serverVersion = serverVersion;
    }
}
