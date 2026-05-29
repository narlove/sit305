require("dotenv").config();
const express = require("express");
const { connectDb } = require("./db");
const userRoutes = require("./routes/users");
const taskRoutes = require("./routes/tasks");
const questionRoutes = require("./routes/questions");

// express for api
const app = express();
app.use(express.json());

app.use("/users", userRoutes);
app.use("/tasks", taskRoutes);
app.use("/questions", questionRoutes);

const PORT = process.env.PORT || 3000;

connectDb().then(() => {
    app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
});
