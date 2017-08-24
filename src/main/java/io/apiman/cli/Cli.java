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
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.ParameterException;
import com.google.common.collect.Lists;
import io.apiman.cli.command.AbstractCommand;
import io.apiman.cli.command.Command;
import io.apiman.cli.exception.ExitWithCodeException;
import io.apiman.cli.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * The main class; the root of all Commands.
 *
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public class Cli extends AbstractCommand {
    private static final Logger LOGGER = LogManager.getLogger(Cli.class);

    @Override
    public void run(List<String> args, JCommander jc) {
        jc.setAcceptUnknownOptions(false);
        jc.setProgramName("apiman-cli");
        jc.addObject(this);
        build(jc);
        try {
            jc.parse(args.toArray(new String[]{})); // Hmm
            super.run(args, jc);
        } catch (ParameterException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e);
            } else {
                LOGGER.error(e.getMessage());
            }
            printUsage(jc, new StringBuilder());
        } catch (ExitWithCodeException ec) {
            // print the message and exit with the given code
            LogUtil.OUTPUT.error(ec.getMessage());
            if (ec.isPrintUsage()) {
                printUsage(jc, ec.getExitCode());
            } else {
                System.exit(ec.getExitCode());
            }
        }
    }

    private JCommander getCommand(JCommander in) {
        JCommander jc = in;
        while (jc.getParsedCommand() != null) {
            jc = jc.getCommands().get(jc.getParsedCommand());
        }
        return jc;
    }

    private String getCommandChain(JCommander in) {
        String chain = in.getProgramName() + " ";
        JCommander jc = in;
        while (jc.getParsedCommand() != null) {
            chain += jc.getParsedCommand() + " ";
            jc = jc.getCommands().get(jc.getParsedCommand());
        }
        return chain;
    }

    private void printUsage(JCommander parent, StringBuilder sb) {
        JCommander jc = getCommand(parent);
        String intermediary = "Usage: " + getCommandChain(parent);
        // Handle arguments
        List<ParameterDescription> parameters = jc.getParameters();
        parameters.sort((e1, e2) -> {
            int mandatory = -Boolean.compare(e1.getParameter().required(), e2.getParameter().required());
            return mandatory != 0 ? mandatory : e1.getLongestName().compareTo(e2.getLongestName());
        });

        // Build parameter list
        for (ParameterDescription param : parameters) {
            // Optional open braces
            if (!param.getParameter().required()) {
                intermediary += "[";
            }

            intermediary += param.getNames();

            // Optional close braces
            if (!param.getParameter().required()) {
                intermediary += "]";
            }

            intermediary += " ";
        }

        // Doing it this way in case we decide to have width limits.
        if (intermediary.length() > 0) {
            sb.append(intermediary);
        }

        // Handle sub-commands
        if (!jc.getCommands().isEmpty()) {
            sb.append("<command> [<args>]\n\n");
            sb.append("The following commands are available:\n\n");

            jc.getCommands().forEach((key, value) -> {
                sb.append("   ");
                sb.append(key).append(": ");
                sb.append(jc.getCommandDescription(key));
                sb.append("\n");
            });
        }

        // Handle arguments
        if (!jc.getParameters().isEmpty()) {
            sb.append("\n\nThe following arguments are available:\n\n");

            jc.getParameters().forEach(param -> {
                // Foo: Description
                sb.append("   ");
                sb.append(param.getNames() + ": ");
                sb.append(param.getDescription());
                // If there is a default set and it's not a boolean
                if (param.getDefault() != null &&
                        !(param.getDefault() instanceof Boolean)) {
                    sb.append(" [default: ");
                    sb.append(param.getDefault());
                    sb.append("]");
                }
                sb.append("\n");
            });
        }

        // Print to console
        System.out.println(sb.toString());
    }

    public static void main(String... args) {
        Cli cli = new Cli();
        cli.run(Lists.newArrayList(args), new JCommander());
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
