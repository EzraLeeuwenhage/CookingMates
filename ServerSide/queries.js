const Pool = require('pg').Pool
const pool = new Pool({
  user: 'me',
  host: 'localhost',
  database: 'test',
  password: 'cookingmates',
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

module.exports = {
  getUsers,
  getUserById,
  getUserByName,
  createUser,
  updateUser,
  deleteUser,
}