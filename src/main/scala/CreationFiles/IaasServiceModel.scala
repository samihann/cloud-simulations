package CreationFiles

/*
Created by Samihan Nandedkar
CS441
Homework-1
*/

import HelperUtils.{CreateLogger, ObtainConfigReference}
import com.typesafe.config.{Config, ConfigFactory}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicy, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.resources.PeSimple
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletScheduler, CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm.{VmScheduler, VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModelDynamic, UtilizationModelFull}
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}

import scala.collection.JavaConverters.*

/*
This is a reusible class with the to create all the required resources
for the for Iaas Service Simulation.

Each simulation will be passing the Config name as an parameter to this class.

In SAAS the Customer will only have access over the number of cloudlets and VMs and their parameters.
All the rest of the parameters are configured by Provider.

*/

class IaasServiceModel(var simName: String) {
  //Created a logger for this class.
  val logger = CreateLogger(classOf[IaasServiceModel])
  logger.info("Generated instance of IaasServiceModel class to create the required resources. ")

  // Load the Config file from thr parameters sent to the class.
  val config: Config = ConfigFactory.load(simName)

  /*
  The config are divided by the characteristics and parameters that provider and customer have control over. 
  */
  val provider = simName+".Provider"
  val customer = simName+".Customer"


  // Funtion to be called to create PE list which can be passed to the createHost funtion to create a hostList.
  // The function takes a host index as index and will return a list of Pesimple

  def createPeList(hostnum: Int): List[PeSimple] = {
    logger.info("Staring the PeList resource creation Function.")

    // For IAAS, the provider will control over the following parameter
    val peCount = config.getInt(provider+".host"+hostnum+".numberOfPE")
    val peList = (1 to peCount).map(x => new PeSimple(config.getDouble(provider+".host"+hostnum+".mips"))).toList
    
    logger.info(s"Create PeList function completed for host number  $hostnum")
    return peList
  }

  // The funtion will be called to return the list of Host for the datacenter to be created.
  // It is taking VmScheduler algorithm as input and will return list of Hosts
  def createHostList(vmScheduler: VmScheduler): List[Host] ={
    
    // For SAAS the provider will have access over this configuration
    val hostCount = config.getInt(customer+".dataCenter"+".hostCount")
    val hostList = (1 to hostCount).map(x => createHost(x, vmScheduler)).toList
    return hostList
  }
  // The function is called to create a host with the required parameter and return it.
  // It will take host index and VmScheduling algorithm as input and will return a Host
  def createHost(hostnum: Int, vmScheduler: VmScheduler): Host ={
    logger.info("Staring the Host resource creation Function.")

    /*
    
    For IAAS, Cloud Provider will have access to this configuration
        Hence using provider to fetch the configuration. 

     */

    // Calling createPeList funtion to return a list PeSimple
    val peList = createPeList(hostnum)
    
    // Retrive all the required parameters from the config file.
    val host_ram = config.getLong(provider+".host"+hostnum+".ram")
    val host_storage = config.getLong(provider+".host"+hostnum+".storage")
    val host_bw = config.getLong(provider+".host"+hostnum+".bw")

    // Each Vm Scheduler instance will be associated to a particular Host.
    // So creating new intance of vmScheduler for each host created

    val vm_Scheduler: VmScheduler = vmScheduler.getClass.newInstance()
    val host = new HostSimple(host_ram,host_bw,host_storage,peList.asJava).setVmScheduler(vm_Scheduler)
    logger.info(s"The host number $hostnum is created")

    return host
  }

