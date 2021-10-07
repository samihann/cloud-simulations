# Cloud Environment Simulation
### Description: Create cloud simulators in Scala for evaluating executions of applications in cloud datacenters with different characteristics and deployment models. (CS 441 | UIC)



### Running the Project
The project and be compiled and run two ways. 
* Through Command Line
  * Clone the following repository in your system.
  * On command line please navigate to the following repository. And run the following commands to compile and run the code. 
  ```
  sbt clean compile test
    ``` 
    ```
    sbt clean compile run
    ```
* Through IntelliJ IDEA
  * Clone the following repository in your system. 
  * Import the project into IntelliJ. 
  * Compile and run the simulation from there. 
### Project Structure
1. `Simulation` is the file where all the required simulations mentioned for ease of understanding. Please run the following file to view all the simulation. 
2. Simulations package has all the Simulation files present which are being call in `Simulation`.
3. All the configuration files are present in src/resources/ directory.
4. `CreationResource` class in CreationFiles package is re-useable across project to create any cloud resources. 
5. `IaasServiceModel`, `SaasServiceModel` and `PaasServiceModel` provide re-useability in creation of IAAS, PAAS and SAAS model across the project.
### Project Flow 
1. `Simulation` will call each of the simulation from `ServiceModelSimulation`, `AllocationPolicySimulation`, `DataCenterSimulation`, `SchedulingSimulations` and `ServiceModelSimulation`. 
2. These files contain the main programs to run the simulation. These will be call `CreationFiles` package and their classes to generate various required resources. 
3. The `CreationResource`, `IaasServiceModel`, `SaasServiceModel` and `PaasServiceModel` will be taking the required parameters from the config files present in src/resources directory.

### Cloud Resource ->  created and discussed
* DataCenters 
* Hosts (Physical Machine)
* Virtual Machines (Virtualization over the physical Machine)
* Cloudlet (Processing Unit for Each VM)

### Policies Discussed
These are the various CLoudSimPlus policies and scheduling algorithms discussed.
* Space Shared scheduler (VM & CLoudlet)
* Time Shared scheduler (VM & Cloudlet)
* Simple VM allocation Policy
* First Fit VM Allocation Policy
* Best Fit VM Allocation Policy
* Round Robin VM Allocation Policy

## Simulations

1. Time Shared Scheduling Simulation. 
    
In this simulation we have assumed a single DataCenter, one Host and one VM. And we are trying to run the two cloudlets 
on these machine. 
* For input configuration please refer this file - [TimeShared.conf](/src/main/resources/TimeShared.conf)
* For the Simulation File please refer to - [Simulation-File](/src/main/scala/Simulations/SchedulingSimulations.scala)

```
                                         SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|        0|        40|      40
       1|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|        0|        40|      40
-----------------------------------------------------------------------------------------------------
```

As it can be seen for the Simulation table constructed above, both the Cloudlets run on the VM simultaneously and will be completed
within 40 seconds. During the period the utilization on the VM is more as the VM's CPU is being shared across all the Cloudlets. 

As both the cloudlet have same configurations, both have started and finished together. 

2. Space Shared Simulation

In this simulation, we have assumed similar configuration to that of Simulation 1. 

* For input configuration please refer this file - [SpaceShared.conf](/src/main/resources/SpaceShared.conf)
* For the Simulation File please refer to - [Space-Simulation-File](/src/main/scala/Simulations/SchedulingSimulations.scala)

```
                                         SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|       20|        40|      20
-----------------------------------------------------------------------------------------------------
```

In this Simulation each Cloudlet runs for 20 seconds and then finishes. At a moment, only one Cloudlet will be running on
the VM. 

The total time to complete the execution of both the cloudlets is 40 seconds, which is similar to that of the Time Shared 
Execution. It can be noticed that the execution time of a single Cloudlet is half of that in the case of  Time Shared Scheduling.
This should be because the Cloudlet is utilize the VM resources to highest amount permissible to complete quickly.

3. BestFit, FirstFit, RoundRobin & Simple Allocation Policy

* Best Fit VM Allocation Policy

