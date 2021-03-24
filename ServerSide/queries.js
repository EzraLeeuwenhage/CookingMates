const Pool = require('pg').Pool
const pool = new Pool({
  user: 'api',
  host: 'localhost',
  database: 'application',
  password: 'dK9w3axEix83Qr4ljX8d',
  port: 5432,
})

const getUsers = (request, response) => {
  pool.query("SELECT * FROM users ORDER BY UserId ASC", (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const getUserById = (request, response) => {
  const {username, fullname, email, password, dateofbirth, profilepicture} = request.body;

    try{
        const todo = pool.query("SELECT * FROM users WHERE (username = $1 AND password = $2)", [username, password]);

        if(todo.rows[0])
            response.json(todo.rows[0]);
        else
            response.json("No user with this username.");

    } catch(err){
        console.error(err.message);
    }
    //res.json(req_username+' '+req_password);
}

const getUserByName = (request, response) => {
  const user_name = request.params.username;

  pool.query('SELECT * FROM users WHERE UserName = $1', [user_name], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const createUser = (request, response) => {
  try {   
        console.log(request.body);

        const {username, fullname, email, password, dateofbirth, profilepicture} = request.body;
        //const newTodo = await pool.query("INSERT INTO todo (description) VALUES ($1) RETURNING *", [description] );

        const verify = pool.query("SELECT * FROM users WHERE username = $1", [username]);


        if(verify.rows[0])
        {
            response.json("Username already taken.")
            return;
        }

        const newLog = pool.query("INSERT INTO users (username, fullname, email, password, dateofbirth, profilepicture) VALUES ($1,$2,$3,$4,$5,$6)", [username, fullname, email, password, dateofbirth, profilepicture]);

        response.json("I made a new user with username: " + username + " and password: " + password);
    } catch (err) {
        console.error(err.message);
    }
}

const updateUser = (request, response) => {
  try{
        const {username, fullname, email, password, dateofbirth, profilepicture} = request.body;

        const updateLog = pool.query("UPDATE users SET password = $1 WHERE username = $2", [password, username])
    
        response.json("Password was updated!");
    }catch(err) {
        console.error(err.message);
    }
}

const deleteUser = (request, response) => {
  try {
        const {username, fullname, email, password, dateofbirth, profilepicture} = request.body;

        const deleteLog = pool.query("DELETE FROM users WHERE username = $1", [username]);
        response.json("User was successfully deleted!");
    } catch (err) {
        console.error(err.message);
    }
}

const getRecipes = (request, response) => {
	pool.query("SELECT * FROM recipes ORDER BY recipeid ASC", (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const getRecipeById = (request, response) => {
  const recipeid = parseInt(request.params.recipeid)

  pool.query('SELECT * FROM recipes WHERE recipeid = $1', [recipeid], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const getRecipeByName = (request, response) => {
  const name = request.params.name;

  pool.query('SELECT * FROM recipes WHERE name = $1', [name], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const getRecipeByCreator = (request, response) => {
  const creatorid = request.params.creatorid;

  pool.query('SELECT * FROM recipes WHERE creatorid = $1', [creatorid], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const createRecipe = (request, response) => {
  const { creatorid, name, description, ingredients, quantities, numberpeople, adult, media } = request.body
  pool.query('INSERT INTO recipes (creatorid, name, description, ingredients, quantity, numberpeople, adult, media) VALUES ($1, $2, $3, $4, $5, $6, $7, $8)', 
  [creatorid, name, description, ingredients, quantities, numberpeople, adult, media], (error, results) => {
    if (error) {
      throw error
    }
    response.status(201).send(results[0])
  })
}

const updateRecipe = (request, response) => {
  const recipeid = parseInt(request.params.recipeid)
  const { creatorid, name, description, ingredients, quantities, numberpeople, adult, media } = request.body

  pool.query(
    'UPDATE recipes SET creatorid = $1, name = $2, description = $3, ingredients = $4, quantity = $5, numberpeople = $6, adult = $7, media = $8 WHERE recipeid = $9',
    [creatorid, name, description, ingredients, quantities, numberpeople, adult, media, recipeid],
    (error, results) => {
      if (error) {
        throw error
      }
      response.status(200).send(`Recipe modified with ID: ${recipeid}`)
    }
  )
}

const deleteRecipe = (request, response) => {
  const recipeid = parseInt(request.params.recipeid)

  pool.query('DELETE FROM recipes WHERE recipeid = $1', [recipeid], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).send(`Recipe deleted with ID: ${recipeid}`)
  })
}

module.exports = {
  getUsers,
  getUserById,
  getUserByName,
  createUser,
  updateUser,
  deleteUser,
  getRecipes,
  getRecipeById,
  getRecipeByName,
  getRecipeByCreator,
  createRecipe,
  updateRecipe,
  deleteRecipe,
}