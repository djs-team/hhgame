#!/usr/bin/python
# -*- coding: utf-8 -*-
import os
import hashlib
import sys
import shutil
import binascii
import json
import commands
import collections
from collections import OrderedDict
import re
import time
import errno
import logging
import stat

updateDirConfig = "./updateConfig.json"

filterArray =[
    '_PList.Dir',
    '.DS_Store',
    'version.manifest',
    'project.manifest',
    'test.cfg'
]

PlainTextFileTypes = {
    '.json': 1,
    '.txt': 1,
    '.plist': 1,
    '.ExportJson': 1,
    '.atlas': 1,
    '.fnt': 1
}

def onerror(func, path, _):
    os.chmod(path, stat.S_IWRITE)
    func(path)

# # 由于在win32 下renametree 的不稳定性，根据几种常出现的问题进行重试
# def renametree_temp(src):
#     """
#     Rename tree to temporary name, and return that name, or
#     None if the source directory does not exist.
#     """
#     count = 0
#     while count < 10:      # prevents indefinite loop
#         count += 1
#         tmp = os.path.join(os.path.dirname(src),"_removetree_tmp_%d"%(count))
#         try:
#             os.rename(src, tmp)
#             return tmp      # Success!
#         except OSError as e:
#             time.sleep(1)
#             if e.errno == errno.EACCES:
#                 logging.warning("util.renametree_temp: %s EACCES, retrying"%tmp)
#                 continue    # Try another temp name
#             if e.errno == errno.ENOTEMPTY:
#                 logging.warning("util.renametree_temp: %s ENOTEMPTY, retrying"%tmp)
#                 continue    # Try another temp name
#             if e.errno == errno.EEXIST:
#                 logging.warning("util.renametree_temp: %s EEXIST, retrying"%tmp)
#                 shutil.rmtree(tmp, onerror=onerror)  # Try to clean up old files
#                 continue    # Try another temp name
#             if e.errno == errno.ENOENT:
#                 logging.warning("util.renametree_temp: %s ENOENT, skipping"%tmp)
#                 break       # 'src' does not exist(?)
#             raise           # Other error: propagaee
#     return None

def removetree(tgt):
    """
    Work-around for python problem with shutils tree remove functions on Windows.
    """
    def error_handler(func, path, execinfo):
        e = execinfo[1]
        if e.errno == errno.ENOENT or not os.path.exists(path):
            return          # path does not exist: nothing to do
        if func in (os.rmdir, os.remove) and e.errno == errno.EACCES:
            try:
                os.chmod(path, stat.S_IRWXU| stat.S_IRWXG| stat.S_IRWXO) # 0777
            except Exception as che:
                print("util.removetree: chmod failed: %s"%che)
            try:
                func(path)
            except Exception as rfe:
                print("util.removetree: 'func' retry failed: %s"%rfe)
                if not os.path.exists(path):
                    return      # Gone, assume all is well
                raise
        if e.errno == errno.ENOTEMPTY:
            print("util.removetree: Not empty: %s, %s"%(path, tgt))
            time.sleep(1)
            removetree(path)    # Retry complete removal
            return
        print("util.removetree: rmtree path: %s, error: %s"%(path, repr(execinfo)))
        raise e
    # Try renaming to a new directory first, so that the tgt is immediately
    # available for re-use.
    #tmp = renametree_temp(tgt)
    tmp = tgt
    if tmp:
        shutil.rmtree(tmp, onerror=error_handler)
    return

def getDefaultManifest():
    defaultManifest = collections.OrderedDict()
    defaultManifest['packageUrl'] = 'http://sources1.happyplaygame.net/zzscmj/update/'
    defaultManifest['remoteManifestUrl'] =  'http://sources1.happyplaygame.net/zzscmj/update/project.manifest'
    defaultManifest['remoteVersionUrl'] = 'http://sources1.happyplaygame.net/zzscmj/update/version.manifest'
    defaultManifest['version'] = '1.0.0'
    return defaultManifest

