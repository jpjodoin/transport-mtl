# -*- coding: utf-8 -*-
import urllib2
from HTMLParser import HTMLParser

class MetroListParser(HTMLParser):
    def __init__(self):
        HTMLParser.__init__(self)
        self.metroList = list()
        
    def handle_starttag(self, tag, attrs):
        if tag == "area":
            #print "Start tag:", tag
            alt = ""
            href = ""
            for attr in attrs:
                if attr[0] == "alt":
                    alt = attr[1]
                elif attr[0] == "href":
                    href = attr[1]
            if href != "" and alt != "" and (href[0] =='M'or href[0] =='m'):
                self.metroList.append((href, alt))

def GetMetroList(): 
    # Get Station List
    req2 = urllib2.Request("http://www2.stm.info/metro/mapmetro.htm")
    f2 = urllib2.urlopen(req2)
    text=f2.read()
    parser = MetroListParser()
    parser.feed(text)
    uniqueSet = set(parser.metroList)
    #print uniqueSet
    parser.close()
    return uniqueSet
    #print text
