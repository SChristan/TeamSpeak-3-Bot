# Feature: ActivityDisplay
## Table of Contents
1. [Functionality](#functionality)
1. [Commands](#commands)
1. [Database Structure](#database-structure)
   1. [Authorized Servergroups](#authorized-servergroups)
   1. [Twitch Authentication](#twitch-authentication)
   1. [Monitored Streamers](#monitored-streamers)
1. [Logging](#logging)

## Functionality
This feature displays the activity status of the community streamer in a channel descriptions. It supports two different status: **LIVE** and **OFFLINE**

## Commands
The commands for this feature start with "!td" followed by a colon and the actual command.  
Example: `!td:exit`

The following commands are available:
Command | Description
--- | ---
exit | Stops any activity of this feature.
reload | Reloads all information from the database.
addstreamer:`<user_login>`:`<display_name>` | Adds a streamer for monitoring.<br />`user_login`: Twitch user login.<br />`display_name`: The name which is shown in the channel description.
removestreamer:`<user_login>` | Removes a streamer from monitoring.<br />`user_login`: Twitch user login.
liststreamer | Returns all streamers who are monitored.

## Database Structure
### Authorized Servergroups
The Teamspeak Servergroups, which are allowed to controll the bot via direct messages, are loaded from the database table `td__verified_servergroups`. The application expects the ServerGroup-ID as an **Integer** in the column `group_id`.

### Twitch Authentication
Twitch APIs require access tokens to access resources. To get an access token the application needs a `client_id` and a `client_secret` of a registered Twitch application.  
The client credentials are loaded from the database table `td__authentication`. It needs two columns as shown below.

Column | Type | Description
--- | --- | ---
`id` | `String` | ID for the credential value.<br />`CLIENT_ID`: The registered client ID of a twitch application.<br />`CLIENT_SECRET`: The registered client secret of a twitch application.
`value` | `String` | Display name of the streamer which is shown in the channel description.

### Monitored Streamers
The streamers to be displayed in the channel description are loaded from the database table `td__streamer`. It needs two columns as shown below.

Column | Type | Description
--- | --- | ---
`user_login` | `String` | Login of the user who is streaming.
`display_name` | `String` | Display name of the streamer which is shown in the channel description.

Example:
`user_login` | `display_name`
--- | ---
`germanmonkeys` | `GermanMonkeys`

## Logging
The logging framework for this application is Logback. The properties are defined in the global **logback.xml** file.

The feature should write all information in a separate logfile in the directory **logs**. The log messages can be appended to the parent logger. There are several information, which are logged at any time during the runtime of the application.
