///
/// \file StringHelpers.cpp
/// \brief Méthode de la classe fichier
/// \version 0.1
/// \date 28 mai 2009
///

#include "StringHelpers.h"
#include <sstream> 
#include <assert.h>
#include <ctype.h>




namespace Utils
{



	///
	/// \brief Permet de trouvé une substring contenue entre \b strBefore et \b strAfter.
	/// \param[in]			strString	La string qui contient la string recherché
	/// \param[in]			strBefore	Ce qui précède la string recherché
	/// \param[in]			strAfter	Ce qui suit la string recherché
	/// \param[in, out]		nPos		En entrée, c'est l'index qui pointe vers le début de la recherche. En sorti c'est le pointeur qui pointe à la fin ou à npos si rien a été trouvé
	/// \return							string trouvé
	/// Fonction qui retourne le contenu entre 2 string  contenu dans une string et acceptant un iterateur (il retourne le premier trouvé)
	///
	std::string String::GetBetween(const std::string &strString, const std::string &strBefore, const std::string &strAfter, unsigned int &nPos)
	{
		unsigned int nStartIter = strString.find(strBefore, nPos); 	
		nPos=strString.find(strAfter, nStartIter+strBefore.size());
		if(nStartIter==std::string::npos || nPos==std::string::npos) //start or end of string not found
		{
			nPos=std::string::npos;
			return "";
		}

		return strString.substr(nStartIter+strBefore.size(), nPos-nStartIter-strBefore.size());
	}

	///
	/// \brief Permet de trouvé une substring contenue entre \b strBefore et \b strAfter.
	/// \param[in]			strString	La string qui contient la string recherché
	/// \param[in]			strBefore	Ce qui précède la string recherché
	/// \param[in]			strAfter	Ce qui suit la string recherché
	/// \return							
	/// Fonction qui retourne le contenu entre 2 string  contenu dans une string et acceptant un iterateur (il retourne le premier trouvé)
	///
	std::string String::GetBetween(const std::string &strString, const std::string &strBefore, const std::string &strAfter)
	{
		unsigned int nPos=0;
		return GetBetween(strString, strBefore, strAfter, nPos);
	}
	///
	/// \brief				Cette fonction retourne un vector de string avec tout ce qui est entre 2 string
	/// \param[in]			strSource	La string qui contient la string recherché
	/// \param[in]			strBefore	Ce qui précède la string recherché
	/// \param[in]			strAfter	Ce qui suit la string recherché
	/// \param[in]			strPosDebut	String du texte de début
	/// \param[in]			strPosFin	String du texte de fin
	/// \return							Array de string trouvé
	/// Fonction qui retourne le contenu entre 2 string  contenu dans une string et acceptant un iterateur (il retourne le premier trouvé)
	///
	StringArray String::GetArrayOfBetween(const std::string &strSource, const std::string &strBefore, const std::string &strAfter, std::string strPosDebut, std::string strPosFin)
	{
		return String::GetArrayOfBetween(strSource, strBefore, strAfter, strSource.find(strPosDebut), strSource.find(strPosFin));
	}

	///
	/// \brief				Cette fonction retourne un vector de string avec tout ce qui est entre 2 string
	/// \param[in]			strSource	La string qui contient la string recherché
	/// \param[in]			strBefore	Ce qui précède la string recherché
	/// \param[in]			strAfter	Ce qui suit la string recherché
	/// \param[in]			nPosDebut	Index de debut
	/// \param[in]			nPosFin	Index de fin
	/// \return							Array de string trouvé
	/// Fonction qui retourne le contenu entre 2 string  contenu dans une string et acceptant un iterateur (il retourne le premier trouvé)
	///
	StringArray String::GetArrayOfBetween(const std::string &strSource, const std::string &strBefore, const std::string &strAfter, unsigned int nPosDebut, unsigned int nPosFin)
	{
		StringArray strArray;

		if(nPosFin==0)
		{
			nPosFin=strSource.size();
		}
		if(nPosFin<nPosDebut)
		{
			assert(false);		
		}
		else
		{
			std::string strBuf=String::GetBetween(strSource, strBefore, strAfter, nPosDebut);
			while(nPosDebut!=std::string::npos && nPosFin!=std::string::npos && nPosDebut<nPosFin)
			{
				strArray.push_back(strBuf);
				strBuf=String::GetBetween(strSource, strBefore, strAfter, nPosDebut);
			}
		}	
		return strArray;
	}


	///
	/// \brief Fonction qui compte les occurences d'un caractère
	/// \param[in]		strSource		string source
	/// \param[in]		cFind			Char à trouvé
	/// \param[in]		nPos			à partir de quelle position
	/// \return Count qui est le nombre d'occurence
	///
	int String::Count(const std::string &strSource, char cFind, int nPos)
	{
		int nbCount=0;
		for(unsigned int i=nPos; i<strSource.length(); i++)
		{
			if(strSource[i]==cFind)
				nbCount++;
		}
		return nbCount;
	}


