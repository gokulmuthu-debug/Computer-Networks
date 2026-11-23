set val(chan) Channel/WirelessChannel;
set val(prop) Propagation/TwoRayGround;
set val(netif) Phy/WirelessPhy;
set val(mac) Mac/802_11;
set val(ifq) Queue/DropTail/PriQueue;
set val(ll) LL;
set val(ant) Antenna/OmniAntenna;
set val(ifqlen) 50;
set val(rp) AODV;
set val(nn) 6;
set val(x) 500;
set val(y) 500;

set ns [new Simulator]
set tr [open wireless.tr w]
$ns trace-all $tr
set ftr [open wireless.nam w]
$ns namtrace-all-wireless $ftr 500 500

set topo [new Topography]
$topo load_flatgrid 500 500
create-god 6
set chan1 [new $val(chan)]

$ns node-config -adhocRouting $val(rp) \
        -propType $val(prop) \
        -phyType $val(netif) \
        -macType $val(mac) \
        -ifqType $val(ifq) \
        -llType $val(ll) \
        -antType $val(ant) \
        -ifqLen $val(ifqlen) \
        -topoInstance $topo \
        -movementTrace ON \
        -macTrace ON \
        -routerTrace ON \
        -agentTrace ON \
        -channel $chan1

set n0 [$ns node]
set n1 [$ns node]
set n2 [$ns node]
set n3 [$ns node]
set n4 [$ns node]
set n5 [$ns node]

$n0 random-motion 0
$n1 random-motion 0
$n2 random-motion 0
$n3 random-motion 0
$n4 random-motion 0
$n5 random-motion 0

$ns initial_node_pos $n0 20
$ns initial_node_pos $n1 20
$ns initial_node_pos $n2 20
$ns initial_node_pos $n3 20
$ns initial_node_pos $n4 20
$ns initial_node_pos $n5 50

$n0 set X_ 10.0
$n0 set Y_ 10.0
$n0 set Z_ 0.0

$n1 set X_ 20.0
$n1 set Y_ 20.0
$n1 set Z_ 0.0

$n2 set X_ 30.0
$n2 set Y_ 30.0
$n2 set Z_ 0.0

$n3 set X_ 40.0
$n3 set Y_ 40.0
$n3 set Z_ 0.0

$n4 set X_ 50.0
$n4 set Y_ 50.0
$n4 set Z_ 0.0

$n5 set X_ 60.0
$n5 set Y_ 60.0
$n5 set Z_ 0.0

$ns at 1.0 "$n1 setdest 450.0 450.0 25.0"
$ns at 1.0 "$n4 setdest 200.0 250.0 5.0"
$ns at 1.0 "$n5 setdest 100.0 80.0 10.0"
$ns at 5.0 "$n5 setdest 150.0 300.0 15.0"

set tcp [new Agent/TCP]
set sink [new Agent/TCPSink]
$ns attach-agent $n0 $tcp
$ns attach-agent $n5 $sink
$ns connect $tcp $sink
set ftp [new Application/FTP]
$ftp attach-agent $tcp

set udp [new Agent/UDP]
set null [new Agent/Null]
$ns attach-agent $n2 $udp
$ns attach-agent $n3 $null
$ns connect $udp $null
set cbr [new Application/Traffic/CBR]
$cbr attach-agent $udp

proc finish {} {
        global ns tr ftr
        $ns flush-trace
        close $tr
        close $ftr
        exec nam wireless.nam &
        exec gawk -f wireless.awk wireless.tr &
        exit 0
}

$ns at 1.0 "$ftp start"
$ns at 1.0 "$cbr start"
$ns at 5.0 "$ftp stop"
$ns at 5.0 "$cbr stop"
$ns at 10.0 "finish"

$ns run