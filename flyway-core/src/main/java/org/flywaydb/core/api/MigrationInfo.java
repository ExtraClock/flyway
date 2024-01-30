package org.flywaydb.core.api;

import org.flywaydb.core.extensibility.MigrationType;

import java.util.Date;

/**
 * Info about a migration.
 */
public interface MigrationInfo extends Comparable<MigrationInfo> {
    /**
     * @return The type of migration (BASELINE, SQL, JDBC, ...)
     */
    MigrationType getType();

    /**
     * @return The target version of this migration.
     */
    Integer getChecksum();

    default boolean isVersioned() {
        return getVersion() != null;
    }

    /**
     * @return The schema version after the migration is complete.
     */
    MigrationVersion getVersion();

    /**
     * @return The description of the migration.
     */
    String getDescription();

    /**
     * @return The name of the script to execute for this migration, relative to its classpath or filesystem location.
     */
    String getScript();

    /**
     * @return The state of the migration (PENDING, SUCCESS, ...)
     */
    MigrationState getState();

    /**
     * @return The timestamp when this migration was installed. (Only for applied migrations)
     */
    Date getInstalledOn();

    /**
     * @return The user that installed this migration. (Only for applied migrations)
     */
    String getInstalledBy();

    /**
     * @return The rank of this installed migration. This is the most precise way to sort applied migrations by installation order.
     * Migrations that were applied later have a higher rank. (Only for applied migrations)
     */
    Integer getInstalledRank();

    /**
     * @return The execution time (in millis) of this migration. (Only for applied migrations)
     */
    Integer getExecutionTime();

    /**
     * @return The physical location of the migration on disk.
     */
    String getPhysicalLocation();

    /**
     * @return The result between a comparison of these MigrationInfo's versions.
     */
    int compareVersion(MigrationInfo o);
}