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

const getUserByUsername = async(request, response) => {
  const username = request.params.username;
  const password = request.params.password;

    try{
        const todo = await pool.query("SELECT * FROM users WHERE (username = $1 AND password = $2)", [username, password]);

        if(todo.rows[0])
            response.status(200).json(todo.rows[0]);
        else
            response.status(404).json("No user with this username.");

    } catch(err){
        console.error(err.message);
    }
    //res.json(req_username+' '+req_password);
}

/*const getUserByName = (request, response) => {
  const user_name = request.params.username;

  pool.query('SELECT * FROM users WHERE UserName = $1', [user_name], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}*/

const createUser = async(request, response) => {
  try {   
        console.log(request.body);

        const {username, fullname, email, password, dateofbirth, profilepicture} = request.body;
        //const newTodo = await pool.query("INSERT INTO todo (description) VALUES ($1) RETURNING *", [description] );

        const verify = await pool.query("SELECT * FROM users WHERE username = $1", [username]);


        if(verify.rows[0])
        {
            response.status(404).send("Username already exists")
            return;
        }

        const newLog = await pool.query("INSERT INTO users (username, fullname, email, password, dateofbirth, profilepicture) VALUES ($1,$2,$3,$4,$5,$6)", [username, fullname, email, password, dateofbirth, profilepicture]);

        response.status(200).send("Created user")
    } catch (err) {
        console.error(err.message);
    }
}

const updateUser = async(request, response) => {
  try{
        const {username, fullname, email, password, dateofbirth, profilepicture} = request.body;

        const updateLog = await pool.query("UPDATE users SET password = $1 WHERE username = $2", [password, username])
    
        response.json("Password was updated!");
    }catch(err) {
        console.error(err.message);
    }
}

const deleteUser = async(request, response) => {
  try {
        const {username, fullname, email, password, dateofbirth, profilepicture} = request.body;

        const deleteLog = await pool.query("DELETE FROM users WHERE username = $1", [username]);
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

  pool.query("SELECT * FROM recipes WHERE name LIKE '%' || $1 || '%'", [name], (error, results) => {
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

const getRecipeByIngredient = (request, response) => {
  const ingredient = request.params.ingredient;

  pool.query("SELECT * FROM recipes WHERE EXISTS (SELECT * FROM unnest(ingredients) elem WHERE elem LIKE '%' || $1 || '%')", [ingredient], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const getRecipeByTag = (request, response) => {
  const tag = request.params.tag;

  pool.query("SELECT * FROM recipes WHERE EXISTS (SELECT * FROM unnest(tags) elem WHERE elem LIKE '%' || $1 || '%')", [tag], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const createRecipe = (request, response) => {
  const { creatorid, name, description, ingredients, quantity, numberpeople, adult, media, ratings, reviews, tags } = request.body
  pool.query('INSERT INTO recipes (creatorid, name, description, ingredients, quantity, numberpeople, adult, media, tags) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)', 
  [creatorid, name, description, ingredients, quantity, numberpeople, adult, media, tags], (error, results) => {
    if (error) {
      throw error
    }
    response.status(201).send(results[0])
  })
}

const updateRecipe = (request, response) => {
  const recipeid = parseInt(request.params.recipeid)
  const { creatorid, name, description, ingredients, quantity, numberpeople, adult, media, ratings, reviews } = request.body

  pool.query(
    'UPDATE recipes SET creatorid = $1, name = $2, description = $3, ingredients = $4, quantity = $5, numberpeople = $6, adult = $7, media = $8 WHERE recipeid = $9',
    [creatorid, name, description, ingredients, quantity, numberpeople, adult, media, recipeid],
    (error, results) => {
      if (error) {
        throw error
      }
      response.status(200).send(`Recipe modified with ID: ${recipeid}`)
    }
  )
}

const addReviewToRecipe = (request, response) => {
	const recipeid = parseInt(request.params.recipeid)
	const { creatorid, name, description, ingredients, quantity, numberpeople, adult, media, ratings, reviews } = request.body

	pool.query('UPDATE recipes SET reviews = $1 WHERE recipeid = $2',
		[reviews, recipeid], (error, results) => {
		if (error) {
			throw error
		}
		response.status(200).send(`Recipe modified with ID: ${recipeid}`)
	}
  )
}

const addRatingToRecipe = (request, response) => {
	const recipeid = parseInt(request.params.recipeid)
	const { creatorid, name, description, ingredients, quantity, numberpeople, adult, media, ratings, reviews } = request.body

	pool.query('UPDATE recipes SET ratings = $1 WHERE recipeid = $2',
		[ratings, recipeid], (error, results) => {
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
  getUserByUsername,
  createUser,
  updateUser,
  deleteUser,
  getRecipes,
  getRecipeById,
  getRecipeByName,
  getRecipeByCreator,
  getRecipeByIngredient,
  getRecipeByTag,
  createRecipe,
  updateRecipe,
  addRatingToRecipe,
  addReviewToRecipe,
  deleteRecipe,
}