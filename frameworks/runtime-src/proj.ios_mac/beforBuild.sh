#!/usr/bin/env bash
source ~/.bash_profile

rm -rf game
mkdir ./game

cocos jscompile -s ../../../src -d ./game/src

cocos jscompile -s ../../cocos2d-x/cocos/scripting/js-bindings/script -d ./game/script

cp -a ../../../res ./game
