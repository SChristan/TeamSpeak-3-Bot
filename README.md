# GermanMonkeys Bot
## Table of Contents
1. [Commands](#commands)
1. [Database Information](#database-information)
   1. [Database Connection](#database-connection)
   1. [Database Structure](#database-structure)
      1. [Teamspeakserver Information](#teamspeakserver-information)
      1. [Authorized Servergroups](#authorized-servergroups)
      1. [Channel-IDs](#channel-ids)
1. [Teamspeak](#teamspeak)
   1. [Provided Information](#provided-information)
   1. [Events](#events)
1. [Features](#features)
1. [Logging](#logging)

## Commands
The commands for the bot start with "!bot" followed by a colon and the actual command.  
Example: `!bot:exit`

The following commands are available:
Command | Description
--- | ---
exit | Stops any activity of the bot and terminates the application.
restart | Restarts all functionalities of the bot, like at startup.
nickname:\<name> | Sets the Teamspeak nickname of the bot to the specified name.

## Database Information
### Database Connection
The application expects a `BotConfig.txt` in the same directory, with the following database information.
```
databaseURL=jdbc:mysql://<ip-address>:<port>/<databasename>
databaseUsername=<username>
databasePassword=<password>
```

### Database Structure
#### Teamspeakserver Information
The server connection information are loaded as a **String** from the database table `bot__config`, which contains the following information.

`config_designation` | `value` | Description
--- | --- | ---
`ip_address` | `<ip adress>` | IP-adress of the Teamspeak server as `xxx.xxx.xxx.xxx`.
`server_port` | `<port>` | Voice-Port of the Teamspeak server.
`query_username` | `<login>` | ServerQuery login for the Teamspeak server.
`query_password` | `<password>` | Password of the ServerQuery login.
`bot_nickname` | `<name>` | Visible bot nickname on the Teamspeak server.
`enable_communications_logging` | `<statement>` | `TRUE` or `FALSE`! `TRUE` enables extended logging of the communication between the application and the Teamspeak server.

#### Authorized Servergroups
The Teamspeak Servergroups, which are allowed to controll the bot via direct messages, are loaded from the database table `bot__verified_servergroups`. The application expects the ServerGroup-ID as an **Integer** in the column `group_id`.

#### Channel-IDs
The Channel-IDs are loaded as an **Integer** from the database table `bot__channel_ids`, it supports the following channels.

`channel_designation` | `channel_id` | Description
--- | --- | ---
`management_mitglieder` | `<channel_id>` | Channel-ID of the channel which should contain the `manager` Clients.
`support_mitglieder` | `<channel_id>` | Channel-ID of the channel which should contain the `supporter` Clients.
`warten_bewerbungsgespraech` | `<channel_id>` | Channel-ID of a support channel for sticky client function.
`warten_management` | `<channel_id>` | Channel-ID of a support channel for sticky client function.
`stream_header` | `<channel_id>` | Parent Channel-ID of the streaming channels.
`afk_short` | `<channel_id>` | Channel-ID of the short afk channel.
`afk_long` | `<channel_id>` | Channel-ID of the long afk channel.

## Teamspeak
### Provided Information
The bot provides various information about the Teamspeak server, such as online clients and IDs. All features can use these information, but should not modify them.

### Events
The bot is connected to the Teamspeak server and receives events from the Teamspeak API, which can be subscribed by the features. The bot may not subscribe to all events of the API, but only those that are needed at the moment.

## Features
You can add any features to this bot, which should implement a static `start()` and `stop()` method.

## Logging
The logging framework for this application is Logback. The properties are defined in the global **logback.xml** file.

The application writes all information in a separate logfile in the directory **logs**. The log messages are appended to the parent logger. There are several information, which are logged at any time during the runtime of the application.
