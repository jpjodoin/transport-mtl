# -*- coding: utf-8 -*-
import urllib2
from HTMLParser import HTMLParser
import re

class DirStartEnd():
    def __init__(self):
        self.circuit_name = ""
        self.dir_name = ""
        self.week_start = ""
        self.week_end = ""
        self.sathurday_start = ""
        self.sathurday_dir_end = ""
        self.sunday_start = ""
        self.sunday_end = ""

class StartEndTimeParser(HTMLParser):
    def __init__(self):
        HTMLParser.__init__(self)
        self.insideTD = False
        self.dataStart = False
        self.directionList = list();
        self.groupIdx = 0;
        #self.dir1 = DirStartEnd()
        #self.dir2 = DirStartEnd()
        self.idx = 0
        self.circuitList = list()

    def handle_starttag(self, tag, attrs):
        if tag == "td":
            self.insideTD = True
        #:
           # print
            #print "Start tag:", tag
            #for attr in attrs:
                #print "     attr:", attr
    def handle_data(self, data):
        if self.insideTD == True and not data.isspace():
            if "Ligne " in data:
                self.circuitList.append(data)

            
            metrolist = ["Angrignon", "Honoré-Beaugrand","Montmorency","Côte-Vertu","Longueuil","Berri-UQAM","Snowdon","Saint-Michel"]
            if "Vers" in data and any(metro in data for metro in metrolist):
                self.dataStart = True            
            if self.dataStart == True:
                n = self.idx
                if n == 0:
                    self.directionList.append(DirStartEnd())
                    self.directionList.append(DirStartEnd())
                    self.directionList[self.groupIdx].dir_name = data.replace("Vers ", "")
                    circuitName = re.sub("[^0-9]", "",(self.circuitList[self.groupIdx/2]))
                    self.directionList[self.groupIdx].circuit_name = circuitName
                    self.directionList[self.groupIdx+1].circuit_name = circuitName
                elif n == 1:
                    self.directionList[self.groupIdx+1].dir_name = data.replace("Vers ", "")
                elif n == 7:
                     self.directionList[self.groupIdx].week_start = data.replace(' ', '')
                elif n == 8:
                     self.directionList[self.groupIdx].week_end = data.replace(' ', '')
                elif n == 9:
                    self.directionList[self.groupIdx+1].week_start = data.replace(' ', '')
                elif n == 10:
                    self.directionList[self.groupIdx+1].week_end = data.replace(' ', '')
                elif n == 12:
                     self.directionList[self.groupIdx].sathurday_start = data.replace(' ', '')
                elif n == 13:
                     self.directionList[self.groupIdx].sathurday_end = data.replace(' ', '')
                elif n == 14:
                    self.directionList[self.groupIdx+1].sathurday_start = data.replace(' ', '')
                elif n == 15:
                    self.directionList[self.groupIdx+1].sathurday_end = data.replace(' ', '')
                elif n == 17:
                     self.directionList[self.groupIdx].sunday_start = data.replace(' ', '')
                elif n == 18:
                     self.directionList[self.groupIdx].sunday_end = data.replace(' ', '')
                elif n == 19:
                    self.directionList[self.groupIdx+1].sunday_start = data.replace(' ', '')
                elif n == 20:
                    self.directionList[self.groupIdx+1].sunday_end = data.replace(' ', '')
                    self.idx = -1 # Autoincrement 
                    self.dataStart = False
                    self.groupIdx = self.groupIdx+2
                    
                #print str(self.idx) + " " +  data
                self.idx = self.idx + 1
            #else:
                #print data

    def handle_endtag(self, tag):
        if tag == "td":
            self.insideTD = False



def GetStartEndTime(url):
    parser = StartEndTimeParser()
    req2 = urllib2.Request(url)
    f2 = urllib2.urlopen(req2)
    text=f2.read()
    parser.feed(text)
    #print len(parser.directionList)
    for dir in parser.directionList:
        if 'Longueuil' in dir.dir_name:
            dir.dir_name = 'Longueuil-Université de Sherbrooke'
    return parser.directionList

#GetStartEndTime("http://www2.stm.info/metro/M11.htm")