#初始化子模块更新需要的配置文件
def initSubModulesCfg(gamID,moduleName,remoteUrl,version,depMainVer):
    cfgFilePath = '../../../web/subModulesCfg.json'
    subModuleCfg =  collections.OrderedDict()
    if os.path.exists(cfgFilePath):
        fileJson = json.load(open(cfgFilePath),object_pairs_hook=OrderedDict)
        if (fileJson):
            subModuleCfg = fileJson
    if depMainVer != None:
        subModuleCfg[gamID] = {
            'storagePath': 'update/modules/' + moduleName,
            'localManifest': 'modules/' + moduleName + '/res/project.manifest',
            'remoteManifest': remoteUrl + 'sub/' + moduleName + '/' + version + '/project.manifest',
            "version": version,
            "depMainVersion": depMainVer
        }
    else:
        subModuleCfg[gamID] = {
            'storagePath': 'update/modules/' + moduleName,
            'localManifest': 'modules/' + moduleName + '/res/project.manifest',
            'remoteManifest': remoteUrl + 'sub/' + moduleName + '/' + version + '/project.manifest',
            "version": version
        }
    json.dump(subModuleCfg, open(cfgFilePath, "w+"),indent = 4)


def readyUpadteFiles():
    f = file(updateDirConfig,'rb')
    try:
        updateConfig = json.load(f,object_pairs_hook=OrderedDict)
        keys = []
        if (updateConfig != None):
            if (updateConfig['main']):
                keys.append('main')
            if (updateConfig['modules']):
                for key in updateConfig['modules']:
                    keys.append(key)
        return updateConfig, keys
    except ValueError as e:
        print(u'updateConfig.json 解析失败')
        print('ValueError:', e)

def rmAndCopy(srcPath,destPath):
    if (os.path.exists(destPath)):
        #shutil.rmtree(destPath,onerror=onerror )
        removetree(destPath)
    if(os.path.exists(srcPath)):
        print 'copy %s to %s' % (srcPath, destPath)
        try:
            shutil.copytree(srcPath, destPath,ignore=shutil.ignore_patterns('*_PList.Dir*', '*.DS_Store'))
        except ValueError as e:
            print(u'')

# 初始化主模块
def initMainModule(config, version,oldOutAsserts):
    mainConfig = config['main']
    fixPath = 'main/'+version+'/'
    if (os.path.exists(fixPath)):
        removetree(fixPath)
        #shutil.rmtree(fixPath,onerror=onerror)
    if(mainConfig):
        for key in mainConfig:
            if(key == 'src'):
                print u'生成代码....'
                src_files = mainConfig[key]
                for i in range(len(src_files)):
                    filePath = src_files[i]
                    paths = os.path.split(filePath)
                    tmpPath = paths[len(paths)-1]
                    cmd = 'cocos jscompile -s ' + filePath + ' -d '+fixPath+'src/' + tmpPath
                    print cmd
                    os.system(cmd)
            elif (key == 'res'):
                print u'生成资源....'
                filePath = mainConfig[key]
                paths = os.path.split(filePath)
                tmpPath = fixPath +paths[len(paths) - 1]
                rmAndCopy(filePath,tmpPath)
        # 初始化manifest
        pmPath,changeLog = initManifest(config['manifestUrl'], 'main/',version,oldOutAsserts)

        # 写入changeLog
        changeLogFileName = './mainChangeLog.json'
        json.dump(changeLog, open(changeLogFileName, "w+"), indent=4)

        # 拷贝project.manifest 到 运行目录
        shutil.copy(pmPath,mainConfig['res']+'/project.manifest')

        if(config['outDir']):
            outDir = config['outDir']+'/main/'
            rmAndCopy('./main/',outDir)
            if os.path.exists('./main/'):
                #shutil.rmtree('./main/',onerror=onerror)
                removetree('./main/')
    else:
        print u"未找到main 模块,请检查 updateConfig.json"
        return False

