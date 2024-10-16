package io.quarkiverse.arthas.deployment;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jboss.logging.Logger;

import io.quarkus.bootstrap.app.RunningQuarkusApplication;
import io.quarkus.deployment.dev.DevModeListener;
import io.quarkus.utilities.JavaBinFinder;

public class ArthasDevModeListener implements DevModeListener {

    private static final Logger log = Logger.getLogger(ArthasDevModeListener.class);

    private Process arthasProcess;

    @Override
    public void afterFirstStart(RunningQuarkusApplication application) {
        try {
            startArthasIfNecessary(application);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void beforeShutdown() {
        if ((arthasProcess != null) && arthasProcess.isAlive()) {
            arthasProcess.destroy();
        }
    }

    private void startArthasIfNecessary(RunningQuarkusApplication runner) throws IOException {
        var arthasEnabledOpt = runner.getConfigValue("quarkus.arthas.enabled", Boolean.class);
        if (arthasEnabledOpt.isEmpty() || !arthasEnabledOpt.get()) {
            log.debugf("Arthas will not be started because 'quarkus.arthas.enabled' is not set to 'true'");
            return;
        }

        var parentDirOpt = runner.getConfigValue("quarkus.arthas.jar-directory", String.class);
        if (parentDirOpt.isEmpty()) {
            // should not happen
            return;
        }
        var parentDir = Paths.get(parentDirOpt.get());
        if (!Files.exists(parentDir)) {
            Files.createDirectory(parentDir);
        }

        var arthasHttpPortOpt = runner.getConfigValue("quarkus.arthas.http-port", Integer.class);
        if (arthasHttpPortOpt.isEmpty()) {
            // should not happen
            return;
        }
        var arthasHttpPort = arthasHttpPortOpt.get();

        var arthasVersionOpt = runner.getConfigValue("quarkus.arthas.version", String.class);
        if (arthasVersionOpt.isEmpty()) {
            // should not happen
            return;
        }
        var arthasVersion = arthasVersionOpt.get();
        var arthasDir = parentDir.resolve(String.format("arthas-%s", arthasVersion));
        if (!Files.exists(arthasDir)) {
            Files.createDirectory(arthasDir);
        }

        Runnable r;
        if (!Files.exists(toArthasJar(arthasDir))) {
            r = () -> {
                try {
                    startArthas(downloadArthas(arthasDir, arthasVersion), arthasHttpPort);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            };
        } else {
            r = () -> startArthas(toArthasJar(arthasDir), arthasHttpPort);
        }
        var arthasThread = new Thread(r);
        arthasThread.setName("Arthas starter thread");
        arthasThread.setDaemon(true);
        arthasThread.start();
    }

    private Path downloadArthas(Path arthasDir, String version) throws IOException {
        var downloadURL = String.format(
                "https://repo.maven.apache.org/maven2/com/taobao/arthas/arthas-boot/%s/arthas-boot-%s-jar-with-dependencies.jar",
                version, version);
        log.info("Attempting to download Arthas from " + downloadURL);
        var arthasJar = toArthasJar(arthasDir).toFile();
        try (BufferedInputStream in = new BufferedInputStream(new URL(downloadURL).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(arthasJar)) {
            byte[] dataBuffer = new byte[10240];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 10240)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        }
        log.info("Arthas downloaded");
        return toArthasJar(arthasDir);
    }

    private void startArthas(Path arthasJar, Integer httpPort) {
        log.debug("Attempting to start Arthas");
        ProcessBuilder processBuilder = new ProcessBuilder(JavaBinFinder.findBin(),
                "-jar", arthasJar.toAbsolutePath().toString(),
                "" + ProcessHandle.current().pid(),
                "--http-port",
                "" + httpPort,
                "--target-ip",
                "localhost")
                .directory(arthasJar.getParent().toFile());

        if (log.isDebugEnabled()) {
            processBuilder.inheritIO();
        } else {
            processBuilder.redirectError(ProcessBuilder.Redirect.DISCARD.file())
                    .redirectOutput(ProcessBuilder.Redirect.DISCARD.file());

        }

        try {
            arthasProcess = processBuilder.start();
            try {
                // attaching to the process takes a while
                // TODO: we should probably tail the logs instead of waiting
                Thread.sleep(5_000);
            } catch (InterruptedException ignored) {

            }
            if (arthasProcess.isAlive()) {
                log.infof("Arthas started and is accessible at http://localhost:%d", httpPort);
                // NOTE: we do not need to take care of killing the Arthas process when Quarkus stops, because the OS will reap it automatically
                // as a child process of a dead process
            } else {
                log.warn("Arthas process did not start properly");
            }
        } catch (IOException e) {
            log.warn("Unable to start Arthas", e);
        }
    }

    private Path toArthasJar(Path arthasDir) {
        return arthasDir.resolve("arthas-boot.jar");
    }
}
