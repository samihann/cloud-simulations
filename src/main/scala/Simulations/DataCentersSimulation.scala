package Simulations

/*
Created by Samihan Nandedkar
CS441
Homework-1
*/

import CreationFiles.{IaasServiceModel, PaasServiceModel, SaasServiceModel}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.network.topologies.{BriteNetworkTopology, NetworkTopology}
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import CreationFiles.ExecutionCost
import HelperUtils.CreateLogger

import java.util
import scala.collection.JavaConverters.*

/*

Simulation 7 -> DataCenter Simulation.

*/

class DataCentersSimulation

object DataCentersSimulation extends App:

  val logger = CreateLogger(classOf[DataCentersSimulation])


  // Initialize DataCenter.
  val cloudSim = new CloudSim()
  logger.info("Initialize the CloudSim")


  // Initilize Iaas, Paas, Saas Service Model.
  val iaasmodel = new IaasServiceModel("IaasServiceModel")
  val paasmodel = new PaasServiceModel("PaasServiceModel")
  val saasmodel = new SaasServiceModel("SaasServiceModel")
  logger.info("Initialized the required classes")

  // Create 3 data centers.
  val dc1 = iaasmodel.createDataCenter(cloudSim)
  val dc2 = paasmodel.createDataCenter(cloudSim)
  val dc3 = saasmodel.createDataCenter(cloudSim)
  logger.info("Genreated the required DataCenters.")

  // Create a master broker to manage the allocation of resources on the data centers.
  val masterBroker = new DatacenterBrokerSimple(cloudSim)
  logger.info("Genreated the required DataCenters.")


  // Define a network topology connect the 3 data centers.
  val networkTopology = BriteNetworkTopology("topology.brite")
  cloudSim.setNetworkTopology(networkTopology)
  logger.info("Set the network topology to connect the datacenters.")


  // map the DataCenters dc1, dc2 & dc3 to three nodes of the network topology
  networkTopology.mapNode(dc1, 0)
  networkTopology.mapNode(dc2, 1)
  networkTopology.mapNode(dc3, 2)
  logger.info("Map the datacenters to three nodes of the topology")


  // create a list of Iaas, Paas & Saas resources.
  val iaasvm = iaasmodel.createVMList
  val paasvm = paasmodel.createVMList
  val saasvm = saasmodel.createVMList
  logger.info("Genreated the required Vm")


  val iaascloudlet = iaasmodel.createCloudlets
  val paascloudlet = paasmodel.createCloudlets
  val saascloudlet = saasmodel.createCloudlets
  logger.info("Genreated the required Cloudlets")


  // Combine all the resource for the broker to allocate them on the different data centers.
  val vmlist = List.concat(iaasvm,paasvm,saasvm)
  val cloudletList = List.concat(iaascloudlet, paascloudlet, saascloudlet)
  logger.info("Combine all the resource for the broker to allocate them on the different data centers.")

  // Submit the resources to the Cloud Model.
  masterBroker.submitVmList(vmlist.asJava)
  masterBroker.submitCloudletList(cloudletList.asJava)
  logger.info("Submit the resources to the Cloud Model. ")


  // Simulation Started.
  cloudSim.start()
  logger.info("Simulation Started")


  val finishedCloudlets: util.List[Cloudlet] = masterBroker.getCloudletFinishedList

  // Converting the java list to scala list for further processing.
  val cloudletsDone: List[Cloudlet] = finishedCloudlets.asScala.toList
  logger.info("SImulation COmpleted Successfully ")

  new CloudletsTableBuilder(finishedCloudlets).build()

  // Execution cost of the each cloudlet.
  ExecutionCost.costofExecution(cloudletsDone)







