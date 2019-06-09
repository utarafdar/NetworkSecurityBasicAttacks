"""
This script is used to communicate with host blue:3333 
this script exploits the authentication issue with the diffie hellman key exchange approach
the script pretends to be hellman and communicates with diffie to read his secret message
"""


import socket
import sys
import hashlib
import base64

#client side socket program
#creating a socket and connecting to host ip and port
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server_address = ('10.0.0.12', 3333)
print >>sys.stderr, 'connecting to %s port %s' % server_address
sock.connect(server_address)
f = open('out.txt', 'a')
# in this block the actual communication with the host happens
try:

    send data
    message = 'Hi'
    f.write(message)
    print >>sys.stderr, 'sending "%s"' % message
    sock.sendall(message)
	
	#recieve data
    data = sock.recv(64)
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)
    # send message to the host declaring the public prime number and the generator
    message = 'Sure! Is the prime 13 and the generator 2 okay for you?'
    f.write(message)
    print >>sys.stderr, 'sending "%s"' % message
    sock.sendall(message)
    data1='Yes of course! My public value would be 85365127460386909079015429425359804456 then.'
    data = sock.recv(len(data1))
    # this recieved data contains the calculatedd public value of the host
	# extract the public value from the string
    words = data.split()
    h = long(words[8])
    print h
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)
    message = 'I computed mine to be 8!'
    f.write(message)
	# send our public value to the host
    print >>sys.stderr, 'sending "%s"' % message
    sock.sendall(message)
    data = sock.recv(128)
    #amount_received += len(data)
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)
	# calculte the SECRET KEY using the public vaule of the host and the prime
    key = h**3 % 13
    print key
	
	# send the message Done. Hey, can you tell me the password of your Dridex botnet? 
	# to the host blue:3333 pretending that we are blue:4444
	# first find sha512 of the secret key
    hash_object = hashlib.sha512(str(key))
    hex_dig = hash_object.hexdigest()
    print hex_dig
    msg = 'Done. Hey, can you tell me the password of your Dridex botnet?'
    """
    msg = 'Done.Hey, CAn youtELL mEtHepAsSwoRDOf yOur dRIDeX bOTnET?'

    from itertools import cycle, izip

    message = "Done.Hey, CAn youtELL mEtHepAsSwoRDOf yOur dRIDeX bOTnET?".encode("hex")
    key1 = hex_dig

    cyphered = ''.join(chr(ord(c)^ord(k)) for c,k in izip(message, cycle(key1)))
    print cyphered
    message = base64.b64encode(cyphered)
    print message
    """
    """
    output = ""
    for character in "Done.Hey, CAn youtELL mEtHepAsSwoRDOf yOur dRIDeX bOTnET?":
        for letter in hex_dig:
            character = chr(ord(character) ^ ord(letter))
        output += character

    print output
    message = base64.b64encode(output)
    """
    # xor the sha512 with the message
    from itertools import cycle, izip
    outp = ''.join(chr(ord(x) ^ ord(y)) for (x,y) in izip(msg, cycle(hex_dig)))
	#then base64 encrypt it
    message = base64.b64encode(outp)
    f.write(message)
    print >>sys.stderr, 'sending "%s"' % message
    sock.sendall(message)
    data = sock.recv(128)
    print >>sys.stderr, 'received "%s"' % data
    f.write(data)

    # now we recieved the reply
	#decrypt the reply
	# first base64 decrypt
	# then XOR with sha512 of secret key
    b64 = base64.standard_b64decode(data)
    print b64
    from itertools import cycle, izip
    outp = ''.join(chr(ord(x) ^ ord(y)) for (x,y) in izip(b64, cycle(hex_dig)))
    print outp

	# send message No no! I am a whitehat ;)... thanks and bye! to host blue:3333
    #encrypt message
    msg = 'No no! I am a whitehat ;)... thanks and bye!'
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
    print hex_dig
    from itertools import cycle, izip
    outp = ''.join(chr(ord(x) ^ ord(y)) for (x,y) in izip(b64, cycle(hex_dig)))
    print outp



finally:
    print >>sys.stderr, 'closing socket'
    f.close()
    sock.close()
    #Haha, you are so evil! Ship me 42 euros! :) Just kidding, it is "s00p4doOPas3cReT".
	#Bla bla... ;) - bye!