import wikipediaapi as wapi

AIRLINE_FILE = "airlineid.csv"

LOW_COST_STRINGS = ["low cost", "low fare"]

OUTPUT_STRING = "{airlineid},{airlinename},{isLowCost}\n"

wiki = wapi.Wikipedia("en")

def getAirlineName(completeName):
    ''' returns the airline name so that it is easy to look it up on wikipedia

    Usually names have the form <airline name> Inc: ...
    or <airline name>: ... we only need <airline name>'''
    name = completeName.split("Inc")[0]
    name = name.split(":")[0]
    return name.strip()

def getLowCostFromWiki(airlineName):
    page = wiki.page(airlineName)
    if not page.exists():
        print("\t {airline} Not Found".format(airline=airlineName))
        return False
    if len(page.summary) < 70: # probably a disambiguation page
        return getLowCostFromWiki("{name}_(airline)".format(name=airlineName))
    else:
        for s in LOW_COST_STRINGS:
            if s in page.summary:
                return True

    return False

def isLowCost(airlineName):
    name = airlineName[1:len(airlineName) - 1] # discard quotes
    name = getAirlineName(name)
    return getLowCostFromWiki(name)

    
with open("airline_lowCost.csv", "w") as output:
    with open("airlineid.csv") as f:
        for line in f:
            try:
                airlineid, airlinename = line.split(",")
                airlinename = airlinename[:len(airlinename) - 1]
                low_cost = isLowCost(line.split(",")[1])
                out_line = OUTPUT_STRING.format(airlineid=airlineid, airlinename = airlinename, isLowCost = low_cost)
                output.write(out_line)
            except Exception:
                pass
      


    
