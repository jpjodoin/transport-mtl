#ifndef DATA_ARRAY_2D_H
#define DATA_ARRAY_2D_H
///
/// \file DataArray2D.h
/// \brief Classe templaté qui représente un tableau de donnée multidimensionnel (Fichier de définition)
/// \version 0.1
/// \date 2009/11/08 
///
#ifndef _VECTOR_
#include <vector>
#endif


///
/// \todo Deprecate this
///	\brief  Classe templaté qui représente un tableau de donnée multidimensionnel
///

template<typename dataType>
class DataArray2D
{
public:

	DataArray2D(int nbLigne, int nbColonne);
	
	const dataType& ObtenirDonnees(int _noLigne, int _noColonne);

	void AjouterDonnees(int _noLigne, int _noColonne, dataType _nouvelleDonnee);

	int StringContenuDansQuelLigne(const std::string& strFind);

	unsigned int ObtenirNombreDeColonne();
	unsigned int ObtenirNombreDeLigne();

	DataArray2D<dataType>* ObtenirTableauInterne();
	bool Contient(const std::string& _strToFind);



	~DataArray2D();


private:
	std::vector<std::vector<dataType>> m_InternalArray;

};

template<typename dataType>
int DataArray2D<dataType>::StringContenuDansQuelLigne( const std::string& _strToFind )
{
	int ligne=-1;
	for(unsigned int i=0; i<m_InternalArray.size(); i++)
	{
		for(unsigned int j=0; j<m_InternalArray.at(i).size(); j++)
		{
			if(Utils::String::Contains(m_InternalArray.at(i).at(j), _strToFind))
			{
				ligne=i;
				j=m_InternalArray.at(i).size()-1;
				i=m_InternalArray.size()-1;
			}
		}
	}
	return ligne;
}
template<typename dataType>
bool DataArray2D<dataType>::Contient( const std::string& _strToFind )
{
	bool found=false;
	for(unsigned int i=0; i<m_InternalArray.size(); i++)
	{
		for(unsigned int j=0; j<m_InternalArray.at(i).size(); j++)
		{
			if(Utils::String::Contains(m_InternalArray.at(i).at(j), _strToFind))
			{
				found=true;
				j=m_InternalArray.at(i).size()-1;
				i=m_InternalArray.size()-1;
			}
		}
	}
	return found;
}


template<typename  dataType>
DataArray2D<dataType>::DataArray2D(int _nbLigne, int _nbColonne )
: m_InternalArray(_nbLigne, std::vector<dataType>(_nbColonne))
{
}



template<typename  dataType>
DataArray2D<dataType>::~DataArray2D()
{
}


template<typename dataType>
const dataType& DataArray2D<dataType>::ObtenirDonnees( int _noLigne, int _noColonne )
{
	return m_InternalArray.at(_noLigne).at(_noColonne);
}



template<typename dataType>
void DataArray2D<dataType>::AjouterDonnees( int _noLigne, int _noColonne, dataType _nouvelleDonnee )
{
	m_InternalArray.at(_noLigne).at(_noColonne)=_nouvelleDonnee;
}


template<typename dataType>
unsigned int DataArray2D<dataType>::ObtenirNombreDeLigne()
{
	return m_InternalArray.size();
}
template<typename dataType>
unsigned int DataArray2D<dataType>::ObtenirNombreDeColonne()
{
	return m_InternalArray.at(0).size();
}




#endif

	/*DataArray2D<std::string> test(6,8);
	test.AjouterDonnees(0,0, "l0,c0");
	test.AjouterDonnees(0,1, "l0,c1");
	test.AjouterDonnees(0,2, "l0,c2");
	test.AjouterDonnees(0,3, "l0,c3");
	test.AjouterDonnees(1,0, "l1,c0");
	test.AjouterDonnees(1,1, "l1,c1");
	test.AjouterDonnees(1,2, "l1,c2");
	test.AjouterDonnees(1,3, "l1,c3");
	test.AjouterDonnees(2,0, "l2,c0");
	test.AjouterDonnees(2,1, "l2,c1");
	test.AjouterDonnees(2,2, "l2,c2");
	test.AjouterDonnees(2,3, "l2,c3");
	test.AjouterDonnees(3,0, "l3,c0");
	test.AjouterDonnees(3,1, "l3,c1");
	test.AjouterDonnees(3,2, "l3,c2");
	test.AjouterDonnees(3,3, "l3,c3");
	test.AjouterDonnees(3,4, "l3,c4");
	std::string string=test.ObtenirDonnees(2,2);
	std::string string2=test.ObtenirDonnees(1,2);
	std::string string3=test.ObtenirDonnees(2,1);
	unsigned int nbLigne=test.ObtenirNombreDeLigne();
	unsigned int nbCol=test.ObtenirNombreDeColonne();*/
