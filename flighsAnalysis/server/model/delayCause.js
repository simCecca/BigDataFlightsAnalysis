const mongoose = require("mongoose");

// Article Schema
const delayCauseSchema = mongoose.Schema({
	reason: {
		type: String,
		required: true
	},
	year: {
		type: Number,
		required: true
	},
	delay: {
		type: String,
		required: true
	}
	
});

module.exports = mongoose.model("DelayCause", delayCauseSchema, "delayCause");


