set ns [new Simulator]

set tr [open "wlan.tr" w]
$ns trace-all $tr
set ftr [open "wlan.nam" w]
$ns namtrace-all $ftr

set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]
set n6 [$ns node]

$ns make-lan "$n1 $n2 $n3 $n4 $n5 $n6" 0.2Mb 20ms LL Queue/DropTail Mac/802_3

set tcpsend1 [new Agent/TCP]
set tcpsend2 [new Agent/TCP]
set tcprec1 [new Agent/TCPSink]
set tcprec2 [new Agent/TCPSink]
$ns attach-agent $n1 $tcpsend1
$ns attach-agent $n2 $tcpsend2
$ns attach-agent $n6 $tcprec1
$ns attach-agent $n6 $tcprec2

set ftp1 [new Application/FTP]
set ftp2 [new Application/FTP]
$ftp1 attach-agent $tcpsend1
$ftp2 attach-agent $tcpsend2

$ns connect $tcpsend1 $tcprec1
$ns connect $tcpsend2 $tcprec2

$ns at 0.2 "$ftp1 start"
$ns at 0.4 "$ftp2 start"

proc finish { } {
	global ns tr ftr
	$ns flush-trace
	close $tr
	close $ftr
	exec nam wlan.nam &
	exec gawk -f wlan.awk wlan.tr &
	exit 0
}

$ns at 10 "finish"
$ns run
