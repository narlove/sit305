const express = require("express");
const { getDb } = require("../db");
const { generate_id } = require('../idgen');
const { Long } = require('mongodb')

const router = express.Router();

router.get("/:username", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: GET /users/:username - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        // attempt to find user in users collection
        const user = await getDb()
            .collection("users")
            .findOne({ username: req.params.username });

        if (!user) 
            return res.status(404).json({ error: "user not found" });
        
        // json res to map to use GSON to map to POJO
        res.json(user);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

router.post("/", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: POST /users - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        const newUser = {
            ...req.body,
            _id: generate_id(),
            tasks: req.body.tasks ?? [],
        };

        await getDb().collection("users").insertOne(newUser);
        
        // because we generate the id serverside, we return the new id so that a method caller
        // is able to continue to use the functions we provide (most require a user id)
        res.status(201).json({ userId: newUser._id });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

router.put("/:id", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: PUT /users/:id - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        const { interests } = req.body;

        if (!Array.isArray(interests)) 
            return res.status(400).json({ error: "interests must be an array" });
        
        const result = await getDb()
            .collection("users")
            .updateOne({ _id: Long.fromString(req.params.id) }, { $set: { interests } });

        if (result.matchedCount === 0)
            return res.status(404).json({ error: "user not found" });

        res.json({ message: "user updated successfully" });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

router.get("/:id/tasks", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: GET /users/:id/tasks - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        const user = await getDb()
            .collection("users")
            // projection given like a mask, defines what we should be returned
            // here, we only want the tasks
            .findOne({ _id: Long.fromString(req.params.id) }, { projection: { tasks: 1 } });

        if (!user) 
            return res.status(404).json({ error: "user not found" });

        res.json(user.tasks);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// count endpoint is defined before the tasks POST as otherwise
// express may mistake "count" as a path variable
router.get("/:id/tasks/count", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: GET /users/:id/tasks/count - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        const user = await getDb()
            .collection("users")
            .findOne({ _id: Long.fromString(req.params.id) }, { projection: { tasks: 1 } });

        if (!user) 
            return res.status(404).json({ error: "user not found" });

        res.json({ count: user.tasks.length });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

router.post("/:id/tasks", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: POST /users/:id/tasks - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        const newTask = {
            ...req.body,
            taskId: generate_id()
        };

        const result = await getDb()
            .collection("users")
            .updateOne({ _id: Long.fromString(req.params.id) }, { $push: { tasks: newTask } });

        if (result.matchedCount === 0)
            return res.status(404).json({ error: "user not found" });

        res.status(201).json({ taskId: newTask.taskId });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