```
SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        4| 0|        4|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   1|        6| 1|        2|      10000|          1|        0|        20|      20
       2|SUCCESS| 1|   1|        6| 2|        1|      10000|          1|       20|        40|      20
       3|SUCCESS| 1|   1|        6| 3|        1|      10000|          1|       20|        40|      20
       4|SUCCESS| 1|   1|        6| 4|        2|      10000|          1|       40|        60|      20
       5|SUCCESS| 1|   2|        8| 5|        2|      10000|          1|       40|        60|      20
       6|SUCCESS| 1|   2|        8| 6|        2|      10000|          1|       60|        80|      20
       7|SUCCESS| 1|   2|        8| 7|        2|      10000|          1|       60|        80|      20
-----------------------------------------------------------------------------------------------------
```

According to the BestFit VM Allocation policy, the VMs will be allocated the host which has highest PE in use. 
As it can be noticed from the logs shown below, 

```
20:18:40.077 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyBestFit: Vm 1 has been allocated to Host 1/DC 1
20:18:40.078 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyBestFit: Vm 2 has been allocated to Host 1/DC 1
20:18:40.078 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyBestFit: Vm 3 has been allocated to Host 1/DC 1
20:18:40.078 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyBestFit: Vm 4 has been allocated to Host 1/DC 1
20:18:40.078 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyBestFit: Vm 5 has been allocated to Host 2/DC 1
20:18:40.079 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyBestFit: Vm 6 has been allocated to Host 2/DC 1
20:18:40.079 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyBestFit: Vm 7 has been allocated to Host 2/DC 1
```

The VMs will be allocated to Host 1 as it has the highest number of PE in use. Once there is not enough PEs left in Host 1 to
allocate new VMs. It will start allocating to Host 2.

* First Fit VM Allocation Policy

```
                                         SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
      ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        4| 0|        4|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   1|        6| 1|        2|      10000|          1|        0|        20|      20
       2|SUCCESS| 1|   1|        6| 2|        1|      10000|          1|       20|        40|      20
       3|SUCCESS| 1|   1|        6| 3|        1|      10000|          1|       20|        40|      20
       4|SUCCESS| 1|   1|        6| 4|        2|      10000|          1|       40|        60|      20
       5|SUCCESS| 1|   2|        8| 5|        2|      10000|          1|       40|        60|      20
       6|SUCCESS| 1|   2|        8| 6|        2|      10000|          1|       60|        80|      20
       7|SUCCESS| 1|   2|        8| 7|        2|      10000|          1|       60|        80|      20
-----------------------------------------------------------------------------------------------------
```

In the first fit allocation policy, the VM will be assigned the first host which has available resources. So for our configuration,
this will act similar to BestFit. The VMs are assigned to Host 1 till its resources are exhausted, then it will assign the VMs to Host 2.
It can be seen in the logs given below. 

```
20:18:40.102 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyFirstFit: Vm 1 has been allocated to Host 1/DC 1
20:18:40.102 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyFirstFit: Vm 2 has been allocated to Host 1/DC 1
20:18:40.103 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyFirstFit: Vm 3 has been allocated to Host 1/DC 1
20:18:40.103 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyFirstFit: Vm 4 has been allocated to Host 1/DC 1
20:18:40.103 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyFirstFit: Vm 5 has been allocated to Host 2/DC 1
20:18:40.103 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyFirstFit: Vm 6 has been allocated to Host 2/DC 1
20:18:40.103 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicyFirstFit: Vm 7 has been allocated to Host 2/DC 1
```

* Round Robin Allocation Policy

