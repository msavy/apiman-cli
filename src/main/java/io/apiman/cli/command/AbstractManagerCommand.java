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

import static io.apiman.cli.util.AuthUtil.DEFAULT_SERVER_PASSWORD;
import static io.apiman.cli.util.AuthUtil.DEFAULT_SERVER_USERNAME;
import static io.apiman.cli.util.LogUtil.LINE_SEPARATOR;

import io.apiman.cli.core.common.model.ManagementApiVersion;
import io.apiman.cli.exception.CommandException;
import io.apiman.cli.exception.ExitWithCodeException;
import io.apiman.cli.management.ManagementApiUtil;
import io.apiman.cli.util.LogUtil;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * @author Pete Cornish {@literal <outofcoffee@gmail.com>}
 */
public abstract class AbstractManagerCommand extends AbstractCommand {
    private static final String DEFAULT_SERVER_ADDRESS = "http://localhost:8080/apiman";

    @Option(name = "--server", aliases = {"-s"}, usage = "Management API server address")
    protected String serverAddress = DEFAULT_SERVER_ADDRESS;

    @Option(name = "--serverUsername", aliases = {"-su"}, usage = "Management API server username")
    private String serverUsername = DEFAULT_SERVER_USERNAME;

    @Option(name = "--serverPassword", aliases = {"-sp"}, usage = "Management API server password")
    private String serverPassword = DEFAULT_SERVER_PASSWORD;

    public AbstractManagerCommand() {
        super();
    }

    /**
     * @param clazz the Class for which to build a client
     * @param <T>   the API interface
     * @return an API client for the given Class
     */
    protected <T> T buildServerApiClient(Class<T> clazz) {
        return buildServerApiClient(clazz, ManagementApiVersion.UNSPECIFIED);
    }

    /**
     * @param clazz         the Class for which to build a client
     * @param serverVersion the server version
     * @param <T>           the API interface
     * @return an API client for the given Class
     */
    protected <T> T buildServerApiClient(Class<T> clazz, ManagementApiVersion serverVersion) {
        return ManagementApiUtil.buildServerApiClient(
                clazz,
                getManagementApiEndpoint(),
                getManagementApiUsername(),
                getManagementApiPassword(),
                getLogDebug(),
                serverVersion);
    }

    protected String getManagementApiEndpoint() {
        // TODO consider reading from config file/environment
        return serverAddress;
    }

    private String getManagementApiUsername() {
        // TODO consider reading from config file/environment
        return serverUsername;
    }

    private String getManagementApiPassword() {
        // TODO consider reading from config file/environment
        return serverPassword;
    }

}
