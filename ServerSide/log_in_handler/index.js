const express = require("express");
const app = express();
const pool = require("./db");

app.use(express.json()); // => req.body


//ROUTES//

//get all logs

/*app.get("/log", async(req, res) => {
    try{
        const allTodos = await pool.query("SELECT * FROM todo");

        res.json(allTodos.rows);  // trimite inapoi la API caller ceva informatii
    }catch (err) {
        console.error(err.message);
    }
});*/

//get the id of the user (working)

app.get("/log/", async(req, res) => {
    //const {user} = req.params;
    //const {password} = req.params;
    const { req_username } = req.body;
    const { req_password } = req.body;

    try{
        const todo = await pool.query("SELECT * FROM log_in WHERE (username = $1 AND password = $2)", [req_username, req_password]);

        if(todo.rows[0])
            res.json(todo.rows[0]["log_id"]);
        else
            res.json("No user with this username.");

    } catch(err){
        console.error(err.message);
    }
    //res.json(req_username+' '+req_password);
});

//create a user (working)

app.post("/log/", async (req, res) => {
    try {   
        console.log(req.body);

        const { req_username } = req.body;
        const { req_password } = req.body;
        //const newTodo = await pool.query("INSERT INTO todo (description) VALUES ($1) RETURNING *", [description] );

        const verify = await pool.query("SELECT * FROM log_in WHERE username = $1", [req_username]);


        if(verify.rows[0])
        {
            res.json("Username already taken.")
            return;
        }

        const newLog = await pool.query("INSERT INTO log_in (username, password) VALUES ($1,$2)", [req_username, req_password]);

        res.json("I made a new user with username: " + req_username + " and password: " + req_password);
    } catch (err) {
        console.error(err.message);
    }
});

//update a log (working)

app.put("/log/", async(req, res) => {
    try{
        const { req_username } = req.body;
        const { req_password } = req.body;

        const updateLog = await pool.query("UPDATE log_in SET password = $1 WHERE username = $2", [req_password, req_username])
    
        res.json("Password was updated!");
    }catch(err) {
        console.error(err.message);
    }
})

//delete a log (working)

app.delete("/log/", async(req, res) => {
    try {
        const { req_username } = req.body;

        const deleteLog = await pool.query("DELETE FROM log_in WHERE username = $1", [req_username]);
        res.json("User was successfully deleted!");
    } catch (err) {
        console.error(err.message);
    }
});


app.listen(5000, () => {
    console.log("server is listening on port 5000");
});