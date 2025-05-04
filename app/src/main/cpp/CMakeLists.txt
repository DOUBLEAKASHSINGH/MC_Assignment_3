cmake_minimum_required(VERSION 3.10.2)
project("matrixcalc")

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_library(
        matrixcalc
        SHARED
        native-lib.cpp
)

find_library(log-lib log)

target_link_libraries(
        matrixcalc
        ${log-lib}
        c++_static
)