# 初始化子模块
def initSubModule(config,name,version,oldOutAsserts,depMainVer):
    module = config['modules'][name]
    moduleDir = 'sub/'+name+'/'+version+'/'
    if (os.path.exists('./modules')):
        removetree('./modules')
        #shutil.rmtree('./modules',onerror=onerror)
    if (module):
        for key in module:
            filePath = module[key]
            paths = os.path.split(filePath)
            print key
            if (key == 'src'):
                print u'生成代码....'
                tmpPath = paths[len(paths) - 1]
                cmd = 'cocos jscompile -s ' + filePath + ' -d ./' + moduleDir + tmpPath
                print cmd
                os.system(cmd)
            elif (key == 'res'):
                print u'生成资源....'
                tmpPath = './' + moduleDir + paths[len(paths) - 1]
                projectManifestOutDir = tmpPath
                rmAndCopy(filePath, tmpPath)
        #初始化manifest
        pmPath,changeLog = initManifest(config['manifestUrl'],'sub/'+name+'/',version,oldOutAsserts)

        # 写入changeLog
        changeLogFileName = './'+name+'ChangeLog.json'
        json.dump(changeLog, open(changeLogFileName, "w+"), indent=4)

        # 拷贝project.manifest 到 运行目录
        shutil.copy(pmPath, module['res']+'/project.manifest')

        if(config['outDir']):
            outDir = config['outDir']+'/sub/'+name+'/'
            rmAndCopy('sub/'+name+'/',outDir)
            if os.path.exists('./sub/'):
                #shutil.rmtree('./sub/',onerror=onerror)
                removetree('./sub/')

        # 初始化子模块更新的配置文件
        initSubModulesCfg(module['gameID'],name,config['manifestUrl'],version,depMainVer)

    else:
        print (encodeforPlatform("未找到[%s]模块,updateConfig.json") %name)
        return False

def wirteManifest(filePath,manifest):
    json.dump(manifest,open(filePath, "w"),indent = 4)

def initManifest(remoteURL,dir,version = '1.0.0',oldOutAsserts = None):
    changeLog = []
    projectDir = dir+version+'/'
    versionDir = dir
    defaultManifest = getDefaultManifest()
    packageUrl = remoteURL+projectDir

    remoteManifestUrl = packageUrl+'project.manifest'
    remoteVersionUrl =  remoteURL+versionDir+'version.manifest'
    defaultManifest['packageUrl'] = packageUrl
    defaultManifest['remoteManifestUrl'] = remoteManifestUrl
    defaultManifest['remoteVersionUrl'] = remoteVersionUrl
    defaultManifest['version'] = version

    vm_outPath = dir+'version.manifest'
    wirteManifest(vm_outPath,defaultManifest)

    defaultManifest['assets']={}
    defaultManifest['searchPaths'] = {}
    versionGenerator(projectDir, '', defaultManifest,oldOutAsserts,changeLog)
    pm_outPath =  projectDir + 'project.manifest'
    wirteManifest(pm_outPath,defaultManifest)
    return pm_outPath,changeLog

def getVersionFromManifest(fileName):
    ver = ''
    f = file(fileName, 'rb')
    if(f):
        content = f.read()
        if (content):
            jsonVal = json.loads(content)
            ver = jsonVal['version']
            return  ver
    else:
        None

def getAddVersionList(verList):
    nums=len(verList)
    needAddIndex=0
    if nums>1:
        for i in range(nums-1,-1,-1):
            verNum=int(verList[i])
            if verNum>=99:
                verNum=0
                needAddIndex=i-1
                verList[i]=str(verNum)
                if needAddIndex<0:
                    print u"请更新版号迭代策略,目前策略不够用了...."
                    return None
            else:
                verNum=verNum+1
                verList[i]=str(verNum)
                verStr = ''
                for i in range(len(verList)):
                    if i==0:
                        verStr=verStr+verList[i]
                    else:
                        verStr=verStr+'.'+verList[i]

                return verStr
    else:
        print u"版本号格式错误...."
        return None