```
                                         SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
      ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        4| 0|        4|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   1|        6| 1|        2|      10000|          1|        0|        20|      20
       2|SUCCESS| 1|   2|        8| 2|        1|      10000|          1|       20|        40|      20
       3|SUCCESS| 1|   3|        8| 3|        1|      10000|          1|       20|        40|      20
       4|SUCCESS| 1|   1|        6| 4|        2|      10000|          1|       40|        60|      20
       5|SUCCESS| 1|   2|        8| 5|        2|      10000|          1|       40|        60|      20
       6|SUCCESS| 1|   3|        8| 6|        2|      10000|          1|       60|        80|      20
       7|SUCCESS| 1|   1|        6| 7|        2|      10000|          1|       60|        80|      20
-----------------------------------------------------------------------------------------------------

```
In the Round Robin Allocation Policy, the VM is assigned to Host in sequentially. As it can be seen from logs given below. 
The VM1 are allocated to Host1, then VM2 to Host2, VM3 to Host3 and so on. 
```
20:18:40.119 [main] INFO  DatacenterBroker - 0.10: DatacenterBrokerSimple2: Sending Cloudlet 0 to Vm 0 in Host 0/DC 1.
20:18:40.119 [main] INFO  DatacenterBroker - 0.10: DatacenterBrokerSimple2: Sending Cloudlet 1 to Vm 1 in Host 1/DC 1.
20:18:40.119 [main] INFO  DatacenterBroker - 0.10: DatacenterBrokerSimple2: Sending Cloudlet 2 to Vm 2 in Host 2/DC 1.
20:18:40.119 [main] INFO  DatacenterBroker - 0.10: DatacenterBrokerSimple2: Sending Cloudlet 3 to Vm 3 in Host 3/DC 1.
20:18:40.119 [main] INFO  DatacenterBroker - 0.10: DatacenterBrokerSimple2: Sending Cloudlet 4 to Vm 4 in Host 1/DC 1.
20:18:40.119 [main] INFO  DatacenterBroker - 0.10: DatacenterBrokerSimple2: Sending Cloudlet 5 to Vm 5 in Host 2/DC 1.
20:18:40.119 [main] INFO  DatacenterBroker - 0.10: DatacenterBrokerSimple2: Sending Cloudlet 6 to Vm 6 in Host 3/DC 1.
20:18:40.120 [main] INFO  DatacenterBroker - 0.10: DatacenterBrokerSimple2: Sending Cloudlet 7 to Vm 7 in Host 1/DC 1.
```

* Simple Allocation Policy.

```
                SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
      ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   2|        8| 0|        4|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   3|        8| 1|        2|      10000|          1|        0|        20|      20
       2|SUCCESS| 1|   1|        6| 2|        1|      10000|          1|       20|        40|      20
       3|SUCCESS| 1|   3|        8| 3|        1|      10000|          1|       20|        40|      20
       4|SUCCESS| 1|   1|        6| 4|        2|      10000|          1|       40|        60|      20
       5|SUCCESS| 1|   3|        8| 5|        2|      10000|          1|       40|        60|      20
       6|SUCCESS| 1|   0|        4| 6|        2|      10000|          1|       60|        80|      20
       7|SUCCESS| 1|   2|        8| 7|        2|      10000|          1|       60|        80|      20
-----------------------------------------------------------------------------------------------------
```

In Simple Allocation Policy the VMs are allocated to the Host which has lowest PE in use. This can be noticed in the 
logs given below. The VMs is randomly allocated to the first Host. Then it will look for the host with the lowest PE in use
to get allocated. 

```
20:18:40.130 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicySimple: Vm 1 has been allocated to Host 3/DC 1
20:18:40.130 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicySimple: Vm 2 has been allocated to Host 1/DC 1
20:18:40.130 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicySimple: Vm 3 has been allocated to Host 3/DC 1
20:18:40.130 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicySimple: Vm 4 has been allocated to Host 1/DC 1
20:18:40.130 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicySimple: Vm 5 has been allocated to Host 3/DC 1
20:18:40.130 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicySimple: Vm 6 has been allocated to Host 0/DC 1
20:18:40.130 [main] INFO  VmAllocationPolicy - 0.00: VmAllocationPolicySimple: Vm 7 has been allocated to Host 2/DC 1
```

4. IAAS, PAAS and SAAS Service Model Simulation. 

For creating IAAS, PAAS and SAAS service model we have considered a few assumptions according to service definition are
outlined below,


