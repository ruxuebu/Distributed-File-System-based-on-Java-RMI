RMI Based Simple Distributed File System
===============================

Implementation of a simple distribute file system that mimics [Google File System](http://en.wikipedia.org/wiki/Google_File_System) based on Java RMI.

Problem Statement
----------------------------
It is required to implement a replicated file system. There will be one main server (master) and, data will be partitioned and replicated on multiple replicaServers. This file system allows its concurrent users to perform transactions, while guaranteeing ACID properties.

# Design Assumptions #
- Master server never transfers data.
- Create new file operation is atomic.
- All files of active transactions can fit in memory of replica servers.
- Files are small that the can be returned as one bulk in respond to read requests.
- File chunks have fixed size defined in Configurations class.
- Writes must be performed through the primiary replica of the file.


# System Design #
## Master Server ##
+ Keeps track of replica servers addresses and state (up/down) .
+ Saves meta-data about files (mapping between filenames and primary replica server responsible for that file, mapping between filenames and replica servers hosting a copy of that file).
+ Reguralry sends Hearbeat messages to replica servesr to check their state.
+ Grants r/w request from clients, on approval replies with:
	+ readAck < List of replicas having the files >
	+ writeAck < primary replica location, timeStamp, transaction ID >

## Replica Server ##
+ Keeps track of files that it is their primary replica, also keeps the location of other replica servers having those files.
+ Makes sure that the data of active transations are not reflected in the system until the commit is called.

## Client ##
+ Contacts the master for any approving r/w requests.
+ If master approves communicates with replica server(s) to perform the transaction.

# Read / Write Flow #
Read
----------
> 
1. Client contacts the master server requesting read operation with file name.
2. Master replies with a list of all replica servers having a copy of the file.
3. Client contact one or more replica servers asking for the file.
4. If the file is currently being updatet (write lock) the call at the Replica server blocks untile the update operation if completed.
5. Replica server sends the file to the client on bulk.


Write
--------
> 
1. Client contacts the master server requesting write operation with file name.
2. If the file is not found in system, Master randomly selects 3 lucky replicas to save the new file and assigns one of them as the primary replica for the new file.
	1. Master initiates a CreateNewFile opearion at each of the lucky replicas.
	2. Master informs the primary replica of the file to take charge of  that file.
3. Master replies to the client with a write acknowledgement containing primary replica location, time stamp, transaction ID.
4. Client initiates a write operation with the primary replica using the transaction ID obtained from the writeAck.
5. Clients sends chunks of the file tagged with a sequence number for every message.
6. For every chunk received at the primary replica, the server replies with a chunk ack. contanting the same seq. number.
7. The primary replica saves the received chunks in memory until a commit operaition is executed.

Commit
-----------
> 
1. Client request commit from the primary replica server of the file using the transaction ID obtained in the writeAck message.
2. The primary replica initates a reflect update operation at slave servesr (servers having copies of the same file).
3. The client send the file content to all slave servers.
4. Slave servers locks the file and write the file content on disk.
5. Primary replica server flushed the file to the disk after obtaining appropriate locks.
6. Primary replica informs the slave replicas that the commit operation is done so that they can release locks obtained in step 4.


# Communication Between Entities #
##### Master →  Replica Server #####
```java
public void createFile(String fileName)
public void takeCharge(String fileName, List<ReplicaLoc> slaveReplicas)
public boolean isAlive()
```
##### Replica Sever → Replica Server #####
```java
public boolean reflectUpdate(long txnID, String fileName, ArrayList<byte[]> data)
public void releaseLock(String fileName)
```
##### Client → Master #####
```java
public List<ReplicaLoc> read(String fileName)
public WriteAck write(String fileName)
public ReplicaLoc locatePrimaryReplica(String fileName)
```

##### Client → Replica Server #####
```java
public FileContent read(String fileName)
public ChunkAck write(long txnID, long msgSeqNum, FileContent data)
public boolean commit(long txnID, long numOfMsgs)
public boolean abort(long txnID)
```