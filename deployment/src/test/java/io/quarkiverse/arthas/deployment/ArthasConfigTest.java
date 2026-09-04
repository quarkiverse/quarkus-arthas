package io.quarkiverse.arthas.deployment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import jakarta.inject.Inject;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import io.quarkiverse.arthas.runtime.ArthasConfig;
import io.quarkus.test.QuarkusUnitTest;

/**
 * Verifies that the extension registers its {@link ArthasConfig} mapping and
 * that the documented defaults are applied when nothing is configured.
 */
public class ArthasConfigTest {

    @RegisterExtension
    static final QuarkusUnitTest test = new QuarkusUnitTest()
            .setArchiveProducer(() -> ShrinkWrap.create(JavaArchive.class));

    @Inject
    ArthasConfig config;

    @Test
    public void defaultsAreApplied() {
        assertFalse(config.enabled());
        assertEquals("4.0.4", config.version());
        assertEquals(8563, config.httpPort());
        assertEquals(System.getProperty("user.home") + "/.quarkus/arthas", config.jarDirectory());
    }
}