	///
	/// \brief Fonction qui compte les occurences d'un caractère
	/// \param[in]		strSource		string source
	/// \param[in]		strFind			string à trouvé
	/// \param[in]		nPos			à partir de quelle position
	/// \return Count qui est le nombre d'occurence
	///
	int String::Count(const std::string &strSource, const std::string &strFind, int nPos)
	{
		unsigned int nbCount=0;
		unsigned int nbLoc=nPos;

		while((nbLoc=strSource.find(strFind, nbLoc+1))!=std::string::npos)
			nbCount++;

		return nbCount;
	}

	int String::Count( const std::string &strSource, const std::string &strFind, int nPos, int nPosFin )
	{
		unsigned int nbCount=0;
		int nbLoc = nPos;

		while((nbLoc = strSource.find(strFind, nbLoc+1))!=std::string::npos && nbLoc<=nPosFin)
			nbCount++;

		return nbCount;
	}


	bool String::Contains(const std::string &strSource, const std::string &strToFind)
	{
		return strSource.find(strToFind)!=std::string::npos?true:false;
	}

	///
	/// \brief Fonction qui converti un int en string.
	/// \param[in]		nNumber		int a convertir
	/// \return			int d'entrée converti
	///
	std::string String::IntToString(int nNumber)
	{
		std::stringstream strStream;
		std::string strBuf;
		strStream << nNumber;
		strStream >> strBuf;
		return strBuf;
	}
	///
	/// \brief Fonction qui converti un unsigned int en string.
	/// \param[in]		nNumber		unsigned int a convertir
	/// \return			unsigned int d'entrée converti
	///
	std::string String::UIntToString(unsigned int nNumber)
	{
		std::stringstream strStream;
		std::string strBuf;
		strStream << nNumber;
		strStream >> strBuf;
		return strBuf;
	}

	///
	/// \brief Fonction qui converti un string en int.
	/// \param[in]		strSource	string a convertir
	/// \param[out]		nOut		Conteneur de la traduction
	/// \return			true en cas de réussite.
	///
	bool String::StringToInt(const std::string &strSource, int &nOut)
	{
		std::stringstream strStream;
		strStream<<strSource;
		return strStream>>nOut?true:false;
	}

	///
	/// \brief Fonction qui converti un string en unsigend int.
	/// \param[in]		strSource	string a convertir
	/// \param[out]		nOut		Conteneur de la traduction
	/// \return			true en cas de réussite.
	///
	bool String::StringToUInt(const std::string &strSource, unsigned int &nOut)
	{
		std::stringstream strStream;
		strStream<<strSource;
		return strStream>>nOut?true:false;
	}




	///
	/// \brief Fonction qui enlève les espaces, retour de chariot et nouvelle ligne au début et à la fin des strings.
	/// \param[in]		strSource Source à formater.
	/// \return			Le source formater.
	///
	std::string String::EnleveEspaceDebutFin(const std::string &strSource)
	{
		unsigned int nPosDebut=0;
		unsigned int nPosFin=strSource.size()-1;
		char tab(9);
		if(strSource.size()!=0)
		{
			while( (strSource[nPosDebut]==' ' || strSource[nPosDebut]=='\n' || strSource[nPosDebut]=='\r' || strSource[nPosDebut]==tab ) && nPosDebut<strSource.size())
				nPosDebut++;
			while((strSource[nPosFin]==' ' || strSource[nPosFin]=='\r' || strSource[nPosFin]=='\n' || strSource[nPosFin]==tab)&& nPosFin>0)
				nPosFin--;		}

		return nPosDebut<=nPosFin?strSource.substr(nPosDebut, nPosFin+1-nPosDebut):"";
	}

	///
	/// \brief Fonction qui sépare une string comprennant "sep" en deux substring sans le "sep". ex:  "GRANDE ALLEE/PINE" en "GRANDE ALLEE" et "PINE"
	/// \param[in]		strSource		Source à formater.
	/// \param[out]		strSub1			String cible pour la première substring
	/// \param[out]		strSub2			String cible pour la deuxième substring
	/// \param[in]		sep				Caractère qui sépare les deux strings.
	/// \return			True si il n'y a pas eu d'erreur
	///
	bool String::SeparerStringAvecChar(const std::string &strSource, std::string &strSub1, std::string &strSub2, char sep)
	{
		unsigned int nPosDebut=0;
		unsigned int nPosFin=strSource.find(sep);
		if (nPosFin == std::string::npos)
		{
			strSub1 = String::majPremiereLettreMots(String::EnleveEspaceDebutFin(strSource));
			strSub2 = "";
		}
		else if(nPosFin == (strSource.size()-1))
		{
			strSub1 = String::majPremiereLettreMots(String::EnleveEspaceDebutFin(strSource.substr(0, (strSource.size()-1))));
			strSub2 = "";
		}
		else if(nPosDebut == nPosFin)  //ex: /Terminus Longueuil
		{
			strSub1 = String::majPremiereLettreMots(String::EnleveEspaceDebutFin(strSource.substr(1, (strSource.size()-1))));
			strSub2 = "";
		}
		else
		{
			strSub1 = String::majPremiereLettreMots(String::EnleveEspaceDebutFin(strSource.substr(nPosDebut, nPosFin-nPosDebut)));
			strSub2 = String::majPremiereLettreMots(String::EnleveEspaceDebutFin(strSource.substr(nPosFin+1)));
		}
		return true;
	}

