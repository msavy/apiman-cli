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

package io.apiman.cli.command;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.apiman.cli.exception.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static io.apiman.cli.util.LogUtil.LINE_SEPARATOR;

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public abstract class AbstractCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(AbstractCommand.class);

    /**
     * Maps commands (e.g. 'org' or 'create') to their implementations.
     */
    private final Map<String, Class<? extends Command>> commandMap;

    @Parameter(names = "--debug", description = "Log at DEBUG level")
    private boolean logDebug;

    @Parameter(names = {"--help", "-h"}, description = "Display usage only", help = true)
    private boolean displayHelp;

    /**
     * The parent Command (<code>null</code> if root).
     */
    private Command parent;

    /**
     * The name of this command.
     */
    private String commandName;
    private Injector injector;

    public AbstractCommand() {
        // get child commands
        commandMap = Maps.newHashMap();
        populateCommands(commandMap);
        injector = Guice.createInjector();
    }

    /**
     * Subclasses should populate the Map with their child Commands.
     *
     * @param commandMap the Map to populate
     */
    protected abstract void populateCommands(Map<String, Class<? extends Command>> commandMap);

    /**
     * @return human-readable short description for this command (e.g. 'Manage Plugins')
     */
    protected abstract String getCommandDescription();

    @Override
    public void setParent(Command parent) {
        this.parent = parent;
    }

    /**
     * @param commandName the name of this command
     */
    @Override
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    /**
     * @return the name of this command
     */
    @Override
    public String getCommandName() {
        return commandName;
    }

    private static JCommander addSubCommand(JCommander parentCommand,
                                            String commandName, Object commandObject) {
        parentCommand.addCommand(commandName, commandObject);
        return parentCommand.getCommands().get(commandName);
    }

    @Override
    public void build(JCommander jc) {
        //jc.addCommand(this);
        for (Map.Entry<String, Class<? extends Command>> entry : commandMap.entrySet()) {
            Command childAction = getChildAction(entry.getKey(), jc);
            System.out.println("entry.getKey(): " + entry.getKey());
            JCommander sub = addSubCommand(jc, entry.getKey(), childAction);
            childAction.build(sub);
        }
    }

    @Override
    public void run(List<String> args, JCommander jc) {
        jc.parse(args.toArray(new String[]{}));

        if (args.size() == 0 || displayHelp){
            jc.usage();
            System.exit(0);
        }

        System.out.println("Foo! " + args);



//        //final CmdLineParser parser = new CmdLineParser(this);
//        final JCommander parser = JCommander.newBuilder()
//                .addO

//        if (!permitNoArgs() && 0 == args.size()) {
//            //printUsage(parser, false);
//            return;
//        }
//
//        final Command child = getChildAction(args, builder);
//
//        if (null == child) {
//            try {
//                parser.parseArgument(args);
//
//                // update log config based on parsed arguments
//                LogUtil.configureLogging(logDebug);
//
//                if (displayHelp) {
//                   // printUsage(parser, true);
//                } else {
//                    performAction(parser);
//                }
//
//            } catch (CmdLineException e) {
//                // handling of wrong arguments
//                if (LOGGER.isDebugEnabled()) {
//                    LOGGER.debug(e);
//                } else {
//                    LOGGER.error(e.getMessage());
//                }
//
//                printUsage(parser, false);
//
//            } catch (ExitWithCodeException ec) {
//                // print the message and exit with the given code
//                LogUtil.OUTPUT.error(ec.getMessage());
//
//                if (ec.isPrintUsage()) {
//                    printUsage(parser, ec.getExitCode());
//                } else {
//                    System.exit(ec.getExitCode());
//                }
//
//            } catch (Exception e) {
//                LOGGER.error("Error in " + getCommandDescription(), e);
//                System.exit(1);
//            }
//
//        } else {
//            // begin execution
//            child.run(args.subList(1, args.size()));
//        }
    }

    /**
     * @return <code>true</code> if the Command is permitted to accept no arguments, otherwise <code>false</code>
     */
    protected boolean permitNoArgs() {
        return false;
    }

    /**
     * @param parser
     */
    @Override
    public void performAction(JCommander parser) throws CommandException {
//        printUsage(parser, false);
    }

    /**
     * Print usage information, then exit.
     *
     * @param parser  the command line parser containing usage information
     * @param success whether this is due to a successful operation
     */
//    private void printUsage(CmdLineParser parser, boolean success) {
//        printUsage(parser, success ? 0 : 255);
//    }

    /**
     * Print usage information, then exit.
     *
     * @param parser   the command line parser containing usage information
     * @param exitCode the exit code
     */
    private void printUsage(JCommander.Builder parser, int exitCode) {
        System.out.println(getCommandDescription() + " usage:");

        // additional usage message
        System.out.println(getAdditionalUsage());

        System.out.println("Print usage...");
        //parser.printUsage(System.out);
        System.exit(exitCode);
    }

    /**
     * Returns additional usage information; by default this is a list of the supported child commands.
     *
     * @return additional usage information
     */
    protected String getAdditionalUsage() {

        final StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEPARATOR);

        final String parentCommand = getCommandChain();

        if (commandMap.isEmpty()) {
            sb.append(" ");
            sb.append(parentCommand);
            sb.append(" [args...]");
            sb.append(LINE_SEPARATOR);

        } else {
            for (String commandName : commandMap.keySet()) {
                sb.append(" ");
                sb.append(parentCommand);
                sb.append(" ");
                sb.append(commandName);
                sb.append(" [args...]");
                sb.append(LINE_SEPARATOR);
            }
        }

        return sb.toString();
    }

    /**
     * See {@link Command#getCommandChain()}
     */
    @Override
    public String getCommandChain() {
        return (null != parent ? parent.getCommandChain() + " " : "") + getCommandName();
    }

    /**
     * @param args   the arguments
     * @param parser the command line parser containing usage information
     * @return a child Command for the given args, or <code>null</code> if not found
     */
    protected Command getChildAction(List<String> args, JCommander parser) {
        final String commandName = args.get(0);

        // find implementation
        final Class<? extends Command> commandClass = commandMap.get(commandName);
        if (null != commandClass) {
            try {
                final Command command = injector.getInstance(commandClass);
                command.setParent(this);
                command.setCommandName(commandName);
                return command;
            } catch (Exception e) {
                throw new CommandException(String.format("Error getting child command for args: %s", args), e);
            }
        }
        return null;
    }

    protected Command getChildAction(String commandName, JCommander parser) {
        // find implementation
        final Class<? extends Command> commandClass = commandMap.get(commandName);
        if (null != commandClass) {
            try {
                final Command command = injector.getInstance(commandClass);
                command.setParent(this);
                command.setCommandName(commandName);
                return command;
            } catch (Exception e) {
                throw new CommandException(String.format("Error getting child command for args: %s", commandName), e);
            }
        }
        return null;
    }


    public void setLogDebug(boolean logDebug) {
        this.logDebug = logDebug;
    }

    public boolean getLogDebug() {
        return logDebug;
    }
}
