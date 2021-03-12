const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const db = require('./queries')
const port = 3000

app.use(bodyParser.json())
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
)

app.get('/', (request, response) => {
  response.json({ info: 'Node.js, Express, and Postgres API' })
})

app.get('/recipes', db.getRecipes)
app.get('/recipes/:id', db.getRecipeById)
app.get('/recipes/:id/:title', db.getRecipeByTitle)
app.post('/recipes', db.createRecipe)
app.put('/recipes/:id', db.updateRecipe)
app.delete('/recipes/:id', db.deleteRecipe)

app.listen(port, () => {
  console.log(`App running on port ${port}.`)
})