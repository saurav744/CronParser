## Cron parser

### Steps to Run the application

1. Install Java version 1.8 or above.
2. Download the project folder
3. Go to the target folder containing the jar file "cronparser-0.0.1-SNAPSHOT.jar"
4. On the command line run the following command with an input cron expression as command line argument :-

              java -jar cronparser-0.0.1-SNAPSHOT.jar "1 1 15 JAN-DEC MON-FRI /usr/bin/find"
	
### Cases handled

Standard cron format with five time fields - *minute*, *hour*, *day of month*, *month*, and *day of week*

Special characters handled-    *comma(,) dash(-) asterisk(*) slash(/)* and *question mark (?)*

Characters not handled :      *L, W, #* and other special strings

### Important java classes
```
ParsedCronExpression.java - Object of this java class type stores the parsed cron fields
```

```
CronParser.java - Java class having static method parse() which takes cron expression string as input and returns 
ParsedCronExpression object containing all parsed cron fields.
```


###Example cron strings

A few sample cron strings to test the application :

         "* * * * * /folder/command1"
         "1,5 15 10 * ? /command2"
         "0 5 15 1-12 1-5 /command3"
         "0-45/5 14,18 1/2 JAN-JUN/2,JUL-DEC/3 * /command4"
         "*/5 0-5 ? MAR-DEC * /command5"
         "0 1-12/2,13-23/3 14 JAN,MAR SUN-SAT/2 /command6"
         "15 10 ? * ? /command9"
         "0 0 12 * ? * /command10"  (Should fail : Excess arguments)
         "0 11 11 ? /command11"   (Should fail:  Fewer arguments) 
