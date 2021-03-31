# Distributed_Facility_Booking

## Experiments

### 1. Client
The default settings of Retransmitting message to Server:
 * Request Failure Rate: 0
 * Acknowledgement Failure Rate: 0
 * UDP timeout (in second): 5
 * Max counts of UDP timeout: 10
 * Max counts of resend requests: 100

If you want to change the default settings, you can change by:
1. Indicate in command line: `java ClientApp <Request Failure Rate> <Acknowledgement Failure Rate> 
   <UDP timeout (in second)> <Max counts of UDP timeout> <Max counts of resend requests>`
   
2. Change in `Distributed_Facility_Booking/ClientServer/src/config/Constants.java`

### 2. Server
The default settings:
* Invocation Semantics : AT_MOST_ONCE
* Response Failure Rate: 0

If you want to change the default settings, you can change by:
1. Indicate in command line: `java ServerApp <Invocation Semantics(0: AT_MOST_ONCE; 1: AT_LEAST_ONCE)> <Response Failure Rate (<=1)>`
2. Change in `Distributed_Facility_Booking/ClientServer/src/config/Constants.java`

## Summary of Params

Params | Purpose
-------|---------
REQFRATE | Simulate the failure rate when client send requests to server 
ACKFRATE | Simulate the failure rate when client send ACK to server 
TIMEOUT | The tiemout of UDP DatagramSocket, set by `setSoTimeout`  
MAXTIMEOUTCOUNT | # of times for DatagramSocket timeouts 
MAXRESENDS | # of resending requests after client detects message lost 
APPLIEDSEMANTICS | Either AT_MOST_ONCE or AT_LEAST_ONCE enabled by server
RESFRATE | Simulate the failure rate when servers send reponses to client 
