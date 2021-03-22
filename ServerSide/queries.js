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
  const id = parseInt(request.params.id)

  pool.query('SELECT * FROM users WHERE UserId = $1', [id], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
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
  const { user_name, email, user_password } = request.body

  pool.query('INSERT INTO users (UserName, Email, Password) VALUES ($1, $2, $3)', [user_name, email, user_password], (error, results) => {
    if (error) {
      throw error
    }
    response.status(201).send(results[0])
  })
}

const updateUser = (request, response) => {
  const id = parseInt(request.params.id)
  const { user_name, user_password } = request.body

  pool.query(
    'UPDATE users SET UserName = $1, Password = $2 WHERE UserId = $3',
    [user_name, user_password, id],
    (error, results) => {
      if (error) {
        throw error
      }
      response.status(200).send(`User modified with ID: ${id}`)
    }
  )
}

const deleteUser = (request, response) => {
  const id = parseInt(request.params.id)

  pool.query('DELETE FROM users WHERE UserId = $1', [id], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).send(`User deleted with ID: ${id}`)
  })
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
  pool.query('INSERT INTO recipes (creatorid, name, description, ingredients, quantity, numberpeople, adult, media) VALUES ($1, $2, $3, $4)', 
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
    'UPDATE recipes SET creatorid = $1, name = $2, description = $3, ingredients = $4, quantity = $5, numberpeople = $6, adult = $7, media = $8 WHERE recipeid = $5',
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