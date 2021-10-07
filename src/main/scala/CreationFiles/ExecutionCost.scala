package CreationFiles

/*
Created by Samihan Nandedkar
CS441
Homework-1
*/

import HelperUtils.CreateLogger
import org.cloudbus.cloudsim.cloudlets.Cloudlet

/*
* This Function will calculate the total cost of the execution for each of the cloudlet executed on the Cloud resource.
* */
class ExecutionCost
object ExecutionCost{
  val logger = CreateLogger(classOf[ExecutionCost])
  def costofExecution(cloudlets: List[Cloudlet]): Unit ={
    cloudlets.map(i => {
      // The get the ID for each Cloudlet
      val id = i.getId
      // Get the total cost for each cloudlet.
      val cost = i.getTotalCost
      logger.info(s"Cost of the execution for each Cloudlet $id is $cost")
    })
  }}