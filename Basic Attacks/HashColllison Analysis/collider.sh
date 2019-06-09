#!/bin/bash

#this for loop is used to loop through 5 times for
# 4,8,12,16,20 bit prefixes
# here 1=4, 2=8 and so on.. as SHA256 generates hexadecimal
# 1 hexa value corresponds to 4 bits
for var in 1 2 3 4 5  
do
count1=1
#this while loop is used to rerpeat the code 5 times
#so that 5 count values for each prefix bits are generated
while [ $count1 -lt 6 ]
do
count=0
while
# here the 2 independent SHA256 hashes are stored in variables
hash1=$(echo -n $(openssl rand -base64 $(( 64 * 3/4 )))| sha256sum )
hash2=$(echo -n $(openssl rand -base64 $(( 64 * 3/4 )))| sha256sum )
((count++))
echo $count
#looping until a collision occurs
[ "${hash1:0:$var}" != "${hash2:0:$var}" ]
do :; done
# dumping the count values to outputfile
echo $((var*4)) $count  >> colliderout.txt
((count1++)) 
done
done
echo finish  
