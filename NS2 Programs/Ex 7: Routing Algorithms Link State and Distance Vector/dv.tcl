set ns [new Simulator]
$ns rtproto DV
set tr [open dv.tr w]
$ns trace-all $tr
set ftr [open dv.nam w]
$ns namtrace-all $ftr
set ft [open "dv_th" w]

set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]
set n6 [$ns node]

$ns duplex-link $n0 $n1 1.5Mb 10ms DropTail
$ns duplex-link $n1 $n2 1.5Mb 10ms DropTail
$ns duplex-link $n2 $n3 1.5Mb 10ms DropTail
$ns duplex-link $n3 $n4 1.5Mb 10ms DropTail
$ns duplex-link $n4 $n5 1.5Mb 10ms DropTail
$ns duplex-link $n5 $n6 1.5Mb 10ms DropTail
$ns duplex-link $n6 $n0 1.5Mb 10ms DropTail

set tcp [new Agent/TCP]
set sink [new Agent/TCPSink]
$ns attach-agent $n0 $tcp
$ns attach-agent $n3 $sink
$ns connect $tcp $sink
set ftp [new Application/FTP]
$ftp attach-agent $tcp

proc record { } {
    global sink ft
    set ns [Simulator instance]
    set time 0.1
    set now [$ns now]
    set bw0 [$sink set bytes_]
    puts $ft "$now [expr $bw0/$time*8/1000000]"
    $sink set bytes_ 0
    $ns at [expr $time+$now] "record"
}

proc finish { } {
global ns tr ftr ft
$ns flush-trace
close $tr
close $ftr
close $ft
exec nam dv.nam &
exec xgraph dv_th &
exec gawk -f ra.awk dv.tr &
exit 0
}

$ns at 0.55 "record"

$ns at 0.5 "$ftp start"

$ns rtmodel-at 2.0 down $n2 $n3

$ns rtmodel-at 3.0 up $n2 $n3

$ns at 4.9 "$ftp stop"
$ns at 5.0 "finish"

$ns run