package org.keycloak.testsuite.model;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.keycloak.models.ApplicationModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.testsuite.rule.KeycloakRule;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class CacheTest {
    @ClassRule
    public static KeycloakRule kc = new KeycloakRule();

    @Test
    public void testStaleCache() throws Exception {
        String appId = null;
        {
            // load up cache
            KeycloakSession session = kc.startSession();
            RealmModel realm = session.realms().getRealmByName("test");
            ApplicationModel testApp = realm.getApplicationByName("test-app");
            Assert.assertNotNull(testApp);
            appId = testApp.getId();
            Assert.assertTrue(testApp.isEnabled());
            kc.stopSession(session, true);
        }
        {
            // update realm, then get an AppModel and change it.  The AppModel would not be a cache adapter
            KeycloakSession session = kc.startSession();
            RealmModel realm = session.realms().getRealmByName("test");
            Assert.assertTrue(realm instanceof org.keycloak.models.cache.RealmAdapter);
            realm.setAccessCodeLifespanLogin(200);
            ApplicationModel testApp = realm.getApplicationByName("test-app");
            Assert.assertNotNull(testApp);
            testApp.setEnabled(false);
            kc.stopSession(session, true);
        }
        // make sure that app cache was flushed and enabled changed
        {
            KeycloakSession session = kc.startSession();
            RealmModel realm = session.realms().getRealmByName("test");
            ApplicationModel testApp = session.realms().getApplicationById(appId, realm);
            Assert.assertFalse(testApp.isEnabled());
            kc.stopSession(session, true);

        }


    }
}