  // the function is called to create a DataCenter and return the same.
  // It is taking CloudSim, vmSchduling algorithm, vm Allocation policay as input and will return of type DataCenterSimple.
  def createDataCenter(cloudsim: CloudSim, vmScheduler: VmScheduler, vmAllocationPolicy: VmAllocationPolicy): DatacenterSimple ={
    logger.info("Staring the DataCenter resource creation Function.")
    // Calling createHostList funtion to create list of hosts for the datacenter
    val hostList = createHostList(vmScheduler)

    /*
   
   For IAAS, Cloud Provider will have access to this configuration
       Hence using provider to fetch the configuration. 

    */
    val cps = config.getDouble(provider+".dataCenter.costPerSecond")
    val cpm = config.getDouble(provider+".dataCenter.costPerMem")
    val cpst = config.getDouble(provider+".dataCenter.costPerStorage")
    val cpbw = config.getDouble(provider+".dataCenter.costPerBw")
    val dc = new DatacenterSimple(cloudsim, hostList.asJava,vmAllocationPolicy )

    dc.getCharacteristics()
      .setCostPerSecond(cps)
      .setCostPerMem(cpm)
      .setCostPerStorage(cpst)
      .setCostPerBw(cpbw);
    return dc
  }

  def createDataCenter(cloudSim: CloudSim, vmScheduler: VmScheduler): DatacenterSimple ={
    createDataCenter(cloudSim,vmScheduler,new VmAllocationPolicySimple())
  }

  def createDataCenter(cloudSim: CloudSim,vmAllocationPolicy: VmAllocationPolicy): DatacenterSimple ={
    createDataCenter(cloudSim,new VmSchedulerSpaceShared(),vmAllocationPolicy)
  }

  def createDataCenter(cloudSim: CloudSim): DatacenterSimple ={
    createDataCenter(cloudSim,new VmSchedulerSpaceShared(),new VmAllocationPolicySimple())
  }

  // Create VM List
  // It takes Cloudlet Scheduler as input parameter. It returns List of Vm

  def createVMList(cloudletScheduler: CloudletScheduler): List[Vm] ={
    logger.info("Starting the Vm List Creation Function")

    /*

   For IAAS, Customer will have access to this configuration
       Hence using customer to fetch the configuration.

    */
    val vmCount = config.getInt(customer+".dataCenter"+".vmCount")
    val vmList = (1 to vmCount).map(x => createVm(x, cloudletScheduler)).toList
    logger.info("Vm List creation function is created")
    return vmList
  }

  // Create VM list
  // Taking CloudletSchedulerSpaceShared as default when not mentioned.
  def createVMList: List[Vm] ={
    createVMList(new CloudletSchedulerSpaceShared)
  }

  // Create a VM
  // Taking Vm index & Cloudlet Scheduler as input
  def createVm(vmNum: Int, cloudletScheduler: CloudletScheduler):Vm = {
    /*

   For IAAS, Customer will have access to this configuration
       Hence using customer to fetch the configuration.

    */
    val vm_mips = config.getDouble(customer+".vm"+vmNum+".mips")
    val vm_pe = config.getLong(customer+".vm"+vmNum+".numberOfPE")
    val vm_ram = config.getLong(customer+".vm"+vmNum+".ram")
    val vm_bw = config.getLong(customer+".vm"+vmNum+".bw")
    val vm_size = config.getLong(customer+".vm"+vmNum+".size")

    // Create VM after passing all the parameters from the VmSimple.
    val vm = VmSimple(vm_mips,vm_pe,cloudletScheduler).setRam(vm_ram)
      .setBw(vm_bw).setSize(vm_size)
    logger.info(s"THe VM number is created $vmNum")
    return vm
  }

  // Create list of Cloudlets.
  // It returns the list of Cloudet.
  def createCloudlets: List[Cloudlet] ={
    /*

   For IAAS, Customer will have access to this configuration
       Hence using customer to fetch the configuration.

    */
    // Retrive all the parameters from the config file.
    val utilizationRatio = config.getDouble(customer+".cloudlet.utilizationRatio")
    val cloudlet_pe = config.getInt(customer+".cloudlet.numberOfPE")
    val length = config.getInt(customer+".cloudlet.length")
    val cloudletCount = config.getInt(customer+".dataCenter.cloudletCount")

    // Create a utilization model from the given UtilizationModelDynamic and utilizationRatio
    val utilizationmodel = new UtilizationModelDynamic(utilizationRatio)

    val cloudletList = (1 to cloudletCount).map(x => new CloudletSimple(length, cloudlet_pe, utilizationmodel).setSizes(config.getInt(customer+".cloudlet.size"))).toList

    logger.info("The Cloudlet List creation function is completed.")

    return cloudletList
  }
}
