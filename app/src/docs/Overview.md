# ExecSec Overview

ExecSec is designed to help Executives organize mass scheduling.

## Contents
* [Overview](#overview)
  * [Automation](#automation)
  * [Scheduling transactions](#scheduling-transactions)
    * [Active](#active-transactions)
    * [Inactive](#inactive-transactions)
    * [Stale](#stale-transactions)
    * [Closed](#close-transactions)
* [Mass Scheduling](#mass-appointment-scheduling)
  * [Name-number matching](#name-number-matching)
  * [Draft Formatters](#draft-formatters)
* [Moving an appointment](#moving-an-appointment)
* [Cancelling an appointment](#cancelling-an-appointment)
    

# Overview

It scans incoming text messages and uses natural language processing techniques to assess their intent.
Based on inferred intent, it takes appropriate actions, [responding to the message](#automation) [and setting appointments](#scheduling-transactions).
It also [reminds](#reminders) individuals of their appointments.

If intent in a message cannot be inferred, the user is notified to handle the message manually.

Data is taken from google spreadsheets (in the form of CSV files), and consumed to form databases.
Required fields include: Given Name, Surname, and phone number.

## Automation

Automation is optional, but supported (TODO)
If automation is enabled, Users will be notified, and able to review automated actions for accuracy.
If automation is disabled, incoming messages will still be analyzed, and responses will still be generated as usual,
however, the user will have to authorize all outgoing messages.

## Scheduling transactions

When a message is received, the corresponding phone number will be searched up in the database to get the name of the individual desiring an appointment,
and a schedule transaction will be initiated.

A Scheduling transaction contains the name and phone number of the individual, as well as the time the first message was received, and the date and time of the appointment.
It also contains some metadata pertaining to a transaction, such as the state of the transaction, when/how often the appointment has been re-scheduled,
as well as if it can be rescheduled (this can be managed in transaction settings).
It retains the messages (incoming and outgoing) which were exchanged between the first message, and the time the appointment happens or is canceled.
It is then archived, and later reviewable by the user.

A transaction state can be "active", "inactive", "stale", "closed/archived",

### Active transactions

Active transactions are transactions which are in the middle fo the scheduling process.
This includes
* The appointment is initially being set
* The appointment is in the process of being re-scheduled
* The appointment is in the process of being cancelled

### Inactive transactions

An inactive transaction is a transaction in which the appointment is set, but has not yet happened.

### Stale transactions

A transaction is "stale" if a certain amount of time passes in the "active" state without a response from the scheduling individual.
The default amount of time required for a message to go stale can be managed in the settings menu, and can be changed for individual transactions.
When a transaction goes stale, the user will either receive a notification suggesting the user follow up,
or a follow-up will be sent automatically, depending on automation settings. 

### Closed transactions

A closed or archived transaction is one for which the appointment has happened.
On the day appointments are scheduled to happen, they will appear in a certain place,
but must be manually re-scheduled (if the individual doesn't show; reschedule messages are automatically generated but not sent)
or checked off (if they do show).

Cancelled appointments are also 

# Mass appointment Scheduling

A list of names and/or phone numbers will be accepted. [Attempts will be made](#name-number-matching) to match names to phone numbers.
Failed matches will give a list of "best guesses" from which the user can choose the desired individual,
or enter something completely different if desired. 

It then allows a message to be drafted.
Drafts can be created with [formatters](#draft-formatters) to personalize messages.
The message can be previewed, then sent.

## Name-Number Matching

Searching first checks for matching last names (string equality then string containment). 
When unique matches are found, these are used.
If ambiguity exists, then first names are matched (first string equality, then containment).
If ambiguity still exists, then all matching names are returned as options, and the user must select one.
If no match is found, the number is flagged as an error.

Messages cannot be sent until all unmatched numbers, and all ambiguities are resolved.

## Draft formatters

Format tags can be used in message drafts to mimic personalization.

Available tags are:
```
%name       Full name (first then last)
%fname      First name
%lname      Last name
%appttime   The time (hour am/pm) of the appointment (invalid without active transaction)
%apptdate   The date of the appointment (weekday, month day number) (invalid without active transaction)
```

Messages can be previewed before they are sent, with sample values put in-line.

# Moving an appointment

A message intent may be to re-schedule an appointment.
Certain keywords in a message may specify this intent (re-schedule, move, etc.).
If the system is automated, it will request confirmation of the supposed intent, but if it's running manually, it will not.

A (post-confirmation) message will be sent querying for when they would like to reschedule.
This will transition the transaction to the "active" state.
 
Appointments can also be re-scheduled manually at any time.

# Cancelling an appointment

A message intent may be to cancel an appointment.
Certain keywords in a message may specify this intent (cancel, can't make, etc.).

The user must confirm this action.
If it is confirmed, then the transaction is closed and archived.

An appointment can also be cancelled manually at any time.