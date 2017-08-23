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
import com.google.common.base.Strings;
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

            buildUsage(jc, new StringBuilder());

            //e.printStackTrace();
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

    private void buildUsage(JCommander jc2, StringBuilder sb) {
        JCommander jc = getCommand(jc2);
        int maxWidth = 84;
        String intermediary = "Usage: " + getCommandChain(jc2);
        boolean indent = false;
        int padChars = intermediary.length();

        // Handle arguments
        List<ParameterDescription> parameters = jc.getParameters();

        if (parameters != null) {
            for (ParameterDescription param : parameters) {
                if (!param.getParameter().required()) {
                    intermediary += "[" + param.getNames() + "] ";
                } else {
                    intermediary += param.toString() + " ";
                }
                // Length
                if (intermediary.length() > maxWidth) {
                    if(indent) {
                        sb.append("\n");
                        sb.append(' ');
                    }
                    sb.append("\n" + intermediary);
                    intermediary = "";
                }
                // After first iteration, indent.
                indent = true;
            }
            if (intermediary.length() > 0) {
                sb.append("\n" + Strings.padStart(intermediary, padChars, 'z'));
            }
        }

        // Handle sub-commands
        if (null != jc.getParsedCommand()) {
            sb.append("\n\n");
            sb.append("The following commands are available:\n\n");

            jc.getCommands().entrySet().stream().forEach(pair -> {
                sb.append("   ");
                sb.append(pair.getKey() + ": ");
                sb.append(jc.getCommandDescription(pair.getKey()));
                sb.append("\n");
            });
        }

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
