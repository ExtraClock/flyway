---
subtitle: Prepare
---

# Prepare

{% include enterprise.html %}

The `prepare` command generates a deployment script from a schemaModel, an environment, a diff artifact, or migrations

## Usage examples

### Generate a deployment script to update target database based on schema model

```
flyway prepare -source=schemaModel -target=production -scriptFilename="D__deployment.sql"
```

> [!Important]  
> When the schemas are different between source and target, make sure to
> configure [schema model schemas](<Configuration/Parameters/Flyway/Schema Model Schemas>).
> If a target schema does not exist in the schema model, the generated script will contain SQL for deleting the schema.

### Generate a deployment script to update target database based on source database

```
flyway prepare -source=env:dev -target=production -scriptFilename="D__deployment.sql"
```

### Generate a deployment script from a diff artifact

```
flyway prepare -artifactFilename="myArtifactPath" -changes="-" -scriptFilename="D__deployment.sql"
```

Generates a deployment script which can be executed against a target database to make it match the target of the
original comparison.

The artifact needed for this approach is generated by [`flyway diff`](<Commands/Diff>).

This approach allows for programmatic partial selection of changes to deploy.

### Generate a deployment script from migrations

```
flyway prepare -source=migrations -target=production-equivalent -scriptFilename="D__deployment.sql"
```

Generates a deployment script which can be executed against a target database to deploy all pending migrations.

This is a useful alternative to `flyway migrate` when you need to deploy to equivalent databases where flyway is not
available, or want to review the exact script to be run before execution, but it does come with limitations:

* This approach does not handle Java migrations or other non-SQL migrations
* Callbacks are not supported

This is currently functionally equivalent to running a [dry run](<Concepts/Dry Runs>) on `migrate`.

## Complete list of parameters for deploying from schema model or source database

### Required

| Parameter | Namespace | Description                                                                   |
|-----------|-----------|-------------------------------------------------------------------------------|
| `source`  | prepare   | Either `schemaModel` or a source database (e.g. `env:dev`) for this workflow. |
| `target`  | prepare   | The target environment to deploy to.                                          |

### Optional

| Parameter                                                                        | Namespace | Description                                                                                                                                                                                         |
|----------------------------------------------------------------------------------|-----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `scriptFilename`                                                                 | prepare   | The path to the deployment script that will be generated, relative to the working directory. Defaults to `deployments/D__deployment.sql`.                                                           |
| `abortOnWarningSeverity`                                                         | prepare   | Will cause an error to be returned if any warnings of configured severity are raised when generating the deployment script. Valid values are: `None`, `High`, `Medium`, `Low` . Defaults to `None`. |
| `force`                                                                          | prepare   | If the deployment script already exists, overwrite it. Defaults to `false`.                                                                                                                         |
| [`configFiles`](<Configuration/Parameters/Flyway/Config Files>)                  | (root)    | The location of the flyway configuration files.                                                                                                                                                     |
| [`encoding`](<Configuration/Parameters/Flyway/Encoding>)                         | (root)    | The encoding to use for the generated script.                                                                                                                                                       |
| [{environment parameters}](<Configuration/Parameters/Environments>)              | (root)    | Environment configuration for the source and/or target environments.                                                                                                                                |
| [`schemaModelLocation`](<Configuration/Parameters/Flyway/Schema Model Location>) | (root)    | The path to the schema model.                                                                                                                                                                       |
| [`schemaModelSchemas`](<Configuration/Parameters/Flyway/Schema Model Schemas>)   | (root)    | The schemas in the schema model.                                                                                                                                                                    |
| [`workingDirectory`](<Configuration/Parameters/Flyway/Working Directory>)        | (root)    | The directory to consider the current working directory. All relative paths will be considered relative to this.                                                                                    |

Note that comparison options, filters, and static data configuration are all supported, but can currently only be
specified within the toml config file, and cannot yet be overridden on the command line

## Complete list of options for deploying from artifact

### Optional

