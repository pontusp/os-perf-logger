set datafile separator ","
set grid
set term svg size 800,600

set xlabel 'VM uptime [days]'

set key width -18

set xrange [ 24.82 : 24.9 ]
set yrange [ 0 : 60 ]

FILE1="data/osPerformanceLogger_win_jdk17_0_12.csv"
FILE2="data/osPerformanceLogger_win_jdk23_0_1.csv"
FILE3="data/osPerformanceLogger_win_jdk17_0_14_osmxbean_patch.csv"

set output 'osPerformanceLogger-sys-cpu.svg'
set ylabel 'System CPU usage [%]'
set title "OS Performance - System CPU usage\n{/*0.9 All test runs}"
plot FILE3 skip 2143290 using ($1/1000/3600/24):($2*100) with lines title 'Win11 jdk-17.0.14-osmxbean.patch', \
    FILE1 skip 2143290 using ($1/1000/3600/24):($2*100) with lines title 'Win11 jdk-17.0.12+7-LTS', \
    FILE2 skip 2143290 using ($1/1000/3600/24):($2*100) with lines title 'Win11 jdk-23.0.1+11'

unset yrange
set output 'osPerformanceLogger-proc-cpu.svg'
set ylabel 'Process CPU usage [%]'
set title "OS Performance - Process CPU usage\n{/*0.9 All test runs}"
plot FILE3 skip 2143290 using ($1/1000/3600/24):($3*100) with lines title 'Win11 jdk-23.0.1+11-osmxbean.patch', \
    FILE1 skip 2143290 using ($1/1000/3600/24):($3*100) with lines title 'Win11 jdk-17.0.12+7-LTS', \
    FILE2 skip 2143290 using ($1/1000/3600/24):($3*100) with lines title 'Win11 jdk-23.0.1+11'