	///
	/// \brief Fonction qui met une lettre majuscule à chaque début de mot et met le reste des caractère en minuscule. ex:  "GRANDE ALLEE" en "Grande Allee"
	/// \param[in]		strSource		Source à formater.
	/// \return			La string formater
	///
	std::string String::majPremiereLettreMots(const std::string &strSource)
	{
		if(strSource.size()==0)
			return "";
		std::string phrase=String::EnleveEspaceDebutFin(strSource);
		unsigned int nPosFin=phrase.size()-1;
		if(phrase.size()==0)
			return "";
		for(unsigned int i = 0; i <= nPosFin; i++)
			phrase.at(i) = String::ToLowerChar(phrase.at(i));
		phrase.at(0)=String::ToUpperChar(phrase.at(0));
		for(unsigned int nPosDebut=0; nPosDebut <= nPosFin; nPosDebut++)
		{
			if(phrase.at(nPosDebut) == ' ' && phrase.at(nPosDebut+1) != ' ')
				phrase.at(nPosDebut+1) = String::ToUpperChar(phrase.at(nPosDebut+1));
		}
		return phrase;
	}

	///
	/// \brief Fonction qui convertis un char Majuscule en miniscule. ex:  É -->  é   ou    O  --->o o -->o  
	/// \test Hack du char set windows potentiellement non portable ?
	/// \param[in]		cChar		Source à formater.
	/// \return			La char formaté
	///
	char String::ToLowerChar(const char &cChar)
	{
		char cCharResponse=cChar;
		if(cChar>0)
			cCharResponse=tolower(cChar);
		else if(cChar<-32 && cChar>-65)
			cCharResponse=cChar+32;

		return cCharResponse;
	}

	///
	/// \brief Fonction qui convertis un char miniscules en majuscule.  ex:  é  -->  É   ou    o -->O    O  --> O
	/// \test Hack du char set windows potentiellement non portable ?
	/// \param[in]		cChar		Source à formater.
	/// \return			La char formaté
	///
	char String::ToUpperChar(const char &cChar)
	{
		char cCharResponse=cChar;
		if(cChar>0)
			cCharResponse=toupper(cChar);
		else if(cChar<0 && cChar>-33)
			cCharResponse=cChar-32;

		return cCharResponse;

	}

	///
	/// \brief Fonction qui enleve un string dans la string en entrée
	/// \param[in]		strSource		String source
	/// \param[in]		strRemove		La string a enleve
	/// \return							La string strSource sans strRemove
	///
	std::string String::RemoveString(const std::string &strSource, const std::string &strRemove)
	{
		std::string strStringReturn(strSource);
		unsigned int iterator=0;
		while((iterator=strStringReturn.find(strRemove, iterator))!=std::string::npos)
		{
			std::string strBuf(strStringReturn);
			strStringReturn=strBuf.substr(0, iterator)+strBuf.substr(iterator+strRemove.size(), strBuf.size()-iterator);
		}
		return strStringReturn;
	}



	///
	/// \brief Fonction qui permet de remplacer toutes les occurences d'une string par une autre
	/// \param[in]		strSource		String source
	/// \param[in]		strFind			String qu'on veut remplacer 
	/// \param[in]		strReplace		String de remplacement
	/// \return							La string strSource sans les encodages html
	///
	std::string String::Replace(const std::string &strSource, const std::string &strFind, const std::string &strReplace)
	{
		std::string strReturn(strSource);
		unsigned int pos=0;
		while((pos=strReturn.find(strFind, pos))!=std::string::npos)
		{
			strReturn.replace(pos, strFind.size(),strReplace);
			pos+=strReplace.size();
		}
		return strReturn;
	}

	///
	/// \brief Fonction qui enlève les espaces, retour de chariot et nouvelle ligne au début et à la fin d'un StringArray
	/// \param[in]		strSource Source à formater.
	/// \return			Le source formater.
	///
	StringArray String::EnleveEspaceDebutFin(const StringArray &strSourceArray)
	{	
		StringArray strReturnArray;
		for(unsigned int iterator=0;iterator<strSourceArray.size(); iterator++) 
		{	
			strReturnArray.push_back(EnleveEspaceDebutFin(strSourceArray.at(iterator)));
		}

		return strReturnArray;
	}

