const MAX_HEIGHT = 400;
const BAR_WIDTH = 90;
const BASELINE_Y = 450;
const OFFSET = 25;
const COLORS = ["#ff773d", "#ffd573", "#ffe545", "#ffab03"];


const svg = d3.select("svg");
const axis = d3.axisRight(null);
svg.append("g")
    .attr("transform", `translate(0,${BASELINE_Y - MAX_HEIGHT})`);


async function getDataForYear(year) {
	const data = await fetch(`/delayCause?year=${year}`);
	const json = await data.json();
	return new Promise(resolve => resolve(json));
}

function update(year) {

	getDataForYear(year).then(data => {

		let max = d3.max(data, d => parseFloat(d.delay));
		let heighScale = d3.scaleLinear().domain([0, max]).range([0, MAX_HEIGHT]);

		let axisScale = d3.scaleLinear().domain([0, max]).range([MAX_HEIGHT, 0]);
		

		axis.scale(axisScale);
		svg.select("g").transition().duration(500).call(axis);

		// DATA JOIN
		// Join new data with old elements, if any.
		const bars = svg.selectAll("rect")
			.data(data, d => d.reason);


		// ENTER
		// Create new elements as needed.
		//
		// ENTER + UPDATE
		// After merging the entered elements with the update selection,
		// apply operations to both.
		bars.enter().append("rect")
			.attr("x",  (d, i) => 50 + OFFSET + (BAR_WIDTH + OFFSET) * i)
			.attr("y", (d) => BASELINE_Y)
			.attr("width", BAR_WIDTH)
			.attr("height", 0)
			.attr("fill", (_, i) => COLORS[i % 4])
			.merge(bars)
			.transition().duration(1000).attr("height", (d) => heighScale(parseFloat(d.delay)))
			.attr("y", (d) => BASELINE_Y - heighScale(parseFloat(d.delay)));

		const texts = svg.selectAll("text")
			.data(data, d => d.reason); 
		texts.enter().append("text")
			.attr("x", (d, i) => 50 + OFFSET + (BAR_WIDTH + OFFSET) * i)
			.style("font-size", "24px")
			.merge(texts)
			.text(d => d.reason)
			.transition().duration(1000).attr("y", d => BASELINE_Y - heighScale(parseFloat(d.delay)));
		

		// EXIT
		// Remove old elements as needed.
		//bars.exit().transition().duration(500).attr("height", 0).attr("y", BASELINE_Y).remove();
	});
}


