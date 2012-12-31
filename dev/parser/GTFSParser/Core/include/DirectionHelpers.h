#ifndef DIRECTION_HELPERS_H
#define DIRECTION_HELPERS_H

#include "TransportStruct.h"

namespace Utils
{
namespace String
{
	char DirectionToChar(eDirection oDirection);
	eDirection DirectionStringToChar(const std::string &direction);
	eDirection InverserDirection(eDirection oDirection);
	eDirection CharToDirection(char cDirection);
}
}

#endif