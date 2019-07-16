# Data-Centre-Management-Using-SJF-and-FCFS
##  Algorithm of the Scheduling Process
Have used the capabilities of CloudSim simulation to create a cloud infrastructure that makes use of  SJF or Shortest Job First and FCFS or First Come First Served algorithm, to assign tasks to the VMs that are hosted on the datacentres.
### SJF (Shortest Job First)
Shortest Job First scheduling works on the process with the shortest burst time or duration first. Following are some of it’s critical properties :-
•	This is the best approach to minimize waiting time.
•	This is used in Batch Systems.
•	It is of two types:
(i)	Non Pre-emptive
(ii)	Pre-emptive
•	To successfully implement it, the burst time/duration time of the processes should be known to the processor in advance, which is practically not feasible all the time.
•	This scheduling algorithm is optimal if all the jobs/processes are available at the same time. (either Arrival time is 0 for all, or Arrival time is same for all)
### FCFS ( First Come First Served ) :-
First Come First Served ( FCFS ) is a non-preemptive discipline in which waiting job (or process) at the front of the process list/queue is executed first. Following are some of it’s critical properties :-
•	Non-preemptive scheduling algorithm.
•	Follow first in first out (FIFO) method.
•	One ready queue.
•	FIFO strategy assigns priority to processes in the order in which they request the processor.
•	When a process comes in, add its PCB to the tail of ready queue.
•	Next process from head of queue.
•	Dispatcher selects first job in queue and this job runs to completion of CPU burst.
•	Running process does not give up the CPU until it terminates or it performs IO
