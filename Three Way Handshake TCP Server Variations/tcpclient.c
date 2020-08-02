#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <sys/socket.h>
#include <netinet/ip.h>
#include <arpa/inet.h>

#define MAX_LINE 20

#define DEBUG 0
#if( DEBUG )
	#define DBG(_x)	((void)(_x))
#else
	#define DBG(_x)	((void)0)
#endif

/* TCP socket client implementation for demonstrating Socket API in C. */

int main (int argc, char *argv[]) {
  //takes as input: the server address, server port number, & an initial sequence number
  char* host_addr = argv[1];
  int port = atoi(argv[2]);
  int X = atoi(argv[3]);
  int Y = 0;
  int Z = 0;
  

  /* Open a socket */
  int s;
  if((s = socket(PF_INET, SOCK_STREAM, 0)) <0){
    perror("simplex-talk: socket");
    exit(1);
  }

  /* Config the server address */
  struct sockaddr_in sin;
  sin.sin_family = AF_INET; 
  sin.sin_addr.s_addr = inet_addr(host_addr);
  sin.sin_port = htons(port);
  // Set all bits of the padding field to 0
  memset(sin.sin_zero, '\0', sizeof(sin.sin_zero));

  /* Connect to the server */
  if(connect(s, (struct sockaddr *)&sin, sizeof(sin))<0){
    perror("simplex-talk: connect");
    close(s);
    exit(1);
  }
  
  /* construct HELLO X message to send */
  char msg[MAX_LINE] = {'\0'};
  snprintf(msg, MAX_LINE, "HELLO %d", X);
  
  DBG(printf("%s is %lu characters\n", msg, sizeof(msg)));
  
  if(send(s, msg, (strlen(msg) + 1), 0) < 0) {
	  perror("Error sending from client");
	  close(s);
	  exit(1);
  }
  else {
	  DBG(printf("Success: send from client\n"));
  }
  
  /* receive HELLO Y message */
  socklen_t len = sizeof(sin);
  char buf[MAX_LINE] = {'\0'};
  
  if( (len = recv(s, buf, sizeof(buf), 0)) < 0) {
		perror("Error receiving from server");
		close(s);
		exit(1);
	}
	DBG(printf("Success: received from server\n"));
	buf[sizeof(buf)-1] = '\n';
	printf("%s", buf);
	printf("\n");
	
	/* read in & verify Y */
	Y = atoi( &buf[5]);
	DBG(printf("Y is %d\n", Y));
	
	if ( (X+1) != Y) {
		perror("Error Y is not X+1");
		close(s);
		exit(1);
	}
	
	/* calculate Z */
	Z = Y + 1;
	DBG(printf("Z is %d\n", Z));

	/* clear msg buffer & construct HELLO Z message to send */
	
	int i = 0;
	for (i=0; i < MAX_LINE; i++) {
		msg[i] = '\0';
	}
	snprintf(msg, MAX_LINE, "HELLO %d", Z);
	DBG(printf("%s is %lu characters\n", msg, sizeof(msg)));

	if(send(s, msg, (strlen(msg) + 1), 0) < 0) {
	  perror("Error sending from client");
	  exit(1);
	}
	else {
	  DBG(printf("Success: send from client\n"));
	}

  return 0;
}
