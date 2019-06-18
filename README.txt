////////////////classes//////////////////////////

1) Scheduler_Queue.java

Scheduler sends and receives packets from both Floor subsystem and Elevator subsystem. It schedules requests into queues and keeps track of the arrival sensors.
It also polls the arrival sensor for an elevator time out.

2) Elevator_model.java

It controls the Elevator motor and outputs the location of the elevator.
waits for a message from scheduler after an error occurs.

3) ReadMethod.java

used to read inputs from a readfile specified in the project instructions.

4) Request.java

helper structure for queueing requests

5) ArrivalSensor.Java

helper structure for keeping track of the location of the elevator.
Also keeps track of the time of the elevators last communication with scheduler.

6) Floor.Java

outputs the lights visible on the floor.
Also fowards packets from Readmethod to scheduler.

7) Logger.Java

loggs events and outputs them in a txt file

//////////////////////////Testing the code/////////////////////////////

Notes: 
output key:
elevator movement is output in the command window of Elevator_Model.
critical packet error is output in the command window of Scheduler_Queue.
lights are displayed in Floor.
events are displayed in the log files

/////////////////Running the Code////////////////////////////

1. Import the java class files into the source folder of a java project.
2. Create run configurations for the elevator model.
	a) open the class Elevator_Model
	b) from the run dropdown button click on run configurations
	c) right click on elevator model on the left side and click on new run configuration
	e) repeat c 2 more times
	f) for the first run configuration click on the arguements tab and add:
		i) on the first line type: 2050
		ii) on the second line type: 1
	g) for the following 3 configurations, repeat f with the arguements: (2051,2),(2052,3),(2053,4)

3. Create run configuration for ReadMethod
	a) on the arguements tab type the of Readfile.txt
	b) be sure that there are quotation marks around the path Readfile.txt is at the end of the path and that slashes are foward.
	c) example "C:/Users/patrick/Desktop/Readfile.txt"

4. Create run configuration for Scheduler
	a) on the arguements tab type where the log will be generated and the log file's name.
	b) be sure that there are quotation marks around the path log1.txt is at the end of the path and that slashes are foward.
	c) example "C:/Users/patrick/Desktop/log1.txt"

5. Create run configuration for floor
	a) on the arguements tab type where the log will be generated and the log file's name.
	b) be sure that there are quotation marks around the path log2.txt is at the end of the path and that slashes are foward.
	c) example "C:/Users/patrick/Desktop/log2.txt"

6. run programs in the following order:
	SchedulerQueue
	Floor
	the 4 run configurations created for elevator model
	the run configuration created for read method

5.each elevator will output its location in the command window associated with that run configuration

////////////Resources//////////////
Packet Information.
Please refer to the word Document CommunicationProtocol.docx

Results of timing analysis are in the results pdf.

diagrams are in their respective pdf files.

/////////////////Distribution of work.///////////////////////

There was a final exam for computer system eng studens the week of iteration 4. This had an effect on the quality of this iteration. Multiple computer implementation of subsystems not complete.

Patrick
Iter 1: Worked on Scheduler and Elevator model Subsystems, inPanel, outPanel.
Iter 2: SchedulerQueue, ElevatorModel, ReadMethod, ArrivalSensor, Request, testing, Readme
Iter 3: SchedulerQueue, ElevatorModel, ArrivalSensor, testing, readme
Iter 4: Logger, Modificatios of code to use logger, Data collection & analysis, Readme

Goutham
Iter 1: Worked on Floor and Scheduler subsytems, readme. 
Iter 2: ReadMethod, SchedulerQueue
Iter 3: Floor, Readmethod, SchedulerQueue, testing


Nikhil
Iter 1: Worked on Elevator inPanel, outPanel, Queues and UML class diagrams.
Iter 2: ReadMethod, SchedulerQueue, testing
Iter 3: Readmethod, SchedulerQueue, Diagrams, testing
Iter 4: IP connection

Zehui
Iter 1: Worked on State machine Diagram
Iter 2: sequence diagram. 
Iter 3: Timing diagram