const express = require("express");
const { getDb } = require("../db");
const { Long } = require('mongodb');

const router = express.Router();

router.get("/:id", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: GET /tasks/:id - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        // everything basically needs to be done in terms of user because that is the collection
        // hence here we can only find the users task array object with a specific index and have the whole collection returned
        const user = await getDb()
            .collection("users")
            .findOne(
                { "tasks.taskId": Long.fromString(req.params.id) },
                { projection: { tasks: 1 } }
            );

        if (!user) return res.status(404).json({ error: "task not found" });

        // then we grab the actual value here
        const task = user.tasks.find((t) => t.taskId === req.params.id);

        res.json(task);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

router.get("/owningUser/:id", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: GET /tasks/owningUser/:id - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        const user = await getDb()
            .collection("users")
            .findOne(
                { "tasks.taskId": Long.fromString(req.params.id) }
                // no projection, need user id
            )

        if (!user) return res.status(404).json({ error: "task not found" });

        res.status(201).json({ userId: user._id });
    } catch (err) {
        res.status(500).json({ error: err.message })
    }
});

// TODO: edit this so that it does not remove and simply alters an active variable
// when implementing history later
router.delete("/:id", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: DELETE /tasks/:id - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        const result = await getDb()
            .collection("users")
            .updateOne(
                { "tasks.taskId": Long.fromString(req.params.id) },
                // use pull because we're interacting with an array
                { $pull: { tasks: { taskId: Long.fromString(req.params.id) } } }
            );

        if (result.matchedCount === 0)
            return res.status(404).json({ error: "task not found" });

        res.json({ message: "task deleted" });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
