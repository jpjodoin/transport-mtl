///
/// \file ExtraInfo.h
/// \brief Déclaration de la classe qui convertis des string extra info en code
/// \version 0.1
/// \date 28 mai 2009
///

#ifndef EXTRAINFOHELPERS_H
#define EXTRAINFOHELPERS_H

#include "Exports.h"

#ifndef _STRING_
#include <string>
#endif

#ifndef _MAP_
#include <map>
#endif 

#ifndef _LIST_
#include <list>
#endif 

class RHATECCOREDLL_API ExtraInfoHelpers
{
public:
	ExtraInfoHelpers(int nCode_Default=100);
	std::string ExtraInfoStringToCode(const std::string &strString);
	std::string ExtraInfoCodeToString(const std::string &strString);
	std::string GetExtraInfoList(bool includeUnusedCode = false);
	void LoadExtraInfoList(const std::string& filename);
	void PrependString(const std::string& _str);


private:
	std::list<std::pair<std::string, bool>> m_List;
	unsigned int m_Incrementor;
	unsigned int m_Code_Default;
	

};



#endif

