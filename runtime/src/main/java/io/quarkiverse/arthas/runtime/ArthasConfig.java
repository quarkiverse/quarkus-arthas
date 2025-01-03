package io.quarkiverse.arthas.runtime;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * Configuration related to <a href="https://arthas.aliyun.com/en">Arthas</a>.
 * <p>
 * These configuration values only apply to dev-mode.
 */
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "quarkus.arthas")
public interface ArthasConfig {

    /**
     * Whether Arthas should be started when Quarkus dev-mode is launched
     */
    @WithDefault("false")
    boolean enabled();

    /**
     * The versions of Arthas to use
     */
    @WithDefault("4.0.4")
    String version();

    /**
     * The directory into which to save the arthas-boot jar if it doesn't exist
     */
    @WithDefault("${user.home}/.quarkus/arthas")
    String jarDirectory();

    /**
     * The port on which the Arthas Web Console will be available
     */
    @WithDefault("8563")
    Integer httpPort();
}
