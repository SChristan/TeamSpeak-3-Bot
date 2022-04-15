# Feature: WelcomeMessage
## Table of Contents
1. [Functionality](#functionality)
1. [Database Structure](#database-structure)
   1. [Authorized Servergroups](#authorized-servergroups)
   1. [Welcome Message](#welcome-message)
1. [Logging](#logging)

## Functionality
This feature sends a private text message to all clients that connect to the Teamspeak server.

## Database Structure
### Authorized Servergroups
The Teamspeak Servergroups, which are allowed to controll the bot via direct messages, are loaded from the database table `wm__verified_servergroups`. The application expects the ServerGroup-ID as an **Integer** in the column `group_id`.

### Welcome Message
The welcome message is loaded from the database table `wm__welcome_message`. It needs two columns as shown below.

Column | Type | Description
--- | --- | ---
`id` | `int` | Determines the message id.
`message` | `text` | Message that is sent to the clients.

## Logging
The logging framework for this application is Logback. The properties are defined in the global **logback.xml** file.

The feature should write all information in a separate logfile in the directory **logs**. The log messages can be appended to the parent logger. There are several information, which are logged at any time during the runtime of the application.
