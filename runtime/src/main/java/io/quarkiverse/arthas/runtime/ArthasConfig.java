package io.quarkiverse.arthas.runtime;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

/**
 * Configuration related to <a href="https://arthas.aliyun.com">Arthas</a>.
 *
 * These configuration values only apply to dev-mode.
 */
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public class ArthasConfig {

    /**
     * Whether Arthas should be started when Quarkus dev-mode is launched
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    /**
     * The versions of Arthas to use
     */
    @ConfigItem(defaultValue = "3.6.1")
    public String version;

    /**
     * The directory into which to save the arthas-boot jar if it doesn't exist
     */
    @ConfigItem(defaultValue = "${user.home}/.quarkus/arthas")
    public String jarDirectory;

    /**
     * The port on which the Arthas Web Console will be available
     */
    @ConfigItem(defaultValue = "8563")
    public Integer httpPort;
}
