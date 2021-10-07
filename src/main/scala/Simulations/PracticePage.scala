package Simulations

import CreationFiles.CreateResource
import HelperUtils.{CreateLogger, ObtainConfigReference}
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.CloudletSimple
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.hosts.HostSimple
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.vm.{VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.vms.VmSimple
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared
import scala.collection.JavaConverters.*

class PracticePage
val cloudsim = new CloudSim()
object PracticePage extends App:
  val simulation = simu

  def simu = {
    val create = new CreateResource("TimeShared")
    val dc = create.createDataCenter(cloudsim, new VmSchedulerSpaceShared())
    val vmList = create.createVMList(new CloudletSchedulerSpaceShared)
    println(vmList)
    val cloudlets = create.createCloudlets
//    println(cloudlets)
//    val dc = createDataCenter
//    val vmList = createVM
//    println(vmList)
//    val cloudlets = createCloudlets
    println(cloudlets)
    val broker1 = new DatacenterBrokerSimple(cloudsim)

    broker1.submitVmList(vmList.asJava)
    broker1.submitCloudletList(cloudlets.asJava)
    cloudsim.start()
    val cloudletsDone = broker1.getCloudletFinishedList
    println("Simulation done")
    new CloudletsTableBuilder(cloudletsDone).build()

  }
  def createVM = {
    (1 to 2).map(x => new VmSimple(1000, 4, new CloudletSchedulerSpaceShared()).setRam(512).setBw(1000).setSize(10000)).toList
  }

  def createCloudlets = {
    val utilizationmodel = new UtilizationModelDynamic(0.25)
    (1 to 2).map (x => new CloudletSimple(10000,2,utilizationmodel)).toList
  }

  def createDataCenter = {
    val hostlist = (1 to 1).map(x => createHost).toList
    new DatacenterSimple(cloudsim, hostlist.asJava, new VmAllocationPolicySimple())
  }

  def createHost = {
    val peList = (1 to 8).map (x => new PeSimple(1000).asInstanceOf[Pe]).toList
    val host_ram: Long = 2048
    val host_bw: Long = 10000
    val storage: Long = 100000
    new HostSimple(host_ram, host_bw, storage, peList.asJava).setVmScheduler(new VmSchedulerSpaceShared())
  }




