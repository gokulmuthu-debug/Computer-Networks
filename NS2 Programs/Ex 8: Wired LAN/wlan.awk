BEGIN {
	drp=0
	rec=0
	
	st1=0
	et1=0
	lat1=0
	fs1=0
	
	st2=0
	et2=0
	lat2=0
	fs2=0
	
	f0=0
	f1=0
	
	bw0=0
	bw1=0
}
{
	if($1=="r" && $3==6)
	{
		if(f1==0)
		{
			f1=1
			st1=$2
		}
		fs1+=$6
		et1+=$2
		lat1=et1-st1
		if(lat1!=0)bw1=fs1/lat1
		printf "%f %f\n", et1, bw1>> "file3.xg"
	}
}
END {
	print("\nFile Size: ", fs1)
	lat1=et1-st1
	print("\nLatency: ", lat1)
	if(la1!=0)bw1=fs1/lat1
	print("\nThroughput: ", bw1/10^6)
}
