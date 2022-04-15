# Feature: ActivityDisplay
## Table of Contents
1. [Functionality](#functionality)
1. [Database Structure](#database-structure)
   1. [Authorized Servergroups](#authorized-servergroups)
   1. [ON/OFF Channel Description](#onoff-channel-description)
1. [Logging](#logging)

## Functionality
This feature displays the activity status of the management members in two different channel descriptions. It supports three different status: **ON**, **OFF**, **AFK**

## Database Structure
### Authorized Servergroups
The Teamspeak Servergroups, which are allowed to controll the bot via direct messages, are loaded from the database table `ad__verified_servergroups`. The application expects the ServerGroup-ID as an **Integer** in the column `group_id`.

### ON/OFF Channel Description
The shape of the channel descriptions are loaded from the database table `ad__channeldescription`. It needs four columns as shown below.

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