| Parameter                                                                 | Namespace | Description                                                                                                                                          |
|---------------------------------------------------------------------------|-----------|------------------------------------------------------------------------------------------------------------------------------------------------------|
| `artifactFilename`                                                        | prepare   | The path to the diff artifact (has the same default as `diff.artifactFilename` and does not need to be specified separately if chained with `diff`.) |
| `changes`                                                                 | prepare   | A comma separated list of change ids. If unspecified, all changes will be used. May specify `-` to read changes from stdin                           |
| `scriptFilename`                                                          | prepare   | The path to the deployment script that will be generated, relative to the working directory. Defaults to `deployments/D__deployment.sql`.            |
| [`configFiles`](<Configuration/Parameters/Flyway/Config Files>)           | (root)    | The location of the flyway configuration files.                                                                                                      |
| [`encoding`](<Configuration/Parameters/Flyway/Encoding>)                  | (root)    | The encoding to use for the generated script.                                                                                                        |
| [`workingDirectory`](<Configuration/Parameters/Flyway/Working Directory>) | (root)    | The directory to consider the current working directory. All relative paths will be considered relative to this.                                     |

Note that comparison options, filters, and static data configuration are all supported, but can currently only be
specified within the toml config file, and cannot yet be overridden on the command line

## Complete list of options for deploying from migrations

### Required

| Parameter | Namespace | Description                                    |
|-----------|-----------|------------------------------------------------|
| `source`  | prepare   | Must be set to `migrations` for this workflow. |
| `target`  | prepare   | The target environment to deploy to.           |

### Optional

| Parameter                                                                                | Namespace | Description                                                                                                                               |
|------------------------------------------------------------------------------------------|-----------|-------------------------------------------------------------------------------------------------------------------------------------------|
| `scriptFilename`                                                                         | prepare   | The path to the deployment script that will be generated, relative to the working directory. Defaults to `deployments/D__deployment.sql`. |
| [`baselineOnMigrate`](<Configuration/Parameters/Flyway/Baseline On Migrate>)             | (root)    | Whether to generate schema history table as part of the deployment script (required if does not exist in target database).                |
| [`baselineVersion`](<Configuration/Parameters/Flyway/Baseline Version>)                  | (root)    | If `baselineOnMigrate` is set, specifies the baseline version (only subsequent versioned migrations will be included in deployment).      |
| [`cherryPick`](<Configuration/Parameters/Flyway/Cherry Pick>)                            | (root)    | A custom selection of migrations to use for generating the deployment script.                                                             |
| [`configFiles`](<Configuration/Parameters/Flyway/Config Files>)                          | (root)    | The location of the flyway configuration files.                                                                                           |
| [`defaultSchema`](<Configuration/Parameters/Flyway/Default Schema>)                      | (root)    | The location of the flyway schema history table.                                                                                          |
| [`encoding`](<Configuration/Parameters/Flyway/Encoding>)                                 | (root)    | The encoding to use for the generated script.                                                                                             |
| [{environment parameters}](<Configuration/Parameters/Environments>)                      | (root)    | Environment configuration for the source and/or target environments.                                                                      |
| [`ignoreMigrationPatterns`](<Configuration/Parameters/Flyway/Ignore Migration Patterns>) | (root)    | Pattern for excluding migrations from the deployment script.                                                                              |
| [`locations`](<Configuration/Parameters/Flyway/Locations>)                               | (root)    | Locations of the migrations to deploy.                                                                                                    |
| [{placeholders}](<Configuration/Parameters/Placeholders>)                                | (root)    | Placeholders in migration scripts to replace with runtime information.                                                                    |
| [`placeholderPrefix`](<Configuration/Parameters/Flyway/Placeholder Prefix>)              | (root)    | Placeholder prefix characters.                                                                                                            |
| [`placeholderReplacement`](<Configuration/Parameters/Flyway/Placeholder Replacement>)    | (root)    | Whether or not to replace placeholders.                                                                                                   |
| [`placeholderSeparator`](<Configuration/Parameters/Flyway/Placeholder Separator>)        | (root)    | Placeholder separator characters.                                                                                                         |
| [`placeholderSuffix`](<Configuration/Parameters/Flyway/Placeholder Suffix>)              | (root)    | Placeholder suffix characters.                                                                                                            |
| [`workingDirectory`](<Configuration/Parameters/Flyway/Working Directory>)                | (root)    | The directory to consider the current working directory. All relative paths will be considered relative to this.                          |

## JSON output format

```json
{
  "scriptWasGenerated": true,
  "scriptFilename": "C:\\workingDirectory\\D__deployment.sql",
  "includedDependencies": [
    "dbo.someDependency"
  ],
  "warnings": [
    {
      "type": "WARNING_TYPE",
      "message": "Warning message"
    }
  ]
}
```