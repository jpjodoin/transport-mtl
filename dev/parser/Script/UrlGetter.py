import urllib2
import string
import re
class UrlGetter:
    """A simple class to contains information useful for a transport service"""
    def __init__(self):
        print 'UrlGetter'
    def getUrl(self, url):
        print url
        return url


class RTLUrlGetter:
    def __init__(self):
        print 'RTLUrlGetter'
        
    def getUrl(self, dummy):

        req = urllib2.Request('http://www.rtl-longueuil.qc.ca/fr-CA/donnees-ouvertes/fichiers-gtfs/')
        req.add_header('User-Agent', "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
        f = urllib2.urlopen(req)
        page=f.read()
        regex = ur"/CMS/MediaFree/file/GTFS/(.+?).zip"
        fileName = re.findall(regex, page);
        date = map(int, fileName)
        maxDate =  max(date)
        link = "http://www.rtl-longueuil.qc.ca/CMS/MediaFree/file/GTFS/"+ str(maxDate) +".zip"
        return link        
        """req = urllib2.Request('http://www.rtl-longueuil.qc.ca/W1/Booking.asp')
        req.add_header('Referer', 'http://www.rtl-longueuil.qc.ca/W1/gtfs.htm');
        req.add_header('User-Agent', "Mozilla/5.0 (X11; U; Linux i686) Gecko/20071127 Firefox/2.0.0.11");
        f = urllib2.urlopen(req)

        
        y=f.read()
        annees_dispo=list()
        position=string.find(y,"<a href=\"")
        while position!=-1:    
            annees_dispo.append(int(y[position+len("<a href=\""):position+len("<a href=\"")+4]))
            position=string.find(y,"<a href=\"", position+1)
        annee=max(annees_dispo)
        lien="http://www.rtl-longueuil.qc.ca/W1/"+str(annee)+".htm"
        

        req2 = urllib2.Request(lien)
        f2 = urllib2.urlopen(req2)
        y2=f2.read()
        position1=string.find(y2,"<a href=\"")
        position2=string.find(y2,"\">", position1)
        urldispo=list()
        datesdispo=list()
        while position1!=-1:
            urldispo.append(y2[position1+len("<a href=\""):position2])
            datesdispo.append(int(y2[position2+len("\">"):position2+len("\">")+8]))
            position1=string.find(y2,"<a href=\"", position1+1)
            position2=string.find(y2,"\">", position2+1)
        dernieredate=max(datesdispo)
        dernierurl="http://www.rtl-longueuil.qc.ca/W1/" + str(annee)+"/"+str(dernieredate)+".zip"
        #dernierurl="http://www.rtl-longueuil.qc.ca/W1/2012/20120402.zip"# + str(annee)+"/"+str(dernieredate)+".zip"
        print dernierurl
        return dernierurl"""

