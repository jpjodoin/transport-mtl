#ifndef FILE_DATABASE_CREATOR_H
#define FILE_DATABASE_CREATOR_H

#include "Exports.h"

#include <string>

#include "DatabaseCreator.h"

class RHATECCOREDLL_API FileDatabaseCreator: public DatabaseCreator
{

public:
	FileDatabaseCreator(Information info, const std::string& folderPrefix = "");

	virtual ~FileDatabaseCreator();
protected:
	void ReadFile(const std::string& fileName);

private:
	std::string m_folderPathPrefix;

};



#endif 



