package Simulations

/*
Created by Samihan Nandedkar
CS441
Homework-1
*/

import CreationFiles.CreateResource
import HelperUtils.CreateLogger
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerTimeShared, VmSchedulerSpaceShared}
import org.cloudsimplus.builders.tables.CloudletsTableBuilder

import scala.collection.JavaConverters.*
import org.cloudbus.cloudsim.core.CloudSim

/*

Simulation 1 & 2 -> Time Shared and Space Shared Simulations.

*/

class SchedulingSimulations

object SchedulingSimulations:
  val logger = CreateLogger(classOf[SchedulingSimulations])
  logger.info("Created logger to perform the Time and Space Shared Simulations.")


  // The function to perform the Time Shared Simulations
  def TimeShared() = {
    //Initializa CloudSim
    val cloudsim = new CloudSim();
    // Initialize the CreateResource class to create the all required resources.
    val create = new CreateResource("TimeShared")

    logger.info("Initialized the required classes")

    // Calling the createDataCenter function from CreateResource class to generate and return the DataCenter.
    // The VMSchedulerTimeShared is passed to the funtion
    val dc = create.createDataCenter(cloudsim,new VmSchedulerTimeShared())
    logger.info(s"The Datacenter is generated: $dc")

    // Calling the createVMList funtion from CreateResource class to generate and return tlist of Vms
    // Cloudlet Sheduling is passed to the function.
    val vmList = create.createVMList(new CloudletSchedulerTimeShared)
    logger.info(s"The VMs are generated: $vmList")

    // Creating cloudlets.
    val cloudlets = create.createCloudlets
    logger.info(s"The Cloudlets are generated: $cloudlets")

    //Creating a broker to send the VMs and CLoudlets to the Datacenter.
    val broker0 = new DatacenterBrokerSimple(cloudsim)
    logger.info(s"Generated the broker: $broker0")

    //Send the list of VMs
    broker0.submitVmList(vmList.asJava)
    broker0.submitCloudletList(cloudlets.asJava)
    logger.info("Vm List and CLoudlet list are sent by broker.")

    // Starting the Simulation
    cloudsim.start()
    logger.info("The Simulation has started.")

    // Get the list of completed cloudlets
    val cloudletsDone = broker0.getCloudletFinishedList
    logger.info("The Simulation has completed Successfully.")

    // Build the Simulation Table.
    new CloudletsTableBuilder(cloudletsDone).build()
  }

  def SpaceShared() = {

    //Initializa CloudSim
    val cloudsim = new CloudSim();

    // Initialize the CreateResource class to create the all required resources.
    val create = new CreateResource("SpaceShared")

    logger.info("Initialized the required classes")

    // Calling the createDataCenter function from CreateResource class to generate and return the DataCenter.
    // The VMSchedulerSpaceShared is passed to the funtion
    val dc = create.createDataCenter(cloudsim,new VmSchedulerSpaceShared())
    logger.info(s"The Datacenter is generated: $dc")

    // Calling the createVMList funtion from CreateResource class to generate and return tlist of Vms
    // Cloudlet Sheduling is passed to the function.
    val vmList = create.createVMList(new CloudletSchedulerSpaceShared)
    logger.info(s"The VMs are generated: $vmList")

    // Creating cloudlets.
    val cloudlets = create.createCloudlets
    logger.info(s"The Cloudlets are generated: $cloudlets")

    //Creating a broker to send the VMs and CLoudlets to the Datacenter.
    val broker0 = new DatacenterBrokerSimple(cloudsim)
    logger.info(s"Generated the broker: $broker0")

    //Send the list of VMs
    broker0.submitVmList(vmList.asJava)
    broker0.submitCloudletList(cloudlets.asJava)
    logger.info("Vm List and CLoudlet list are sent by broker.")

    // Starting the Simulation
    cloudsim.start()
    logger.info("The Simulation has started.")

    // Get the list of completed cloudlets
    val cloudletsDone = broker0.getCloudletFinishedList
    logger.info("The Simulation has completed Successfully.")

    // Build the Simulation Table.
    new CloudletsTableBuilder(cloudletsDone).build()
  }


