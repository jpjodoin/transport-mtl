///
/// \file Base64Helpers.h
/// \version 1.0
///

#ifndef BASE64_H
#define BASE64_H

#include <string>

namespace Utils
{
namespace Encoding
{

std::string EncoderBase64(unsigned char const* , unsigned int len); //Convertir en string
std::string DecoderBase64(std::string const& s);

}
}
#endif
