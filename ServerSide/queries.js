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
	pool.query("SELECT * FROM recipes ORDER BY id ASC", (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const getRecipeById = (request, response) => {
  const id = parseInt(request.params.id)

  pool.query('SELECT * FROM recipes WHERE id = $1', [id], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const getRecipeByTitle = (request, response) => {
  const title = request.params.title;

  pool.query('SELECT * FROM recipes WHERE title = $1', [title], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).json(results.rows)
  })
}

const createRecipe = (request, response) => {
  const { title, description, filepath, filename } = request.body
  pool.query('INSERT INTO recipes (title, description, filename, filepath) VALUES ($1, $2, $3, $4)', [title, description, filename, filepath], (error, results) => {
    if (error) {
      throw error
    }
    response.status(201).send(results[0])
  })
}

const updateRecipe = (request, response) => {
  const id = parseInt(request.params.id)
  const { title, description, filepath, filename } = request.body

  pool.query(
    'UPDATE recipes SET title = $1, description = $2, filename = $3, filepath = $4 WHERE id = $5',
    [title, description, filename, filepath, id],
    (error, results) => {
      if (error) {
        throw error
      }
      response.status(200).send(`Recipe modified with ID: ${id}`)
    }
  )
}

const deleteRecipe = (request, response) => {
  const id = parseInt(request.params.id)

  pool.query('DELETE FROM recipes WHERE id = $1', [id], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).send(`Recipe deleted with ID: ${id}`)
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
  getRecipeByTitle,
  createRecipe,
  updateRecipe,
  deleteRecipe,
}