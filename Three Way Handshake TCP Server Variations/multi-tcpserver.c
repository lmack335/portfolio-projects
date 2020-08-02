#include <pthread.h>
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

/* Multi-threaded TCP socket server implementation for demonstrating Socket API in C. */
 
typedef struct {
	int new_s;
	int X;
	int Y;
	int Z;
	char buf[MAX_LINE];
	char msg[MAX_LINE];
} t_connect_info;

void *handshake_thread(void *t_connect);

int main(int argc, char *argv[]) {
  
  /* server should take in a server port argument; hardcoded to localhost IP per project directions */
  char* host_addr = "127.0.0.1";
  int port = atoi(argv[1]);
  
  
  /* setup threads */
  pthread_t tids[MAX_PENDING];
  t_connect_info t_connections[MAX_PENDING];
  int i=0;
  
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
  socklen_t len = sizeof(sin);
  
  while(1) {
    
	if((t_connections[i].new_s = accept(s, (struct sockaddr *)&sin, &len)) <0){
      perror("simplex-talk: accept");
	  close(t_connections[i].new_s);
      exit(1);
    } else {
		DBG(printf("accepted t_sock_fd[%d]\n", i));
		pthread_create(&tids[i], NULL, handshake_thread, (void*) &t_connections[i]);
		i++;
	}
  }
  
  // Make sure the main thread terminates only after all spawned threads terminate
  // Also pthread_join enable the caller to get the return value from the callees
	for (i=0; i<MAX_PENDING; i++) {
		close(t_connections[i].new_s);
		pthread_join(tids[i], NULL);
	}

  return 0;
}

/* Thread receives HELLO X, constructs HELLO Y, receives & verifies HELLO Z */

void *handshake_thread(void *t_connect) {
	t_connect_info thread = *((t_connect_info*) t_connect);

	//initialize values
	thread.X = 0;
	thread.Y = 0;
	thread.Z = 0;
	
	int j = 0;
	
	for (j = 0; j < MAX_LINE; j++) {
		thread.buf[j] = '\0';
	}
  
	/* receive HELLO X message */
	if(recv(thread.new_s, thread.buf, sizeof(thread.buf), 0) < 0) {
		perror("Error receiving from client");
		close(thread.new_s);
		exit(1);
	} else {
		DBG(printf("Success: received from client\n"));
		thread.buf[sizeof(thread.buf)-1] = '\n';
		printf("%s", thread.buf);
		printf("\n");
		
	}

	/* read in X, calculate Y */
	thread.X = atoi( &thread.buf[5]);
	DBG(printf("X is %d\n", thread.X));
	thread.Y = thread.X + 1;
	DBG(printf("Y is %d\n", thread.Y));


	/* construct HELLO Y message to send back */
	for (j = 0; j < MAX_LINE; j++) {
		thread.msg[j] = '\0';
	}
	
	snprintf(thread.msg, MAX_LINE, "HELLO %d", thread.Y);

	DBG(printf("%s is %lu characters\n", thread.msg, sizeof(thread.msg)));

	if(send(thread.new_s, thread.msg, (strlen(thread.msg) + 1), 0) < 0) {
	  perror("Error sending from server");
	  close(thread.new_s);
	  exit(1);
	}
	else {
	  DBG(printf("Success: send from server\n"));
	}

	/* receive HELLO Z message */

	if(recv(thread.new_s, thread.buf, sizeof(thread.buf), 0) < 0) {
		perror("Error receiving from client");
		close(thread.new_s);
		exit(1);
	} else {
		DBG(printf("Success: received from client\n"));
		thread.buf[sizeof(thread.buf)-1] = '\n';
		printf("%s", thread.buf);
		printf("\n");
		
	}

	/* read in Z & verify*/
	thread.Z = atoi( &thread.buf[5]);
	DBG(printf("Z is %d\n", thread.Z));

	if ( (thread.Y+1) != thread.Z) {
		perror("Error Z is not Y+1");
		close(thread.new_s);
		exit(1);
	}
	  
	/* Exit - the current thread terminates */
	pthread_exit(NULL);
}
