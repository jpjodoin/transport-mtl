# -*- coding: utf-8 -*-
from GetMetroList import GetMetroList
from GetStartEndTime import GetStartEndTime

def WriteMetroStartAndEndTimes(folder):
    fWriter = open(folder + '/' + 'metrodebfin.txt', 'w')
    metroList = GetMetroList()
    for metro in metroList:
        # Function
        stationName = metro[1]
        if 'Longueuil' in stationName:
            stationName = 'Longueuil-Université de Sherbrooke'
        
        print metro[0] + " " + stationName
        url = "http://www2.stm.info/metro/"+metro[0]
        dirList =GetStartEndTime(url)
        for direction in dirList:
            fWriter.write(direction.circuit_name + ";" + stationName + ";" + direction.dir_name + ";" + direction.week_start + ";" + direction.week_end + ";" + direction.sathurday_start + ";" + direction.sathurday_end + ";" + direction.sunday_start+  ";" + direction.sunday_end + "\n")
    fWriter.close()

#WriteMetroStartAndEndTimes("./")
