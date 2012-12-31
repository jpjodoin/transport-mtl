# -*- coding: cp1252 -*-
import os, utf8tolatin1, ziputility, shutil, transportconfig, urllib, urllib2, Download, UrlGetter, reflection, calendarpreprocess
from versionmanager import *
from WriteMetroStartAndEndTimes import WriteMetroStartAndEndTimes

def getUrl(className, BaseUrl):
	c = reflection.get_class("UrlGetter."+className)
	instance = c()
	return instance.getUrl(BaseUrl)



msg = ""
opener = urllib.FancyURLopener({})
v = VersionManager()
#Prepare folder for GTFS acquisition
gtfsCacheFolder = "gtfs_archive";
shutil.rmtree(gtfsCacheFolder, True)
os.mkdir(gtfsCacheFolder)

oldDbaCacheFolder = "old_dba"
shutil.rmtree(oldDbaCacheFolder, True)
os.mkdir(oldDbaCacheFolder)

extraInfoPersistance = "extrainfodb"
if not os.path.exists(extraInfoPersistance):
        os.mkdir(extraInfoPersistance)



loggingLevel = "-v"
listing = os.listdir("./conf")
for infile in listing:
    #Read config from file
    ts = transportconfig.TransportConfig("./conf/"+infile) #This will come when we iterate through the conf folder...

    #Get GTFS adress
    gtfsAdress = getUrl(ts.urlgetter, ts.baseurl)
    #Check if GTFS is more recent
    datelastdownloadedGTFS=v.getDate(ts.shortName)
    dateCurrentGTFS=Download.CheckDateFile(gtfsAdress)
    if datelastdownloadedGTFS!=dateCurrentGTFS:
            #envoyer un courriel pour avertir
            msg += "Nouvelle version pour "  + ts.shortName + " "+ ts.longName + " " + dateCurrentGTFS + "\nVersion précédente : " +str(datelastdownloadedGTFS)+ "\n"
            print "Nouvelle dba pour " + ts.shortName

            #Faire la persistance des extra infos
            extrainfodbPath = extraInfoPersistance+"/"+ts.shortName+"extrainfocode.dba"
            if not os.path.exists(extrainfodbPath):
                    oldDbaName = opener.open("http://www.rhatec.net/update/transportmtlv2/update.php?l=fr&2=" + ts.shortName).read()
                    oldDBAZipPath = Download.GetFileFromUrl(oldDbaCacheFolder, "http://www.rhatec.net/update/transportmtlv2/"+oldDbaName)
                    pathToUncompressedDBAFile = oldDbaCacheFolder + "/"+ ts.shortName+"/"
                    ziputility.decompressArchive(oldDBAZipPath, oldDbaCacheFolder)
                    shutil.copyfile(pathToUncompressedDBAFile+"listeextrainfodirection.dba",  extrainfodbPath)

            #Get GTFS file
            gtfsZipPath=Download.GetFileFromUrl("./", gtfsAdress)
            pathToUncompressedGTFSFile = gtfsCacheFolder + "/"+ ts.shortName+"/"
            os.mkdir(pathToUncompressedGTFSFile)
            ziputility.decompressArchive(gtfsZipPath, pathToUncompressedGTFSFile)
            if ts.shortName == 'stm':                    
                    WriteMetroStartAndEndTimes(pathToUncompressedGTFSFile) #STM Refuse de fournir des horaires fiable pour la STM.
            
            #Converting GTFS to the correct caracter encoding
            print("Converting caracters to an appropriate set\n")
            utf8tolatin1.ConvertUTF8FolderToLatin1(pathToUncompressedGTFSFile)
            if ts.shortName == 'stm':
                    calendarpreprocess.ChangeCalendarFromDayToDayCode(pathToUncompressedGTFSFile)


            #Preparing to do the conversion from GTFS to our format
            commandLine = "GTFSParsingServer.exe " + pathToUncompressedGTFSFile + " " + ts.shortName + " " + ts.longName + " " + ts.transportType + " " + ts.displayMap + " " + ts.displaySearch + " " + ts.usestop_id + " " + ts.usecolor + " " + loggingLevel;
            print("Running " + commandLine)
            os.system(commandLine)

            #Record GTFS date
            v.setDate(ts.shortName,dateCurrentGTFS)

v.save()

#Clean repository
#shutil.rmtree(gtfsCacheFolder, True)



