#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <sys/socket.h>
#include <netinet/ip.h>
#include <arpa/inet.h>

#define MAX_PENDING 10
#define MAX_LINE 20

#define DEBUG 0
#if( DEBUG )
	#define DBG(_x)	((void)(_x))
#else
	#define DBG(_x)	((void)0)
#endif

/* TCP socket single-client server implementation for demonstrating Socket API in C. */

int main(int argc, char *argv[]) {
  
  //server should take in a server port argument; set to default to localhost IP
  char* host_addr = "127.0.0.1";  //hardcoded per project directions
  int port = atoi(argv[1]);
  int X = 0;
  int Y = 0;
  int Z = 0;

  /*setup passive open*/
  int s;
  if((s = socket(PF_INET, SOCK_STREAM, 0)) <0) {
    perror("simplex-talk: socket");
    exit(1);
  } else {
	  DBG(printf("Got past socket\n"));
  }

  /* Config the server address */
  struct sockaddr_in sin;
  sin.sin_family = AF_INET; 
  sin.sin_addr.s_addr = inet_addr(host_addr);
  sin.sin_port = htons(port);
  // Set all bits of the padding field to 0
  memset(sin.sin_zero, '\0', sizeof(sin.sin_zero));

  /* Bind the socket to the address */
  if((bind(s, (struct sockaddr*)&sin, sizeof(sin)))<0) {
    perror("simplex-talk: bind");
    exit(1);
  } else {
	 DBG(printf("Got past bind\n"));
  }

  // connections can be pending if many concurrent client requests
  listen(s, MAX_PENDING);
  DBG(printf("Got past listen\n"));

  /* wait for connection, then receive and print text */
  int new_s;
  socklen_t len = sizeof(sin);
  
  while(1) {
    char buf[MAX_LINE] = {'\0'};
	if((new_s = accept(s, (struct sockaddr *)&sin, &len)) <0){
      perror("simplex-talk: accept");
	  close(new_s);
      exit(1);
    } else {
		DBG(printf("accepted new_s\n"));
	}
	
	/* receive HELLO Y message */
	
	if(recv(new_s, buf, sizeof(buf), 0) < 0) {
		perror("Error receiving from client");
		close(new_s);
		exit(1);
	} else {
		DBG(printf("Success: received from client\n"));
		buf[sizeof(buf)-1] = '\n';
		printf("%s", buf);
		printf("\n");
		
	}
	
	/* read in X, calculate Y */
	X = atoi( &buf[5]);
	DBG(printf("X is %d\n", X));
	Y = X + 1;
	DBG(printf("Y is %d\n", Y));

	
	/* construct HELLO Y message to send back */
	char msg[MAX_LINE] = {'\0'};
	snprintf(msg, MAX_LINE, "HELLO %d", Y);

	DBG(printf("%s is %lu characters\n", msg, sizeof(msg)));

	if(send(new_s, msg, (strlen(msg) + 1), 0) < 0) {
	  perror("Error sending from server");
	  close(new_s);
	  exit(1);
	}
	else {
	  DBG(printf("Success: send from server\n"));
	}
	
	/* receive HELLO Z message */
	
	if(recv(new_s, buf, sizeof(buf), 0) < 0) {
		perror("Error receiving from client");
		close(new_s);
		exit(1);
	} else {
		DBG(printf("Success: received from client\n"));
		buf[sizeof(buf)-1] = '\n';
		printf("%s", buf);
		printf("\n");
		
	}
	
	/* read in Z & verify*/
	Z = atoi( &buf[5]);
	DBG(printf("Z is %d\n", Z));
	
	if ( (Y+1) != Z) {
		perror("Error Z is not Y+1");
		close(new_s);
		exit(1);
	}
	
    close(new_s);
  }

  return 0;
}
