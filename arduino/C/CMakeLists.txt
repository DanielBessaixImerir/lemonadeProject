cmake_minimum_required(VERSION 3.6)
project(EnvoieDonneePOST)

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c11 -lcurl -ljson")

set(SOURCE_FILES main.c)

add_executable(EnvoieDonneePOST ${SOURCE_FILES})
target_link_libraries(EnvoieDonneePOST m curl json)
