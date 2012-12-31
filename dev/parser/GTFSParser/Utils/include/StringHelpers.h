#ifndef STRINGHELPERS_H
#define STRINGHELPERS_H


#include <string>
#include <vector>
#include <sstream>



typedef std::vector<std::string> StringArray;

///
/// \brief Permet d'aider avec les strings
namespace Utils
{
namespace String
{
	
	StringArray SplitInLines(const std::string& pages);

	std::string GetBetween(const std::string &strString, const std::string &strBefore, const std::string &strAfter, unsigned int &nPos);
	std::string GetBetween(const std::string &strString, const std::string &strBefore, const std::string &strAfter);
	StringArray GetArrayOfBetween(const std::string &strSource, const std::string &strBefore, const std::string &strAfter, std::string strPosDebut, std::string strPosFin);
	StringArray GetArrayOfBetween(const std::string &strSource, const std::string &strBefore, const std::string &strAfter, unsigned int nPosDebut=0, unsigned int nPosFin=0);
	bool Contains(const std::string &strSource, const std::string &strToFind);
	int Count(const std::string &strSource, char cFind, int nPos);
	int Count(const std::string &strSource, const std::string &strFind, int nPos);
	int Count(const std::string &strSource, const std::string &strFind, int nPos, int nPosFin);
	template <class T>
	std::string toString(T data)
	{
		std::stringstream strStream;
		std::string strBuf;
		strStream << data;
		strStream >> strBuf;
		return strBuf;
	}

	template <class T>
	bool StringToType(const std::string& data, T& out)
	{
		std::stringstream strStream;
		strStream << data;
		return strStream >> out?true:false;
	}


	std::string IntToString(int nNumber);
	std::string UIntToString(unsigned int nNumber);
	bool StringToInt(const std::string &strSource, int &nOut);
	bool StringToUInt(const std::string &strSource, unsigned int &nOut);
	std::string EnleveEspaceDebutFin(const std::string &strSource);

	bool SeparerStringAvecChar(const std::string &strSource, std::string &strSub1, std::string &strSub2, char sep = '/');
	std::string majPremiereLettreMots(const std::string &strSource);
	std::string RemoveString(const std::string &strSource, const std::string &strRemove);
	StringArray Split(const std::string &strSource, std::string strSplitString, unsigned int nPos=0);
	std::string Replace(const std::string &strSource, const std::string &strFind, const std::string &strReplace);
	std::string UrlEncode(const std::string &strSource);



	//char Methods
	char ToLowerChar(const char &cChar);
	char ToUpperChar(const char &cChar);

	//Version StringArray
	StringArray EnleveEspaceDebutFin(const StringArray &strSourceArray);
}
}



#endif //STRINGHELPERS
