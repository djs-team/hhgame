#!/usr/bin/python
# -*- coding: utf-8 -*-
import os,stat
import sys
import shutil

curFilePath = sys.path[0]

scriptDir = curFilePath+'/../../cocos2d-x/cocos/scripting/js-bindings/script'
srcDir= curFilePath+'/../../../src'
resDir= curFilePath+'/../../../res'
projectJsonDir = curFilePath+'/../../../project.json'


#modulesSrcDir = curFilePath+'/../../../modules/'
#modulesDestDir = curFilePath+ '/Debug.win32/Resources/modules/'

mainLinks =[
    '/Debug.win32/Resources/script',
    '/Debug.win32/Resources/src',
    '/Debug.win32/Resources/res',
    '/Debug.win32/Resources/project.json'
]

def clearLink():
    for i in range(len(mainLinks)):
        dir = curFilePath+mainLinks[i]
        dir = dir.replace('\\','/').replace('//','/')
        if os.path.exists(dir):
            if(os.path.isdir(dir)):
                print dir
                cmd = 'RD /S/Q '+ '\"'+dir+'\"'
                os.system(cmd)
            else:
                os.remove(dir)

def link():
    clearLink()

#    if not os.path.exists('Debug.win32/Resources/modules'):
#       os.mkdir('Debug.win32/Resources/modules')

    cmdSP = 'mklink /D ' + '\"' + 'Debug.win32/Resources/script' + '\"' + '  ' + '\"' + scriptDir + '\"'
    os.system(cmdSP)

    cmdSRC = 'mklink /D ' + '\"' + 'Debug.win32/Resources/src' + '\"' + '  ' + '\"' + srcDir + '\"'
    os.system(cmdSRC)

    cmdRES = 'mklink /D ' + '\"' + 'Debug.win32/Resources/res' + '\"' + '  ' + '\"' + resDir + '\"'
    os.system(cmdRES)

    cmdPJ = 'mklink ' + '\"' + 'Debug.win32/Resources/project.json' + '\"' + '  ' + '\"' + projectJsonDir + '\"'
    os.system(cmdPJ)


'''
    fl = os.listdir(modulesSrcDir)
    for f in fl:
        srcDir = modulesSrcDir+f
        destDir = modulesDestDir+f
        if not os.path.exists(destDir):
            os.mkdir(destDir)
        print
        subf = os.listdir(srcDir)
        for sf in subf:
            if (sf.lower() == 'src' or sf.lower() == 'res'):
                src = srcDir +'/'+sf
                dest = destDir + '/' + sf
                cmd = 'mklink /D '+ '\"'+ dest +'\"'+'  '+'\"'+ src+'\"'
                os.system(cmd)
'''

if __name__ == "__main__":
    link()