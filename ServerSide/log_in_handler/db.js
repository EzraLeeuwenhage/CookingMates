const Pool = require("pg").Pool;

const pool = new Pool({
    user: "postgres",
    password: "qweqwe",
    database: "log_in_database",
    host: "localhost",
    port: 5432
});

module.exports = pool;