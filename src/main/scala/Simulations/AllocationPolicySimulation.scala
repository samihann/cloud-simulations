package Simulations

/*
Created by Samihan Nandedkar
CS441
Homework-1
*/
import CreationFiles.CreateResource
import HelperUtils.CreateLogger
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicy, VmAllocationPolicyBestFit, VmAllocationPolicyFirstFit, VmAllocationPolicyRoundRobin, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudbus.cloudsim.core.CloudSim

import scala.collection.JavaConverters.*

/*

Simulation 3, 4 & 5 -> BEST Fit, First Fit & RoundRobin Simulations.

*/

class AllocationPolicySimulation

object AllocationPolicySimulation extends App:
  val logger = CreateLogger(classOf[AllocationPolicySimulation])
  logger.info("Logger for different Allocation Policy Simulation is created.")

  def sim(vmAllocationPolicy: VmAllocationPolicy) = {
    logger.info(s"Starting the Simulation for the following Allocation Policy: $vmAllocationPolicy")

    //Initializa CloudSim
    val cloudsim = new CloudSim();
    val create = new CreateResource("AllocationPolicy")
    logger.info("Initialized the required classes")

    // Create DataCenter
    val dc = create.createDataCenter(cloudsim,vmAllocationPolicy)
    logger.info(s"The Datacenter is generated: $dc")

    // Create VMs
    val vmList = create.createVMList
    logger.info(s"The VMs are generated: $vmList")

    // Create Cloudlets.
    val cloudlets = create.createCloudlets
    logger.info(s"The Cloudlets are generated: $cloudlets")

    // Create broker
    val broker0 = new DatacenterBrokerSimple(cloudsim)
    logger.info(s"Generated the broker: $broker0")


    broker0.submitVmList(vmList.asJava)
    broker0.submitCloudletList(cloudlets.asJava)
    logger.info("Vm List and CLoudlet list are sent by broker.")


    cloudsim.start()
    logger.info("The Simulation has started.")

    val cloudletsDone = broker0.getCloudletFinishedList
    println("Simulation Completed Successfully")
    new CloudletsTableBuilder(cloudletsDone).build()
  }







