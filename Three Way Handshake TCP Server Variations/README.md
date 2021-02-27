# TCP Client & Server Implementation

Three variations of TCP Socket server implementations for demonstrating the Socket API in C.

Server responds to clients' message by incrementing the value by 1.  Client receives and responds by incrementing the value by 1 as well.  Both client and server verify the values and respond accordingly.

1. tcpserver.c - single-client socket
2. multi-tcpserver.c - Multi-threaded TCP socket server for multiple clients
3. event-driven TCP socket server for multiple clients