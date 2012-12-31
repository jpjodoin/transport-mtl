class TransportConfig:
	"""A simple class to contains information useful for a transport service"""
	def __init__(self, fileConfigPath):
		f = open(fileConfigPath, 'r')
		data = f.read()
		f.close()
		transportConfigArray = data.split(';')
		self.shortName = transportConfigArray[0]
		self.longName = "\""+transportConfigArray[1]+"\""
		self.transportType = transportConfigArray[2]
		self.displayMap = transportConfigArray[3]
		self.displaySearch = transportConfigArray[4]
		self.urlgetter = transportConfigArray[5]
		self.baseurl = transportConfigArray[6]
		self.usestop_id = transportConfigArray[7]
		self.usecolor = transportConfigArray[8]
		
