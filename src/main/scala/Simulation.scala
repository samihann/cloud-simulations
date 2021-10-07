import HelperUtils.{CreateLogger, ObtainConfigReference}
import Simulations.{AllocationPolicySimulation, SchedulingSimulations}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicyBestFit, VmAllocationPolicyFirstFit, VmAllocationPolicyRoundRobin, VmAllocationPolicySimple}
import com.typesafe.config.ConfigFactory
import Simulations.ServiceModelSimulation
import Simulations.DataCentersSimulation
import org.slf4j.LoggerFactory

object Simulation:
  val logger = CreateLogger(classOf[Simulation])

  @main def runSimulation =
    logger.info("Strating all the Simulations")

    logger.info("Starting the Time Shared Simulation for the configuration given in TimeShared.conf")
    SchedulingSimulations.TimeShared()

    println("\n\n\n")
    println("Simulation 2")
    println("\n\n\n")

    logger.info("Starting the Space Shared Simulation for the configuration given in TimeShared.conf")
    SchedulingSimulations.SpaceShared()

    println("\n\n\n")
    println("Simulation 3")
    println("\n\n\n")

    logger.info("Started the Simulation for Best Fit Allocation Policy")
    AllocationPolicySimulation.sim(new VmAllocationPolicyBestFit)

    println("\n\n\n")
    println("Simulation 4")
    println("\n\n\n")

    logger.info("Started the Simulation for First Fit Allocation Policy")
    AllocationPolicySimulation.sim(new VmAllocationPolicyFirstFit)

    println("\n\n\n")
    println("Simulation 5")
    println("\n\n\n")

    logger.info("Started the Simulation for Round Robin Allocation Policy")
    AllocationPolicySimulation.sim(new VmAllocationPolicyRoundRobin)

    println("\n\n\n")
    println("Simulation 6")
    println("\n\n\n")

    logger.info("Started the Simulation for Simple Allocation Policy")
    AllocationPolicySimulation.sim(new VmAllocationPolicySimple)


    println("\n\n\n")
    println("Simulation 7")
    println("\n\n\n")

    logger.info("Started the Simulation for IAAS")
    ServiceModelSimulation.IaasServiceModelSim

    println("\n")

    logger.info("Started the Simulation for IAAS")
    ServiceModelSimulation.PaasServiceModelSim

    println("\n")

    logger.info("Started the Simulation for IAAS")
    ServiceModelSimulation.SaasServiceModelSim

    println("\n\n\n")
    println("Simulation 8")
    println("\n\n\n")

    logger.info("Started the Simulation for 3 DataCenter")
    DataCentersSimulation



    logger.info("Finished cloud simulation...")

class Simulation