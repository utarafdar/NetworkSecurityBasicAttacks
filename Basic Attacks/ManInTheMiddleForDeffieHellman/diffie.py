"""
This script is used to communicate with host blue:4444 
this script exploits the authentication issue with the diffie hellman key exchange approach
the script pretends to be hellman and communicates with diffie to read his secret message
"""


import socket
import sys
import base64
import hashlib

#client side socket program
#creating a socket and connecting to host ip and port
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = ('10.0.0.12', 4444)
print >>sys.stderr, 'connecting to %s port %s' % server_address
sock.connect(server_address)
f = open('out.txt', 'a')
# in this block the actual communication with the host happens
try:
	
    # Send data
    message = 'Hi'
    f.write(message)
    print >>sys.stderr, 'sending "%s"' % message
    sock.sendall(message)
	
	# recieving data
    data = sock.recv(16)
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)
	
	
    data1="Sure! Is the prime 293836598403636400087132837842343934939 and the generator 2 okay for you?"
    data = sock.recv(len(data1))
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)
    words = data.split()
	# in this message we get the shared prime number and generator
	#split the data and extract the prime number and generator from the message
    print words[4]
	print words[8]
    p = long(words[4])
	g = long(words[8])
	# here 1237 is our private number
	# compute the value to share with the other host
    val = g**1237 % p
    print val
    message = "Yes of course! My public value would be "+ str(val) +" then."
	#we send our public value in this message to host white:4444
    #message = 'Yes of course! My public value would be 85365127460386909079015429425359804456 then.'
    f.write(message)
    print >>sys.stderr, 'sending "%s"' % message
    sock.sendall(message)
    data = sock.recv(64)
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)
    data = sock.recv(64)
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)
    # in the above message we recieved host white:4444's public value
	# split the data and extract the public value
    words = data.split()
    pa = words[5][:-1]
    print pa
    print p
	# use this public value to find the SECRET KEY
    v = long(pa)**1237 % p
    print v

    message = 'Got it! Lets take the SHA512 value of our shared secret as XOR-key for encryption and then communicate Base64 encoded :)'
    f.write(message)
    print >>sys.stderr, 'sending "%s"' % message
    sock.sendall(message)
    data = sock.recv(512)
    #amount_received += len(data)
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)
	
	# the base64 encrypted message is recieved
	# decrypt this message
	# first base64 decryption, then xor with sha512 hashed secret key
	
    b64 = base64.standard_b64decode(data)
    print b64
    hash_object = hashlib.sha512(str(v))
    hex_dig = hash_object.hexdigest()
    print hex_dig
	# XOR 
    from itertools import cycle, izip
    outp = ''.join(chr(ord(x) ^ ord(y)) for (x,y) in izip(b64, cycle(hex_dig)))
    print outp
	
	# send the message Haha, you are so evil! Ship me 42 euros! :) Just kidding, it is "s00p4doOPas3cReT".
	# to the host blue:4444 pretending that we are blue:3333
	# first find sha512 of the secret ket
	# then xor the sha512 with the message
	# then encrypt base 64
	#encrypt message
    msg = 'Haha, you are so evil! Ship me 42 euros! :) Just kidding, it is "s00p4doOPas3cReT".'
    from itertools import cycle, izip
    outp = ''.join(chr(ord(x) ^ ord(y)) for (x,y) in izip(msg, cycle(hex_dig)))
    message = base64.b64encode(outp)
    f.write(message)
    print >>sys.stderr, 'sending "%s"' % message
    sock.sendall(message)
    data = sock.recv(128)
    #amount_received += len(data)
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)

    #decrypt the recieved message
    b64 = base64.standard_b64decode(data)
    print b64
    hash_object = hashlib.sha512(str(v))
    hex_dig = hash_object.hexdigest()
 

finally:
    print >>sys.stderr, 'closing socket'
    f.close()
    sock.close()
    #Done. Hey, can you tell me the password of your Dridex botnet?
	#No no! I am a whitehat ;)... thanks and bye!