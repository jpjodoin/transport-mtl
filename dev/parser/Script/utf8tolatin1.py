import codecs, os

def ConvertUTF8FolderToLatin1(path):
    listing = os.listdir(path)
    for infile in listing:
        if infile != '__Licence.txt':
            print 'Processing ' + infile
            fullFilePath = path+infile
            fr = codecs.open(fullFilePath, 'r', "utf-8")
            title = fr.read()
            fr.close()
            processedString = title.encode('latin_1', 'ignore')
            # En cas de manque de memoire, modifier binaire python pour etre Large Adress Aware
            os.linesep.join([s for s in processedString.splitlines() if s])
            #Ligne pour regler un bogue bizarre avec la stm ou j'ai des \r\r\n apres le remplacement...
            processedString = processedString.replace('\r\n', '\n')
            fw = open(fullFilePath, 'w')
            fw.write(processedString)
            fw.close()
            print 'done'
