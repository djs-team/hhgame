!/bin/bash

rm -rf game
mkdir ./game

cocos jscompile -s ../../../src -d ./game/src

cp -a ../../../res ./game
