import pandas as pd
import requests
from bs4 import BeautifulSoup
from time import sleep

AIRPORT_ICAO = "KJFK"  # Where the measurements come from
AIRPORT_NAME = "JFK" # The name of the airport
YEARS = [2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017]
MONTHS = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12]

URL = "https://www.wunderground.com/history/airport/%s/%s/%s/1/MonthlyHistory.html?req_city=&req_state=&req_statename=&reqdb.zip=&reqdb.magic=&reqdb.wmo="


def cleanDataFrame(df):
	df = df.drop(df.index[0])
	newdf = df[df.columns[[3, 18, 19, 20]]]
	return newdf

def getWeatherMontlyTable(url):
	res = requests.get(url)
	html = res.text

	soup = BeautifulSoup(html, "html.parser")
	for body in soup("tbody"):
		body.unwrap()

	df = pd.read_html(str(soup), flavor="bs4")

	return cleanDataFrame(df[3])


def downloadData():
	totalDf = None
	for year in YEARS:
		for month in MONTHS:
			success = False
			while not success:
				try:
					sleep(1.5)
					print("downloading %s/%s" % (year, month))
					url = URL % (AIRPORT_ICAO, year, month)
					downloaded = getWeatherMontlyTable(url)
					downloaded.columns = ["MIN_TEMP", "MAX_WIND", "PRECIPITATION_mm", "EVENTS"]
					downloaded["MONTH"] = month
					downloaded["YEAR"] = year
					downloaded["AIRPORT_NAME"] = AIRPORT_NAME
					downloaded["DAY"] = downloaded.apply(lambda r: r.name, axis = 1)
					if totalDf is None:
						totalDf = downloaded
					else:
						totalDf = pd.concat([totalDf, downloaded], ignore_index = True)
					success = True
				except Exception:
					print("error while downloading, trying again")
					pass
		

	totalDf.to_csv(("%s_weather.csv" % AIRPORT_ICAO), index = False)

if __name__ == "__main__":
	print("Location (ICAO) set to: " + AIRPORT_ICAO)
	downloadData()
			