# 解决不同平的编码问题
def encodeforPlatform(str):
    if(sys.platform == 'win32'):
        return str.decode('UTF-8').encode('GBK')
    return str

# 根据本地的配置或者输入生成版本号
def genVersion(filePath):
    outAsserts = {}
    if (os.path.exists(filePath)):
        outAsserts = json.load(open(filePath,'rb'),object_pairs_hook=OrderedDict)
        versionOld = getVersionFromManifest(filePath)
        print ('curModule version:'+ versionOld)
    else:
        print ('not find curModule version file!!')
    autoGen = raw_input(encodeforPlatform('请选择是否自动生成版本号:(y/n), 请输入 : ')).lower()
    version = '1.0.0'
    if autoGen == 'y':
        if(os.path.exists(filePath)):
            versionOld = getVersionFromManifest(filePath)
            if versionOld == None:
                print u'未找到本地版本文件,请手动输入版本号'
            else:
                verList = versionOld.split('.')
                version = getAddVersionList(verList)
        else:
            return version
    else:
        version = raw_input(encodeforPlatform('请输入版本号:'))

    return version,outAsserts

# 读取manifest 文件并设置版本号
def readManifestAndSetVersion(file,version):
    f = file(file, 'rb')
    content = f.read()
    if(content):
        jsonVal = json.loads(content)
        jsonVal['version'] = version
        json.dumps(jsonVal)

# 筛选文件类型
def filterFileType(fileName):
    exeName = os.path.splitext(fileName)[1]
    for key in PlainTextFileTypes:
        if(key.lower() == exeName.lower() and PlainTextFileTypes[key]==1):
            return True
    return False

# 生成md5
def generatorMd5(filename):
    filename = filename.replace('\\', '/')
    size = os.path.getsize(filename)
    myhash = hashlib.md5()
    if filterFileType(filename):
        data = open(filename, 'r').read()
        data = data.replace('\r\n', '\n')
        myhash.update(data)
        open(filename, 'wb+').write(data)
    else:
        data = open(filename, 'rb').read()
        myhash.update(data)

    return myhash.hexdigest(),size

def versionGenerator(path,prefix,manifest,oldOutAsserts,changeLog):
    fl = os.listdir(path)
    for f in fl:
        if os.path.isdir(os.path.join(path,f)):
            if prefix == '':
                versionGenerator(os.path.join(path,f), f,manifest,oldOutAsserts,changeLog)
            else:
                versionGenerator(os.path.join(path,f), prefix + '/' + f,manifest,oldOutAsserts,changeLog)
        else:
            if filterPath(os.path.join(path,f) )!= None:
                md5,size = generatorMd5(os.path.join(path,f))
                relative = prefix + '/' + f
                manifest['assets'][relative] ={
                    'size':size,
                    'md5':md5
                }
                generatorChangLog(changeLog,oldOutAsserts,relative,md5)
                shotname, extension = os.path.splitext(f)
                if(extension.lower() == "zip"):
                    manifest['asserts'][relative]['compressed'] = True

# 生成回滚文件版本号
def generatorRollBackVersion(versionFile,curVersion):
    verList = curVersion.split('.')
    backVersion = getAddVersionList(verList)
    versionJson = json.load(open(versionFile,'rb'),object_pairs_hook=OrderedDict)
    oldVer = versionJson['version']
    print oldVer  + ' '+ backVersion
    versionJson['packageUrl']  = versionJson['packageUrl'].replace(oldVer, backVersion)
    versionJson['remoteManifestUrl'] = versionJson['remoteManifestUrl'].replace(oldVer, backVersion)
    versionJson['version'] = backVersion
    json.dump(versionJson, open(versionFile, "w+"), indent=4)
    return oldVer,backVersion


