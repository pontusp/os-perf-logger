set datafile separator ","
set grid
set term svg size 800,600

set xlabel 'VM uptime [s]'

#set key box
set key width -18
#set key horiz
#set key outside
#set key bmargin

#set key font ",6"

#set xrange [ 30 : 100 ] noreverse nowriteback


#set xrange [ 0 : 250 ]
set yrange [ 0 : 100 ]

FILE1="osPerformanceLogger_jdk17_0_10_7.csv"
FILE2="osPerformanceLogger_jdk_23_0_1_patched.csv"

set output 'osPerformanceLogger.svg'
set ylabel 'System CPU usage [%]'
set title 'OS Performance - System CPU usage'
plot FILE1 using ($1/1000):($2*100) with lines title 'jdk-17.0.10-7', \
    FILE2 using ($1/1000):($2*100) with lines title 'jdk-23.0.1-patched'

