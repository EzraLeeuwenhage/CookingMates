const Pool = require('pg').Pool
const pool = new Pool({
  user: 'me',
  host: 'localhost',
  database: 'api',
  password: 'cookingmates',
  port: 5432,
})

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
  const { title, description } = request.body

  pool.query('INSERT INTO recipes (title, description) VALUES ($1, $2)', [title, description], (error, results) => {
    if (error) {
      throw error
    }
    response.status(201).send(results[0])
  })
}

const updateRecipe = (request, response) => {
  const id = parseInt(request.params.id)
  const { title, description } = request.body

  pool.query(
    'UPDATE recipes SET title = $1, description = $2 WHERE id = $3',
    [title, description, id],
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
  getRecipes,
  getRecipeById,
  getRecipeByTitle,
  createRecipe,
  updateRecipe,
  deleteRecipe,
}