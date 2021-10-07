package Simulations

import org.scalatest.funsuite.AnyFunSuite
import org.cloudbus.cloudsim.core.CloudSim
import CreationFiles.CreateResource
import CreationFiles.{IaasServiceModel, PaasServiceModel, SaasServiceModel}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.datacenters.Datacenter
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerTimeShared
import org.cloudbus.cloudsim.schedulers.vm.VmSchedulerTimeShared
import org.cloudbus.cloudsim.vms.Vm

import scala.collection.JavaConverters.*


class SimulationTestCases extends AnyFunSuite {

  test("Test to check if cloudsim gets initiated") {
    val cloudSim = new CloudSim()
    assert(cloudSim.isInstanceOf[CloudSim])
  }

  test ("Test to check if the DataCenter are getting created by CreateResource class"){
    val cloudSim = new CloudSim()
    val create = CreateResource("TimeShared")
    val datacenter = create.createDataCenter(cloudSim)
    assert(datacenter.isInstanceOf[Datacenter])

  }

  test("Test to check if VmList is getting created by CreateResource Class"){
    val create = new CreateResource("TimeShared")
    val vmList = create.createVMList
    vmList.map (x => assert(x.isInstanceOf[Vm]))
  }

  test("Test to Check of CloudletList is is getting generated by CreateResource Class"){
    val create = new CreateResource("TimeShared")
    val cloudletlist = create.createCloudlets
    assert(cloudletlist.isInstanceOf[List[Cloudlet]])
  }

  test("Test to check if IAAS model is being created"){
    val simName = new CloudSim()
    val iaasmodel = IaasServiceModel("IaasServiceModel")
    val iaasdc = iaasmodel.createDataCenter(simName)
    val iaasvmList = iaasmodel.createVMList
    val iaascloudletList = iaasmodel.createCloudlets

    assert(iaasdc.isInstanceOf[Datacenter])
    iaasvmList.map(i => assert(i.isInstanceOf[Vm]))
    iaascloudletList.map(i => assert(i.isInstanceOf[Cloudlet]))
  }

  test("Test to check if PAAS model is being created"){
    val simName = new CloudSim()
    val paasmodel = PaasServiceModel("PaasServiceModel")
    val paasdc = paasmodel.createDataCenter(simName)
    val paasvmList = paasmodel.createVMList
    val paascloudletList = paasmodel.createCloudlets

    assert(paasdc.isInstanceOf[Datacenter])
    paasvmList.map(i => assert(i.isInstanceOf[Vm]))
    paascloudletList.map(i => assert(i.isInstanceOf[Cloudlet]))
  }

  test("Test to check if SAAS model is being created"){
    val simName = new CloudSim()
    val saasmodel = SaasServiceModel("SaasServiceModel")
    val saasdc = saasmodel.createDataCenter(simName)
    val saasvmList = saasmodel.createVMList
    val saascloudletList = saasmodel.createCloudlets

    assert(saasdc.isInstanceOf[Datacenter])
    saasvmList.map(i => assert(i.isInstanceOf[Vm]))
    saascloudletList.map(i => assert(i.isInstanceOf[Cloudlet]))
  }

  test("Test to check if Simulation is running at runtime"){
    val cloudSim = new CloudSim()
    val create = new CreateResource("TimeShared")
    val dc = create.createDataCenter(cloudsim,new VmSchedulerTimeShared())
    val vmList = create.createVMList(new CloudletSchedulerTimeShared)
    val cloudlets = create.createCloudlets
    val broker0 = new DatacenterBrokerSimple(cloudsim)
    broker0.submitVmList(vmList.asJava)
    broker0.submitCloudletList(cloudlets.asJava)
    cloudsim.start()
    val cloudletsDone = broker0.getCloudletFinishedList

    assert(cloudSim.isRunning == false)

    assert(cloudlets.size == cloudletsDone.size())

  }


}
