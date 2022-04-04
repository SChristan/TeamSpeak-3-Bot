# Feature: ActivityDisplay
## Table of Contents
1. [Database Information](#database-information)
   1. [Database Connection](#database-connection)
   1. [Database Structure](#database-structure)
      1. [Authorized Servergroups](#authorized-servergroups)
      1. [ON/OFF Channel Description](#onoff-channel-description)
1. [Logging](#logging)

## Database Information
### Database Connection
The application expects a `BotConfig.txt` in the same directory, with the following database information.
```
databaseURL_management=jdbc:mysql://<ip-address>:<port>/<databasename>
databaseUsername_management=<username>
databasePassword_management=<password>
```

### Database Structure
#### Authorized Servergroups
The Teamspeak Servergroups, which are allowed to controll the bot via direct messages, are loaded from the database table `verified_servergroups`. The application expects the ServerGroup-ID as an **Integer** in the column `group_id`.

#### ON/OFF Channel Description
The shape of the channel descriptions are loaded from the database table `channeldescription_content`. It needs four columns as shown below.

Column | Type | Description
--- | --- | ---
`sort` | `int` | Determines the order in the channel description.
`servergroup_id` | `int` | ServerGroup-ID of the listed ServerGroup.
`servergroup_designation` | `String` | `manager` for the `management_mitglieder` or `supporter` for the `support_mitglieder` channel.
`servergroup_header` | `String` | Title of the ServerGroup in the channel description.

Example:
`sort` | `servergroup_id` | `servergroup_designation` | `servergroup_header`
--- | --- | --- | ---
`1` | `123` | `manager` | `[b]Admin[/b]`
`2` | `456` | `supporter` | `[b]Manager[/b]`

## Logging
The logging framework for this application is Logback. The properties are defined in the global **logback.xml** file.

The feature should write all information in a separate logfile in the directory **logs**. The log messages can be appended to the parent logger. There are several information, which are logged at any time during the runtime of the application.
