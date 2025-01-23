cmake_minimum_required(VERSION 3.10.2)

project("secure-config")

add_library(
        secure-config
        SHARED
        secure_config.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        secure-config
        ${log-lib}
)
