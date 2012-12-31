class VersionManager:
    """A simple class to contains information useful for a transport service"""
    def __init__(self):
        f = open('version.txt', 'r+')
        data = f.read()
        f.close()
        transportdate = data.split('\n')
        pairlist = [];
        for line in transportdate:
            if line != "":
                val = line.split(';')
                t = tuple(val)
                pairlist.append(t)
        self.transportDateMap = dict(pairlist)

    def getDate(self, name):
        return self.transportDateMap.get(name, 0);

    def setDate(self, name, date):
        self.transportDateMap[name] = date;
        

    def save(self):        
        f = open('version.txt', 'w+')
        for k,v in self.transportDateMap.iteritems():
            f.write(k+";"+str(v)+"\n")
        f.close()
