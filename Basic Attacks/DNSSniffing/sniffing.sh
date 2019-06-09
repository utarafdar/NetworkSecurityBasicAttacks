#!/bin/bash
#with this command the traffic related to DNS request and response is recorded
#DNS uses standard port 53 , hence we filter the udp packets going to and coming from port 53
#we use awk to filter the relevant data captured by tcpdump
sudo tcpdump -n -l  udp port 53 | awk '{ if ($7=="A?") print "DNS request victim ip:",$3,"\t DNS IP= ",$5," type =",$7," domain name = ",$8,"transaction ID =",$6; else print "DNS response  DNS ip:",$3,"\t victim IP= ",$5," type =",$8," domain IP = ",$9,"Transaction ID =",$6; }'