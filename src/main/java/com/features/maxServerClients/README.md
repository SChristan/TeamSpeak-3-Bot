# Feature: MaxServerClients
## Table of Contents
1. [Functionality](#functionality)
1. [Logging](#logging)

## Functionality
This feature cheacks the amount of online clients on the Teamspeak server. If the maxclient property on the server tends to be reached, this feature increases the maxclient property of the server. If the number of clients decreases, this feature also deacreases the maxclient property.

## Logging
The logging framework for this application is Logback. The properties are defined in the global **logback.xml** file.

The feature should write all information in a separate logfile in the directory **logs**. The log messages can be appended to the parent logger. There are several information, which are logged at any time during the runtime of the application.
