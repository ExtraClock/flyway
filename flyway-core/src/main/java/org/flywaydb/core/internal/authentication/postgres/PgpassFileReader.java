package org.flywaydb.core.internal.authentication.postgres;

import lombok.CustomLog;
import org.flywaydb.core.internal.authentication.ExternalAuthFileReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@CustomLog
public class PgpassFileReader implements ExternalAuthFileReader {

    @Override
    public List<String> getAllContents() {
        List<String> fileContents = new ArrayList<>();

        String pgpassFilePath = getPgpassFilePath();
        if (pgpassFilePath == null) {
            return fileContents;
        }

        LOG.debug("Found pgpass file '" + pgpassFilePath + "'.");
        try {
            fileContents.add(new String(Files.readAllBytes(Paths.get(pgpassFilePath))));
        } catch (IOException e) {
            LOG.debug("Unable to read from pgpass file '" + pgpassFilePath + "'.");
        }

        return fileContents;
    }

    public String getPgpassFilePath() {
        // Priority for the pgpass file goes to the environment variable
        String pgpassEnvPath = System.getenv("PGPASSFILE");
        if (pgpassEnvPath != null) {
            return pgpassEnvPath;
        }

        File pgpassFile;
        boolean isWindows = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win");
        // The pgpass file is under 'APPDATA' in windows and the user's home directory otherwise
        if (isWindows) {
            pgpassFile = new File(System.getenv("APPDATA"), "postgresql\\pgpass.conf");
        } else {
            pgpassFile = new File(System.getProperty("user.home"), ".pgpass");
        }
        if (pgpassFile.exists()) {
            return pgpassFile.getAbsolutePath();
        }
        return null;
    }
}