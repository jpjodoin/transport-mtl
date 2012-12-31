import urllib2


def GetFileFromUrl(savepath, url):
    print url
    filename = url.split('/')[-1]
    u = urllib2.urlopen(url)
    f = open(savepath+"/"+filename, 'wb')
    meta = u.info()
    file_size = int(meta.getheaders("Content-Length")[0])
    print "Telechargement de: %s Fichier: %s ko" % (filename, file_size/1000)

    file_size_dl = 0
    block_sz = 8192
    while True:
        buffer = u.read(block_sz)
        if not buffer:
            break

        file_size_dl += len(buffer)
        f.write(buffer)
        status = r"%10d ko [%3.2f%%]" % (file_size_dl/1000, file_size_dl * 100. / file_size)
        status = status + chr(8)*(len(status)+1)
        print status,

    f.close()
    return savepath+"/"+filename

def CheckDateFile(url):
    req = urllib2.Request(url)
    url_handle = urllib2.urlopen(req)
    headers = url_handle.info() 
    etag = headers.getheader("ETag")
    last_modified = headers.getheader("Last-Modified")
    return last_modified


