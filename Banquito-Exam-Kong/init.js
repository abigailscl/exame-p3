db = db.getSiblingDB('branch');
db.createCollection('branches');

var brancheColl = db.getCollection('branches');

brancheColl.insertMany([
    {"code": "001", "name": "Guayaquil"},
	{"code": "002", "name": "Quito"},
	{"code": "003", "name": "Cuenca"},
	{"code": "004", "name": "Ambato"}
]);
