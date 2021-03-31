# Distributed_Facility_Booking

## Features

### 1. Todo
Adds a task of type 'todo' into the task list.

Format: `todo <description>`

Example:
* Input: `todo read book`
* Output:
  ````
  ____________________________________________________________
   Alright, I've added this task:
   [T][ ] read book
   Now you have 1 tasks in your list.
  ____________________________________________________________
  ````

### 2. Deadline
Adds a task of type 'deadline' into the task list.

Format: `deadline <description> /by <date> <timing>`
* Format for date: `yyyy-mm-dd`
* Format for time: `hh:mm`

Example:
* Input: `deadline submit assignment /by 2021-02-24 23:00`
* Output:
  ````
  ____________________________________________________________
   Alright, I've added this task:
   [D][ ] submit assignment (by: Feb 24 2021, 11:00 PM)
   Now you have 2 tasks in your list.
  ____________________________________________________________
  ````

### 3. Event
Adds a task of type 'event' into the task list.

Format: `event <description> /at <date> <timing>`
* Format for date: `yyyy-mm-dd`
* Format for time: `hh:mm`

Example:
* Input: `event project meeting /at 2019-05-23 13:00`
* Output:
  ````
  ____________________________________________________________
   Alright, I've added this task:
   [E][ ] project meeting (at: May 23 2019, 01:00 PM)
   Now you have 3 tasks in your list.
  ____________________________________________________________
  ````

### 4. Done
Marks a task as done.

Format: `done <task number>`

Example:
* Input: `done 1`
* Output:
  ````
  ____________________________________________________________
   Nice! I've marked this task as done:
   [T][X] read book
  ____________________________________________________________
  ````

### 5. List
Lists out all the task in your current task list.

Format: `list`

Example:
* Input: `list`
* Output:
  ````
  ____________________________________________________________
   Here are the tasks in your list:
   1.[T][X] borrow book
   2.[D][ ] submit assignment (by: Feb 24 2021, 11:00 PM)
   3.[E][ ] project meeting (at: May 23 2019, 01:00 PM)
  ____________________________________________________________
  ````

### 6. Delete
Deletes a task from the task list.

Format: `delete <task number>`

Example:
* Input: `delete 3`
* Output:
  ````
  ____________________________________________________________
   Alright, I've deleted this task:
   [E][ ] project meeting (at: May 23 2019, 01:00 PM)
   Now you have 2 tasks in your list.
  ____________________________________________________________
  ````

### 5. Find
Finds and lists out tasks with descriptions containing the keyword.

Format: `find <keyword>`

Example:
* Input: `find book`
* Output:
  ````
  ____________________________________________________________
   Here are the matching tasks in your list:
   1.[T][X] borrow book
  ____________________________________________________________
  ````

### 5. Bye
Exits the application.

Format: `delete <task number>`

Example:
* Input: `bye`
* Output:
  ````
  ____________________________________________________________
   Bye. Hope to see you again soon!
  ____________________________________________________________
  ````

## Summary of Commands

Command | Purpose | Format
-------|---------|-------------
todo | Adds a task of type 'todo' | `todo <description>`
deadline | Adds a task of type 'deadline' | `deadline <description> /by <date> <time>`
event | Adds a task of type 'event' | `event <description> /at <date> <time>`
done | Marks a task as done | `done <task number>`
list | Prints the task lisk | `list`
delete | Deletes a task | `delete <task number>`
find | Finds tasks containing a keyword | `find <keyword>`
bye | Exits application | `bye`