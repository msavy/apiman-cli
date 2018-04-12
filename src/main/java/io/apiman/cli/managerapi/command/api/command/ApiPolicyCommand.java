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

import com.beust.jcommander.Parameters;
import io.apiman.cli.command.core.Command;
import io.apiman.cli.managerapi.AbstractManagerCommand;
import io.apiman.cli.managerapi.service.ManagementApiService;

import javax.inject.Inject;
import java.util.Map;

/**
 * Root Command for managing API policies.
 *
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */

@Parameters(commandDescription = "Manage API policies")
public class ApiPolicyCommand extends AbstractManagerCommand {
    @Inject
    public ApiPolicyCommand(ManagementApiService managementApiService) {
        super(managementApiService);
    }

    @Override
    protected void populateCommands(Map<String, Class<? extends Command>> commandMap) {
        commandMap.put("add", ApiPolicyAddCommand.class);
    }

}