# Winter 2020 Project Description
![alt text](https://github.com/fork1028/ECSE211_W20/blob/master/Final_Project/Diagrams/final_project.png)
## Specific Details

The WiFi class which you will receive before the Beta demo, delivers the game parameters
which are summarized in the following section. The procedure that each player must follow is
summarized in the following steps and must be adhered to:

1. Each robot is placed in the corner specified by the marshal running the competition
round. You will be instructed as to where to place and orient your machine.

2. Once placed and the start button pushed, you are no longer permitted to touch your
machine. If there is any contact with the machine the team is disqualified for that round.

3. One started, the machine waits for the game server to deliver the parameters for the
current run. This is done through a method call which will block until complete.

4. Each machine localizes to the grid. When the localization is completed, the machine
must stop and issue a sequence of 3 beeps.

5. Each machine navigates to their corresponding bridge, transits, and then proceeds to their
search area. Upon arriving, each machine will again stop and issue a sequence of 3
beeps.

6. Each machine searches for the stranded vehicle and connects to it.

7. Each robot returns to its starting corner, towing the rescued vehicle. The robot does not
need to be returned to the starting orientation.

8. Upon returning to the starting corner, each robot halts and issues a sequence of 5 beeps.
