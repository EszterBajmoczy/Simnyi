cmake_minimum_required(VERSION 3.0)
project(native)

set(CMAKE_CXX_STANDARD 11)

add_executable(native CiffCaffParser.cpp Ciff.cpp Ciff.h Caff.cpp Caff.h)
