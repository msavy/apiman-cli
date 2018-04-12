/*
 * Copyright 2018 JBoss Inc
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

package io.apiman.cli.managerapi.command.plan.command;

import com.beust.jcommander.Parameters;
import io.apiman.cli.command.core.Command;
import io.apiman.cli.managerapi.AbstractManagerCommand;
import io.apiman.cli.managerapi.service.ManagementApiService;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */
@Parameters(commandDescription = "Manage Plans")
public class PlanCommand extends AbstractManagerCommand {

    @Inject
    public PlanCommand(ManagementApiService managementApiService) {
        super(managementApiService);
    }

    @Override
    protected void populateCommands(Map<String, Class<? extends Command>> commandMap) {
        commandMap.put("create", PlanCreateCommand.class);
        commandMap.put("list", PlanListCommand.class);
        commandMap.put("lock", PlanLockCommand.class);
        commandMap.put("policy", PlanPolicyCommand.class);
    }

}