import zipfile, os, errno, shutil;

def decompressArchive(archivePath, destPath):
    if not zipfile.is_zipfile(archivePath):
        print archivePath + "is not a valid zip file"
    else:
        zfile = zipfile.ZipFile( archivePath, "r" )
        zfile.printdir()


        for info in zfile.infolist():
            fname = info.filename
            if fname.endswith('/'):
                    os.mkdir(os.path.join(destPath, fname))
            else:
            # decompress each file's data
                    data = zfile.read(fname)

                    fout = open(os.path.join(destPath, fname), "w")
                    fout.write(data)
                    fout.close()
                    print "Uncompressed file : %s" % fname



