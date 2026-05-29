const express = require("express");
const { getDb } = require("../db");
const { Long } = require('mongodb');

const router = express.Router();

router.get("/:id", async (req, res) => {
    console.log(`[${new Date().toISOString()}] route: GET /questions/:id - params=${JSON.stringify(req.params)} query=${JSON.stringify(req.query)} body=${JSON.stringify(req.body)}`);
    try {
        const user = await getDb()
            .collection("users")
            .findOne(
                { "tasks.questions.questionId": Long.fromString(req.params.id) },
                { projection: { tasks: 1 } }
            );

        if (!user) 
            return res.status(404).json({ error: "question not found" });

        let question = null;
        for (const task of user.tasks) {
            question = task.questions.find(q => q.questionId === Long.fromString(req.params.id));
            if (question) break;
        }

        res.json(question);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

module.exports = router;
