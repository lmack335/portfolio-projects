all:
	gcc tcpclient.c -o tcpclient 
	gcc multi-tcpserver.c -o multi-tcpserver -l pthread
	gcc tcpserver.c -o tcpserver
	
tcpclient:
	gcc tcpclient.c -o tcpclient
	
tcpserver:
	gcc tcpserver.c -o tcpserver
	
multi-tcpserver:
	gcc multi-tcpserver.c -o multi-tcpserver -l pthread
		
clean:
	rm -f *.o tcpclient tcpserver multi-tcpserver