| Cloud Service Models | IAAS | PAAS | SAAS|
|-------|----------|----------|--------|
|Provider| The Provider has complete control over the Hardware layer and has Admin Control over the Hypervisor. So, we can assume that the Provider has control over Datacenter Configuration, Host Configuration.| The Provider has access over the Hardware, Virtualization. So we can assume that the provider has control over DataCenter, Host & VMs| The Provider has control over the whole environment. So we can assume that the provider has control over DataCenter, Hosts, VMs and Cloudlets|
|Customer|The Customer has control Virtualization and can make request to Hypervisor. So we can assume that Customer has access over VM & Cloudlet and it can specify number of host it needs.|The Customer has control over the application. So we can assume that Customer can specify the Cloudlet characteristics and Number of VMs | The Customer just uses the services. So we can assume that the customer can just make a request for number of cloudlets. |

Following the above given assumption, the created models for the Service Models

* For input configuration please refer this file 
  * [IAAS.conf](/src/main/resources/IaasServiceModel.conf)
  * [PAAS.conf](/src/main/resources/PaasServiceModel.conf)
  * [SAAS.conf](/src/main/resources/SaasServiceModel.conf)
* For the Simulation File please refer to - [Simulation-File](/src/main/scala/Simulations/ServiceModelSimulation.scala)

The Results for the same can be seen below. 

* SAAS Simulation

```

                                         SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
      ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|       20|        40|      20
-----------------------------------------------------------------------------------------------------
21:08:22.056 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 0 is 13.605
21:08:22.056 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 1 is 13.655000000000001
```

* PAAS Simulation

```
                                         SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
      ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|       20|        40|      20
-----------------------------------------------------------------------------------------------------
21:08:22.052 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 0 is 23.61
21:08:22.052 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 1 is 23.71
```

* IAAS Simulation

```
                                         SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
      ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|       20|        40|      20
-----------------------------------------------------------------------------------------------------
21:08:22.047 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 0 is 43.61999999999999
21:08:22.047 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 1 is 43.81999999999999
```

5. DataCenter Simulation

```
SIMULATION RESULTS

Cloudlet|Status |DC|Host|Host PEs |VM|VM PEs   |CloudletLen|CloudletPEs|StartTime|FinishTime|ExecTime
      ID|       |ID|  ID|CPU cores|ID|CPU cores|         MI|  CPU cores|  Seconds|   Seconds| Seconds
-----------------------------------------------------------------------------------------------------
       0|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|        0|        20|      20
       1|SUCCESS| 1|   0|        8| 1|        1|      10000|          1|        0|        20|      20
       2|SUCCESS| 1|   0|        8| 2|        1|      10000|          1|        0|        20|      20
       3|SUCCESS| 1|   0|        8| 0|        1|      10000|          1|       20|        40|      20
       4|SUCCESS| 1|   0|        8| 1|        1|      10000|          1|       20|        40|      20
       5|SUCCESS| 1|   0|        8| 2|        1|      10000|          1|       20|        40|      20
-----------------------------------------------------------------------------------------------------
21:18:02.900 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 0 is 43.61999999999999
21:18:02.900 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 1 is 43.61999999999999
21:18:02.900 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 2 is 43.61999999999999
21:18:02.900 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 3 is 43.81999999999999
21:18:02.900 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 4 is 43.81999999999999
21:18:02.900 [main] INFO  java.lang.Class - Cost of the execution for each Cloudlet 5 is 43.81999999999999
21:18:02.900 [main] INFO  java.lang.Class - Finished cloud simulation...
```

For this simulation we have created three Data Centers. Each datacenter should have IAAS, PAAS and SAAS services. 
All the Datacenters dc1, dc2 & dc3 are connected to each other though brite network topology (`topology.brite`). The Datacenters are mapped to Node 0,1,2 of the brite topology. 
It can be noticed from the simulation result that when a single broker is created, the Host are not assigned to DataCenter 2 & 3. 


## Observation and Analysis

* In Time Scheduling and Space Scheduling Algorithm, the time required for execution of each cloudlet for TimeShared is more than that of SpaceShared. 
* The VM allocation policies will not make a lot of difference in the CPU utilization and cost of the complete execution.
* In the case of multiple datacenters, a single broker will not assign the resources equally between all the DataCenters.








