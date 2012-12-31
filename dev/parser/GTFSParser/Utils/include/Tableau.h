#ifndef TABLEAU_H
#define TABLEAU_H

#include <vector>
#include <string>
#include <boost/regex.hpp> 
#include "CelluleTableau.h"



namespace Utils
{
namespace Html
{
class Tableau
{
public:
	Tableau(const std::string& tableString);	
	~Tableau();
	void Afficher();
	unsigned int ObtenirNumLigne();
	std::vector<CelluleTableau> ObtenirLigne(int i);
	bool Contient(const std::string& string);
	bool Contient(const boost::regex& regex); 

private:
	std::vector<std::vector<CelluleTableau>> m_tableau;

};
}
}

#endif