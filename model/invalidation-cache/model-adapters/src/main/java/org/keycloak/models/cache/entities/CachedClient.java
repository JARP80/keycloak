package org.keycloak.models.cache.entities;

import org.keycloak.models.ClientIdentityProviderMappingModel;
import org.keycloak.models.ClientModel;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RealmProvider;
import org.keycloak.models.RoleModel;
import org.keycloak.models.cache.RealmCache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CachedClient {
    protected String id;
    protected String name;
    protected String realm;
    protected Set<String> redirectUris = new HashSet<String>();
    protected boolean enabled;
    protected String secret;
    protected String protocol;
    protected Map<String, String> attributes = new HashMap<String, String>();
    protected boolean publicClient;
    protected boolean fullScopeAllowed;
    protected boolean directGrantsOnly;
    protected boolean frontchannelLogout;
    protected int notBefore;
    protected Set<String> scope = new HashSet<String>();
    protected Set<String> webOrigins = new HashSet<String>();
    private List<ClientIdentityProviderMappingModel> identityProviders = new ArrayList<ClientIdentityProviderMappingModel>();
    private Set<ProtocolMapperModel> protocolMappers = new HashSet<ProtocolMapperModel>();

    public CachedClient(RealmCache cache, RealmProvider delegate, RealmModel realm, ClientModel model) {
        id = model.getId();
        secret = model.getSecret();
        name = model.getClientId();
        this.realm = realm.getId();
        enabled = model.isEnabled();
        protocol = model.getProtocol();
        attributes.putAll(model.getAttributes());
        notBefore = model.getNotBefore();
        directGrantsOnly = model.isDirectGrantsOnly();
        frontchannelLogout = model.isFrontchannelLogout();
        publicClient = model.isPublicClient();
        fullScopeAllowed = model.isFullScopeAllowed();
        redirectUris.addAll(model.getRedirectUris());
        webOrigins.addAll(model.getWebOrigins());
        for (RoleModel role : model.getScopeMappings())  {
            scope.add(role.getId());
        }
        this.identityProviders = model.getIdentityProviders();
        for (ProtocolMapperModel mapper : model.getProtocolMappers()) {
            this.protocolMappers.add(mapper);
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRealm() {
        return realm;
    }

    public Set<String> getRedirectUris() {
        return redirectUris;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getSecret() {
        return secret;
    }

    public boolean isPublicClient() {
        return publicClient;
    }

    public boolean isDirectGrantsOnly() {
        return directGrantsOnly;
    }

    public int getNotBefore() {
        return notBefore;
    }

    public Set<String> getScope() {
        return scope;
    }

    public Set<String> getWebOrigins() {
        return webOrigins;
    }

    public boolean isFullScopeAllowed() {
        return fullScopeAllowed;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public boolean isFrontchannelLogout() {
        return frontchannelLogout;
    }

    public List<ClientIdentityProviderMappingModel> getIdentityProviders() {
        return this.identityProviders;
    }

    public boolean hasIdentityProvider(String providerId) {
        for (ClientIdentityProviderMappingModel model : getIdentityProviders()) {
            if (model.getIdentityProvider().equals(providerId)) {
                return true;
            }
        }

        return false;
    }

    public Set<ProtocolMapperModel> getProtocolMappers() {
        return protocolMappers;
    }

    public boolean isAllowedRetrieveTokenFromIdentityProvider(String providerId) {
        for (ClientIdentityProviderMappingModel model : getIdentityProviders()) {
            if (model.getIdentityProvider().equals(providerId)) {
                return model.isRetrieveToken();
            }
        }

        return false;
    }
}