def filterPath(filePath):
    for i in range(len(filterArray)):
        str = filterArray[i]
        if filePath.find(str) != -1:
            return None
    return filePath

# 生成修改日志
def generatorChangLog(changeLog,oldOutAsserts,file,md5):
    if oldOutAsserts:
        if oldOutAsserts.has_key('assets'):
            md5Data = oldOutAsserts['assets'].get(file)
        if not md5Data:
            changeLog.append(file)
        elif md5Data['md5'] != md5:
            changeLog.append(file)


# 生成回滚
def generatorRollBACK(type,srcDir,curVersion):
    autoGen = raw_input(encodeforPlatform('请选择是否生成回滚包:(y/n), 请输入 : ')).lower()
    if autoGen == 'y':
        print u'生成回滚包.....'
        destDir = './RollBack'
        if (type == 'main'):
            srcDir = srcDir + '/' + type
            destDir = destDir + '/' + type
        else:
            srcDir = srcDir + '/sub/' + type
            destDir = destDir + '/sub/' + type

        if (os.path.exists(destDir)):
            removetree(destDir)
            #shutil.rmtree(destDir,onerror=onerror)
        if (os.path.exists(srcDir)):
            print 'copy %s to %s' % (srcDir, destDir)
            shutil.copytree(srcDir, destDir)
            verFilePath = destDir + '/version.manifest'
            oldVer, backVer = generatorRollBackVersion(verFilePath, curVersion)
            print u'回滚版本为:' + backVer
            if (os.path.exists(destDir + '/' + oldVer)):
                os.rename(destDir + '/' + oldVer, destDir + '/' + backVer)
                projectFilePath = destDir + '/' + backVer + '/project.manifest'
                generatorRollBackVersion(projectFilePath, curVersion)
            else:
                print u'生成回滚包失败...'
        else:
            print u'找不到上一个版本无法生成回滚包...'


                    
if __name__ == "__main__":
    print (u"解析配置文件....")
    updateConfig,keys = readyUpadteFiles()
    while True:
        print "..........................................................."
        for i in range(len(keys)):
            key = keys[i]
            print '(%d) module: %s' % (i, key)
        print '(%d) input this Index to exit' % (len(keys))
        print "..........................................................."
        selectIndex = int(raw_input(encodeforPlatform('请选择需要操作的模块, 输入数字编号 : ')))
        if selectIndex < len(keys) and  selectIndex>=0 :
            key = keys[selectIndex]
            if(key == 'main'):
                resPath = updateConfig[key]['res']
                versionFile = resPath + '/project.manifest'
                version,oldOutAsserts = genVersion(versionFile)
                rollbackSrc = updateConfig['outDir']
                if updateConfig.has_key('rollBack'):
                    rollbackSrc = updateConfig['rollBack']
                generatorRollBACK(key,rollbackSrc,version)
                initMainModule(updateConfig,version,oldOutAsserts)
            else:
                depMainVer = None
                isdep  = raw_input(encodeforPlatform('是否依赖主模块版本?(y/n) : '))
                if isdep=='y' or isdep=='Y':
                    depMainVer = raw_input(encodeforPlatform('请输入依赖主模块版本号 : '))

                resPath = updateConfig['modules'][key]['res']
                versionFile = resPath + '/project.manifest'
                version, oldOutAsserts = genVersion(versionFile)
                rollbackSrc = updateConfig['outDir']
                if updateConfig.has_key('rollBack'):
                    rollbackSrc = updateConfig['rollBack']
                generatorRollBACK(key, rollbackSrc, version)
                initSubModule(updateConfig, key, version, oldOutAsserts, depMainVer)
                print version
        elif selectIndex == len(keys) :
            break
        else:
            print u'输入有误请重试...'