	StringArray String::SplitInLines(const std::string& pages)
	{
		StringArray sSplit;
		if(!pages.empty())
		{
		std::string separator("\r\n");
		//Verify if we have atleast one windows separator. 
		//Else, we will use linux\mac separator. If we want to handle mixed separator, we will have to do a more complex code
		if(!String::Contains(pages, separator)) 
			separator = "\n";
		sSplit = String::Split(pages, separator);
		while(!sSplit.empty() && sSplit.at(sSplit.size()-1) == "") // Remove the last line if empty
			sSplit.pop_back();
		}

		return sSplit;
	}


	///
	/// \brief Fonction qui sépare une ligne autour d'une string (voir split dans Java ou autres langage)
	/// \param[in]		strSource Source à splitter
	/// \param[in]		strSplitString String utilisé pour splitter
	/// \return			UN tableau des string splitter
	///
	StringArray String::Split(const std::string &strSource, std::string strSplitString, unsigned int nPosStart)
	{
		StringArray strReturnArray;
		if(!strSource.empty())
		{
			unsigned int nPosEnd=strSource.find(strSplitString, nPosStart);
			unsigned int countSplit = Utils::String::Count(strSource,strSplitString, nPosStart);
			strReturnArray.reserve(countSplit);
			while (nPosEnd!=std::string::npos && nPosStart!=std::string::npos)
			{		
				std::string strWord(strSource.substr(nPosStart, nPosEnd-nPosStart));
				//if(strWord!="") //Ceci n'est pas acceptable car nous ne pouvons parser convenablement des strings comme: "Sainte-Thérèse,60,3,,CITLA,,,60"
				strReturnArray.push_back(strWord);

				nPosStart=nPosEnd+strSplitString.size();
				nPosEnd=strSource.find(strSplitString, nPosStart);
			}
			//On ramasse la dernière séquence si elle finit pas par le string split...
			if(nPosStart<strSource.size())
			{
				strReturnArray.push_back(strSource.substr(nPosStart, strSource.size()-nPosStart));
			}
			else
			{
				assert(strSource.substr(strSource.size()-strSplitString.size(), strSplitString.size()) == strSplitString);
				strReturnArray.push_back(""); //Séquence fini par string split
			}
		}


		return strReturnArray;

	}


	//http://www.blooberry.com/indexdot/html/tagpages/o/object.htm
	std::string String::UrlEncode( const std::string &strSource )
	{
		std::string strUrlCorrected = String::Replace(strSource, "%", "%25"); //Must be the first !
		strUrlCorrected = String::Replace(strUrlCorrected, "$", "%24");
		strUrlCorrected = String::Replace(strUrlCorrected, "&", "%26");
		strUrlCorrected = String::Replace(strUrlCorrected, "+", "%2B");
		strUrlCorrected = String::Replace(strUrlCorrected, ",", "%2C");
		strUrlCorrected = String::Replace(strUrlCorrected, "/", "%2F");
		strUrlCorrected = String::Replace(strUrlCorrected, ":", "%3A");
		strUrlCorrected = String::Replace(strUrlCorrected, ";", "%3B");
		strUrlCorrected = String::Replace(strUrlCorrected, "=", "%3D");
		strUrlCorrected = String::Replace(strUrlCorrected, "?", "%3F");
		strUrlCorrected = String::Replace(strUrlCorrected, "@", "%40");
		strUrlCorrected = String::Replace(strUrlCorrected, " ", "%20");	
		strUrlCorrected = String::Replace(strUrlCorrected, "#", "%23");
		strUrlCorrected = String::Replace(strUrlCorrected, "<", "%3C");
		strUrlCorrected = String::Replace(strUrlCorrected, ">", "%3E");
		strUrlCorrected = String::Replace(strUrlCorrected, "\"", "%22");
		strUrlCorrected = String::Replace(strUrlCorrected, "{", "%7B");
		strUrlCorrected = String::Replace(strUrlCorrected, "}", "%7D");
		strUrlCorrected = String::Replace(strUrlCorrected, "|", "%7C");
		strUrlCorrected = String::Replace(strUrlCorrected, "\\", "%5C");
		strUrlCorrected = String::Replace(strUrlCorrected, "^", "%5E");
		strUrlCorrected = String::Replace(strUrlCorrected, "~", "%7E");
		strUrlCorrected = String::Replace(strUrlCorrected, "[", "%5B");
		strUrlCorrected = String::Replace(strUrlCorrected, "]", "%5D");
		strUrlCorrected = String::Replace(strUrlCorrected, "`", "%60");
		return strUrlCorrected;
	}



} //Fin de l'espace de nom
