#include "FileDatabaseCreator.h"
#include <string>
#include <fstream>
#include <streambuf>

#include "Logger.h"

FileDatabaseCreator::FileDatabaseCreator(Information info, const std::string& folderPrefix)
: DatabaseCreator(info)
, m_folderPathPrefix(folderPrefix)
{	
	
	
}
FileDatabaseCreator::~FileDatabaseCreator()
{

}


void FileDatabaseCreator::ReadFile(const std::string& fileName)
{
	LOGDEBUG("Reading file " << fileName);
	//todo: support utf8-encoding. ATM we must preprocess the file with python
	std::string fullFilePath = m_folderPathPrefix + fileName;
	std::ifstream t(fullFilePath.c_str());
	std::string str;
	if (t.is_open())
	{
		t.seekg(0, std::ios::end);   
		str.reserve(t.tellg());
		t.seekg(0, std::ios::beg);
		str.assign((std::istreambuf_iterator<char>(t)),	std::istreambuf_iterator<char>());
		t.close();
	}
	LOGDEBUG("Reading file " << fileName << " done.");
	RxData(str);
	
}