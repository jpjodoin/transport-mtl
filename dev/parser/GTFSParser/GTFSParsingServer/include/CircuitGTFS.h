#pragma once
#include "Circuit.h"
#include <string>
#include <vector>
#include "ArretGTFS.h"
#include <map>
class CircuitGTFS: public Circuit
{
public:
	CircuitGTFS(std::string strNom, unsigned int nNumero, eDirection Direction, std::string strExtraInfoDirection = "null", std::string strExtraInfoCircuit = "null");
	virtual ~CircuitGTFS();
	std::vector<std::string> m_tripsList;
	std::map<std::string, int> m_TripHeadSignNbReference;
	void sortStopList()
	{
		std::sort(m_vArret.begin(), m_vArret.end(), AscendingArretGTFSSort());
	}

	struct AscendingArretGTFSSort
	{
		bool operator()(Arret* a1, Arret* a2)
		{
			//std::sort(vHoraire->begin(), vHoraire->end(), AscendingHoraireDateSort());
			return ( ((ArretGTFS*)a1)->m_stop_sequence < ((ArretGTFS*)a2)->m_stop_sequence);
		}
	};


};

