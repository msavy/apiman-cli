package io.apiman.cli.managerapi.service.delegates;

import io.apiman.cli.command.api.model.ApiPolicy;
import io.apiman.cli.managerapi.command.api.PolicyApi;
import io.apiman.cli.managerapi.command.client.ClientApi;
import retrofit.client.Response;

import java.util.List;

/**
 * @author Marc Savy {@literal <marc@rhymewithgravy.com>}
 */

public class ClientPolicyDelegate implements PolicyApi {

    private final ClientApi api;

    public ClientPolicyDelegate(ClientApi api) {
        this.api = api;
    }

    public static ClientPolicyDelegate wrap(ClientApi clientApi) {
        return new ClientPolicyDelegate(clientApi);
    }

    @Override
    public Response addPolicy(String orgName, String entityName, String version, ApiPolicy policyConfig) {
        return api.addPolicy(orgName, entityName, version, policyConfig);
    }

    @Override
    public ApiPolicy fetchPolicy(String orgName, String entityName, String version, Long policyId) {
        return api.fetchPolicy(orgName, entityName, version, policyId);
    }

    @Override
    public Response configurePolicy(String orgName, String entityName, String apiVersion, Long policyId, ApiPolicy policyConfig) {
        return api.configurePolicy(orgName, entityName, apiVersion, policyId, policyConfig);
    }

    @Override
    public List<ApiPolicy> fetchPolicies(String orgName, String entityName, String version) {
        return api.fetchPolicies(orgName, entityName, version);
    }
}