package Simulations

/*
Created by Samihan Nandedkar
CS441
Homework-1
*/

import org.cloudbus.cloudsim.core.CloudSim
import CreationFiles.{CreateResource, ExecutionCost, IaasServiceModel, PaasServiceModel, SaasServiceModel}
import HelperUtils.CreateLogger
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudsimplus.builders.tables.CloudletsTableBuilder

import java.util
import scala.collection.JavaConverters.*

/*

Simulation 6 -> IAAS, PAAS & SAAS Service Model simulation

  */

class ServiceModelSimulation

object ServiceModelSimulation extends App:
  val cloudSim = new CloudSim()
  val logger = CreateLogger(classOf[ServiceModelSimulation])

  // The simulation of IaasServiceModel.
  def IaasServiceModelSim: Unit = {
    val cloudSim = new CloudSim()
    // Initializa IaasServiceModel class by passing the config file name as parameter.
    val iaasmodel = new IaasServiceModel("IaasServiceModel")
    // the Configuration parameters managed by Provider and Customer are divided and mentioned in Config file.

    logger.info("Initialized the required classes")

    // create Data center
    val dc1 = iaasmodel.createDataCenter(cloudSim)
    logger.info(s"The Datacenter is generated: $dc1")

    val broker = new DatacenterBrokerSimple(cloudSim)

    val iaasvm = iaasmodel.createVMList
    logger.info(s"The VMs are generated: $iaasvm")

    broker.submitVmList(iaasvm.asJava)
    logger.info("VM submitted by the broker.")

    val iaascloudlet = iaasmodel.createCloudlets
    logger.info(s"The Cloudlets are generated: $iaascloudlet")

    broker.submitCloudletList(iaascloudlet.asJava)
    logger.info("Cloudlets submitted by the broker.")

    cloudSim.start()
    logger.info("Simulation Started.")

    // Get the list of completed cloudlets.
    val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList
    logger.info("The Simulation has completed Successfully.")


    // As java list is reveived by .getCloudletFinishedList, converting it to a scala list before further processing.
    val cloudletsDone: List[Cloudlet] = finishedCloudlets.asScala.toList

    new CloudletsTableBuilder(finishedCloudlets).build()

    // printing the cost of execution for each cloudlet.
    ExecutionCost.costofExecution(cloudletsDone)

  }

  def SaasServiceModelSim: Unit ={
    val cloudSim = new CloudSim()

    // Initializa SaasServiceModel class by passing the config file name as parameter.
    val saasmodel = new SaasServiceModel("SaasServiceModel")

    //Create Datacenter
    val dc1 = saasmodel.createDataCenter(cloudSim)
    logger.info(s"The Datacenter is generated: $dc1")

    val broker = new DatacenterBrokerSimple(cloudSim)
    logger.info(s"Generated the broker: $broker")


    val saasvm = saasmodel.createVMList
    logger.info(s"The VMs are generated: $saasvm")

    broker.submitVmList(saasvm.asJava)

    val saascloudlet = saasmodel.createCloudlets
    logger.info(s"The Cloudlets are generated: $saascloudlet")

    broker.submitCloudletList(saascloudlet.asJava)

    cloudSim.start()
    logger.info("The Simulation has started.")

    val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList
    logger.info("The Simulation has completed Successfully.")

    val cloudletsDone: List[Cloudlet] = finishedCloudlets.asScala.toList

    new CloudletsTableBuilder(finishedCloudlets).build()

    // printing the cost of execution for each cloudlet.
    ExecutionCost.costofExecution(cloudletsDone)
  }

  def PaasServiceModelSim: Unit ={
    val cloudSim = new CloudSim()

    // Initializa PaasServiceModel class by passing the config file name as parameter.
    val paasmodel = new PaasServiceModel("PaasServiceModel")

    val dc1 = paasmodel.createDataCenter(cloudSim)
    logger.info(s"The Datacenter is generated: $dc1")

    val broker = new DatacenterBrokerSimple(cloudSim)
    logger.info(s"Generated the broker: $broker")

    val paasvm = paasmodel.createVMList
    logger.info(s"The VMs are generated: $paasvm")

    broker.submitVmList(paasvm.asJava)

    val paascloudlet = paasmodel.createCloudlets
    logger.info(s"The Cloudlets are generated: $paascloudlet")

    broker.submitCloudletList(paascloudlet.asJava)

    cloudSim.start()
    logger.info("The Simulation has started.")

    val finishedCloudlets: util.List[Cloudlet] = broker.getCloudletFinishedList
    logger.info("The Simulation has completed Successfully.")

    val cloudletsDone: List[Cloudlet] = finishedCloudlets.asScala.toList

    new CloudletsTableBuilder(finishedCloudlets).build()

    // printing the cost of execution for each cloudlet.
    ExecutionCost.costofExecution(cloudletsDone)
  }