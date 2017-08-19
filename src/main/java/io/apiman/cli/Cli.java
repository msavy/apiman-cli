/*
 * Copyright 2016 Pete Cornish
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

package io.apiman.cli;

import com.beust.jcommander.JCommander;
import com.google.common.collect.Lists;
import io.apiman.cli.command.AbstractCommand;
import io.apiman.cli.command.Command;

import java.util.Map;

/**
 * The main class; the root of all Commands.
 *
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public class Cli extends AbstractCommand {
    public static void main(String... args) {
        JCommander jc = new JCommander();
        Cli cli = new Cli();
        jc.addObject(cli);
        cli.build(jc);
        cli.run(Lists.newArrayList(args), jc);
    }

    @Override
    protected void populateCommands(Map<String, Class<? extends Command>> commandMap) {
        commandMap.put("manager", ManagerCli.class);
        commandMap.put("gateway", GatewayCli.class);
    }

    @Override
    protected String getCommandDescription() {
        return "apiman-cli";
    }

    @Override
    public String getCommandName() {
        return "apiman";
    }
}
