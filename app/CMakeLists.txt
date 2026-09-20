cmake_minimum_required(VERSION 3.22.1)
project("horizonvip")

add_library(horizonvip SHARED
    src/main/cpp/packet_parser.cpp
    src/main/cpp/scan_loop.cpp
    src/main/cpp/checksum.cpp)

find_library(log-lib log)
target_link_libraries(horizonvip ${log-lib})
