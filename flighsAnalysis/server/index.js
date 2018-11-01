const express  = require("express");
const path = require("path");
const mongoose = require("mongoose");
const DelayCause = require("./model/delayCause");
const bodyParser = require("body-parser")
const neo4j = require('neo4j-driver').v1;

const driver = neo4j.driver('bolt://localhost', neo4j.auth.basic('cecio', 'cecio'));
const session = driver.session();

mongoose.connect("mongodb://localhost/sparkdb");
const db = mongoose.connection;


db.once("open", () => console.log("db ok"));

db.on('error', (err) => console.log("db error " + err));



const app = express();

app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());
app.set('views', path.join(__dirname, "views"));
app.set("view engine", "pug");
app.use(express.static("static"));

const minsToHours = (mins) => {
	const hours = Math.floor(mins/60);
	const absMins = mins % 60
	mins = Math.floor(absMins);
	if (mins < 10) mins = '0' + mins;
	return hours + ":" + mins;
}


const getAirportNames = (results) => {
	const allNames = [];
	results.forEach(r => allNames.push(r._fields[0].properties.name));
	return allNames;
}

const filterResults = (results) => {
	const res = [];
	results[0]._fields[0].segments.forEach(f => {
		if (f.start === undefined) return;
		const start = f.start.properties.name;
		const dest = f.end.properties.name;
		
		const duration = minsToHours(f.relationship.properties.duration - 120);
		res.push({start: start, dest: dest, duration: duration});
	});
	return res;
}

const getTotalDuration = (results) => {
	return minsToHours(results[0]._fields[1] - 120);
}

app.get("/", (req, res) => {
	res.redirect("index.html");

});


app.get("/delayCause", (req, res) => {

	const condition = {year: parseInt(req.query.year)};
	console.log("request for: " + req.query.year);
	
	DelayCause.find(condition).exec()
	.then(causes => {console.log(causes); return res.send(causes)})
	.catch(err => console.log(err));

});

app.get("/routes", (req, res) => {
    const from = req.query.from;
    const to = req.query.to;
    
    console.log(from, to);
    
    session
    	.run("match(n) return n")
    	.then(result => {
    		return getAirportNames(result.records);
    	})
    	.then(airportNames => {
    		session
        	.run(`match(st:Airport {name: ${from}}), (en:Airport {name: ${to}}) call apoc.algo.dijkstra(st, en, 'Distance', 'duration') YIELD path, weight return path, weight`)
        	.then(result => {
            	/*res.send(result.records);
            	return;*/
            	res.render("routes", {
            		airportNames: airportNames,
					routes: filterResults(result.records),
					totDuration: getTotalDuration(result.records)
				});
            	session.close();
        	})
        	.catch(err => {
        		console.log(err);
            	res.render("routes", {
            		airportNames: airportNames,
					routes:[],
					totDuration: 0
				});
            	session.close();
        	});
    		
    	});
	/*
    
        */

});

app.listen(3000, () => {
	console.log("server started");
});


