///
/// \file ExtraInfoHelpers.cpp
/// \brief Méthode de la classe ExtraInfoHelpers
/// \version 0.1
/// \date 14 juillet 2009
///
#include "ExtraInfoHelpers.h"
#include "StringHelpers.h"
#include "HTMLHelpers.h"
#include <iostream>
#include <fstream>
#include "FichierHelpers.h"
#include "Logger.h"
//TODO: Remplacer ça par une map ???
ExtraInfoHelpers::ExtraInfoHelpers(int nCode_Default)
: m_Code_Default(nCode_Default)
, m_Incrementor(1)
{
}


std::string ExtraInfoHelpers::ExtraInfoStringToCode(const std::string &strString)
{
	std::string strCode("");
	if(Utils::String::Contains(strString, "ouest") && strString.size()<10) //bug stm (todo: Add link)
		strCode="";
	else if(Utils::String::Contains(strString, "est") && strString.size()<10) //bug stm
		strCode="";
	else if(Utils::String::Contains(strString, "Ouest") && strString.size()<10) //bug stm
		strCode="";
	else if(Utils::String::Contains(strString, "Est") && strString.size()<10 ) //bug stm
		strCode="";
	else if(Utils::String::Contains(strString, "NULL"))  //bug stm
		strCode="";
	else if(Utils::String::Contains(strString, "lundi") || Utils::String::Contains(strString, "Lundi") || Utils::String::Contains(strString, "LUNDI"))
		strCode="";
	else if(Utils::String::Contains(strString, "mardi") || Utils::String::Contains(strString, "Mardi") || Utils::String::Contains(strString, "MARDI"))
		strCode="";
	else if(Utils::String::Contains(strString, "mercredi") || Utils::String::Contains(strString, "Mercredi") || Utils::String::Contains(strString, "MERCREDI"))
		strCode="";
	else if(Utils::String::Contains(strString, "jeudi") || Utils::String::Contains(strString, "Jeudi") || Utils::String::Contains(strString, "JEUDI"))
		strCode="";
	else if(Utils::String::Contains(strString, "vendredi") || Utils::String::Contains(strString, "Vendredi") || Utils::String::Contains(strString, "VENDREDI"))
		strCode="";
	else if(Utils::String::Contains(strString, "samedi") || Utils::String::Contains(strString, "Samedi") || Utils::String::Contains(strString, "SAMEDI"))
		strCode="";
	else if(Utils::String::Contains(strString, "dimanche") || Utils::String::Contains(strString, "Dimanche")|| Utils::String::Contains(strString, "DIMANCHE"))
		strCode="";
	else
	{
		//Why don't we use a map ???
		std::string strCleanString(Utils::String::EnleveEspaceDebutFin(Utils::Html::HtmlDecode(strString)));
		if(strCleanString!="")
		{
			std::list< std::pair<std::string,bool> >::iterator iter=m_List.begin();
			unsigned int i=1;
			while(strCode=="" && iter!=m_List.end())
			{
				if(iter->first == strCleanString)
				{
					strCode=Utils::String::UIntToString(m_Code_Default+i);
					iter->second = true;
					//std::cout<<"Vieux Code Extra Info:"<<strCode<<std::endl;
				}
				++i;
				++iter;
			}
			if(strCode=="")
			{
				m_List.push_back(std::pair<std::string, bool>(strCleanString, true));
				strCode=Utils::String::UIntToString(m_Code_Default+i);
				//std::cout<<"Nouveau Code Extra Info:"<<strCode<<std::endl;
			}
		}		
	}

	return strCode;
}


void ExtraInfoHelpers::LoadExtraInfoList(const std::string& filename)
{
	std::ifstream t(filename.c_str());
	std::string str;
	if (t.is_open())
	{
		t.seekg(0, std::ios::end);   
		str.reserve(t.tellg());
		t.seekg(0, std::ios::beg);
		str.assign((std::istreambuf_iterator<char>(t)),	std::istreambuf_iterator<char>());
		t.close();
		StringArray codeList = Utils::String::SplitInLines(str);
		for (unsigned int i = 0; i < codeList.size(); ++i)
		{
			StringArray codeStringMap = Utils::String::Split(codeList.at(i), ";");
			m_List.push_back(std::pair<std::string, bool>(codeStringMap.at(1), false)); //Attention, l'assomption que les codes sont en ordre croissant est faite ici (ce qui est toujours le cas présentement)
			LOGINFO("Adding " << codeStringMap.at(1) << " to map.")	;
		}
	}
	else
		LOGWARNING("No dba provided. Creating new codes...");
	
	
}


std::string ExtraInfoHelpers::GetExtraInfoList(bool includeUnusedCode)
{
	std::string strReturn;
	std::list< std::pair<std::string, bool> >::iterator iter=m_List.begin();
	unsigned int i=1;
	while(iter!=m_List.end())//std::pair<std::string, bool>(strCleanString, true)
	{			
		if(includeUnusedCode || iter->second) // Write only the important one
		{
			strReturn.append(Utils::String::UIntToString(i+m_Code_Default)+";"+(iter->first)+";\n");			
		}
		++i;
		++iter;
	}
	return strReturn;
}

std::string ExtraInfoHelpers::ExtraInfoCodeToString(const std::string &code)
{
	std::string returnStr("");
	unsigned int iCode;
	std::list<std::pair<std::string,bool>>::iterator iter = m_List.begin();
	if(Utils::String::StringToUInt(code, iCode))
	{
		int posInList = iCode - m_Code_Default-1;
		for(int i=0; i<posInList; ++i)
		{
			++iter;
		}
		returnStr = iter->first;		
	}
	return returnStr;
}

void ExtraInfoHelpers::PrependString(const std::string& _str)
{
	std::list<std::pair<std::string,bool>>::iterator iter=m_List.begin();

	while(iter!=m_List.end())
	{			
		(iter->first) = _str + iter->first;
		++iter;
	}
}