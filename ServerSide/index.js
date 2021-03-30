const express = require('express')
const multer = require('multer')
const fs = require('fs')
const crypto = require('crypto')
const path = require('path')
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

storage = multer.diskStorage({
    destination: './uploads/',
    filename: function(req, file, cb) {
      return crypto.pseudoRandomBytes(16, function(err, raw) {
        if (err) {
          return cb(err)
        }
        return cb(null, "" + (raw.toString('hex')) + (path.extname(file.originalname)));
      })
    }
  })

app.get('/', (request, response) => {
  response.json({ info: 'Node.js, Express, and Postgres API' })
})

app.get('/users', db.getUsers)
app.get('/users/:username/:password', db.getUserByUsername)
app.post('/users', db.createUser)
app.patch('/users', db.updateUser)
app.delete('/users', db.deleteUser)

app.get('/recipes', db.getRecipes)
app.get('/recipes/:recipeid', db.getRecipeById)
app.get('/recipes/:recipeid/:name', db.getRecipeByName)
app.get('/recipes/:recipeid/:name/:creatorid', db.getRecipeByCreator)
app.get('/recipes/:recipeid/:name/:creatorid/:ingredient', db.getRecipeByIngredient)
app.post('/recipes', db.createRecipe)
app.put('/recipes/:recipeid', db.updateRecipe)
app.delete('/recipes/:recipeid', db.deleteRecipe)

// Post images
app.post("/upload", multer({storage: storage}).single('upload'), function(req, res) {
    console.log(req.file)
    console.log(req.body)
	res.send("" + req.file.filename)
    res.redirect("/uploads/" + req.file.filename)
    console.log(req.file.filename)
    return res.status(200).end()
  })

app.get('/uploads/:upload', function (req, res){
  file = req.params.upload
  console.log(req.params.upload)
  var img = fs.readFileSync(__dirname + "/uploads/" + file)
  res.writeHead(200, {'Content-Type': 'image/png' })
  res.end(img, 'binary')
})

app.listen(port, () => {
  console.log(`App running on port ${port}.`)
})