hydra.Prms-testRequirement = "Test CacheSnapshot (load/save) oprations with many regions";
hydra.Prms-testDescription = "With one controller vms and a configured number of peers hosting a variety of regions: do random ops, pause, write the map of region snapshots to the blackboard, clear all members (ShutDownAll or Recycle VMs), restart and recreate regions on restart.  Import entire cache and verify all regions.";

INCLUDE $JTESTS/hydraconfig/hydraparams1.inc;
INCLUDE $JTESTS/hydraconfig/topology_2.inc;

// all topology groups are peers
hydra.GemFirePrms-distributedSystem = ds;

THREADGROUP controllerThread
    totalThreads = 1
    clientNames  = fcn "hydra.TestConfigFcns.generateNames
                        (\"${A}\", ${${A}Hosts}, true)"
                   ncf;
THREADGROUP workThreads
    totalThreads = fcn
                   ${${B}Hosts} * ${${B}VMsPerHost} * ${${B}ThreadsPerVM}
                   ncf
    clientNames  = fcn "hydra.TestConfigFcns.generateNames
                        (\"${B}\", ${${B}Hosts}, true)"
                   ncf;

INITTASK     taskClass   = util.StopStartVMs  taskMethod = StopStart_initTask;

INITTASK     taskClass   = diskRecovery.RecoveryTestVersionHelper  taskMethod = removeDiskFiles
             runMode = dynamic;

INITTASK     taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_multiRegionInitialize
             threadGroups = workThreads
             runMode = always;

// the controller thread is also an admin vm (bring up other vms first)
INITTASK     taskClass     = util.AdminHelper  taskMethod = HydraTask_initializeAdminDS
             threadGroups = controllerThread;
  
INITTASK     taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_load
             batch
             threadGroups = workThreads;

INITTASK     taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_resetUniqueKeyIndex
             threadGroups = workThreads;

INITTASK     taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_createXmlFile
             threadGroups = workThreads
             runMode = once;
  
// dynamic init tasks to run only when bringing back a vm
INITTASK     taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_importAndVerify
             threadGroups = workThreads
             runMode = dynamic;
  
TASK         taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_snapshotController
             threadGroups = controllerThread; 

TASK         taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_snapshotResponder
             threadGroups = workThreads; 

hydra.Prms-totalTaskTimeSec = 31536000;  // don't let hydra terminate based on time
diskRecovery.RecoveryPrms-secondsToRun = 1200;
hydra.Prms-maxResultWaitSec = 1200;

INCLUDE $JTESTS/util/randomValues.inc;
util.RandomValuesPrms-objectType = byte[];
util.RandomValuesPrms-elementSize = RANGE 1 50 EGNAR;
util.ValueHolderPrms-useExtraObject = true;
hydra.GemFirePrms-conserveSockets = true;

hydra.CachePrms-names           = cache1;
hydra.CachePrms-searchTimeout   = 600;

util.TestHelperPrms-minTaskGranularitySec = 30;

//hydra.DiskStorePrms-names = ds1 ds2 .... dsN;
// note the notUsed diskStoreName is used to fool hydra; diskStores are specified programmatically in the test code 
hydra.DiskStorePrms-names = notUsed fcn "hydra.TestConfigFcns.generateNames(\"ds\", \"${numDiskStores}\", false)" ncf;
hydra.DiskStorePrms-autoCompact          = true;
hydra.DiskStorePrms-allowForceCompaction = ONEOF true false FOENO;
hydra.DiskStorePrms-compactionThreshold  = RANGE 0 100 EGNAR;
hydra.DiskStorePrms-diskDirNum           = RANGE 1 10 EGNAR;
hydra.DiskStorePrms-diskDirSizes         = RANGE 35 50 EGNAR;
hydra.DiskStorePrms-maxOplogSize         = RANGE 4 10 EGNAR;
hydra.DiskStorePrms-queueSize            = RANGE 1 1000 EGNAR;
hydra.DiskStorePrms-timeInterval         = RANGE 1 3000 EGNAR;
hydra.DiskStorePrms-writeBufferSize      = RANGE 1000 50000 EGNAR;

diskRecovery.RecoveryPrms-operations = ONEOF add add destroy destroy invalidate invalidate update update get get getNew getNew putAll cacheOperations cacheOperations FOENO;
diskRecovery.RecoveryPrms-lowerThreshold = 10;
diskRecovery.RecoveryPrms-upperThreshold = 200;
diskRecovery.RecoveryPrms-lowerThresholdOperations = ONEOF add putAll cacheOperations FOENO;
diskRecovery.RecoveryPrms-upperThresholdOperations = ONEOF destroy cacheOperations FOENO;
diskRecovery.RecoveryPrms-numPutAllNewKeys = RANGE 1 10 EGNAR;
diskRecovery.RecoveryPrms-numPutAllExistingKeys = RANGE 1 10 EGNAR;
diskRecovery.RecoveryPrms-useUniqueKeys = true;
// don't recover from disk (disk files removed in dynamic inittask)
diskRecovery.RecoveryPrms-createRegionsWithXml = false;

util.StopStartPrms-stopModes = ONEOF nice_exit nice_kill mean_exit mean_kill FOENO;

snapshot.SnapshotPrms-useFilterOnExport = ${useFilterOnExport};
snapshot.SnapshotPrms-useFilterOnImport = ${useFilterOnImport};
snapshot.SnapshotPrms-numFilterObjects = 100;  // per Region

CLOSETASK    taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_waitForMyStartupRecovery
             threadGroups = workThreads;

CLOSETASK    taskClass   = diskRecovery.RecoveryTest  taskMethod = HydraTask_verifyPRs
             threadGroups = workThreads;

diskRecovery.RecoveryPrms-useShutDownAll = ONEOF true false FOENO;

