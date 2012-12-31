#pragma once

#include <vector>
#include "Tableau.h"

namespace Utils
{
namespace Html
{
class Page
{
public:
	Page(const std::string& source);
	~Page(void);
	unsigned int ObtenirNombreTableau() { return m_vectorTableau.size();}
	void AfficherTableaux();
	const Tableau& ObtenirTableau(int index);
	std::vector<Tableau*> Contient(const std::string& string);
	std::vector<Tableau*> Contient(const boost::regex& regex);
	std::vector<Tableau*> ContientNLignes(unsigned int n);

private:
	void GetTableFromPage(const std::string& source);

	std::vector<Tableau> m_vectorTableau;
};
}
}